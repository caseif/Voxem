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
			System.out.println("Generating chunk " + i);
			if (!isChunkGenerated(i)){
				Chunk c = new Chunk(i);
				for (int x = 0; x < 16; x++){
					int h = (int)((MineFlat.noise.noise(getBlockXFromChunk(i, x)) / 2 + 0.5) * MineFlat.terrainVariation);
					int leftHeight = (int)((MineFlat.noise.noise(getBlockXFromChunk(i, x) - 1) / 2 + 0.5) * MineFlat.terrainVariation);
					int rightHeight = (int)((MineFlat.noise.noise(getBlockXFromChunk(i, x) + 1) / 2 + 0.5) * MineFlat.terrainVariation);
					h = (h + leftHeight + rightHeight) / 2;
					for (int y = h; y < 128; y++){
						Material mat = Material.STONE;
						if (y - h == 0)
							mat = Material.GRASS;
						else if (y - h <= MineFlat.dirtDepth)
							mat = Material.DIRT;
						new Block(mat, new Location(getBlockXFromChunk(c.getNum(), x), y));
					}
				}
			}
		}
		//TODO: Fix this damn thing
		// second smoothing pass
		/*System.out.println("Smoothing terrain...");
		for (int i = MineFlat.player.getLocation().getChunk() - MineFlat.renderDistance; i <= MineFlat.player.getLocation().getChunk() + MineFlat.renderDistance; i++){
			Chunk c = new Chunk(i);
			for (int x = 0; x < 16; x++){
				int h = BlockUtil.getTop(x);
				int leftHeight = BlockUtil.getTop(x);
				if (leftHeight == -1)
					leftHeight = (int)((MineFlat.noise.noise(getBlockXFromChunk(i, x) - 1) / 2 + 0.5) * MineFlat.terrainVariation);
				int rightHeight = BlockUtil.getTop(x);
				if (rightHeight == -1)
					rightHeight = (int)((MineFlat.noise.noise(getBlockXFromChunk(i, x) + 1) / 2 + 0.5) * MineFlat.terrainVariation);
				h = (h + leftHeight + rightHeight) / 2;
				for (int y = h; y < 128; y++){
					Material mat = Material.STONE;
					if (y - h == 0)
						mat = Material.GRASS;
					else if (y - h <= MineFlat.dirtDepth)
						mat = Material.DIRT;
					new Block(mat, new Location(getBlockXFromChunk(c.getNum(), x), y));
				}
			}
		}*/
		System.out.println("Lighting terrain...");
		for (Block b : Block.blocks)
			b.updateLight();
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
