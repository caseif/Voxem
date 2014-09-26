package com.headswilllol.mineflat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.headswilllol.mineflat.noise.SimplexNoiseGenerator;

public class Terrain {

	/**
	 * The game's noise generator for use in terrain generation
	 */
	public static final SimplexNoiseGenerator noise = new SimplexNoiseGenerator(Main.world.seed);

	/**
	 * The level of variation the terrain should have
	 */
	public static final int terrainVariation = 13;

	public static void generateTerrain(){
		generateChunks();
		smoothTerrain();
		generateOres();
		generateCaves();
		plantGrass();
		plantTrees();
		lightTerrain();
	}

	public static void generateChunks(){
		System.out.println("Generating chunks...");
		//TODO: Save chunks to disk after generating so as not to keep them in memory
		for (Level l : Main.world.getLevels()){
			for (int i = Main.world.getChunkCount() / -2; i <= Main.world.getChunkCount() / 2; i++){
				if (!l.isChunkGenerated(i) && i != 0){
					Chunk c = new Chunk(l, i);
					for (int x = 0; x < Main.world.getChunkLength(); x++){
						int h = (int)Math.floor((
								noise.noise(Chunk.getWorldXFromChunkIndex(i, x)) / 2 + 0.5) *
								terrainVariation);
						int leftHeight = (int)Math.floor((
								noise.noise(Chunk.getWorldXFromChunkIndex(i, x) - 1) / 2 + 0.5) *
								terrainVariation);
						int rightHeight = (int)Math.floor((
								noise.noise(Chunk.getWorldXFromChunkIndex(i, x) + 1) / 2 + 0.5) *
								terrainVariation);
						h = (h + leftHeight + rightHeight) / 2;
						Material mat = null;
						for (int y = 0; y < Main.world.getChunkHeight(); y++){
							if (mat != Material.STONE){
								if (y < h)
									mat = Material.AIR;
								else if (y == h)
									mat = Material.GRASS;
								else if (y < 15)
									mat = Material.DIRT;
								else if (y >= 15 && y < 17){
									if ((int)(noise.noise(
											Chunk.getWorldXFromChunkIndex(c.getIndex(), x), y) + 1) == 0)
										mat = Material.STONE;
								}
								else if (y >= 17)
									mat = Material.STONE;
							}
							else if (y == Main.world.getChunkHeight() - 1)
								mat = Material.BEDROCK;
							Block b = new Block(
									mat, new Location(l, Chunk.getWorldXFromChunkIndex(c.getIndex(), x), y));
							b.addToWorld();
						}
					}
				}
			}
		}
	}

	public static void smoothTerrain(){
		System.out.println("Smoothing terrain...");
		for (Level l : Main.world.getLevels()){
			for (Chunk c : l.chunks.values()){
				for (int x = 0; x < Main.world.getChunkLength(); x++){
					int h = Block.getTop(new Location(l, Chunk.getWorldXFromChunkIndex(c.getIndex(), x), 0));
					int leftHeight = Block.getTop(new Location(l, Chunk.getWorldXFromChunkIndex(c.getIndex(), x) - 1, 0));
					if (leftHeight != -1){
						h = (h + leftHeight) / 2;
						if (leftHeight - h >= 3)
							h = leftHeight - 2;
						else if (leftHeight - h <= -3)
							h = leftHeight + 2;
						Material mat = null;
						for (int y = h; y < Main.world.getChunkHeight(); y++){
							if (mat != Material.STONE){
								if (y == h){
									mat = Material.GRASS;
									if (Block.isSolid(l, x, y + 1) &&
											c.getBlock(x, y + 1).getType() == Material.GRASS)
										c.getBlock(x, y + 1).setType(Material.DIRT);
								}
								else if (y < 15){
									mat = Material.DIRT;
								}
								else if (y >= 15 && y < 17){
									if ((int)(noise.noise(
											Chunk.getWorldXFromChunkIndex(c.getIndex(), x), y) + 1) == 0)
										mat = Material.STONE;
								}
								else if (y >= 17 && y < Main.world.getChunkHeight() - 1)
									mat = Material.STONE;
							}
							Block prev = new Location(l, Chunk.getWorldXFromChunkIndex(c.getIndex(), x), y)
									.getBlock();
							if (Block.isSolid(prev))
								prev.setType(mat);
							else {
								Block b = new Block(mat,
										new Location(l, Chunk.getWorldXFromChunkIndex(c.getIndex(), x), y));
								b.addToWorld();
							}
						}
					}
					if (Chunk.getWorldXFromChunkIndex(c.getIndex(), x) == Main.player.getX())
						Main.player.setY(h - 2);
				}
			}
		}
	}

	public static void generateOres(){
		System.out.println("Generating ores...");
		Random r = new Random(Main.world.seed);
		int coalChance = 15;
		int ironChance = 10;
		int goldChance = 2;
		int diamondChance = 1;
		for (Level l : Main.world.getLevels()){
			for (Chunk c : l.chunks.values()){
				for (int xx = 0; xx < Main.world.getChunkLength(); xx++){
					for (int yy = 0; yy < Main.world.getChunkHeight(); yy++){
						int x = (int)Chunk.getWorldXFromChunkIndex(c.getIndex(), xx);
						int y = yy;
						if (Block.isSolid(l, x, y) &&
								Block.getBlock(l, x, y).getType() == Material.STONE){
							int roll = r.nextInt(10000);
							Material vein = null;
							if (roll < coalChance)
								vein = Material.COAL_ORE;
							else if (roll >= coalChance &&
									roll < coalChance + ironChance &&
									yy >= 0.25 * Main.world.getChunkHeight())
								vein = Material.IRON_ORE;
							else if (roll >= coalChance + ironChance &&
									roll < coalChance + ironChance + goldChance &&
									yy >= 0.75 * Main.world.getChunkHeight())
								vein = Material.GOLD_ORE;
							else if (roll >= coalChance + ironChance + goldChance &&
									roll < coalChance + ironChance + goldChance +
											diamondChance && yy >= Main.world.getChunkHeight() - 16)
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
								Block.getBlock(l, x, y).setType(vein);
								for (int i = 1; i < maxSize; i++){
									List<Block> surrounding = new ArrayList<Block>();
									if (y > 0 &&
											Block.isSolid(l, x, y - 1) &&
											Block.getBlock(l, x, y - 1).getType() == Material.STONE)
										surrounding.add(Block.getBlock(l, x, y - 1));
									if (y < 126 &&
											Block.isSolid(l, x, y + 1) &&
											Block.getBlock(l, x, y + 1).getType() == Material.STONE)
										surrounding.add(Block.getBlock(l, x, y + 1));
									if (x > (Main.world.getChunkCount() / 2 + 1) *
											Main.world.getChunkLength() - 1 &&
											Block.isSolid(l, x - 1, y) &&
											Block.getBlock(l, x - 1, y).getType() == Material.STONE)
										surrounding.add(Block.getBlock(l, x - 1, y));
									if (x < (Main.world.getChunkCount() / 2 + 1) *
											Main.world.getChunkLength() - 1 &&
											Block.isSolid(l, x + 1, y) &&
											Block.getBlock(l, x + 1, y).getType() == Material.STONE)
										surrounding.add(Block.getBlock(l, x + 1, y));
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
		}
	}

	public static void generateCaves(){
		System.out.println("Generating caves...");
		for (Level l : Main.world.getLevels()){
			for (Chunk c : l.chunks.values()){
				if (CaveFactory.r.nextInt(2) == 0){
					int x = (int)Chunk.getWorldXFromChunkIndex(c.getIndex(),
							CaveFactory.r.nextInt(Main.world.getChunkLength()));
					new CaveFactory(new Location(l, x, Block.getTop(new Location(l, x, 0)) + 1));
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
			// analyze and improve cave systems
			for (Chunk c : l.chunks.values()){
				for (int xx = 0; xx < Main.world.getChunkLength(); xx++){
					int x = (int)Chunk.getWorldXFromChunkIndex(c.getIndex(), xx);
					for (int y = 0; y < Main.world.getChunkHeight(); y++){
						if (Block.isSolid(l, x, y)){
							List<Block> surrounding = new ArrayList<Block>();
							if (y > 0 && Block.isSolid(l, x, y - 1))
								surrounding.add(Block.getBlock(l, x, y - 1));
							if (y < Main.world.getChunkHeight() - 1 &&
									Block.isSolid(l, x, y + 1))
								surrounding.add(Block.getBlock(l, x, y + 1));
							if (x > (Main.world.getChunkCount() / 2 + 1) *
									-Main.world.getChunkLength() - 1 &&
									Block.isSolid(l, x - 1, y))
								surrounding.add(Block.getBlock(l, x - 1, y));
							if (x < (Main.world.getChunkCount() / 2 + 1) *
									Main.world.getChunkLength() - 1 &&
									Block.isSolid(l, x + 1, y))
								surrounding.add(Block.getBlock(l, x + 1, y));
							// remove lonely strands
							if (surrounding.size() == 1){
								if (surrounding.contains(Block.getBlock(l, x + 1, y)) ||
										(y < Main.world.getChunkHeight() - 1 &&
												surrounding.contains(Block.getBlock(l, x, y + 1)))){
									boolean vert = false;
									if (y < Main.world.getChunkHeight() - 1 &&
											surrounding.contains(Block.getBlock(l, x, y + 1)))
										vert = true;
									Block b = surrounding.get(0);
									boolean strand = false;
									List<Block> remove = new ArrayList<Block>();
									while (true){
										if (!vert){
											if (y <= 0 || Block.isAir(l, b.getX(), y - 1))
												if (y >= Main.world.getChunkHeight() - 1 ||
														Block.isAir(l, b.getX(), y + 1)){
													remove.add(b);
													if (Block.isAir(l, b.getX() + 1, y)){
														strand = true;
														break;
													}
													else {
														b = Block.getBlock(l, b.getX() + 1, y);
														continue;
													}
												}
											break;
										}
										else {
											if (Block.isAir(l, x - 1, b.getY()))
												if (Block.isAir(l, x + 1, b.getY())){
													remove.add(b);
													if (y >= Main.world.getChunkHeight() - 1 ||
															Block.isAir(l, x, b.getY() + 1)){
														strand = true;
														break;
													}
													else {
														b = Block.getBlock(l, x, b.getY() + 1);
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
							if (y > 0 && !Block.isAir(l, x, y - 1))
								surrounding.add(Block.getBlock(l, x, y - 1));
							if (y < Main.world.getChunkHeight() - 1 &&
									!Block.isAir(l, x, y + 1))
								surrounding.add(Block.getBlock(l, x, y + 1));
							if (x > Main.world.getChunkCount() / 2 * -Main.world.getChunkLength() &&
									!Block.isAir(l, x - 1, y))
								surrounding.add(Block.getBlock(l, x - 1, y));
							if (x < (Main.world.getChunkCount() / 2 + 1) *
									Main.world.getChunkLength() - 1 &&
									!Block.isAir(l, x + 1, y))
								surrounding.add(Block.getBlock(l, x + 1, y));
							// remove lonely blocks
							if (surrounding.size() == 0)
								Block.getBlock(l, x, y).destroy();
								// remove lonely islands
							else if (surrounding.size() == 3){
								for (Block b : surrounding){
									List<Block> surround = new ArrayList<Block>();
									if (b.getY() > 0  &&
											!Block.isAir(l, b.getX(), b.getY() - 1))
										surround.add(Block.getBlock(l, b.getX(), b.getY() - 1));
									if (b.getY() < Main.world.getChunkHeight() - 1 &&
											!Block.isAir(l, b.getX(), b.getY() + 1))
										surround.add(Block.getBlock(l, b.getX(), b.getY() + 1));
									if (b.getX() > Main.world.getChunkCount() / 2 *
											-Main.world.getChunkLength() &&
											!Block.isAir(l, b.getX() - 1, b.getY()))
										surround.add(Block.getBlock(l, b.getX() - 1, b.getY()));
									if (b.getX() < (Main.world.getChunkCount() / 2 + 1) *
											Main.world.getChunkLength() - 1 &&
											!Block.isAir(l, b.getX() + 1, b.getY()))
										surround.add(Block.getBlock(l, b.getX() + 1, b.getY()));
									int lonely = 0;
									for (Block bl : surround){
										if (surrounding.contains(bl) || b.equals(Block.getBlock(l, x, y)))
											lonely += 1;
									}
									if (lonely == 3){
										for (Block bl : surround)
											bl.destroy();
										Block.getBlock(l, x, y).destroy();
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public static void plantGrass(){
		System.out.println("Planting grass...");
		for (Level l : Main.world.getLevels()){
			for (Chunk c : l.chunks.values()){
				for (int x = 0; x < Main.world.getChunkLength(); x++){
					int y = Block.getTop(new Location(l, Chunk.getWorldXFromChunkIndex(c.getIndex(), x), 0));
					Block b = Block.getBlock(l, Chunk.getWorldXFromChunkIndex(c.getIndex(), x), y);
					if (b.getType() == Material.DIRT)
						b.setType(Material.GRASS);
				}
			}
		}
	}

	public static void plantTrees(){
		System.out.println("Planting trees...");
		Random r = new Random(Main.world.seed);
		for (Level l : Main.world.getLevels()){
			for (Chunk c : l.chunks.values()){
				for (int x = 0; x < Main.world.getChunkLength(); x++){
					if (r.nextInt(12) == 0){ // plant a tree
						l.plantTree((int)Chunk.getWorldXFromChunkIndex(c.getIndex(), x),
								Block.getTop(new Location(l, Chunk.getWorldXFromChunkIndex(c.getIndex(), x), 0)));
					}
				}
			}
		}
	}

	public static void lightTerrain(){
		for (Level l : Main.world.getLevels()){
			for (Chunk c : l.chunks.values()){
				c.updateLight();
			}
		}
	}

}
