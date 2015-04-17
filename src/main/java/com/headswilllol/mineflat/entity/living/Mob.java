/*
 * MineFlat
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
package com.headswilllol.mineflat.entity.living;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.headswilllol.mineflat.entity.EntityType;
import com.headswilllol.mineflat.entity.living.Living;
import com.headswilllol.mineflat.world.Location;

public abstract class Mob extends Living {

	protected float walkDistance;
	protected float distance;
	protected float lastX;
	public static final int MOB_CAPACITY = 100;
	public static final List<EntityType> mobTypes = new ArrayList<>();
	public static final HashMap<EntityType, Integer> light = new HashMap<>();
	
	public Mob(EntityType type, Location location, float width, float height){
		super(type, location, width, height);
	}
	
	public static void initialize(){
		mobTypes.add(EntityType.GHOST);
		light.put(EntityType.GHOST, 8);
		mobTypes.add(EntityType.SNAIL);
		light.put(EntityType.SNAIL, 16);
	}
	
	public float getPlannedWalkDistance(){
		return walkDistance;
	}
	
	public float getActualWalkDistance(){
		return distance;
	}
	
	public float getLastX(){
		return lastX;
	}
	
	public void setPlannedWalkDistance(float distance){
		this.walkDistance = distance;
	}
	
	public void setActualWalkDistance(float distance){
		this.distance = distance;
	}
	
	public void setLastX(float x){
		this.lastX = x;
	}
	
	public int getMaximumLightLevel(){
		return light.get(type);
	}
	
	public static int getMaximumLightLevel(EntityType type){
		return light.get(type);
	}

}
