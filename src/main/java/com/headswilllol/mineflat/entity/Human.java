package com.headswilllol.mineflat.entity;

import com.headswilllol.mineflat.event.Event;
import com.headswilllol.mineflat.event.human.HumanMoveEvent;
import com.headswilllol.mineflat.location.WorldLocation;

/**
 * Represents a human in the world.
 */
public class Human extends LivingEntity {

	public Human(WorldLocation location){
		super(EntityType.HUMAN, location, 0.5f, 2f);
	}

	public WorldLocation getLocation(){
		return location;
	}

	@Override
	public void setX(float x){
		WorldLocation old = getLocation();
		super.setX(x);
		Event.fireEvent(new HumanMoveEvent(this, getLocation(), old));
	}

	@Override
	public void setY(float y){
		WorldLocation old = getLocation();
		super.setY(y);
		Event.fireEvent(new HumanMoveEvent(this, getLocation(), old));
	}

	public float getYVelocity(){
		return yVelocity;
	}

	public void setYVelocity(float v){
		yVelocity = v;
	}

}
