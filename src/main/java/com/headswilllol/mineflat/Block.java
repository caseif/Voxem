package com.headswilllol.mineflat;

import java.util.HashMap;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class Block {

	public static int blockHandle;

	protected Location location;

	protected Material type;

	protected int light = 0;

	protected int chunk;

	public static final int maxLight = 16;
	public static final int minLight = 0;
	public static final float horShadow = 2f / maxLight;

	public static final int horAngle = 4;

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
	public static final int length = 44;

	/**
	 * The factor by which the light level of a block should decrease from its brightest adjacent
	 * block
	 */
	public static final int lightDistance = 1; // I'm pretty sure this now does literally nothing

	public int lastLightUpdate = -1;

	public HashMap<String, Object> metadata = new HashMap<String, Object>();

	public Block(Material m, Location location){
		this.type = m;
		this.location = location;
		for (int i = (int)location.getY() - 1; i >= 0; i--){
			Block b = Block.getBlock(getLevel(), (int)location.getX(), i);
			if (b != null){
				light = b.getLightLevel() - lightDistance;
				break;
			}
		}
		chunk = location.getChunk();
	}

	public void addToWorld(){
		Chunk c = location.getLevel().getChunk(location.getChunk());
		if (c == null)
			c = new Chunk(location.getLevel(), location.getChunk());
		c.setBlock(Math.abs((int)location.getX() % Main.world.getChunkLength()),
				(int)location.getY(), this);
	}

	public Level getLevel(){
		return location.getLevel();
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

	public boolean updateLight(){
		int newLight = 0;
		Block up = null, down = null, left = null, right = null;
		if (getY() > 0)
			up = Block.getBlock(getLevel(), (int)getX(), getY() - 1);
		if (getY() < Main.world.getChunkHeight() - 1)
			down = Block.getBlock(getLevel(), (int)getX(), getY() +
					1);
		left = Block.getBlock(getLevel(), (int)getX() - 1, getY());
		right = Block.getBlock(getLevel(), (int)getX() + 1, getY());
		Block[] adjacent = new Block[]{up, down, left, right};
		if (getY() <= Block.getTop(location))
			newLight = Block.maxLight;
		else {
			float average = 0;
			int total = 0;
			for (Block bl : adjacent){
				if (bl != null){
					average += bl.getLightLevel();
					total += 1;
				}
			}
			average /= total;
			if ((int)Math.floor(average) - Block.lightDistance >= Block.minLight)
				newLight = (int)Math.floor(average) - Block.lightDistance;
			else
				newLight = Block.minLight;
		}
		boolean changed = newLight != getLightLevel();
		lastLightUpdate = TickManager.getTicks();
		if (changed){
			setLightLevel(newLight);
			for (Block bl : adjacent)
				if (bl != null && bl.lastLightUpdate != TickManager.getTicks())
					bl.updateLight();
		}
		for (int y = getY() + 1; y < 128; y++){
			Block b = Block.getBlock(getLevel(), getX(), y);
			if (b.lastLightUpdate != TickManager.getTicks())
				if (!b.updateLight())
					break;
				else
					break;
		}
		return changed;
	}

	public void destroy(){
		setType(Material.AIR);
	}

	public Block clone(){
		return new Block(type, location);
	}

	public static int getTop(Location location){
		for (int yy = 0; yy < Main.world.getChunkHeight(); yy++){
			if (isSolid(location.getLevel(), location.getX(), yy)){
				return yy;
			}
		}
		return -1;
	}

	public static Block getBlock(Level level, int x, int y){
		if (y >= 0 && y < Main.world.getChunkHeight()){
			Chunk c = level.getChunk(new Location(level, x, y).getChunk());
			if (c != null)
				return c.getBlock(Math.abs(x % Main.world.getChunkLength()), y);
		}
		return null;
	}

	public static Block getBlock(Level level, float x, float y){
		return new Location(level, x, y).getBlock();
	}

	public static void updateSelectedBlock(){
		double mouseX = (Mouse.getX() - GraphicsHandler.xOffset) / (float)Block.length;
		double mouseY = (Display.getHeight() - Mouse.getY() - GraphicsHandler.yOffset) /
				(float)Block.length;
		double xDiff = mouseX - Main.player.getX();
		double yDiff = mouseY - Main.player.getY();
		double angle = Math.atan2(xDiff, yDiff);
		boolean found = false;
		for (double d = 0.5; d <= 5; d += 0.5){
			double xAdd = d * Math.sin(angle);
			double yAdd = d * Math.cos(angle);
			int blockX = (int)Math.floor(Main.player.getX() + xAdd);
			int blockY = (int)Math.floor(Main.player.getY() + yAdd);
			synchronized (Main.lock){
				if (blockY >= 0 && blockY <= Main.world.getChunkHeight() - 1){
					if (!Block.isAir(Main.player.getLevel(), blockX, blockY)){
						//TODO: verfiy that player isn't peeking through blocks
						Block.selected = new Location(Main.player.getLevel(), blockX, blockY);
						found = true;
						break;
					}
				}
			}
		}
		if (!found)
			Block.selected = null;
	}

	/**
	 * Checks whether the block at the given coordinates is air. Please only use this in cases where
	 * the block might actually be null. Otherwise, just check if it's air. :)
	 * @param level the level containing the block.
	 * @param x The x-coordinate of the block
	 * @param y The y-coordinate of the block
	 * @return Whether the block is air
	 */
	public static boolean isAir(Level level, int x, int y){
		if (Block.getBlock(level, x, y) != null)
			if (Block.getBlock(level, x, y).getType() != Material.AIR)
				return false;
		return true;
	}

	/**
	 * Checks whether the block at the given coordinates is air. Please only use this in cases where
	 * the block might actually be null. Otherwise, just check if it's air. :)
	 * @param level the level containing the block.
	 * @param x The x-coordinate of the block
	 * @param y The y-coordinate of the block
	 * @return Whether the block is air
	 */
	public static boolean isAir(Level level, float x, float y){
		return Block.isAir(level, (int)x, (int)y);
	}

	/**
	 * Checks whether the given block is air. Please only use this in cases where
	 * the block might actually be null. Otherwise, just check if it's air. :)
	 * @param b The block to check
	 * @return Whether the block is air
	 */
	public static boolean isAir(Block b){
		if (b != null)
			return Block.isAir(b.getLevel(), b.getX(), b.getY());
		return true;
	}

	public static boolean isAir(Location l){
		return Block.isAir(l.getLevel(), l.getX(), l.getY());
	}

	public static boolean isSolid(Level level, int x, int y){
		return isSolid(level, (float)x, (float)y);
	}

	public static boolean isSolid(Level level, float x, float y){
		return !isAir(level, x, y) &&
				!(new Location(level, x, y).getBlock().hasMetadata("solid") && !(boolean)new Location(level, x, y).getBlock().getMetadata("solid"));
	}

	public static boolean isSolid(Block b){
		return isSolid(b.getLevel(), b.getX(), b.getY());
	}

	public static boolean isSolid(Location l){
		return isSolid(l.getLevel(), l.getX(), l.getY());
	}

	public Object getMetadata(String key){
		return metadata.get(key);
	}

	public boolean hasMetadata(String key){
		return metadata.containsKey(key);
	}

	public void setMetadata(String key, Object value){
		metadata.put(key, value);
	}
}
