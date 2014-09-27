package com.headswilllol.mineflat.world;

import com.headswilllol.mineflat.*;
import com.headswilllol.mineflat.vector.Vector2f;

/**
 * Represents a physical location in a world.
 */
public class Location extends Vector2f {

	protected Level level;

	public Location(Level level, float x, float y){
		super(x, y);
		this.level = level;
	}
	
	public Level getLevel() {
		return level;
	}

	
	public void setLevel(Level level){
		this.level = level;
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
			return c.getBlock(Chunk.getIndexInChunk((int)x), (int)y);
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
