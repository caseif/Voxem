package com.headswilllol.mineflat;

public class Location {

	protected Level level;
	protected float x;
	protected float y;

	public Location(Level level, float x, float y){
		this.level = level;
		this.x = x;
		this.y = y;
	}
	
	public Level getLevel(){
		return level;
	}

	public float getX(){
		return x;
	}

	public float getY(){
		return y;
	}
	
	public void setLevel(Level level){
		this.level = level;
	}

	public void setX(float x){
		this.x = x;
	}

	public void setY(float y){
		this.y = y;
	}
	
	public Location add(Location location){
		return add(location.getX(), location.getY());
	}
	
	public Location add(float x, float y){
		return new Location(this.level, this.x + x, this.y + y);
	}
	
	public Location subtract(Location location){
		return subtract(location.getX(), location.getY());
	}
	
	public Location subtract(float x, float y){
		return new Location(this.level, this.x + x, this.y + y);
	}

	public Block getBlock(){
		Chunk c = Main.world.getLevel(0).getChunk(getChunk());
		if (c != null){
			int index = (int)Math.floor(Math.abs(x % Main.world.getChunkLength()));
			return c.getBlock(index, (int)y);
		}
		return null;
	}

	public boolean equals(Object o){
		if (o instanceof Location){
			Location l = (Location)o;
			return l.getX() == this.x && l.getY() == this.y;
		}
		return false;
	}

	public int hashCode(){
		return 41 * (int)(x * 37 + y * 43 + 41);
	}

	public int getChunk(){
		return x >= 0 ? (int)x / Main.world.getChunkLength() + 1 :
			(int)(x + 1) / Main.world.getChunkLength() - 1;
	}

	public float getPosInChunk(){
		return x >= 0 ? Math.abs(x) % Main.world.getChunkLength() :
			Main.world.getChunkLength() - Math.abs(x) % Main.world.getChunkLength(); 
	}

	public int getPixelX(){
		return (int)(getX() * Block.length);
	}

	public int getPixelY(){
		return (int)(getY() * Block.length);
	}

	public static int getXFromPixels(int x){
		return x / Block.length - GraphicsHandler.xOffset;
	}

	public static int getYFromPixels(int y){
		return y / Block.length - GraphicsHandler.yOffset;
	}

}
