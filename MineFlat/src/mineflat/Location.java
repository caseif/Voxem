package mineflat;

public class Location {

	protected float x;

	protected float y;

	public Location(float x, float y){
		this.x = x;
		this.y = y;
	}

	public float getX(){
		return x;
	}

	public float getY(){
		return y;
	}

	public void setX(float x){
		this.x = x;
	}

	public void setY(float y){
		this.y = y;
	}

	public Block getBlock(){
		Chunk c = Chunk.getChunk(getChunk());
		if (c != null){
			int index = (int)Math.floor(Math.abs(x % MineFlat.world.getChunkLength()));
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
		return x >= 0 ? (int)x / MineFlat.world.getChunkLength() + 1 :
			(int)(x + 1) / MineFlat.world.getChunkLength() - 1;
	}

	public float getPosInChunk(){
		return x >= 0 ? Math.abs(x) % MineFlat.world.getChunkLength() :
			MineFlat.world.getChunkLength() - Math.abs(x) % MineFlat.world.getChunkLength(); 
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
