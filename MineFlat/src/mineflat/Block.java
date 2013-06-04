package mineflat;

import java.util.ArrayList;
import java.util.List;

import mineflat.util.BlockUtil;

import org.newdawn.slick.opengl.Texture;

import static org.lwjgl.opengl.GL11.*;

public class Block {

	public static int blockHandle;

	protected Location location;

	protected Material type;

	public static final int length = 16;

	public static List<Block> blocks = new ArrayList<Block>();

	public Block(Material m, Location location){
		this.type = m;
		this.location = location;
		blocks.add(this);
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

	public static void draw(){
		for (Block b : Block.blocks){
			if (b.getType() != Material.AIR){
				try {
					glPushMatrix();
					Texture t = BlockUtil.textures.get(b.getType());
					glBindTexture(GL_TEXTURE_2D, t.getTextureID());
					glTranslatef(b.getX() * length, b.getY() * length + 50, 0);
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

}
