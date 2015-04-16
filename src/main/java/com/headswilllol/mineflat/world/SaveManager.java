/*
 * MineFlat
 * Copyright (c) 2014, Maxim Roncacé <mproncace@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.headswilllol.mineflat.world;

import com.google.gson.*;
import com.headswilllol.mineflat.Direction;
import com.headswilllol.mineflat.Main;
import com.headswilllol.mineflat.Material;
import com.headswilllol.mineflat.entity.*;
import com.headswilllol.mineflat.entity.living.Living;
import com.headswilllol.mineflat.entity.living.Mob;
import com.headswilllol.mineflat.entity.living.hostile.Ghost;
import com.headswilllol.mineflat.entity.living.passive.Snail;
import com.headswilllol.mineflat.entity.living.player.Human;
import com.headswilllol.mineflat.entity.living.player.Player;
import com.headswilllol.mineflat.util.FileUtil;
import com.headswilllol.mineflat.util.VboUtil;

import java.io.*;
import java.util.Map;

public class SaveManager {

	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	/**
	 * Parses all loaded chunks in a world to a JSON object.
	 */
	@SuppressWarnings("unchecked")
	public static JsonObject saveWorld(World world){

		System.out.println("Saving chunks...");

		JsonObject save = world.getJson();
		save.addProperty("name", Main.world.getName());
		save.addProperty("createTime", Main.world.creationTime);
		save.addProperty("modifyTime", System.currentTimeMillis() / 1000L);
		save.addProperty("chunkCount", Main.world.getChunkCount());
		save.addProperty("chunkLength", Main.world.getChunkLength());
		save.addProperty("chunkHeight", Main.world.getChunkHeight());
		save.addProperty("seed", Main.world.getSeed());
		save.addProperty("ticks", TickManager.getTotalTicks());
		save.addProperty("playerLevel", Main.player.getLevel().getIndex());
		save.addProperty("playerChunk", Main.player.getLocation().getChunk());

		JsonObject levels = (JsonObject)save.get("levels");
		if (levels == null)
			levels = new JsonObject();
		for (Level level : world.getLevels()){
			levels.add(Integer.toString(level.getIndex()), saveLevel(level));
		}
		save.add("levels", levels);
		return save;
	}

	public static void saveWorldToMemory(World world){
		world.setJson(saveWorld(world));
	}

	public static void writeWorldToDisk(World world){

		saveWorldToMemory(world);
		System.out.println("Writing chunks...");
		File saveFolder = new File(FileUtil.getAppDataFolder() + File.separator +
				".mineflat", "saves");
		saveFolder = new File(saveFolder, world.getName());
		if (!saveFolder.exists())
			saveFolder.mkdirs();
		File saveFile = new File(saveFolder, "level.json");
		File zippedFile = new File(saveFolder, "level.gz");
		if (saveFile.exists())
			saveFile.delete();
		if (zippedFile.exists())
			zippedFile.delete();
		try {
			saveFile.createNewFile();
			PrintWriter writer = new PrintWriter(saveFile);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonElement je = new JsonParser().parse(gson.toJson(world.getJson()));
			writer.write(gson.toJson(je));
			writer.close();
			FileUtil.gzip(saveFile.getAbsolutePath(), zippedFile.getAbsolutePath());
		}
		catch (IOException ex){
			ex.printStackTrace();
			System.err.println("Failed to save world to disk!");
		}
		finally {
			saveFile.delete();
		}
	}

	//TODO: make this method more type-safe
	public static void loadWorld(String world) {
		System.out.println("Loading world \"" + world + "\"");
		File saveFolder = new File(FileUtil.getAppDataFolder() + File.separator +
				".mineflat", "saves");
		saveFolder = new File(saveFolder, world);
		if (!saveFolder.exists())
			saveFolder.mkdirs();
		File zippedFile = new File(saveFolder, "level.gz");
		File saveFile = new File(saveFolder, "level.json");
		if (!zippedFile.exists())
			return;
		FileUtil.ungzip(zippedFile.getAbsolutePath(), saveFile.getAbsolutePath());
		try {
			JsonObject save = (JsonObject)new JsonParser().parse(new FileReader(saveFile));
			Main.world = new World(
					save.get("name").getAsString(),
					longToInt(save.get("chunkCount").getAsLong()),
					longToInt(save.get("chunkLength").getAsLong()),
					longToInt(save.get("chunkHeight").getAsLong())
			);
			Main.world.setJson(save);
			Main.world.seed = save.get("seed").getAsLong();
			Main.world.creationTime = save.get("createTime").getAsLong();
			TickManager.setTicks(longToInt(save.get("ticks").getAsLong()));
			loadLevel(Main.world, longToInt(save.get("playerLevel").getAsLong()));
			Main.world.setTicking(true);
		}
		catch (FileNotFoundException ex){
			ex.printStackTrace();
			System.err.println("Failed to load world from disk - save file cannot be found!");
		}
		catch (ClassCastException ex){
			ex.printStackTrace();
			System.err.println("Failed to load world from disk - save file is invalid!");
		}
		finally {
			saveFile.delete();
		}
	}

	public static Chunk loadChunk(Level level, int chunk){
		JsonObject jChunk = level.getWorld().getJson().getAsJsonObject("levels")
				.getAsJsonObject(Integer.toString(level.getIndex()))
				.getAsJsonObject("chunks").getAsJsonObject(Integer.toString(chunk));
		if (jChunk != null) {
			System.out.println("Loading chunk " + chunk);
			Biome biome = Biome.getById(jChunk.get("biome").toString());
			Chunk c = new Chunk(level, chunk, biome);
			for (JsonElement blockObj : jChunk.getAsJsonArray("blocks")) {
				JsonObject block = blockObj.getAsJsonObject();
				Material type = Material.valueOf(block.get("type").getAsString());
				if (type == null)
					type = Material.AIR;
				int data = block.get("data") != null ? block.get("data").getAsInt() : 0;
				Block b = new Block(type, data, new Location(level, Chunk.getWorldXFromChunkIndex(chunk,
						block.get("x").getAsLong()),
						block.get("y").getAsLong()));
				JsonObject meta = (JsonObject)block.get("metadata");
				for (Map.Entry<String, JsonElement> e : meta.entrySet()){
					JsonPrimitive prim = meta.get(e.getKey()).getAsJsonPrimitive();
					Object value;
					if (prim.isBoolean())
						value = prim.getAsBoolean();
					else if (prim.isNumber())
						value = prim.getAsNumber();
					else if (prim.isString())
						value = prim.getAsString();
					else
						value = prim.getAsCharacter();
					b.setMetadata(e.getKey(), value);
				}
				b.addToWorld();
			}
			for (Object entityObj : jChunk.get("entities").getAsJsonArray()) {
				JsonObject entity = (JsonObject)entityObj;
				EntityType type = EntityType.valueOf(entity.get("type").getAsString());
				float x = Chunk.getWorldXFromChunkIndex(
						c.getIndex(), Float.valueOf(Double.toString(entity.get("x").getAsDouble()))
				);
				float y = Float.valueOf(Double.toString(entity.get("y").getAsDouble()));
				float w = Float.valueOf(Double.toString(entity.get("w").getAsDouble()));
				float h = Float.valueOf(Double.toString(entity.get("h").getAsDouble()));
				Entity e;
				if (entity.has("living")) {
					if (entity.has("mob")) {
						switch (type) {
							case GHOST:
								e = new Ghost(new Location(level, x, y));
								break;
							case SNAIL:
								e = new Snail(new Location(level, x, y));
								break;
							default:
								continue; // ignore it
						}
						((Mob)e).setPlannedWalkDistance(entity.get("pwd").getAsFloat());
						((Mob)e).setActualWalkDistance(entity.get("awd").getAsFloat());
						((Mob)e).setLastX(entity.get("lx").getAsFloat());
					}
					else {
						switch (type) {
							case PLAYER:
								e = new Player(new Location(level, x, y));
								break;
							case HUMAN:
								e = new Human(new Location(level, x, y));
								break;
							default:
								continue; // ignore it
						}
					}
					((Living)e).setFacingDirection(Direction.valueOf(entity.get("fd").getAsString()));
					((Living)e).setJumping(entity.get("j").getAsBoolean());
				}
				else
					e = new Entity(type, new Location(level, x, y), w, h);
				e.getVelocity().setX(Float.valueOf(Double.toString(entity.get("xv").getAsDouble())));
				e.getVelocity().setY(Float.valueOf(Double.toString(entity.get("yv").getAsDouble())));
				level.addEntity(e);
				if (type == EntityType.PLAYER)
					Main.player = (Player)e;
			}
			c.updateLight();
			Chunk left = c.getLevel().getChunk(c.getIndex() == 1 ? -1 : c.getIndex() - 1);
			Chunk right = c.getLevel().getChunk(c.getIndex() == -1 ? 1 : c.getIndex() + 1);
			if (left != null){
				for (int y = 0; y < c.getLevel().getWorld().getChunkHeight(); y++){
					left.getBlock(c.getLevel().getWorld().getChunkLength() - 1, y).updateLight();
					VboUtil.updateChunkArray(c.getLevel(), left.getIndex());
				}
			}
			if (right != null){
				for (int y = 0; y < c.getLevel().getWorld().getChunkHeight(); y++){
					right.getBlock(0, y).updateLight();
					VboUtil.updateChunkArray(c.getLevel(), right.getIndex());
				}
			}
			System.gc(); //TODO: temporary fix until I have the motivation to find the memory leak
			return c;
		}
		return null;
	}

	public static Level loadLevel(World world, int level){
		Main.world.addLevel(level);
		Level l = Main.world.getLevel(level);
		if (longToInt(world.getJson().get("playerLevel").getAsLong()) == level) {
			loadChunk(l, longToInt(world.getJson().get("playerChunk").getAsLong()));
			Chunk.handleChunkLoading(true);
		}
		for (Chunk c : l.chunks.values())
			c.updateLight();
		return l;
	}

	/**
	 * Saves a chunk to a JSON object. <strong>This method does not save the JSON to memory or to disk.</strong>
	 * @param chunk the chunk to save.
	 * @return the created JSON object.
	 */
	public static JsonObject saveChunk(Chunk chunk){
		JsonObject c = new JsonObject();
		JsonArray blocks = new JsonArray();
		for (int x = 0; x < Main.world.getChunkLength(); x++){
			for (int y = 0; y < Main.world.getChunkHeight(); y++){
				Block block = chunk.getBlock(x, y);
				if (block != null){
					JsonObject b = new JsonObject();
					b.addProperty("x", x);
					b.addProperty("y", y);
					b.addProperty("type", block.getType().toString());
					if (block.getData() != 0)
						b.addProperty("data", block.getData());
					blocks.add(b);
					JsonObject meta = new JsonObject();
					for (String key : block.getAllMetadata()){
						Object data = block.getMetadata(key);
						if (data instanceof String)
							meta.addProperty(key, (String)block.getMetadata(key));
						else if (data instanceof Number)
							meta.addProperty(key, (Number)block.getMetadata(key));
						else if (data instanceof Boolean)
							meta.addProperty(key, (Boolean)block.getMetadata(key));
						else if (data instanceof Character)
							meta.addProperty(key, (Character)block.getMetadata(key));
						else
							System.err.println("Failed to save metadata \"" + key + "\" for block at " +
									"{level=" + chunk.getLevel().getIndex() + ", x=" + block.getX() +
									", y=" + block.getY() + "}");
					}
					b.add("metadata", meta);
				}
			}
		}
		c.addProperty("biome", chunk.getBiome().getId());
		c.add("blocks", blocks);
		JsonArray entities = new JsonArray();
		for (Entity entity : chunk.getEntities()){
			JsonObject e = new JsonObject();
			e.addProperty("type", entity.getType().toString());
			e.addProperty("x", entity.getLocation().getPosInChunk());
			e.addProperty("y", entity.getY());
			e.addProperty("w", entity.width);
			e.addProperty("h", entity.height);
			e.addProperty("xv", entity.getVelocity().getX());
			e.addProperty("yv", entity.getVelocity().getY());
			if (entity instanceof Living){
				e.addProperty("living", true);
				Living le = (Living)entity;
				e.addProperty("fd", le.getFacingDirection().toString());
				e.addProperty("j", le.isJumping());
				if (le instanceof Mob){
					e.addProperty("mob", true);
					Mob m = (Mob)le;
					e.addProperty("pwd", m.getPlannedWalkDistance());
					e.addProperty("awd", m.getActualWalkDistance());
					e.addProperty("lx", m.getLastX());
				}
			}
			entities.add(e);
		}
		c.add("entities", entities);
		return c;
	}

	/**
	 * Saves a level to a JSON object. <strong>This method does not save the JSON to memory or to disk.</strong>
	 * @param level the chunk to save.
	 * @return the created JSON object.
	 */
	public static JsonObject saveLevel(Level level){
		JsonObject l = new JsonObject();
		JsonObject chunks = new JsonObject();
		JsonObject levels = (JsonObject)level.getWorld().getJson().get("levels");
		if (levels != null) {
			l = levels.get(Integer.toString(level.getIndex())).getAsJsonObject();
			chunks = l.get("chunks").getAsJsonObject();
		}
		for (Chunk chunk : level.chunks.values()){
			chunks.add(Integer.toString(chunk.getIndex()), saveChunk(chunk));
		}
		l.add("chunks", chunks);
		return l;
	}

	/**
	 * Saves a chunk to its world's {@link JsonObject JSON object}
	 * @param chunk the chunk to save.
	 */
	public static void saveChunkToMemory(Chunk chunk){
		JsonObject world = chunk.getLevel().getWorld().getJson();
		JsonObject levels = world.get("levels").getAsJsonObject();
		if (levels == null)
			levels = new JsonObject();
		JsonObject level = levels.get(Integer.toString(chunk.getLevel().getIndex())).getAsJsonObject();
		if (level == null)
			level = new JsonObject();
		JsonObject chunks = level.get("chunks").getAsJsonObject();
		if (chunks == null)
			chunks = new JsonObject();
		JsonObject jChunk = saveChunk(chunk);
		chunks.add(Integer.toString(chunk.getIndex()), jChunk);
		level.add("chunks", chunks);
		levels.add(Integer.toString(chunk.getLevel().getIndex()), level);
		world.add("levels", levels);
	}

	/**
	 * Saves a level to its world's {@link JsonObject JSON object}
	 * @param level the chunk to save.
	 */
	public static void saveLevelToMemory(Level level){
		JsonObject world = level.getWorld().getJson();
		JsonObject levels = (JsonObject)world.get("levels");
		if (levels == null)
			levels = new JsonObject();
		JsonObject jLevel = saveLevel(level);
		levels.add(Integer.toString(level.getIndex()), jLevel);
		world.add("levels", levels);
	}

	// this saves me a bit of casting and makes the code look nicer
	private static int longToInt(long number){
		return (int)number;
	}

	public static void prepareWorld(){
		VboUtil.updateArray();
		VboUtil.prepareBindArray();

		Thread chunkLoader = new Thread(new Runnable() {
			public void run() {
				while (true) {
					Chunk.handleChunkLoading();
					try {
						Thread.sleep(Chunk.LOAD_CHECK_INTERVAL);
					}
					catch (InterruptedException ex) {
						ex.printStackTrace();
					}
					if (Main.closed)
						return;
				}
			}
		});
		chunkLoader.start();
	}

}
