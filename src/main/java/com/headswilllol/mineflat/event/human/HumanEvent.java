package com.headswilllol.mineflat.event.human;

import com.headswilllol.mineflat.entity.Human;
import com.headswilllol.mineflat.entity.Player;
import com.headswilllol.mineflat.event.Event;

public class HumanEvent extends Event {
	
	/**
	 * The human involved in this event.
	 */
	protected Human entity;

	public Human getEntity(){
		return entity;
	}

	public void setEntity(Human entity){
		this.entity = entity;
	}
	
}
