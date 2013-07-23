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
				for (int b = 0; b < 16; b++){
					int h = (int)((MineFlat.noise.noise(getBlockXFromChunk(i, b)) / 2 + 0.5) * MineFlat.terrainVariation);
					for (int y = h; y < 128; y++){
						c.setBlock(Material.DIRT, b, y);
						new Block(Material.DIRT, new Location(getBlockXFromChunk(c.getNum(), b), y));
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
