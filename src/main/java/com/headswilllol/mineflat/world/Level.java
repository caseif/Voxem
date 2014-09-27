package com.headswilllol.mineflat.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.headswilllol.mineflat.Material;
import com.headswilllol.mineflat.entity.Entity;
import com.headswilllol.mineflat.entity.LivingEntity;
import com.headswilllol.mineflat.entity.Mob;
import com.headswilllol.mineflat.location.WorldLocation;
import org.json.simple.JSONObject;

public class Level {

	public final HashMap<Integer, Chunk> chunks = new HashMap<Integer, Chunk>();

	private final World world;
	private final int index;
	private final Collection<Entity> entities = new ArrayList<Entity>();

	public Level(World world, int index){
		this.world = world;
		this.index = index;
	}

	/**
	 * @return The world containing this level.
	 */
	public World getWorld(){
		return world;
	}

	public int getIndex(){
		return index;
	}

	/**
	 * @return A list containing all entities in the world
	 */
	public Collection<Entity> getEntities(){
		return entities;
	}

	/**
	 * Adds an entity to the world
	 * @param e The entity to add
	 */
	public void addEntity(Entity e){
		entities.add(e);
	}

	/**
	 * Removes an entity from the world
	 * @param e The entity to remove
	 * @return Whether the entity was succesfully removed (returns false if it wasn't in the world to begin with)
	 */
	public boolean removeEntity(Entity e){
		if (entities.contains(e)){
			entities.remove(e);
			return true;
		}
		return false;
	}

	/**
	 * @return The number of entities in the world
	 */
	public int getEntityCount(){
		return entities.size();
	}

	/**
	 * @return The number of living entities in the world
	 */
	public int getLivingEntityCount(){
		int count = 0;
		for (Entity e : entities)
			if (e instanceof LivingEntity)
				count += 1;
		return count;
	}

	/**
	 * The same as {@link Level#getLivingEntityCount()}, but without players
	 * @return The number of mobs in the world
	 */
	public int getMobCount(){
		int count = 0;
		for (Entity e : entities)
			if (e instanceof Mob)
				count += 1;
		return count;
	}

	public Chunk getChunk(int i){
		return chunks.get(i);
	}

	public boolean isChunkGenerated(int i){
		return getChunk(i) != null;
	}

	/**
	 * Plants a tree on the specified block.
	 * @param x the x-position of the block.
	 * @param y the y-position of the block.
	 */
	public void plantTree(int x, int y){
		// check that all blocks are open
		int[] xCheck = new int[]{0, 0, 0, 0, 0, 0, -1, -1, -2, 1, 1, 2};
		int[] yCheck = new int[]{-1, -2, -3, -4, -5, -6, -4, -5, -4, -4, -5, -4};
		for (int i = 0; i < xCheck.length; i++){
			int xx = xCheck[i];
			int yy = yCheck[i];
			if (new WorldLocation(this, x + xx, y + yy).getBlock() == null ||
					new WorldLocation(this, x + xx, y + yy).getBlock() != null && new WorldLocation(this, x + xx, y + yy).getBlock().getType() != Material.AIR)
				return;
		}

		for (int i = 0; i < xCheck.length; i++){
			int xx = xCheck[i];
			int yy = yCheck[i];
			if (i < 3){
				new WorldLocation(this, x + xx, y + yy).getBlock().setType(Material.LOG);
				new WorldLocation(this, x + xx, y + yy).getBlock().setMetadata("solid", false);
			}
			else {
				new WorldLocation(this, x + xx, y + yy).getBlock().setType(Material.LEAVES);
				new WorldLocation(this, x + xx, y + yy).getBlock().setMetadata("solid", false);
			}
		}
	}

	public JSONObject save(){
		return SaveManager.saveLevel(this);
	}

	public void saveToMemory(){
		SaveManager.saveLevelToMemory(this);
	}

}
