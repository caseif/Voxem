package com.headswilllol.mineflat;

import com.headswilllol.mineflat.entity.Entity;
import com.headswilllol.mineflat.util.VboUtil;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Chunk {

	protected final Level level;
	protected final int index;

	private Block[][] blocks;

	// the player's position when the game last tried to load new chunks
	private static float lastX = Float.NaN;
	// when the game last tried to load new chunks
	private static long lastLoadCheck = 0L;
	// how often the game should try to load new chunks
	public static final long LOAD_CHECK_INTERVAL = 1000L;

	private static final int CHUNKS_TO_LOAD = 7; // the number of chunks to keep loaded at a time (will be incremented if even)

	public Chunk(Level level, int num){
		this.level = level;
		this.index = num;
		blocks = new Block[Main.world.getChunkLength()][Main.world.getChunkHeight()];
		level.chunks.put(num, this);
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
		List<Entity> entities = new ArrayList<Entity>();
		for (Entity e : level.getEntities())
			if (e.getLocation().getChunk() == this.getIndex())
				entities.add(e);
		return entities;
	}

	public JSONObject save(){
		return SaveManager.saveChunk(this);
	}

	public void saveToMemory(){
		SaveManager.saveChunkToMemory(this);
	}

	/**
	 * Unloads the chunk from memory. This implicitly parses the object into JSON and saves it to memory as such.
	 */
	public void unload(){
		saveToMemory();
		level.chunks.remove(this.getIndex());
		System.out.println("unloaded " + this.getIndex());
	}

	public static void handleChunkLoading(){
		handleChunkLoading(false);
	}

	public static void handleChunkLoading(boolean doNotRender) {
		if (System.currentTimeMillis() - lastLoadCheck >= LOAD_CHECK_INTERVAL) {
			System.out.println("========================= checking =========================");
			lastLoadCheck = System.currentTimeMillis();
			if (Main.player.getX() != lastX) { // get whether player has moved
				System.out.println("player moved");
				int lastChunk = getChunkNum((int)lastX);
				int currentChunk = getChunkNum((int)Main.player.getX());
				System.out.println("last chunk: " + lastChunk);
				System.out.println("current chunk: " + currentChunk);
				if (lastChunk != currentChunk || lastX != lastX){
					System.out.println("chunk changed");
					int minChunk = currentChunk - CHUNKS_TO_LOAD / 2;
					int maxChunk = currentChunk + CHUNKS_TO_LOAD / 2;
					if ((minChunk > 0) != (maxChunk > 0)) // range passes through x=0 so we need to compensate for no chunk 0
						if (Main.player.getX() >= 0)
							minChunk -= 1;
						else
							maxChunk += 1;
					System.out.println("Loading chunks " + minChunk + " through " + maxChunk);
					List<Chunk> unloadChunks = new ArrayList<Chunk>();
					boolean rebind = false;
					for (Chunk c : Main.player.getLevel().chunks.values()) {
						System.out.println("checking " + c.getIndex() + " for unload");
						if (c.getIndex() < minChunk || c.getIndex() > maxChunk){
							System.out.println("unloading " + c.getIndex());
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
							System.out.println("checking " + i + " for load");
							if (Main.player.getLevel().getChunk(i) == null) {
								System.out.println(i + " is not loaded");
								SaveManager.loadChunk(Main.player.getLevel(), i);
								rebind = true;
								if (!doNotRender)
									VboUtil.updateChunkArray(Main.player.getLevel(), i);
							}
						}
					}
					if (rebind && !doNotRender)
						VboUtil.prepareBindArray();
				}
			}
			lastX = Main.player.getX();
			System.out.println("======================= done checking ======================");
		}
	}

}
