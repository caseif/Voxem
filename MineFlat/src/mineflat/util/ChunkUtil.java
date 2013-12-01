package mineflat.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mineflat.Block;
import mineflat.CaveFactory;
import mineflat.Chunk;
import mineflat.Location;
import mineflat.Material;
import mineflat.MineFlat;

public class ChunkUtil {

	/**
	 * The number of chunks to be included in a world
	 */
	public static int totalChunks = 32;

	public static void generateChunks(){
		System.out.println("Generating chunks...");
		//TODO: Save chunks to disk after generating so as not to keep them in memory
		for (int i = totalChunks * -1 / 2; i <= totalChunks / 2; i++){
			if (!isChunkGenerated(i)){
				Chunk c = new Chunk(i);
				for (int x = 0; x < 16; x++){
					int h = (int)Math.floor((
							MineFlat.noise.noise(getBlockXFromChunk(i, x)) / 2 + 0.5) *
							MineFlat.terrainVariation);
					int leftHeight = (int)Math.floor((
							MineFlat.noise.noise(getBlockXFromChunk(i, x) - 1) / 2 + 0.5) *
							MineFlat.terrainVariation);
					int rightHeight = (int)Math.floor((
							MineFlat.noise.noise(getBlockXFromChunk(i, x) + 1) / 2 + 0.5) *
							MineFlat.terrainVariation);
					h = (h + leftHeight + rightHeight) / 2;
					Material mat = null;
					for (int y = h; y < 128; y++){
						if (mat != Material.STONE){
							if (y == h){
								mat = Material.GRASS;
							}
							else if (y < 15)
								mat = Material.DIRT;
							else if (y >= 15 && y < 17 && mat != Material.STONE){
								if ((int)(MineFlat.noise.noise(
										ChunkUtil.getBlockXFromChunk(c.getNum(), x), y) + 1) == 0)
									mat = Material.STONE;
							}
							else if (y >= 17)
								mat = Material.STONE;
						}
						Block b = new Block(
								mat, new Location(getBlockXFromChunk(c.getNum(), x), y));
						b.addToWorld();
					}
				}
			}
		}
		// smoothing pass
		System.out.println("Smoothing terrain...");
		for (Chunk c : Chunk.chunks){
			for (int x = 0; x < 16; x++){
				int h = BlockUtil.getTop(ChunkUtil.getBlockXFromChunk(c.getNum(), x));
				int leftHeight = BlockUtil.getTop(ChunkUtil.getBlockXFromChunk(c.getNum(), x) - 1);
				if (leftHeight != -1){
					h = (h + leftHeight) / 2;
					if (leftHeight - h >= 3)
						h = leftHeight - 2;
					else if (leftHeight - h <= -3)
						h = leftHeight + 2;
					Material mat = null;
					for (int y = 0; y < 128; y++){
						if (y >= h){
							if (mat != Material.STONE || y == 127){
								if (y == h){
									mat = Material.GRASS;
									if (c.getBlock(x, y + 1) != null && 
											c.getBlock(x, y + 1).getType() == Material.GRASS)
										c.getBlock(x, y + 1).setType(Material.DIRT);
								}
								else if (y < 15){
									mat = Material.DIRT;
								}
								else if (y >= 15 && y < 17 && mat != Material.STONE){
									if ((int)(MineFlat.noise.noise(
											ChunkUtil.getBlockXFromChunk(c.getNum(), x), y) + 1) == 0)
										mat = Material.STONE;
								}
								else if (y >= 17 && y < 127)
									mat = Material.STONE;
								else
									mat = Material.BEDROCK;
							}
							if (mat != null){
								Block prev = new Location(getBlockXFromChunk(c.getNum(), x), y)
								.getBlock();
								if (prev != null)
									prev.setType(mat);
								else {
									Block b = new Block(mat,
											new Location(getBlockXFromChunk(c.getNum(), x), y));
									b.addToWorld();
								}
							}
						}
						else if (c.getBlock(x, y) != null)
							c.getBlock(x, y).destroy();
					}
				}
				if (ChunkUtil.getBlockXFromChunk(c.getNum(), x) == MineFlat.player.getX())
					MineFlat.player.setY(h - 2);
			}
		}
		// add grass to top layer of terrain
		System.out.println("Planting grass...");
		for (Chunk c : Chunk.chunks){
			for (int x = 0; x < 16; x++){
				for (int y = 0; y < 128; y++){
					if (c.getBlock(x, y) != null){
						if (c.getBlock(x, y).getType() != Material.GRASS){
							Block b = new Block(Material.GRASS, c.getBlock(x, y).getLocation());
							b.addToWorld();
						}
						break;
					}
				}
			}
		}
		// generate ore veins
		System.out.println("Generating ores...");
		Random r = new Random(MineFlat.seed);
		int coalChance = 15;
		int ironChance = 10;
		int goldChance = 2;
		int diamondChance = 1;
		for (Chunk c : Chunk.chunks){
			for (int xx = 0; xx < 16; xx++){
				for (int yy = 0; yy < 128; yy++){
					int x = ChunkUtil.getBlockXFromChunk(c.getNum(), xx);
					int y = yy;
					if (!BlockUtil.isBlockEmpty(BlockUtil.getBlock(x, y)) &&
							BlockUtil.getBlock(x, y).getType() == Material.STONE){
						int roll = r.nextInt(10000);
						Material vein = null;
						if (roll < coalChance)
							vein = Material.COAL_ORE;
						else if (roll >= coalChance &&
								roll < coalChance + ironChance && yy >= 32)
							vein = Material.IRON_ORE;
						else if (roll >= coalChance + ironChance &&
								roll < coalChance + ironChance + goldChance && yy >= 96)
							vein = Material.GOLD_ORE;
						else if (roll >= coalChance + ironChance + goldChance &&
								roll < coalChance + ironChance + goldChance +
								diamondChance && yy >= 112)
							vein = Material.DIAMOND_ORE;
						if (vein != null){
							int maxSize = 0;
							if (vein == Material.COAL_ORE)
								maxSize = 16;
							else if (vein == Material.IRON_ORE)
								maxSize = 8;
							else if (vein == Material.GOLD_ORE)
								maxSize = 5;
							else if (vein == Material.DIAMOND_ORE)
								maxSize = 4;
							BlockUtil.getBlock(x, y).setType(vein);
							for (int i = 1; i < maxSize; i++){
								List<Block> surrounding = new ArrayList<Block>();
								if (y > 0 &&
										!BlockUtil.isBlockEmpty(BlockUtil.getBlock(x, y - 1)) &&
										BlockUtil.getBlock(x, y - 1).getType() == Material.STONE)
									surrounding.add(BlockUtil.getBlock(x, y - 1));
								if (y < 126 &&
										!BlockUtil.isBlockEmpty(BlockUtil.getBlock(x, y + 1)) &&
										BlockUtil.getBlock(x, y + 1).getType() == Material.STONE)
									surrounding.add(BlockUtil.getBlock(x, y + 1));
								if (x > ChunkUtil.totalChunks / 2 * -16 &&
										!BlockUtil.isBlockEmpty(BlockUtil.getBlock(x - 1, y)) &&
										BlockUtil.getBlock(x - 1, y).getType() == Material.STONE)
									surrounding.add(BlockUtil.getBlock(x - 1, y));
								if (x < ChunkUtil.totalChunks / 2 * 16 + 15 &&
										!BlockUtil.isBlockEmpty(BlockUtil.getBlock(x + 1, y)) &&
										BlockUtil.getBlock(x + 1, y).getType() == Material.STONE)
									surrounding.add(BlockUtil.getBlock(x + 1, y));
								if (surrounding.size() == 0)
									break;
								Block b = surrounding.get(r.nextInt(surrounding.size()));
								x = b.getX();
								y = b.getY();
								b.setType(vein);
							}
						}
					}
				}
			}
		}
		// generate primitive caves
		System.out.println("Generating caves...");
		for (Chunk c : Chunk.chunks){
			if (CaveFactory.r.nextInt(2) == 0){
				int x = ChunkUtil.getBlockXFromChunk(c.getNum(), CaveFactory.r.nextInt(16));
				new CaveFactory(x, BlockUtil.getTop(x) + 1);
			}
		}
		while (CaveFactory.caveFactories.size() > 0){
			for (int i = 0; i < CaveFactory.caveFactories.size(); i++)
				CaveFactory.caveFactories.get(i).dig();
			for (CaveFactory cf : CaveFactory.deactivate)
				CaveFactory.caveFactories.remove(cf);
			CaveFactory.deactivate.clear();
		}
		CaveFactory.caveFactories.clear();
		CaveFactory.caveFactories = null;
		// improve caves
		System.out.println("Cleaning up...");
		for (Chunk c : Chunk.chunks){
			for (int xx = 0; xx < 16; xx++){
				int x = ChunkUtil.getBlockXFromChunk(c.getNum(), xx);
				for (int y = 0; y < 128; y++){
					if (!BlockUtil.isBlockEmpty(BlockUtil.getBlock(x, y))){
						List<Block> surrounding = new ArrayList<Block>();
						if (y > 0 && !BlockUtil.isBlockEmpty(BlockUtil.getBlock(x, y - 1)))
							surrounding.add(BlockUtil.getBlock(x, y - 1));
						if (y < 127 && !BlockUtil.isBlockEmpty(BlockUtil.getBlock(x, y + 1)))
							surrounding.add(BlockUtil.getBlock(x, y + 1));
						if (x > ChunkUtil.totalChunks / 2 * -16 &&
								!BlockUtil.isBlockEmpty(BlockUtil.getBlock(x - 1, y)))
							surrounding.add(BlockUtil.getBlock(x - 1, y));
						if (x < ChunkUtil.totalChunks / 2 * 16 + 15 &&
								!BlockUtil.isBlockEmpty(BlockUtil.getBlock(x + 1, y)))
							surrounding.add(BlockUtil.getBlock(x + 1, y));
						// remove lonely strands
						if (surrounding.size() == 1){
							if (surrounding.contains(BlockUtil.getBlock(x + 1, y)) ||
									(y < 127 &&
											surrounding.contains(BlockUtil.getBlock(x, y + 1)))){
								boolean vert = false;
								if (y < 127 &&
										surrounding.contains(BlockUtil.getBlock(x, y + 1)))
									vert = true;
								Block b = surrounding.get(0);
								boolean strand = false;
								List<Block> remove = new ArrayList<Block>();
								while (true){
									if (!vert){
										if (y <= 0 || BlockUtil.isBlockEmpty(
												BlockUtil.getBlock(b.getX(), y - 1)))
											if (y >= 127 || BlockUtil.isBlockEmpty(
													BlockUtil.getBlock(b.getX(), y + 1))){
												remove.add(b);
												if (BlockUtil.isBlockEmpty(
														BlockUtil.getBlock(b.getX() + 1, y))){
													strand = true;
													break;
												}
												else {	
													b = BlockUtil.getBlock(b.getX() + 1, y);
													continue;
												}
											}
										break;
									}
									else {
										if (BlockUtil.isBlockEmpty(
												BlockUtil.getBlock(x - 1, b.getY())))
											if (BlockUtil.isBlockEmpty(
													BlockUtil.getBlock(x + 1, b.getY()))){
												remove.add(b);
												if (y >= 127 || BlockUtil.isBlockEmpty(
														BlockUtil.getBlock(x, b.getY() + 1))){
													strand = true;
													break;
												}
												else {
													b = BlockUtil.getBlock(x, b.getY() + 1);
													continue;
												}
											}
										break;
									}
								}
								if (strand)
									for (Block bl : remove){
										bl.destroy();
									}
								remove.clear();
							}
						}
						// recalculate because it's easier than actually fixing the problem
						surrounding.clear();
						if (y > 0 && !BlockUtil.isBlockEmpty(BlockUtil.getBlock(x, y - 1)))
							surrounding.add(BlockUtil.getBlock(x, y - 1));
						if (y < 127 && !BlockUtil.isBlockEmpty(BlockUtil.getBlock(x, y + 1)))
							surrounding.add(BlockUtil.getBlock(x, y + 1));
						if (x > ChunkUtil.totalChunks / 2 * -16 &&
								!BlockUtil.isBlockEmpty(BlockUtil.getBlock(x - 1, y)))
							surrounding.add(BlockUtil.getBlock(x - 1, y));
						if (x < ChunkUtil.totalChunks / 2 * 16 + 15 &&
								!BlockUtil.isBlockEmpty(BlockUtil.getBlock(x + 1, y)))
							surrounding.add(BlockUtil.getBlock(x + 1, y));
						// remove lonely blocks
						if (surrounding.size() == 0)
							BlockUtil.getBlock(x, y).destroy();
						// remove lonely islands
						else if (surrounding.size() == 3){
							for (Block b : surrounding){
								List<Block> surround = new ArrayList<Block>();
								if (b.getY() > 0 && BlockUtil.getBlock(b.getX(), b.getY() - 1)
										!= null && BlockUtil.getBlock(b.getX(),
												b.getY() - 1).getType() != Material.AIR)
									surround.add(BlockUtil.getBlock(b.getX(), b.getY() - 1));
								if (b.getY() < 127 && BlockUtil.getBlock(b.getX(), b.getY() + 1)
										!= null && BlockUtil.getBlock(b.getX(),
												b.getY() + 1).getType() != Material.AIR)
									surround.add(BlockUtil.getBlock(b.getX(), b.getY() + 1));
								if (b.getX() > ChunkUtil.totalChunks / 2 * -16 &&
										BlockUtil.getBlock(b.getX() - 1, b.getY()) != null &&
										BlockUtil.getBlock(b.getX() - 1,
												b.getY()).getType() != Material.AIR)
									surround.add(BlockUtil.getBlock(b.getX() - 1, b.getY()));
								if (b.getX() < ChunkUtil.totalChunks / 2 * 16 + 15 &&
										BlockUtil.getBlock(b.getX() + 1, b.getY()) != null &&
										BlockUtil.getBlock(b.getX() + 1,
												b.getY()).getType() != Material.AIR)
									surround.add(BlockUtil.getBlock(b.getX() + 1, b.getY()));
								int lonely = 0;
								for (Block bl : surround){
									if (surrounding.contains(bl) || b.equals(BlockUtil.getBlock(x, y)))
										lonely += 1;
								}
								if (lonely == 3){
									for (Block bl : surround)
										bl.destroy();
									BlockUtil.getBlock(x, y).destroy();
								}
							}
						}
					}
				}
			}
		}
		// calculate light levels of all blocks
		System.out.println("Lighting terrain...");
		for (Chunk c : Chunk.chunks)
			c.updateLight();
	}

	public static Chunk getChunk(int i){
		for (Chunk c : Chunk.chunks){
			if (c.getNum() == i)
				return c;
		}
		return null;
	}

	public static boolean isChunkGenerated(int i){
		return getChunk(i) != null;
	}

	public static int getBlockXFromChunk(int chunk, int block){
		return chunk > 0 ? (chunk - 1) * 16 + block : chunk * 16 + block;
	}

	public static int getChunkNum(int x){
		int add = 1;
		if (x < 0)
			add = -1;
		return x / 16 + add;
	}

}
