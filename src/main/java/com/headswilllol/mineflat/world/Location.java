/*
 * MineFlat
 * Copyright (c) 2014, Maxim Roncacé <mproncace@gmail.com>
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
package com.headswilllol.mineflat.world;

import com.headswilllol.mineflat.*;
import com.headswilllol.mineflat.vector.Vector2f;

/**
 * Represents a physical location in a world.
 */
public class Location extends Vector2f {

	protected Level level;

	public Location(Level level, float x, float y){
		super(x, y);
		this.level = level;
	}
	
	public Level getLevel() {
		return level;
	}

	
	public void setLevel(Level level){
		this.level = level;
	}

	public Block getBlock(){
		Chunk c = Main.world.getLevel(0).getChunk(getChunk());
		if (c != null){
			return c.getBlock(Chunk.getIndexInChunk((int)x), (int)y);
		}
		return null;
	}

	public boolean equals(Object o){
		if (o instanceof Location){
			Location l = (Location)o;
			return l.getX() == this.x && l.getY() == this.y;
		}
		return false;
	}

	public int hashCode(){
		return 41 * (int)(x * 37 + y * 43 + 41);
	}

	public int getChunk(){
		return x >= 0 ? (int)x / Main.world.getChunkLength() + 1 :
			(int)(x + 1) / Main.world.getChunkLength() - 1;
	}

	public float getPosInChunk(){
		return x >= 0 ? Math.abs(x) % Main.world.getChunkLength() :
			Main.world.getChunkLength() - Math.abs(x) % Main.world.getChunkLength(); 
	}

	public int getPixelX(){
		return (int)(getX() * Block.length);
	}

	public int getPixelY(){
		return (int)(getY() * Block.length);
	}

	public Location add(Location vector){
		return add(vector.getX(), vector.getY());
	}

	public Location add(float x, float y){
		return new Location(level, this.x + x, this.y + y);
	}

	public Location subtract(Location vector){
		return subtract(vector.getX(), vector.getY());
	}

	public Location subtract(float x, float y){
		return new Location(level, this.x - x, this.y - y);
	}

	public static int getXFromPixels(int x){
		return x / Block.length - GraphicsHandler.xOffset;
	}

	public static int getYFromPixels(int y){
		return y / Block.length - GraphicsHandler.yOffset;
	}

	@Override
	public Location clone(){
		return new Location(level, x, y);
	}

}
