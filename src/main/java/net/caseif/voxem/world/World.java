/*
 * Voxem
 * Copyright (c) 2014-2015, Maxim Roncacé <caseif@caseif.net>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.caseif.voxem.world;

import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class World {

	public long seed;

	private final HashMap<Integer, Level> levels = new HashMap<>();

	public long creationTime;
	
	private final String name;
	private final int chunkCount;
	private final int chunkLength;
	private final int chunkHeight;

	private JsonObject json = new JsonObject();

	private boolean ticking = false;

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

	public JsonObject getJson(){
		return json;
	}

	public void setJson(JsonObject json){
		this.json = json;
	}

	public boolean isTicking(){
		return ticking;
	}

	public void setTicking(boolean ticking){
		this.ticking = ticking;
	}

}
