package com.headswilllol.mineflat.event.player;

import com.headswilllol.mineflat.entity.Human;
import com.headswilllol.mineflat.entity.Player;
import com.headswilllol.mineflat.event.Event;
import com.headswilllol.mineflat.event.human.HumanEvent;
import com.headswilllol.mineflat.event.human.HumanMoveEvent;

public class PlayerEvent extends HumanEvent {

	/**
	 * The player involved in this event.
	 */
	protected Player entity;

	@Override
	public Player getEntity(){
		return entity;
	}

	public void setEntity(Player entity){
		this.entity = entity;
	}

	@Override
	public void setEntity(Human entity){
		if (entity instanceof Player)
			super.setEntity(entity);
		else
			throw new IllegalArgumentException("Entity must be a player!");
	}
	
}
