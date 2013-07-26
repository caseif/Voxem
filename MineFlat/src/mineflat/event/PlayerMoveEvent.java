package mineflat.event;

import mineflat.Location;
import mineflat.Player;

/**
 * Fired when the player moves from one point in space to another.
 */
public class PlayerMoveEvent extends Event {
	
	/**
	 * The player involved in this event.
	 */
	private Player player;
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

	public Player getPlayer(){
		return player;
	}

	public void setPlayer(Player player){
		this.player = player;
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
	
}
