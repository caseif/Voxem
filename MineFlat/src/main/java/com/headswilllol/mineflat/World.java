package com.headswilllol.mineflat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.headswilllol.mineflat.entity.Entity;
import com.headswilllol.mineflat.entity.LivingEntity;
import com.headswilllol.mineflat.entity.Mob;

public class World {

	public HashMap<Integer, Chunk> chunks = new HashMap<Integer, Chunk>();

	private String name;
	private int chunkCount;
	private int chunkLength;
	private int chunkHeight;
	private List<Entity> entities = new ArrayList<Entity>();
	
	public World(String name, int chunkCount, int chunkLength, int chunkHeight){
		this.name = name;
		this.chunkCount = chunkCount;
		this.chunkLength = chunkLength;
		this.chunkHeight = chunkHeight;
	}
	
	/**
	 * @return The name of the world
	 */
	public String getName(){
		return name;
	}

	/**
	 * Sets the name of the world
	 * @param name The name of the world
	 */
	public void setName(String name){
		this.name = name;
	}

	/**
	 * @return The number of chunks in the world
	 */
	public int getChunkCount(){
		return chunkCount;
	}

	/**
	 * Sets the number of chunks in the world
	 * @param chunkCount The number of chunks in the world
	 */
	public void setChunkCount(int chunkCount){
		this.chunkCount = chunkCount;
	}

	/**
	 * @return The length of chunks in the world
	 */
	public int getChunkLength(){
		return chunkLength;
	}

	/**
	 * Sets the length of chunks in the world
	 * @param chunkLength The length of chunks in the world
	 */
	public void setChunkLength(int chunkLength){
		this.chunkLength = chunkLength;
	}

	/**
	 * @return The height of chunks in the world
	 */
	public int getChunkHeight(){
		return chunkHeight;
	}

	/**
	 * Sets the height of chunks in the world
	 * @param chunkHeight The height of chunks in the world
	 */
	public void setChunkHeight(int chunkHeight){
		this.chunkHeight = chunkHeight;
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
	 * The same as {@link World#getLivingEntityCount()}, but without players
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
