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

import com.google.gson.JsonObject;
import com.headswilllol.mineflat.Main;
import com.headswilllol.mineflat.entity.Entity;
import com.headswilllol.mineflat.util.VboUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Chunk {

	public final Object lockAndLoad = new Object(); // hehe

	protected final Level level;
	protected final int index;

	private Block[][] blocks;

	private Biome biome = Biome.HILLS;

	// the player's position when the game last tried to load new chunks
	private static float lastX = Float.NaN;
	// when the game last tried to load new chunks
	private static long lastLoadCheck = 0L;
	// how often the game should try to load new chunks
	public static final long LOAD_CHECK_INTERVAL = 1000L;

	// the number of chunks to keep loaded at a time (will be incremented if even)
	private static final int CHUNKS_TO_LOAD = 5;

	public Chunk(Level level, int num, Biome biome){
		this.level = level;
		this.index = num;
		this.biome = biome;
		blocks = new Block[Main.world.getChunkLength()][Main.world.getChunkHeight()];
		level.chunks.put(num, this);
	}

	public Chunk(Level level, int num){
		this(level, num, Biome.SNOWY_HILLS);
	}

	public Level getLevel(){
		return level;
	}

	public int getIndex(){
		return index;
	}

	public Block getBlock(int x, int y){
		if (x >= 0 && x < Main.world.getChunkLength() && y >= 0 && y < Main.world.getChunkHeight())
			return blocks[x][y];
		return null;
	}

	public Block[][] getBlocks(){
		return blocks;
	}

	public void setBlock(int x, int y, Block b){
		blocks[x][y] = b;
	}

	public Biome getBiome(){
		return this.biome;
	}

	public void setBiome(Biome biome){
		this.biome = biome;
	}

	public void updateLight(){
		for (int x = 0; x < Main.world.getChunkLength(); x++)
			for (int y = 0; y < Main.world.getChunkHeight(); y++)
				if (this.getBlock(x, y) != null && Block.isSolid(level, x, y)) this.getBlock(x, y).setLightLevel(0);
		for (int i = 0; i < 2; i++){
			for (int xx = 0; xx < Main.world.getChunkLength(); xx++)
				for (int yy = 0; yy < Main.world.getChunkHeight(); yy++){
					int x = i == 0 ? xx : 15 - xx;
					int y = i == 0 ? yy : Main.world.getChunkHeight() - 1 - yy;
					Block b = this.getBlock(x, y);
					if (b != null){
						if (b.getY() <= Block.getTop(new Location(level, b.getX(), 0)))
							b.setLightLevel(Block.maxLight);
						else {
							Block up = null, down = null, left, right;
							if (b.getY() > 0)
								up = Block.getBlock(level, b.getX(), b.getY() - 1);
							if (b.getY() < Main.world.getChunkHeight() - 1)
								down = Block.getBlock(level, b.getX(), b.getY() +
										1);
							left = Block.getBlock(level, b.getX() - 1, b.getY());
							right = Block.getBlock(level, b.getX() + 1, b.getY());
							Block[] adjacent = new Block[]{up, down, left, right};
							float average = 0;
							int total = 0;
							for (Block bl : adjacent){
								if (bl != null){
									average += bl.getLightLevel();
									total += 1;
								}
							}
							average /= total;
							if (!Block.isSolid(b)){
								b.setLightLevel((int)Math.floor(average));
							}
							else if ((int)Math.floor(average) - Block.lightDistance >= Block.minLight)
								b.setLightLevel((int)Math.floor(average) - Block.lightDistance);
							else
								b.setLightLevel(Block.minLight);
						}
					}
				}
		}
	}

	public static float getWorldXFromChunkIndex(int chunk, float block){
		return chunk > 0 ? (chunk - 1) * Main.world.getChunkLength() + block :
			   chunk * Main.world.getChunkLength() + block;
	}

	public static int getChunkNum(int x){
		int add = 1;
		if (x < 0)
			add = -1;
		return x / Main.world.getChunkLength() + add;
	}

	public static int getIndexInChunk(int x){
		return Math.abs(x < 0 ? (x % Main.world.getChunkLength() == 0 ? 0 :
								 Main.world.getChunkLength() - Math.abs(x % Main.world.getChunkLength())) :
						x % Main.world.getChunkLength());
		//return Math.abs(x % Main.world.getChunkLength());
	}

	public Collection<Entity> getEntities() {
		List<Entity> entities = new ArrayList<>();
		for (Entity e : level.getEntities())
			if (e.getLocation().getChunk() == this.getIndex())
				entities.add(e);
		return entities;
	}

	public JsonObject save(){
		return SaveManager.saveChunk(this);
	}

	public void saveToMemory(){
		SaveManager.saveChunkToMemory(this);
	}

	/**
	 * Unloads the chunk from memory. This implicitly parses the object into JSON and saves it to memory as such.
	 */
	public void unload(){
		synchronized(this.lockAndLoad){
			saveToMemory();
			level.chunks.remove(this.getIndex());
		}
	}

	/**
	 * Checks for and loads into memory chunks within appropriate range of the player
	 * (as defined by {@link Chunk#CHUNKS_TO_LOAD}.
	 */
	public static void handleChunkLoading(){
		handleChunkLoading(false);
	}

	/**
	 * Checks for and loads into memory chunks within appropriate range of the player
	 * (as defined by {@link Chunk#CHUNKS_TO_LOAD}.
	 * @param doNotRender Whether the method should skip updating the VBOs. This should
	 * only be used the first time chunks are loaded into memory, when the game is
	 * initially launched.
	 */
	public static void handleChunkLoading(boolean doNotRender) {
		if (System.currentTimeMillis() - lastLoadCheck >= LOAD_CHECK_INTERVAL) {
			lastLoadCheck = System.currentTimeMillis();
			if (Main.player.getX() != lastX) { // get whether player has moved
				int lastChunk = getChunkNum((int)lastX);
				int currentChunk = getChunkNum((int)Main.player.getX());
				if (lastChunk != currentChunk || lastX != lastX){
					int minChunk = currentChunk - CHUNKS_TO_LOAD / 2;
					int maxChunk = currentChunk + CHUNKS_TO_LOAD / 2;
					if ((minChunk > 0) != (maxChunk > 0))
						// range passes through x=0 so we need to compensate for no chunk 0
						if (Main.player.getX() >= 0)
							minChunk -= 1;
						else
							maxChunk += 1;
					List<Chunk> unloadChunks = new ArrayList<>();
					boolean rebind = false;
					for (Chunk c : Main.player.getLevel().chunks.values()) {
						if (c.getIndex() < minChunk || c.getIndex() > maxChunk){
							unloadChunks.add(c);
							rebind = true;
						}
					}
					for (Chunk c : unloadChunks) {
						c.unload();
						if (!doNotRender)
							VboUtil.updateChunkArray(c.getLevel(), c.getIndex());
					}
					for (int i = minChunk; i <= maxChunk; i++){
						if (i != 0) {
							if (Main.player.getLevel().getChunk(i) == null) {
								Chunk c = SaveManager.loadChunk(Main.player.getLevel(), i);
								if (c != null) {
									c.updateLight();
									if (!doNotRender){
										rebind = true;
										VboUtil.updateChunkArray(Main.player.getLevel(), i);
									}
									Chunk adj1 = c.getLevel().getChunk(i == -1 ? 1 : i + 1);
									if (adj1 != null){
										adj1.updateLight();
										if (!doNotRender)
											VboUtil.updateChunkArray(Main.player.getLevel(), c.getIndex());
									}
									Chunk adj2 = c.getLevel().getChunk(i == 1 ? -1 : i - 1);
									if (adj2 != null){
										adj2.updateLight();
										if (!doNotRender)
											VboUtil.updateChunkArray(Main.player.getLevel(), c.getIndex());
									}
								}
							}
						}
					}
					if (rebind && !doNotRender)
						VboUtil.prepareBindArray();
				}
			}
			lastX = Main.player.getX();
		}
	}

}
