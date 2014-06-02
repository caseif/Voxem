package com.headswilllol.mineflat.event.player;

import com.headswilllol.mineflat.entity.Player;
import com.headswilllol.mineflat.event.Event;

public class PlayerEvent extends Event {
	
	/**
	 * The player involved in this event.
	 */
	protected Player player;

	public Player getPlayer(){
		return player;
	}

	public void setPlayer(Player player){
		this.player = player;
	}
	
}
