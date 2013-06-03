package mineflat;

import java.util.HashMap;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import static org.lwjgl.opengl.GL11.*;


public class Block {

	public static HashMap<Block, Integer> blocks = new HashMap<Block, Integer>();

	protected Location location;

	protected Material type;

	protected final int length = 10;

	public Block(Material type, Location location){
		this.type = type;
		this.location = location;
		if (type != Material.AIR){
			int handle = glGenLists(1);
			glNewList(handle, GL_COMPILE);
			{
				try {
				Texture t = TextureLoader.getTexture("PNG",
						Block.class.getClassLoader().getResourceAsStream(
						"textures/" + type.toString().toLowerCase() + ".png"));
				glBindTexture(GL_TEXTURE_2D, t.getTextureID());
				glTexCoord2f(0, 0);
				glVertex2f(location.x, location.y); // top left
				glTexCoord2f(1, 0);
				glVertex2f(location.x + length, location.y); // top right
				glTexCoord2f(1, 1);
				glVertex2f(location.x + length, location.y + length); // bottom right
				glTexCoord2f(0, 1);
				glVertex2f(location.x, location.y + length); // bottom left
				glBindTexture(GL_TEXTURE_2D, 0);
				}
				catch (Exception ex){
					System.err.println("Exception occurred while rendering block at (" +
							location.x + ", " + location.y + ")");
					ex.printStackTrace();
				}
			}
			glEnd();

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
			glCallList(blocks.get(b));
		}
	}

}
