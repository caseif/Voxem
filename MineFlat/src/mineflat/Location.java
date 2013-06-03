package mineflat;

public class Location {

	protected int x;
	
	protected int y;
	
	public Location(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public Block getBlock(){
		for (Block b : Block.blocks.keySet()){
			if (b.getLocation().equals(this))
				return b;
		}
		return new Block(Material.AIR, this);
	}
	
	public boolean equals(Object o){
		if (o instanceof Location){
			Block b = (Block)o;
			return b.getX() == this.x && b.getY() == this.y;
		}
		return false;
	}
	
	public int hashCode(){
		return 41 * (x * 37 + y * 43 + 41);
	}
	
}
