package mineflat;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class Block {

	public static int blockHandle;

	protected Location location;

	protected Material type;

	protected int light = 0;

	protected int chunk;

	public static int maxLight = 16;
	public static int minLight = 1;

	/**
	 * The block which is currently selected.
	 */
	public static Location selected = null;
	public static int selectedX = 0;
	public static int selectedY = 0;
	public static boolean isSelected = false;

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
			Block b = Block.getBlock((int)location.getX(), i);
			if (b != null){
				light = b.getLightLevel() - lightDistance;
				break;
			}
		}
		chunk = location.getChunk();
	}

	public void addToWorld(){
		Chunk c = Chunk.getChunk(location.getChunk());
		if (c == null)
			c = new Chunk(location.getChunk());
		c.setBlock(Math.abs((int)location.getX() % MineFlat.world.getChunkLength()),
				(int)location.getY(), this);
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
					GraphicsHandler.renderDistance){
				for (int x = 0; x < MineFlat.world.getChunkLength(); x++){
					for (int y = 0; y < MineFlat.world.getChunkHeight(); y++){
						Block b = c.getBlock(x, y);
						if (b != null){
							glPushMatrix();
							//glBindTexture(GL_TEXTURE_2D,
							//		BlockUtil.textures.get(b.getType()).getTextureID());
							float fracLight = (float)(b.getLightLevel()) / maxLight;
							glColor3f(fracLight, fracLight, fracLight);
							int drawX = b.getX() * length + GraphicsHandler.xOffset;
							int drawY = b.getY() * length + GraphicsHandler.yOffset;
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
		Chunk.getChunk(Chunk.getChunkNum((int)Math.floor(getX())))
		.setBlock((int)Math.abs(Math.floor(getX() % MineFlat.world.getChunkLength())), 
				(int)(Math.floor(getY())), null);
	}

	public Block clone(){
		return new Block(type, location);
	}

	public static int getTop(int x){
		for (int yy = 0; yy < MineFlat.world.getChunkHeight(); yy++){
			if (new Location(x, yy).getBlock() != null){
				return yy;
			}
		}
		return -1;
	}

	public static Block getBlock(int x, int y){
		Chunk c = Chunk.getChunk(new Location(x, y).getChunk());
		if (c != null)
			return c.getBlock(Math.abs(x % MineFlat.world.getChunkLength()), y);
		return null;
	}

	public static boolean isBlockEmpty(Block b){
		if (b == null)
			return true;
		else if (b.getType() == Material.AIR)
			return true;
		return false;
	}

	public static void updateSelectedBlock(){
		double mouseX = (Mouse.getX() - GraphicsHandler.xOffset) / (float)Block.length;
		double mouseY = (Display.getHeight() - Mouse.getY() - GraphicsHandler.yOffset) /
				(float)Block.length;
		double xDiff = mouseX - MineFlat.player.getX();
		double yDiff = mouseY - MineFlat.player.getY();
		double angle = Math.atan2(xDiff, yDiff);
		boolean found = false;
		for (double d = 0.5; d <= 5; d += 0.5){
			double xAdd = d * Math.sin(angle);
			double yAdd = d * Math.cos(angle);
			int blockX = (int)Math.floor(MineFlat.player.getX() + xAdd);
			int blockY = (int)Math.floor(MineFlat.player.getY() + yAdd);
			synchronized (MineFlat.lock){
				if (blockY >= 0 && blockY <= MineFlat.world.getChunkHeight() - 1){
					if (Block.getBlock(blockX, blockY) != null){
						Block.selected = new Location(blockX, blockY);
						found = true;
						break;
					}
				}
			}
		}
		if (!found)
			Block.selected = null;
	}
}
