package mineflat;

import static org.lwjgl.input.Keyboard.*;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import mineflat.event.Event;
import mineflat.event.block.BlockBreakEvent;
import mineflat.event.block.BlockPlaceEvent;
import mineflat.util.BlockUtil;
import mineflat.util.MiscUtil;

public class InputManager {

	private static int left1 = KEY_A;
	private static int left2 = KEY_LEFT;
	private static int right1 = KEY_D;
	private static int right2 = KEY_RIGHT;
	private static int jump1 = KEY_W;
	private static int jump2 = KEY_UP;
	private static int jump3 = KEY_SPACE;

	private static long lastAction = 0;
	private static long actionWait = 350;

	public static void manage(){

		float xShift = 0;

		if (isKeyDown(left1) || isKeyDown(left2)){
			float newX = MineFlat.player.getX() -
					(Player.playerSpeed * (MineFlat.delta / MiscUtil.getTimeResolution()));
			float y = MineFlat.player.getY();
			if ((int)y == y)
				y -= 1;
			Block b1 = null, b2 = null, b3 = null;
			if (y >= 0 && y < 128 && y % 1 != 0)
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
			if (y >= 0 && y < 128 && y % 1 != 0)
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

		if (isKeyDown(KEY_F3))
			System.out.println("Player: " + MineFlat.player.getX() + ", " +
					MineFlat.player.getY());

		/*if (isKeyDown(KEY_F11)){
			if (System.currentTimeMillis() - lastAction >= actionWait){
				try {
					Display.setFullscreen(!Display.isFullscreen());
				}
				catch (Exception ex){
					ex.printStackTrace();
					System.err.println("Could not change fullscreen mode!");
				}
				lastAction = System.currentTimeMillis();
			}
		}*/

		if (Mouse.isButtonDown(0)){
			if (System.currentTimeMillis() - lastAction >= actionWait){
				if (MineFlat.selected != null){
					Block b = BlockUtil.getBlock((int)Math.floor(MineFlat.selected.getX()),
							(int)Math.floor(MineFlat.selected.getY()));
					Event.fireEvent(new BlockBreakEvent(MineFlat.selected, b));
				}
				lastAction = System.currentTimeMillis();
			}
		}

		if (Mouse.isButtonDown(1)){
			if (System.currentTimeMillis() - lastAction >= actionWait){
				if (MineFlat.selected != null){
					int x = MineFlat.selected.getX() > MineFlat.player.getX() ?
							(int)Math.floor(MineFlat.selected.getX()) :
								(int)Math.floor(MineFlat.selected.getX()) + 1;
							int y = MineFlat.selected.getY() > MineFlat.player.getY() ?
									(int)Math.floor(MineFlat.selected.getY()) :
										(int)Math.floor(MineFlat.selected.getY()) + 1;
									double playerX = MineFlat.player.getX();
									double playerY = MineFlat.player.getY();
									double mouseX = (Mouse.getX() - MineFlat.xOffset) /
											(float)Block.length;
									double mouseY = (Display.getHeight() -
											Mouse.getY() - MineFlat.yOffset) /
											(float)Block.length;
									double xDiff = mouseX - playerX;
									double yDiff = mouseY - playerY;
									double m = yDiff / xDiff;
									double b = playerY - m * playerX;
									Location l = null;
									if (m * x + b >= MineFlat.selected.getY() &&
											m * x + b <= MineFlat.selected.getY() + 1){
										if (x == MineFlat.selected.getX())
											x -= 1;
										l = new Location(x, MineFlat.selected.getY());
									}
									else if ((y - b) / m >= MineFlat.selected.getX() &&
											(y - b) / m <= MineFlat.selected.getX() + 1){
										if (y == MineFlat.selected.getY())
											y -= 1;
										l = new Location(MineFlat.selected.getX(), y);
									}
									if ((int)playerY == y)
										playerY -= 1;
									boolean pBlock = false;
									if (playerY >= 0 && playerY < 128)
										if (l.getX() == (float)Math.floor(playerX) &&
										l.getY() == (float)Math.floor(playerY + 1))
											pBlock = true;
									if (playerY >= -1 && playerY < 127)
										if (l.getX() == (float)Math.floor(playerX) &&
										l.getY() == (float)Math.floor(playerY + 2))
											pBlock = true;
									if (playerX % 1 < 0.3){
										if (playerY >= 0 && playerY < 128)
											if (l.getX() == (float)Math.floor(playerX - 1) &&
											l.getY() == (float)Math.floor(playerY + 1))
												pBlock = true;
										if (playerY >= -1 && playerY < 127)
											if (l.getX() == (float)Math.floor(playerX - 1) &&
											l.getY() == (float)Math.floor(playerY + 2))
												pBlock = true;
									}
									else if (playerX % 1 > 0.7){
										if (playerY >= 0 && playerY < 128)
											if (l.getX() == (float)Math.floor(playerX + 1) &&
											l.getY() == (float)Math.floor(playerY))
												pBlock = true;
										if (playerY >= -1 && playerY < 127)
											if (l.getX() == (float)Math.floor(playerX + 1) &&
											l.getY() == (float)Math.floor(playerY + 1))
												pBlock = true;
									}
									if (!pBlock && l.getY() > 0 && l.getY() < 128){
										Block block = new Block(Material.LOG, l);
										block.addToWorld();
										Event.fireEvent(
												new BlockPlaceEvent(l, block));
									}
				}
				lastAction = System.currentTimeMillis();
			}
		}

	}

}
