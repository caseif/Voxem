package mineflat.util;

import mineflat.Block;
import mineflat.Chunk;
import mineflat.Location;
import mineflat.Material;
import mineflat.MineFlat;

public class ChunkUtil {

	/**
	 * The number of chunks to be included in a world
	 */
	public static int totalChunks = 16;

	public static void generateChunks(){
		System.out.println("Generating chunks...");
		//TODO: Save chunks to disk after generating so as not to keep them in memory
		for (int i = totalChunks * -1 / 2; i <= totalChunks / 2; i++){
			//System.out.println("Generating chunk " + i);
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
					for (int y = h; y < 128; y++){
						Material mat = Material.STONE;
						new Block(mat, new Location(getBlockXFromChunk(c.getNum(), x), y));
					}
				}
			}
		}
		// second smoothing pass
		System.out.println("Smoothing terrain...");
		for (Chunk c : Chunk.chunks){
			//System.out.println("Smoothing chunk " + c.getNum());
			for (int x = 0; x < 16; x++){
				int h = BlockUtil.getTop(ChunkUtil.getBlockXFromChunk(c.getNum(), x));
				int leftHeight = BlockUtil.getTop(ChunkUtil.getBlockXFromChunk(c.getNum(), x) - 1);
				if (leftHeight != -1){
					h = (h + leftHeight) / 2;
					if (leftHeight - h >= 3)
						h = leftHeight - 2;
					else if (leftHeight - h <= -3)
						h = leftHeight + 2;
					Material mat = Material.AIR;
					for (int y = 0; y < 128; y++){
						if (y >= h){
							if (mat != Material.STONE){
								if (y == h)
									mat = Material.GRASS;
								else if (y < 15)
									mat = Material.DIRT;
								else if (y >= 15 && y < 17 && mat != Material.STONE){
									if ((int)(MineFlat.noise.noise(
											ChunkUtil.getBlockXFromChunk(c.getNum(), x), y) + 1) == 0)
										mat = Material.STONE;
								}
								else if (y == 17)
									mat = Material.STONE;
							}
							new Block(mat, new Location(getBlockXFromChunk(c.getNum(), x), y));
						}
						else
							c.blocks[x][y] = null;
					}
				}
				if (ChunkUtil.getBlockXFromChunk(c.getNum(), x) == MineFlat.player.getX())
					MineFlat.player.setY(h - 2);
			}
		}
		System.out.println("Lighting terrain...");
		for (Chunk c : Chunk.chunks){
			//System.out.println("Lighting chunk " + c.getNum());
			for (int x = 0; x < 16; x++)
				for (int y = 0; y < 128; y++)
					if (c.getBlock(x, y) != null)
						c.getBlock(x, y).updateLight();
		}
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
		return (chunk - 1) * 16 + block;
	}

	public static int getChunkNum(int x){
		int add = 1;
		if (x < 0)
			add = -1;
		return x / 16 + add;
	}

}
