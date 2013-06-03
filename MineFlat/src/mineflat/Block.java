package mineflat;

import java.util.ArrayList;
import java.util.List;


public class Block {
	
	public static List<Block> blocks = new ArrayList<Block>();
	
	protected int x;
	
	protected int y;
	
	protected Material type;
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public Material getType(){
		return type;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public void setLocation(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void setType(Material type){
		this.type = type;
	}
	
	public void destroy(){
		blocks.remove(this);
	}
	
	public static void draw(){
		for (Block b : blocks){
			//TODO Draw blocks
		}
	}
	
}
