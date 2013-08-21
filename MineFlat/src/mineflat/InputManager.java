package mineflat;

import static org.lwjgl.input.Keyboard.*;

import mineflat.util.MiscUtil;

public class InputManager {

	private static int left1 = KEY_A;
	private static int left2 = KEY_LEFT;
	private static int right1 = KEY_D;
	private static int right2 = KEY_RIGHT;
	private static int jump1 = KEY_W;
	private static int jump2 = KEY_UP;
	private static int jump3 = KEY_SPACE;

	public static void manage(){

		float xShift = 0;

		if (isKeyDown(left1) || isKeyDown(left2)){
			float newX = MineFlat.player.getX() -
					(Player.playerSpeed * (MineFlat.delta / MiscUtil.getTimeResolution()));
			float y = MineFlat.player.getY();
			if ((int)y == y)
				y -= 1;
			Block b1 = null, b2 = null, b3 = null;
			if (y >= 0 && y < 128)
				b1 = new Location((float)Math.floor(newX + xShift),
						(float)Math.floor(y)).getBlock();
			if (y >= -1 && y < 127)
				b2 = new Location((float)Math.floor(newX + xShift),
						(float)Math.floor(y) + 1).getBlock();
			if (y >= -2 && y < 126)
				b3 = new Location((float)Math.floor(newX + xShift),
						(float)Math.floor(y) + 2).getBlock();
			boolean blocked = false;
			if (b1 != null)
				blocked = true;
			else if (b2 != null)
				blocked = true;
			else if (b3 != null)
				blocked = true;
			if (!blocked)
				MineFlat.player.setX(newX);
		}
		if (isKeyDown(right1) || isKeyDown(right2)){
			float newX = MineFlat.player.getX() +
					(Player.playerSpeed * (MineFlat.delta / MiscUtil.getTimeResolution()));
			float y = MineFlat.player.getY();
			if ((int)y == y)
				y -= 1;
			Block b1 = null, b2 = null, b3 = null;
			if (y >= 0 && y < 128)
				b1 = new Location((float)Math.floor(newX + xShift),
						(float)Math.floor(y)).getBlock();
			if (y >= -1 && y < 127)
				b2 = new Location((float)Math.floor(newX + xShift),
						(float)Math.floor(y) + 1).getBlock();
			if (y >= -2 && y < 126)
				b3 = new Location((float)Math.floor(newX + xShift),
						(float)Math.floor(y) + 2).getBlock();
			boolean blocked = false;
			if (b1 != null)
				blocked = true;
			else if (b2 != null)
				blocked = true;
			else if (b3 != null)
				blocked = true;
			if (!blocked)
				MineFlat.player.setX(newX);
		}
		if (isKeyDown(jump1) || isKeyDown(jump2) || isKeyDown(jump3)){
			if (Player.jumpFrame == 0 && !Player.falling){
				Player.jumpFrame += MineFlat.delta / MiscUtil.getTimeResolution();
			}
		}
	}

}
