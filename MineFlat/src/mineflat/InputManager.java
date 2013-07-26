package mineflat;

import static org.lwjgl.input.Keyboard.*;

import org.lwjgl.Sys;

import mineflat.event.Event;
import mineflat.event.PlayerMoveEvent;

public class InputManager {

	private static int left1 = KEY_A;
	private static int left2 = KEY_LEFT;
	private static int right1 = KEY_D;
	private static int right2 = KEY_RIGHT;
	private static int jump1 = KEY_W;
	private static int jump2 = KEY_UP;
	private static int jump3 = KEY_SPACE;
	private static int down1 = KEY_S;
	private static int down2 = KEY_DOWN;

	public static void manage(){
		
		Location old = MineFlat.player.getLocation();

		boolean moved = false;

		if (isKeyDown(left1) || isKeyDown(left2)){
			MineFlat.player.setX(MineFlat.player.getX() -
					(Player.playerSpeed * (MineFlat.delta / Sys.getTimerResolution())));
			moved = true;
		}
		if (isKeyDown(right1) || isKeyDown(right2)){
			MineFlat.player.setX(MineFlat.player.getX() +
					(Player.playerSpeed * (MineFlat.delta / Sys.getTimerResolution())));
			moved = true;
		}
		if (isKeyDown(jump1) || isKeyDown(jump2) || isKeyDown(jump3)){
			MineFlat.player.setY(MineFlat.player.getY() -
					(Player.playerSpeed * (MineFlat.delta / Sys.getTimerResolution())));
			moved = true;
		}
		if (isKeyDown(down1) || isKeyDown(down2)){
			MineFlat.player.setY(MineFlat.player.getY() +
					(Player.playerSpeed * (MineFlat.delta / Sys.getTimerResolution())));
			moved = true;
		}
		
		if (moved){
			Event.fireEvent(new PlayerMoveEvent(MineFlat.player, MineFlat.player.getLocation(), old));
		}

	}

}
