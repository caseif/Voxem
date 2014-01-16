package mineflat;

import static org.lwjgl.input.Keyboard.*;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import mineflat.event.Event;
import mineflat.event.block.BlockBreakEvent;
import mineflat.event.block.BlockPlaceEvent;

public class InputManager {

	private static int left1 = KEY_A;
	private static int left2 = KEY_LEFT;
	private static int right1 = KEY_D;
	private static int right2 = KEY_RIGHT;
	private static int jump1 = KEY_W;
	private static int jump2 = KEY_UP;
	private static int jump3 = KEY_SPACE;
	private static int console = KEY_GRAVE;

	private static boolean f3 = false;
	private static boolean mouse1 = false;
	private static boolean mouse2 = false;
	private static int mouseX = 0;
	private static int mouseY = 0;

	private static long lastAction = 0;
	private static long actionWait = 350;
	private static long lastType = 0;
	private static long typingWait = 25;

	public static boolean consoleEnabled = false;

	public static int[] keysToCheck= {console};
	public static boolean[] keysDownLastFrame = new boolean[keysToCheck.length];
	public static boolean[] keysPressed = new boolean[keysToCheck.length];



	public static void initialize(){
		for(int i = 0; i < keysToCheck.length; i++){
			keysDownLastFrame[i] = false;
			keysPressed[i] = false;
		}
	}

	public static boolean isKeyPressed(int key){
		if (Timing.getTime() - lastType > typingWait){
			for(int i = 0; i < keysToCheck.length; i++){
				if(key == keysToCheck[i]){
					return keysPressed[i];
				}
			}
			lastType = Timing.getTime();
		}
		return false;
	}
	public static void pollInput(){
		for(int i = 0; i < keysToCheck.length; i++){
			if(!isKeyDown(keysToCheck[i]) && keysDownLastFrame[i]){
				keysPressed[i] = true;
			}else{
				keysPressed[i] = false;
			}
			if(isKeyDown(keysToCheck[i]))
				keysDownLastFrame[i] = true;
			else
				keysDownLastFrame[i] = false;
		}



		if(!consoleEnabled){

			if ((isKeyDown(left1) || isKeyDown(left2)) && (isKeyDown(right1) || isKeyDown(right2)))
				MineFlat.player.setDirection(Direction.STATIONARY);
			else if (isKeyDown(left1) || isKeyDown(left2)){
				MineFlat.player.setFacing(Direction.LEFT);
				MineFlat.player.setDirection(Direction.LEFT);
			}else if (isKeyDown(right1) || isKeyDown(right2)){
				MineFlat.player.setDirection(Direction.RIGHT);
				MineFlat.player.setFacing(Direction.RIGHT);
			}else
				MineFlat.player.setDirection(Direction.STATIONARY);
			if (isKeyDown(jump1) || isKeyDown(jump2) || isKeyDown(jump3))
				MineFlat.player.setJumping(true);
			else
				MineFlat.player.setJumping(false);
			if (isKeyDown(KEY_F3))
				f3 = true;
			else
				f3 = false;
			if (Mouse.isButtonDown(0))
				mouse1 = true;
			else
				mouse1 = false;
			if (Mouse.isButtonDown(1))
				mouse2 = true;
			else
				mouse2 = false;
			mouseX = Mouse.getX();
			mouseY = Mouse.getY();

		}else{

		}

		if(isKeyPressed(console)){
			if(consoleEnabled)
				consoleEnabled = false;
			else
				consoleEnabled = true;
		}

	}

	public static void manage(){

		if (f3 && System.currentTimeMillis() - lastAction >= actionWait){
			MineFlat.debug = !MineFlat.debug;
			lastAction = System.currentTimeMillis();
		}

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

		if (mouse1){
			if (System.currentTimeMillis() - lastAction >= actionWait){
				if (Block.selected != null &&
						!Block.isBlockEmpty((Block.selected.getBlock())) &&
						Block.selected.getBlock().getType() != Material.BEDROCK){
					Block b = Block.getBlock((int)Math.floor(Block.selected.getX()),
							(int)Math.floor(Block.selected.getY()));
					Event.fireEvent(new BlockBreakEvent(Block.selected, b));
				}
				lastAction = System.currentTimeMillis();
			}
		}

		if (mouse2){
			if (System.currentTimeMillis() - lastAction >= actionWait){
				if (Block.selected != null){
					int x = Block.selected.getX() > MineFlat.player.getX() ?
							(int)Math.floor(Block.selected.getX()) :
								(int)Math.floor(Block.selected.getX()) + 1;
							int y = Block.selected.getY() > MineFlat.player.getY() ?
									(int)Math.floor(Block.selected.getY()) :
										(int)Math.floor(Block.selected.getY()) + 1;
									double playerX = MineFlat.player.getX();
									double playerY = MineFlat.player.getY();
									double mouseX = (InputManager.mouseX - GraphicsHandler.xOffset) /
											(float)Block.length;
									double mouseY = (Display.getHeight() -
											InputManager.mouseY - GraphicsHandler.yOffset) /
											(float)Block.length;
									double xDiff = mouseX - playerX;
									double yDiff = mouseY - playerY;
									double m = yDiff / xDiff;
									double b = playerY - m * playerX;
									Location l = null;
									if (m * x + b >= Block.selected.getY() &&
											m * x + b <= Block.selected.getY() + 1){
										if (x == Block.selected.getX())
											x -= 1;
										l = new Location(x, Block.selected.getY());
									}
									else if ((y - b) / m >= Block.selected.getX() &&
											(y - b) / m <= Block.selected.getX() + 1){
										if (y == Block.selected.getY())
											y -= 1;
										l = new Location(Block.selected.getX(), y);
									}
									if (l != null){
										if ((int)playerY == y)
											playerY -= 1;
										boolean pBlock = false;
										if (playerY >= 0 && playerY < MineFlat.world.getChunkHeight())
											if (l.getX() == (float)Math.floor(playerX) &&
											l.getY() == (float)Math.floor(playerY))
												pBlock = true;
										if (playerY >= -1 && playerY < MineFlat.world.getChunkHeight() - 1)
											if (l.getX() == (float)Math.floor(playerX) &&
											l.getY() == (float)Math.floor(playerY + 1))
												pBlock = true;
										if (!pBlock && l.getY() > 0 && l.getY() < MineFlat.world.getChunkHeight()){
											Block block = new Block(Material.WOOD, l);
											block.addToWorld();
											Event.fireEvent(
													new BlockPlaceEvent(l, block));
										}
									}
				}
				lastAction = System.currentTimeMillis();
			}
		}

	}

}
