package com.headswilllol.mineflat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.headswilllol.mineflat.util.FileUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class SaveManager {

	/**
	 * Saves all loaded chunks in the current world.
	 */
	@SuppressWarnings("unchecked")
	public static void saveWorld(){

		System.out.println("Saving chunks...");

		JSONObject save = new JSONObject();
		save.put("name", Main.world.getName());
		save.put("createTime", System.currentTimeMillis() / 1000L);
		save.put("modifyTime", System.currentTimeMillis() / 1000L);
		save.put("chunkCount", Main.world.getChunkCount());
		save.put("chunkLength", Main.world.getChunkLength());
		save.put("chunkHeight", Main.world.getChunkHeight());
		save.put("seed", Main.world.getSeed());
		save.put("ticks", TickManager.getTicks());

		JSONArray levels = new JSONArray();
		for (Level level : Main.world.getLevels()){
			JSONObject l = new JSONObject();
			l.put("index", level.getIndex());
			JSONArray chunks = new JSONArray();
			for (Chunk chunk : level.chunks.values()){
				JSONObject c = new JSONObject();
				c.put("index", chunk.getNum());
				JSONArray blocks = new JSONArray();
				for (int x = 0; x < Main.world.getChunkLength(); x++){
					for (int y = 0; y < Main.world.getChunkHeight(); y++){
						Block block = chunk.getBlock(x, y);
						if (block != null){
							JSONObject b = new JSONObject();
							b.put("x", x);
							b.put("y", y);
							b.put("type", block.getType().toString());
							//TODO: store data values
							blocks.add(b);
							JSONObject meta = new JSONObject();
							for (String key : block.getAllMetadata())
								meta.put(key, block.getMetadata(key));
							b.put("metadata", meta);
						}
					}
				}
				//TODO: iterate entities
				c.put("blocks", blocks);
				chunks.add(c);
			}
			l.put("chunks", chunks);
			levels.add(l);
		}
		save.put("levels", levels);

		File saveFolder = new File(FileUtil.getAppDataFolder() + File.separator +
				".mineflat", "saves");
		saveFolder = new File(saveFolder, Main.world.getName());
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
			JsonElement je = new JsonParser().parse(save.toJSONString());
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
			JSONObject save = (JSONObject)new JSONParser().parse(new FileReader(saveFile));
			Main.world = new World((String)save.get("name"), longToInt((Long)save.get("chunkCount")),
					longToInt((Long)save.get("chunkLength")), longToInt((Long)save.get("chunkHeight")));
			Main.world.seed = (Long)save.get("seed");
			TickManager.setTicks(longToInt((Long)save.get("ticks")));
			for (Object levelObj : (JSONArray)save.get("levels")){
				JSONObject level = (JSONObject)levelObj;
				Main.world.addLevel(longToInt((Long)level.get("index")));
				Level l = Main.world.getLevel(longToInt((Long)level.get("index")));
				for (Object chunkObj : (JSONArray)level.get("chunks")){
					JSONObject chunk = (JSONObject)chunkObj;
					int cIndex = longToInt((Long)chunk.get("index"));
					Chunk c = new Chunk(l, cIndex);
					l.chunks.put(cIndex, c);
					for (Object blockObj : (JSONArray)chunk.get("blocks")){
						JSONObject block = (JSONObject)blockObj;
						Material type = Material.valueOf((String)block.get("type"));
						if (type == null)
							type = Material.AIR;
						Block b = new Block(type, new Location(l, Chunk.getBlockXFromChunk(cIndex, longToInt((Long)block.get("x"))),
								longToInt((Long)block.get("y"))));
						JSONObject meta = (JSONObject)block.get("metadata");
						for (Object key : meta.keySet())
							b.setMetadata((String)key, meta.get(key));
						b.addToWorld();
					}
				}
				for (Chunk c : l.chunks.values())
					c.updateLight();
			}
		}
		catch (FileNotFoundException ex){
			ex.printStackTrace();
			System.err.println("Failed to load world from disk - save file cannot be found!");
		}
		catch (IOException ex){
			ex.printStackTrace();
			System.err.println("Failed to load world from disk - save file cannot be read!");
		}
		catch (ParseException ex){
			ex.printStackTrace();
			System.err.println("Failed to load world from disk - save file is invalid!");
		}
		catch (ClassCastException ex){
			ex.printStackTrace();
			System.err.println("Failed to load world from disk - save file is invalid!");
		}
		finally {
			saveFile.delete();
		}
	}

	private static int longToInt(long number){
		return Integer.parseInt(Long.toString(number));
	}

}
