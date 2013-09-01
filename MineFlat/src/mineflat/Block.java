package mineflat;

import mineflat.util.BlockUtil;
import mineflat.util.ChunkUtil;

import static org.lwjgl.opengl.GL11.*;

public class Block {

	public static int blockHandle;

	protected Location location;

	protected Material type;

	protected int light = 0;

	protected int chunk;

	public static int maxLight = 16;
	public static int minLight = 1;

	/**
	 * The diameter of a block
	 */
	public static final int length = 32;

	/**
	 * The factor by which the light level of a block should decrease from its brightest adjacent
	 * block
	 */
	public static final int lightDistance = 1;

	public Block(Material m, Location location){
		this.type = m;
		this.location = location;
		for (int i = (int)location.getY() - 1; i >= 0; i--){
			Block b = BlockUtil.getBlock((int)location.getX(), i);
			if (b != null){
				light = b.getLightLevel() - lightDistance;
				break;
			}
		}
		chunk = location.getChunk();
	}

	public void addToWorld(){
		Chunk c = ChunkUtil.getChunk(location.getChunk());
		if (c == null)
			c = new Chunk(location.getChunk());
		c.setBlock(Math.abs((int)location.getX() % 16), (int)location.getY(), this);
	}

	public int getX(){
		return (int)location.getX();
	}

	public int getY(){
		return (int)location.getY();
	}

	public Location getLocation(){
		return location;
	}

	public Material getType(){
		return type;
	}

	public int getLightLevel(){
		return light;
	}

	public void setX(int x){
		this.location.setX(x);
	}

	public void setY(int y){
		this.location.setY(y);
	}

	public void setLocation(int x, int y){
		this.location.setX(x);
		this.location.setY(y);
	}

	public void setLocation(Location location){
		this.location = location;
	}

	public void setType(Material type){
		this.type = type;
	}

	public void setLightLevel(int light){
		this.light = light; 
	}

	public static void draw(){
		for (Chunk c : Chunk.chunks){
			// check if player is within range
			if (Math.abs(MineFlat.player.getLocation().getChunk() - c.getNum()) <=
					MineFlat.renderDistance){
				for (int x = 0; x < 16; x++){
					for (int y = 0; y < 128; y++){
						Block b = c.getBlock(x, y);
						if (b != null){
							glPushMatrix();
							//glBindTexture(GL_TEXTURE_2D,
							//		BlockUtil.textures.get(b.getType()).getTextureID());
							float fracLight = (float)(b.getLightLevel()) / maxLight;
							glColor3f(fracLight, fracLight, fracLight);
							int drawX = b.getX() * length + MineFlat.xOffset;
							int drawY = b.getY() * length + MineFlat.yOffset;
							glBegin(GL_QUADS);
							glTexCoord2f(0, 0);
							glVertex2f(drawX, drawY); // top left
							glTexCoord2f(1, 0);
							glVertex2f(drawX + length, drawY); // top right
							glTexCoord2f(1, 1);
							glVertex2f(drawX + length, drawY + length); // bottom right
							glTexCoord2f(0, 1);
							glVertex2f(drawX, drawY + length); // bottom left
							glEnd();
							glBindTexture(GL_TEXTURE_2D, 0);
							glPopMatrix();
						}
					}
				}
			}
		}
	}

	public void destroy(){
		ChunkUtil.getChunk(ChunkUtil.getChunkNum((int)Math.floor(getX())))
		.setBlock((int)Math.abs(Math.floor(getX() % 16)), 
				(int)(Math.floor(getY())), null);
	}

	public Block clone(){
		return new Block(type, location);
	}
}
