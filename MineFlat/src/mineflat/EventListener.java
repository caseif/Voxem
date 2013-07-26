package mineflat;

import mineflat.event.EventHandler;
import mineflat.event.Listener;
import mineflat.event.PlayerMoveEvent;

public class EventListener implements Listener {

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e){
		System.out.println("PlayerMoveEvent fired at location (" + e.getTo().getX() + ", " + e.getTo().getY() + ")");
	}
	
}
