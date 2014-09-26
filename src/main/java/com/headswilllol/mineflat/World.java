package com.headswilllol.mineflat;

import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class World {

	public long seed;

	private final HashMap<Integer, Level> levels = new HashMap<Integer, Level>();

	public long creationTime;
	
	private final String name;
	private final int chunkCount;
	private final int chunkLength;
	private final int chunkHeight;

	private JSONObject json;

	public World(String name, int chunkCount, int chunkLength, int chunkHeight){
		this.seed = System.currentTimeMillis() * 1337337331;
		this.name = name;
		this.chunkCount = chunkCount;
		this.chunkLength = chunkLength;
		this.chunkHeight = chunkHeight;
	}
	
	public long getSeed(){
		return seed;
	}
	
	public String getName(){
		return name;
	}
	
	public Level getLevel(int index){
		return levels.get(index);
	}
	
	public List<Level> getLevels(){
		java.util.Collection<Level> var = levels.values();
		return Arrays.asList(var.toArray(new Level[var.size()]));
	}
	
	public void addLevel(int index){
		levels.put(index, new Level(this, index));
	}

	/**
	 * @return The number of chunks in the world
	 */
	public int getChunkCount(){
		return chunkCount;
	}

	/**
	 * @return The length of chunks in the world
	 */
	public int getChunkLength(){
		return chunkLength;
	}

	/**
	 * @return The height of chunks in the world
	 */
	public int getChunkHeight(){
		return chunkHeight;
	}

	public JSONObject getJSON(){
		return json;
	}

	public void setJSON(JSONObject json){
		this.json = json;
	}

}
