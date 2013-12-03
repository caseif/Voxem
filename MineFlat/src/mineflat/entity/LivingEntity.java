package mineflat.entity;

import mineflat.Direction;

public class LivingEntity extends Entity {

	protected Direction dir;
	protected int walkFor;
	protected int walkTime;
	protected int lastUpdate;
	
	public Direction getDirection(){
		return dir;
	}
	
	public int getPlannedWalkTime(){
		return walkFor;
	}
	
	public int getActualWalkTime(){
		return walkTime;
	}
	
	public int getLastUpdate(){
		return lastUpdate;
	}
	
	public void setDirection(Direction dir){
		this.dir = dir;
	}
	
	public void setPlannedWalkTime(int time){
		this.walkFor = time;
	}
	
	public void setActualWalkTime(int time){
		this.walkTime = time;
	}
	
	public void setLastUpdate(int time){ // probably won't need this, but whatever
		this.lastUpdate = time;
	}
}
