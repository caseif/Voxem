package com.headswilllol.mineflat;

import static org.lwjgl.input.Keyboard.*;

import java.util.ArrayList;

import com.headswilllol.mineflat.gui.Gui;
import com.headswilllol.mineflat.world.Location;
import com.headswilllol.mineflat.world.Block;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.headswilllol.mineflat.event.Event;
import com.headswilllol.mineflat.event.block.BlockBreakEvent;
import com.headswilllol.mineflat.event.block.BlockPlaceEvent;
import com.headswilllol.mineflat.event.input.KeyPressEvent;

public class InputManager {

	private static final int LEFT_1 = KEY_A;
	private static final int LEFT_2 = KEY_LEFT;
	private static final int RIGHT_1 = KEY_D;
	private static final int RIGHT_2 = KEY_RIGHT;
	private static final int JUMP_1 = KEY_W;
	private static final int JUMP_2 = KEY_UP;
	private static final int JUMP_3 = KEY_SPACE;
	private static final int CONSOLE = KEY_GRAVE;

	private static boolean f3 = false;
	private static boolean mouse1 = false;
	private static boolean mouse2 = false;
	private static int mouseX = 0;
	private static int mouseY = 0;

	private static long lastAction = 0;
	private static final long ACTION_WAIT = 200;

	public static final ArrayList<Integer> baseKeysToCheck = new ArrayList<Integer>();
	public static ArrayList<Integer> keysToCheck= new ArrayList<Integer>();
	public static final ArrayList<Boolean> keysDownLastFrame = new ArrayList<Boolean>();
	public static final ArrayList<Boolean> keysPressed = new ArrayList<Boolean>();

	private static int kStage = 0;

	public static void updateKeys(ArrayList<Integer> keys){
		keysToCheck = keys;
		keysDownLastFrame.clear();
		keysPressed.clear();
		for (int i = 0; i < keysToCheck.size(); i++){
			keysDownLastFrame.add(false);
			keysPressed.add(false);
		}
	}

	public static void initialize(){
		baseKeysToCheck.add(CONSOLE);
		InputManager.updateKeys(baseKeysToCheck);
	}

	public static void pollInput(){

		while (next()) {
			int key = getEventKey();
			boolean down = getEventKeyState();
			//boolean repeat = isRepeatEvent(); // I might need this at some point...
			char c = getEventCharacter();
			if (down) {
				Event.fireEvent(new KeyPressEvent(key, c));
				if (kStage < 10){
					switch (kStage) {
						case 0:
							if (key == KEY_UP)
								kStage += 1;
							else
								kStage = 0;
							break;
						case 1:
							if (key == KEY_UP)
								kStage += 1;
							else
								kStage = 0;
							break;
						case 2:
							if (key == KEY_DOWN)
								kStage += 1;
							else
								kStage = 0;
							break;
						case 3:
							if (key == KEY_DOWN)
								kStage += 1;
							else
								kStage = 0;
							break;
						case 4:
							if (key == KEY_LEFT)
								kStage += 1;
							else
								kStage = 0;
							break;
						case 5:
							if (key == KEY_RIGHT)
								kStage += 1;
							else
								kStage = 0;
							break;
						case 6:
							if (key == KEY_LEFT)
								kStage += 1;
							else
								kStage = 0;
							break;
						case 7:
							if (key == KEY_RIGHT)
								kStage += 1;
							else
								kStage = 0;
							break;
						case 8:
							if (key == KEY_B)
								kStage += 1;
							else
								kStage = 0;
							break;
						case 9:
							if (key == KEY_A) {
								kStage += 1;
								SoundManager.generateMeme();
							}
							else
								kStage = 0;
							break;
					}
				}
			}
		}

		mouse1 = Mouse.isButtonDown(0);
		mouse2 = Mouse.isButtonDown(1);
		mouseX = Mouse.getX();
		mouseY = Mouse.getY();

		for (Gui gui : GraphicsHandler.guis.values()){
			if (gui.isActive() && mouse1){
				gui.checkMousePos();
			}
		}

		if (Main.player != null) {
			if ((isKeyDown(LEFT_1) || isKeyDown(LEFT_2)) && (isKeyDown(RIGHT_1) || isKeyDown(RIGHT_2)))
				Main.player.setMovementDirection(Direction.STATIONARY);
			else if (isKeyDown(LEFT_1) || isKeyDown(LEFT_2)) {
				Main.player.setFacingDirection(Direction.LEFT);
				Main.player.setMovementDirection(Direction.LEFT);
			} else if (isKeyDown(RIGHT_1) || isKeyDown(RIGHT_2)) {
				Main.player.setFacingDirection(Direction.RIGHT);
				Main.player.setMovementDirection(Direction.RIGHT);
			} else
				Main.player.setMovementDirection(Direction.STATIONARY);
			if (isKeyDown(JUMP_1) || isKeyDown(JUMP_2) || isKeyDown(JUMP_3))
				Main.player.setJumping(true);
			else
				Main.player.setJumping(false);
		}
		f3 = isKeyDown(KEY_F3);

	}

	public static void manage(){

		if (f3 && System.currentTimeMillis() - lastAction >= ACTION_WAIT){
			GraphicsHandler.guis.get("debug").setActive(!GraphicsHandler.guis.get("debug").isActive());
			lastAction = System.currentTimeMillis();
		}

		/*if (isKeyDown(KEY_F11)){
			if (System.currentTimeMillis() - lastAction >= ACTION_WAIT){
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

		/*if (Console.enabled){
			if (mouse1){
				if(mouseX > 30 && mouseX < Display.getWidth() - 30 && mouseY < Display.getHeight() - 270 &&
						mouseY > Display.getHeight() - 290)
					Console.focused = true;
				else
					Console.focused = false;
			}
		}
		else {*/
		if (mouse1){
			if (System.currentTimeMillis() - lastAction >= ACTION_WAIT){
				if (Block.selected != null &&
						!Block.isAir((Block.selected.getBlock())) &&
						Block.selected.getBlock().getType() != Material.BEDROCK){
					Block b = Block.getBlock(Main.player.getLevel(), (int)Math.floor(Block.selected.getX()),
							(int)Math.floor(Block.selected.getY()));
					Event.fireEvent(new BlockBreakEvent(Block.selected, b));
				}
				lastAction = System.currentTimeMillis();
			}
		}

		if (mouse2){
			if (System.currentTimeMillis() - lastAction >= ACTION_WAIT){
				if (Block.selected != null){
					int x = Block.selected.getX() > Main.player.getX() ?
							(int)Math.floor(Block.selected.getX()) :
							(int)Math.floor(Block.selected.getX()) + 1;
					int y = Block.selected.getY() > Main.player.getY() ?
							(int)Math.floor(Block.selected.getY()) :
							(int)Math.floor(Block.selected.getY()) + 1;
					double playerX = Main.player.getX();
					double playerY = Main.player.getY();
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
						l = new Location(Main.player.getLevel(), x, Block.selected.getY());
					}
					else if ((y - b) / m >= Block.selected.getX() &&
							(y - b) / m <= Block.selected.getX() + 1){
						if (y == Block.selected.getY())
							y -= 1;
						l = new Location(Main.player.getLevel(), Block.selected.getX(), y);
					}
					if (l != null && Block.isAir(l)){
						if ((int)playerY == y)
							playerY -= 1;
						boolean pBlock = false;
						if (playerY >= 0 && playerY < Main.world.getChunkHeight())
							if (l.getX() == (float)Math.floor(playerX) &&
									l.getY() == (float)Math.floor(playerY))
								pBlock = true;
						if (playerY >= -1 && playerY < Main.world.getChunkHeight() - 1)
							if (l.getX() == (float)Math.floor(playerX) &&
									l.getY() == (float)Math.floor(playerY + 1))
								pBlock = true;
						if (!pBlock && l.getY() > 0 &&
								l.getY() < Main.world.getChunkHeight()){
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
		//}
	}

}
