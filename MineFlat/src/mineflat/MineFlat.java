package mineflat;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import mineflat.event.EventManager;
import mineflat.noise.SimplexNoiseGenerator;
import mineflat.util.BlockUtil;
import mineflat.util.BufferUtil;
import mineflat.util.ChunkUtil;
import mineflat.util.ImageUtil;
import mineflat.util.MiscUtil;
import mineflat.util.VboUtil;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

/**
 * @author Maxim Roncac�
 * 
 * @License
 *
 * Copyright (c) 2013 Maxim Roncac�
 *
 * THE WORK IS PROVIDED UNDER THE TERMS OF THIS CREATIVE COMMONS PUBLIC LICENSE
 * ("CCPL" OR "LICENSE"). THE WORK IS PROTECTED BY COPYRIGHT AND/OR OTHER APPLICABLE LAW. ANY USE
 * OF THE WORK OTHER THAN AS AUTHORIZED UNDER THIS LICENSE OR COPYRIGHT LAW IS PROHIBITED.
 *
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND AGREE TO BE BOUND BY THE TERMS
 * OF THIS LICENSE. TO THE EXTENT THIS LICENSE MAY BE CONSIDERED TO BE A CONTRACT, THE LICENSOR
 * GRANTS YOU THE RIGHTS CONTAINED HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND
 * CONDITIONS.
 *
 * The full text of this license can be found in the root directory of this JAR file under the file "LICENSE"
 */

public class MineFlat {

	/**
	 * The minimum OpenGL version required to run the game
	 */
	public static double MINIMUM_GL_VERSION = 1.5;

	/**
	 * The system's OpenGL version
	 */
	public static double glVersion;

	/**
	 * The variable used to determine the duration of each iteration so as to move ingame objects at a constant speed
	 */
	public static float delta = 0;

	/**
	 * Used in the calculation of delta
	 */
	public static long time = MiscUtil.getTime();

	/**
	 * Used in the calculation of delta
	 */
	public static long lastTime = MiscUtil.getTime();

	/**
	 * The player of the game, or rather, their virtual doppelganger
	 */
	public static Player player = new Player(new Location(16, 0));

	/**
	 * The seed to be used for terrain generation
	 */
	public static long seed = System.currentTimeMillis();

	/**
	 * The game's noise generator for use in terrain generation
	 */
	public static SimplexNoiseGenerator noise = new SimplexNoiseGenerator(seed);

	/**
	 * The number of horizontal pixels visual elements will be shifted before being rendered
	 * (- is left; + is right)
	 */
	public static int xOffset = 0;

	/**
	 * The number of vertical pixels visual elements will be shifted before being rendered
	 * (- is up; + is down)
	 */
	public static int yOffset = 150;

	/**
	 * The level of variation the terrain should have
	 */
	public static int terrainVariation = 13;

	/**
	 * The number of chunks adjacent to the player's that should be generated/loaded
	 */
	public static int renderDistance = 6;
	
	/**
	 * The block which is currently selected.
	 */
	public static Location selected = null;

	public static void main(String[] args){

		try {
			Display.setDisplayMode(new DisplayMode(Display.getDesktopDisplayMode()
					.getWidth() - 20, Display.getDesktopDisplayMode().getHeight() - 100));
			Display.setTitle("MineFlat");
			ByteBuffer[] icons = null;
			if (System.getProperty("os.name").startsWith("Windows")){
				icons = new ByteBuffer[2];
				BufferedImage icon1 = ImageUtil.scaleImage(
						ImageIO.read(MineFlat.class.getClassLoader()
								.getResourceAsStream("images/icon.png")), 16, 16);
				BufferedImage icon2 = ImageUtil.scaleImage(ImageIO.read(
						MineFlat.class.getClassLoader()
						.getResourceAsStream("images/icon.png")), 32, 32);;
				icons[0] = BufferUtil.asByteBuffer(icon1);
				icons[1] = BufferUtil.asByteBuffer(icon2);
			}
			else if (System.getProperty("os.name").startsWith("Mac")){
				icons = new ByteBuffer[1];
				BufferedImage icon = ImageUtil.scaleImage(ImageIO.read(
						MineFlat.class.getClassLoader()
						.getResourceAsStream("images/icon.png")), 128, 128);
				icons[0] = BufferUtil.asByteBuffer(icon);
			}
			else {
				icons = new ByteBuffer[1];
				BufferedImage icon = ImageUtil.scaleImage(ImageIO.read(
						MineFlat.class.getClassLoader()
						.getResourceAsStream("images/icon.png")), 32, 32);
				icons[0] = BufferUtil.asByteBuffer(icon);
			}
			Display.setIcon(icons);
			Display.create();
			glVersion = Double.parseDouble(Display.getVersion().substring(0, 3));
			if (glVersion < MINIMUM_GL_VERSION){
				System.err.println("Minimum required OpenGL version is " +
			MINIMUM_GL_VERSION + "; " + 
						"current version is " + glVersion);
				Display.destroy();
				System.exit(0);
			}
		}
		catch (Exception ex){
			ex.printStackTrace();
		}

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();

		glEnable(GL_TEXTURE_2D);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

		glClearColor(0.3f, 0.3f, 0.8f, 1f);

		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		BlockUtil.addTexture(Material.DIRT);
		BlockUtil.addTexture(Material.GRASS);
		BlockUtil.addTexture(Material.STONE);
		BlockUtil.addTexture(Material.LOG);
		BlockUtil.addTexture(Material.WOOD);

		Player.initialize();

		ChunkUtil.generateChunks();

		EventManager.registerEventListener(new EventListener());

		VboUtil.initialize();

		while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){

			time = MiscUtil.getTime();
			delta = time - lastTime;
			lastTime = time;

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			VboUtil.render();
			InputManager.manage();
			centerPlayer();
			Player.handleVerticalMovement();
			player.draw();

			double playerX = player.getX();
			double playerY = player.getY();
			double mouseX = (Mouse.getX() - xOffset) / (float)Block.length;
			double mouseY = (Display.getHeight() - Mouse.getY() - yOffset) /
					(float)Block.length;
			double xDiff = (int)(mouseX - playerX);
			double yDiff = mouseY != playerY ? (playerY - mouseY) :
				(playerY - mouseY + 1);
			double angle = Math.toDegrees(Math.atan2(xDiff, yDiff));
			if (xDiff < 0)
				angle += 360;
			
			for (double d = 0.5; d <= 5; d += 0.5){
				double xAdd = d * Math.sin(angle);
				double yAdd = d * Math.cos(angle);
				int blockX = (int)Math.floor(playerX + xAdd);
				int blockY = (int)Math.floor(playerY + yAdd);
				if (blockY >= 0 && blockY <= 127){
					if (BlockUtil.getBlock(blockX, blockY) != null){
						selected = new Location(blockX, blockY);
						break;
					}
				}
			}

			Display.sync(60);
			Display.update();

		}

	}

	public static void centerPlayer(){
		xOffset = Display.getWidth() / 2 - player.getLocation().getPixelX();
		yOffset = Display.getHeight() / 2 - player.getLocation().getPixelY();	
	}

}
