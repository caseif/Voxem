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

	/**
	 * The diameter of a block
	 */
	public static final int length = 16;

	/**
	 * The factor by which the light level of a block should decrease for each block over it
	 */
	public static final int lightDistance = 1;

	public Block(Material m, Location location){
		this.type = m;
		this.location = location;
		for (int i = (int)location.getY() - 1; i >= 0; i--){
			Block b = BlockUtil.getBlock((int)location.getX(), i);
			if (b != null){
				if (b.getType() != Material.AIR){
					light = b.getLightLevel() - lightDistance;
					break;
				}
			}
		}
		chunk = location.getChunk();
		Chunk c = ChunkUtil.getChunk(location.getChunk());
		if (c == null)
			c = new Chunk(location.getChunk());
		c.blocks[Math.abs((int)location.getX() % 16)][(int)location.getY()] = this;
	}

	public Block(Material m, int x, int y){
		new Block(m, new Location(x, y));
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
			if (Math.abs(MineFlat.player.getLocation().getChunk() - c.getNum()) <= MineFlat.renderDistance){
				for (int x = 0; x < 16; x++){
					for (int y = 0; y < 128; y++){
						Block b = c.getBlock(x, y);
						if (b != null){
							if (b.getType() != Material.AIR){
								glPushMatrix();
								//glBindTexture(GL_TEXTURE_2D,
								//		BlockUtil.textures.get(b.getType()).getTextureID());
								float fracLight = (float)(b.getLightLevel()) / 15;
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
	}

	public void updateLight(){
		if (this.getType() != Material.AIR){
			if (BlockUtil.getTop(this.getX()) == this.getY())
				this.setLightLevel(15);
			Block up = null, down = null, left = null, right = null;
			if (this.getY() > 0)
				up = BlockUtil.getBlock((int)this.getX(), this.getY() - 1);
			if (this.getY() < 127)
				down = BlockUtil.getBlock((int)this.getX(), this.getY() + 1);
			left = BlockUtil.getBlock((int)this.getX() - 1, this.getY());
			right = BlockUtil.getBlock((int)this.getX() + 1, this.getY());
			Block[] adjacent = new Block[]{up, down, left, right};
			for (Block b : adjacent){
				if (b != null)
					if (b.getLightLevel() < this.getLightLevel() - 1)
						if (getLightLevel() <= 1)
							b.setLightLevel(0);
						else
							b.setLightLevel(this.getLightLevel() - 1);
			}
		}
	}
}
