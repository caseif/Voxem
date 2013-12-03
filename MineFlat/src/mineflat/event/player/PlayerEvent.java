package mineflat.event.player;

import mineflat.entity.Player;
import mineflat.event.Event;

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
