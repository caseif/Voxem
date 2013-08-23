package mineflat.event.player;

import mineflat.Location;
import mineflat.Player;
import mineflat.event.Cancellable;

/**
 * Fired when a player moves from one point in space to another.
 */
public class PlayerMoveEvent extends PlayerEvent implements Cancellable {
	
	/**
	 * The location the player is moving to.
	 */
	private Location to;
	/**
	 * The location of the player before this event was fired.
	 */
	private Location from;
	
	/**
	 * Fired when the player moves from one point in space to another.
	 * @param player The player involved in this event.
	 * @param to The location the player is moving to.
	 * @param from The location of the player before this event was fired.
	 */
	public PlayerMoveEvent(Player player, Location to, Location from){
		this.player = player;
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
		if (cancelled)
			player.setLocation(from);
		else
			player.setLocation(to);
	}
	
}
