package com.headswilllol.mineflat.event;

public interface Cancellable {
	
	/**
	 * Defines whether an event should be cancelled.
	 * @param cancelled When true, the event will be cancelled.
	 */
	public abstract void setCancelled(boolean cancelled);
	
}
