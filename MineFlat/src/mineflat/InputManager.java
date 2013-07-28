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

		float xShift = 0.305f;

		if (isKeyDown(left1) || isKeyDown(left2)){
			float newX = MineFlat.player.getX() -
					(Player.playerSpeed * (MineFlat.delta / Sys.getTimerResolution()));
			float y = MineFlat.player.getY();
			if ((int)y == y)
				y -= 1;
			Block b1 = null, b2 = null, b3 = null;
			if (y >= 0)
				b1 = new Location((float)Math.floor(newX + xShift),
						(float)Math.floor(y)).getBlock();
			if (y >= -1)
				b2 = new Location((float)Math.floor(newX + xShift),
						(float)Math.floor(y) + 1).getBlock();
			if (y >= -2)
				b3 = new Location((float)Math.floor(newX + xShift),
						(float)Math.floor(y) + 2).getBlock();
			if (b1 != null)
				if (b1.getType() != Material.AIR)
					return;
			if (b2 != null)
				if (b2.getType() != Material.AIR)
					return;
			if (b3 != null)
				if (b3.getType() != Material.AIR)
					return;
			MineFlat.player.setX(newX);
			moved = true;
		}
		if (isKeyDown(right1) || isKeyDown(right2)){
			float newX = MineFlat.player.getX() +
					(Player.playerSpeed * (MineFlat.delta / Sys.getTimerResolution()));
			float y = MineFlat.player.getY();
			if ((int)y == y)
				y -= 1;
			Block b1 = null, b2 = null, b3 = null;
			if (y >= 0)
				b1 = new Location((float)Math.floor(newX + xShift),
						(float)Math.floor(y)).getBlock();
			if (y >= -1)
				b2 = new Location((float)Math.floor(newX + xShift),
						(float)Math.floor(y) + 1).getBlock();
			if (y >= -2)
				b3 = new Location((float)Math.floor(newX + xShift),
						(float)Math.floor(y) + 2).getBlock();
			if (b1 != null)
				if (b1.getType() != Material.AIR)
					return;
			if (b2 != null)
				if (b2.getType() != Material.AIR)
					return;
			if (b3 != null)
				if (b3.getType() != Material.AIR)
					return;
			MineFlat.player.setX(newX);
			moved = true;
		}
		if (isKeyDown(jump1) || isKeyDown(jump2) || isKeyDown(jump3)){
			float newY = MineFlat.player.getY() -
					(Player.playerSpeed * (MineFlat.delta / Sys.getTimerResolution()));
			MineFlat.player.setY(newY);
			moved = true;
		}
		if (isKeyDown(down1) || isKeyDown(down2)){
			float newY = MineFlat.player.getY() +
					(Player.playerSpeed * (MineFlat.delta / Sys.getTimerResolution()));
			MineFlat.player.setY(newY);
			moved = true;
		}

		if (moved){
			Event.fireEvent(new PlayerMoveEvent(MineFlat.player, MineFlat.player.getLocation(), old));
		}

	}

}
