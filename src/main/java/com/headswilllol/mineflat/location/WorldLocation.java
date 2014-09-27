package com.headswilllol.mineflat.location;

import com.headswilllol.mineflat.*;
import com.headswilllol.mineflat.world.Block;
import com.headswilllol.mineflat.world.Chunk;
import com.headswilllol.mineflat.world.Level;

public class WorldLocation extends Location2f {

	protected Level level;

	public WorldLocation(Level level, float x, float y){
		super(x, y);
		this.level = level;
	}
	
	public Level getLevel() {
		return level;
	}

	
	public void setLevel(Level level){
		this.level = level;
	}
	
	public WorldLocation add(WorldLocation location){
		return add(location.getX(), location.getY());
	}
	
	public WorldLocation add(float x, float y){
		return new WorldLocation(this.level, this.x + x, this.y + y);
	}
	
	public WorldLocation subtract(WorldLocation location){
		return subtract(location.getX(), location.getY());
	}
	
	public WorldLocation subtract(float x, float y){
		return new WorldLocation(this.level, this.x + x, this.y + y);
	}

	public Block getBlock(){
		Chunk c = Main.world.getLevel(0).getChunk(getChunk());
		if (c != null){
			return c.getBlock(Chunk.getIndexInChunk((int)x), (int)y);
		}
		return null;
	}

	public boolean equals(Object o){
		if (o instanceof WorldLocation){
			WorldLocation l = (WorldLocation)o;
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
