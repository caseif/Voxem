package mineflat;

import java.util.ArrayList;
import java.util.List;

import mineflat.util.BlockUtil;
import mineflat.util.ChunkUtil;

import org.newdawn.slick.opengl.Texture;

import static org.lwjgl.opengl.GL11.*;

public class Block {

	public static int blockHandle;

	protected Location location;

	protected Material type;

	protected int light = 15;

	protected int chunk;

	/**
	 * The diameter of a block
	 */
	public static final int length = 16;

	/**
	 * The factor by which the light level of a block should decrease for each block over it
	 */
	public static final int lightDistance = 1;

	public static List<Block> blocks = new ArrayList<Block>();

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
		blocks.add(this);
		chunk = ChunkUtil.getChunkNum((int)location.getX());
		Chunk c = ChunkUtil.getChunk(ChunkUtil.getChunkNum(chunk));
		if (c == null)
			c = new Chunk(ChunkUtil.getChunkNum((int)location.getX()));
		c.blocks[Math.abs((int)location.getX() % 16)][(int)location.getY()] = m;
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
		for (Block b : Block.blocks){
			// check if player is within range
			if (Math.abs(MineFlat.player.getX() - b.getX()) <= MineFlat.renderDistance * 16){
				if (b.getType() != Material.AIR){
					try {
						glPushMatrix();
						Texture t = BlockUtil.textures.get(b.getType());
						glBindTexture(GL_TEXTURE_2D, t.getTextureID());
						float fracLight = (float)(b.getLightLevel()) / 15;
						glColor3f(fracLight, fracLight, fracLight);
						glTranslatef(b.getX() * length, b.getY() * length + 150, 0);
						glCallList(blockHandle);
						glBindTexture(GL_TEXTURE_2D, 0);
						glPopMatrix();
					}
					catch (Exception ex){
						ex.printStackTrace();
					}
				}
			}
		}
	}

	public static void initialize(){
		blockHandle = glGenLists(1);
		glNewList(blockHandle, GL_COMPILE);
		{
			try {
				glBegin(GL_QUADS);
				glTexCoord2f(0, 0);
				glVertex2f(0, 0); // top left
				glTexCoord2f(1, 0);
				glVertex2f(length, 0); // top right
				glTexCoord2f(1, 1);
				glVertex2f(length, length); // bottom right
				glTexCoord2f(0, 1);
				glVertex2f(0, length); // bottom left
				glEnd();
			}
			catch (Exception ex){
				System.err.println("Exception occurred while rendering block");
				ex.printStackTrace();
			}
		}
		glEndList();
	}

	public void updateLight(){
		if (this.getType() != Material.AIR){
			for (int y = this.getY() - 1; y >= 0; y--){
				Block bl = BlockUtil.getBlock((int)this.getX(), y);
				if (bl != null){
					if (bl.getType() != Material.AIR){
						light = bl.getLightLevel() - 1;
						if (light < 0)
							light = 0;
						break;
					}
				}
			}
		}

	}
}
