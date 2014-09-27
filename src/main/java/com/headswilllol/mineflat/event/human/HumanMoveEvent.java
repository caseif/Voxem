package com.headswilllol.mineflat.event.human;

import com.headswilllol.mineflat.world.Location;
import com.headswilllol.mineflat.entity.Human;
import com.headswilllol.mineflat.event.Cancellable;

/**
 * Fired when a player moves from one point in space to another.
 */
public class HumanMoveEvent extends HumanEvent implements Cancellable {
	
	/**
	 * The location the entity is moving to.
	 */
	private Location to;
	/**
	 * The location of the entity before this event was fired.
	 */
	private Location from;
	
	/**
	 * Fired when a human moves from one point in space to another.
	 * @param entity The entity involved in this event.
	 * @param to The location the player is moving to.
	 * @param from The location of the player before this event was fired.
	 */
	public HumanMoveEvent(Human entity, Location to, Location from){
		this.entity = entity;
		this.to = to;
		this.from = from;
	}

	public Location getTo(){
		return to;
	}

	public void setTo(Location to){
		this.to = to;
	}

	public Location getFrom(){
		return from;
	}

	public void setFrom(Location from){
		this.from = from;
	}

	public void setCancelled(boolean cancelled){
		if (cancelled){
			entity.setX(from.getX());
			entity.setY(from.getY());
		}
		else {
			entity.setX(to.getX());
			entity.setY(to.getY());
		}
	}
	
}
