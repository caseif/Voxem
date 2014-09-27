package com.headswilllol.mineflat.event.player;

import com.headswilllol.mineflat.world.Location;
import com.headswilllol.mineflat.entity.Player;
import com.headswilllol.mineflat.event.Cancellable;
import com.headswilllol.mineflat.event.Event;
import com.headswilllol.mineflat.event.human.HumanMoveEvent;

/**
 * Fired when a player moves from one point in space to another.
 */
public class PlayerMoveEvent extends HumanMoveEvent implements Cancellable {
	
	/**
	 * Fired when the player moves from one point in space to another.
	 * @param player The player involved in this event.
	 * @param to The location the player is moving to.
	 * @param from The location of the player before this event was fired.
	 */
	public PlayerMoveEvent(Player player, Location to, Location from){
		super(player, to, from);
		Event.fireEvent((HumanMoveEvent)this);
	}
	
}
