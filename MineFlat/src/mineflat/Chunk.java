package mineflat;

import java.util.ArrayList;
import java.util.List;

public class Chunk {
	
	public static List<Chunk> chunks = new ArrayList<Chunk>();
	
	protected int num;
	
	protected Material[][] blocks;
	
	public Chunk(int num){
		this.num = num;
		blocks = new Material[16][128];
		chunks.add(this);
	}
	
	public int getNum(){
		return num;
	}
	
	public Material getBlock(int x, int y){
		return blocks[x][y];
	}
	
	public Material[][] getBlocks(){
		return blocks;
	}
	
	public void setNum(int num){
		this.num = num;
	}

	public void setBlock(Material m, int x, int y){
		blocks[x][y] = m;
	}
	
	public static Chunk getChunk(int i){
		for (Chunk c : chunks){
			if (c.getNum() == i)
				return c;
		}
		return null;
	}
	
	public static boolean isGenerated(int i){
		return getChunk(i) != null;
	}
	
	public static int getActualX(int chunk, int block){
		return (chunk - 1) * 16 + block;
	}
	
}
