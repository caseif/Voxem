package mineflat;

import java.util.ArrayList;
import java.util.List;

public class Chunk {
	
	public static List<Chunk> chunks = new ArrayList<Chunk>();
	
	protected int num;
	
	protected Block[][] blocks;
	
	public Chunk(int num){
		this.num = num;
		blocks = new Block[16][128];
		chunks.add(this);
	}
	
	public int getNum(){
		return num;
	}
	
	public Block getBlock(int x, int y){
		return blocks[x][y];
	}
	
	public Block[][] getBlocks(){
		return blocks;
	}
	
	public void setNum(int num){
		this.num = num;
	}
	
	public void setBlock(int x, int y, Block b){
		blocks[x][y] = b;
	}
}
