package mineflat.util;

import mineflat.Block;
import mineflat.Chunk;
import mineflat.Location;
import mineflat.Material;
import mineflat.MineFlat;

public class ChunkUtil {

	public static void generateChunks(){
		for (int i = MineFlat.player.getLocation().getChunk() - MineFlat.renderDistance; i <= MineFlat.player.getLocation().getChunk() + MineFlat.renderDistance; i++){
			if (!isChunkGenerated(i)){
				Chunk c = new Chunk(i);
				for (int x = 0; x < 16; x++){
					int h = (int)((MineFlat.noise.noise(getBlockXFromChunk(i, x)) / 2 + 0.5) * MineFlat.terrainVariation);
					int leftHeight = (int)((MineFlat.noise.noise(getBlockXFromChunk(i, x) - 1) / 2 + 0.5) * MineFlat.terrainVariation);
					int rightHeight = (int)((MineFlat.noise.noise(getBlockXFromChunk(i, x) + 1) / 2 + 0.5) * MineFlat.terrainVariation);
					h = (h + leftHeight + rightHeight) / 2; //TODO: Second smoothing pass
					for (int y = h; y < 128; y++){
						Material mat = Material.STONE;
						if (y - h == 0)
							mat = Material.GRASS;
						else if (y - h <= MineFlat.dirtDepth)
							mat = Material.DIRT;
						c.setBlock(mat, x, y);
						new Block(mat, new Location(getBlockXFromChunk(c.getNum(), x), y));
					}
				}
			}
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

}
