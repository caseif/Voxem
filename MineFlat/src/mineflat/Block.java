package mineflat;

import java.util.HashMap;

import mineflat.util.BlockUtil;

import org.newdawn.slick.opengl.Texture;

import static org.lwjgl.opengl.GL11.*;

public class Block {

	public static HashMap<Block, Integer> blocks = new HashMap<Block, Integer>();

	protected Location location;

	protected Material type;

	public static final int length = 64;

	public Block(Material type, Location location){
		this.type = type;
		this.location = location;
		if (type != Material.AIR){
			int handle = glGenLists(1);
			glNewList(handle, GL_COMPILE);
			{
				try {
					int actualX = location.getX() * length;
					int actualY = location.getY() * length;
					glBegin(GL_QUADS);
					glTexCoord2f(0, 0);
					glVertex2f(actualX, actualY); // top left
					glTexCoord2f(1, 0);
					glVertex2f(actualX + length, actualY); // top right
					glTexCoord2f(1, 1);
					glVertex2f(actualX + length, actualY + length); // bottom right
					glTexCoord2f(0, 1);
					glVertex2f(actualX, actualY + length); // bottom left
					glBindTexture(GL_TEXTURE_2D, 0);
					glEnd();
				}
				catch (Exception ex){
					System.err.println("Exception occurred while rendering block at (" +
							location.x + ", " + location.y + ")");
					ex.printStackTrace();
				}
			}
			glEndList();

			blocks.put(this, handle);
		}
	}

	public Block(Material m, int x, int y){
		new Block(m, new Location(x, y));
	}

	public int getX(){
		return location.getX();
	}

	public int getY(){
		return location.getY();
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

	public void destroy(){
		blocks.remove(this);
	}

	public static void draw(){
		for (Block b : blocks.keySet()){
			try {
				Texture t = BlockUtil.textures.get(b.getType());
				glBindTexture(GL_TEXTURE_2D, t.getTextureID());
				glCallList(blocks.get(b));
			}
			catch (Exception ex){
				ex.printStackTrace();
			}
		}
	}

}
