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
}
