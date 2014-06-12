package com.headswilllol.mineflat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.headswilllol.mineflat.entity.Entity;
import com.headswilllol.mineflat.entity.LivingEntity;
import com.headswilllol.mineflat.entity.Mob;

public class Level {

	public HashMap<Integer, Chunk> chunks = new HashMap<Integer, Chunk>();

	private World world;
	private int index;
	private List<Entity> entities = new ArrayList<Entity>();
	
	public Level(World world, int index){
		this.world = world;
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
	public List<Entity> getEntities(){
		return entities;
	}
	
	/**
	 * Sets the list of entities in the world (dunno why this would be needed, but please be careful)
	 * @param entities
	 */
	public void setEntities(List<Entity> entities){
		this.entities = entities;
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
	
}
