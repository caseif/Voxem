package com.headswilllol.mineflat;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.headswilllol.mineflat.entity.Entity;
import com.headswilllol.mineflat.entity.LivingEntity;
import com.headswilllol.mineflat.entity.Player;
import com.headswilllol.mineflat.util.BufferUtil;
import com.headswilllol.mineflat.util.GraphicsUtil;
import com.headswilllol.mineflat.util.ImageUtil;
import com.headswilllol.mineflat.util.NumUtil;
import com.headswilllol.mineflat.util.VboUtil;

public class GraphicsHandler implements Runnable {

	/**
	 * The minimum OpenGL version required to run the game
	 */
	public static double MINIMUM_GL_VERSION = 1.5;

	/**
	 * The system's OpenGL version
	 */
	public static double glVersion;

	// fps-related stuff
	public static float renderDelta = 0f;
	public static long lastRenderTime = Timing.getTime();
	public static int fps = 0;
	public static long lastFpsUpdate = 0;
	public static final long fpsUpdateTime = (long)(0.25 * Timing.timeResolution);

	public static int texSize = 16;

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
	 * The number of chunks adjacent to the player's that should be generated/loaded
	 */
	public static int renderDistance = 6;

	private static final float charWHRatio = 3f / 4f;

	// space between characters when height is 32px
	private static final float interCharSpace = 0.1f;

	// offset of character shadows (duh)
	private static final float shadowOffset = 1;

	public static HashMap<Character, Float> specialChars = new HashMap<Character, Float>();

	public void run(){
		try {
			DisplayMode[] modes = Display.getAvailableDisplayModes();
			for (int i = 0; i < modes.length; i++){
				if (modes[i].getWidth() == Display.getDesktopDisplayMode().getWidth() &&
						modes[i].getHeight() == Display.getDesktopDisplayMode()
						.getHeight() && modes[i].isFullscreenCapable()){
					Display.setDisplayMode(modes[i]);
					break;
				}
			}
			Display.setVSyncEnabled(true);
			Display.setTitle("MineFlat");
			Display.setResizable(false);
			ByteBuffer[] icons = null;
			if (System.getProperty("os.name").startsWith("Windows")){
				icons = new ByteBuffer[2];
				BufferedImage icon1 = ImageUtil.scaleImage(
						ImageIO.read(Main.class.getClassLoader()
								.getResourceAsStream("images/icon.png")), GraphicsHandler.texSize,
								GraphicsHandler.texSize);
				BufferedImage icon2 = ImageUtil.scaleImage(ImageIO.read(
						Main.class.getClassLoader()
						.getResourceAsStream("images/icon.png")), 32, 32);;
						icons[0] = BufferUtil.asByteBuffer(icon1);
						icons[1] = BufferUtil.asByteBuffer(icon2);
			}
			else if (System.getProperty("os.name").startsWith("Mac")){
				icons = new ByteBuffer[1];
				BufferedImage icon = ImageUtil.scaleImage(ImageIO.read(
						Main.class.getClassLoader()
						.getResourceAsStream("images/icon.png")), Main.world.getChunkHeight(), Main.world.getChunkHeight());
				icons[0] = BufferUtil.asByteBuffer(icon);
			}
			else {
				icons = new ByteBuffer[1];
				BufferedImage icon = ImageUtil.scaleImage(ImageIO.read(
						Main.class.getClassLoader()
						.getResourceAsStream("images/icon.png")), 32, 32);
				icons[0] = BufferUtil.asByteBuffer(icon);
			}
			Display.setIcon(icons);
			Display.create();
			glVersion = Double.parseDouble(glGetString(GL_VERSION).substring(0, 3));
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
		
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_COLOR_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);

		GraphicsUtil.addTexture(Material.AIR);
		GraphicsUtil.addTexture(Material.DIRT);
		GraphicsUtil.addTexture(Material.GRASS);
		GraphicsUtil.addTexture(Material.GRASS_TOP);
		GraphicsUtil.addTexture(Material.STONE);
		GraphicsUtil.addTexture(Material.LOG);
		GraphicsUtil.addTexture(Material.WOOD);
		GraphicsUtil.addTexture(Material.BEDROCK);
		GraphicsUtil.addTexture(Material.COAL_ORE);
		GraphicsUtil.addTexture(Material.IRON_ORE);
		GraphicsUtil.addTexture(Material.GOLD_ORE);
		GraphicsUtil.addTexture(Material.DIAMOND_ORE);

		//initializeFont();

		try {
			initializeChars();
		}
		catch (Exception ex){
			System.err.println("Exception occurred while preparing texture for characters");
			ex.printStackTrace();
			System.exit(-1);
		}

		LivingEntity.initialize();

		VboUtil.initialize();
		VboUtil.prepareBindArray();

		while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){

			if (Timing.getTime() - lastFpsUpdate >= fpsUpdateTime){
				//TODO: This goes wonky every once in a while. Someone (I) should probably get on that.
				fps = (int)Math.floor(Timing.timeResolution / renderDelta);
				Timing.displayDelta = (int)Timing.delta;
				lastFpsUpdate = Timing.getTime();
			}

			renderDelta = Timing.getTime() - lastRenderTime;
			lastRenderTime = Timing.getTime();
			
			glClearColor(0.3f * Player.light, 0.3f * Player.light, 0.8f * Player.light, 1f);
			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			InputManager.pollInput();
			if (VboUtil.rebindArray)
				VboUtil.bindArray();
			VboUtil.render();

			synchronized (Main.lock){
				if (Block.selected != null){
					Block.selectedX = Block.selected.getPixelX() + xOffset;
					Block.selectedY = Block.selected.getPixelY() + yOffset;
					Block.isSelected = true;
				}
				else
					Block.isSelected = false;
			}
			if (Block.isSelected){
				glColor3f(0f, 0f, 0f);
				glBegin(GL_LINES);
				glVertex2f(Block.selectedX,
						Block.selectedY);
				glVertex2f(Block.selectedX + Block.length,
						Block.selectedY);
				glVertex2f(Block.selectedX + Block.length,
						Block.selectedY);
				glVertex2f(Block.selectedX + Block.length,
						Block.selectedY + Block.length);
				glVertex2f(Block.selectedX + Block.length,
						Block.selectedY + Block.length);
				glVertex2f(Block.selectedX,
						Block.selectedY + Block.length);
				glVertex2f(Block.selectedX,
						Block.selectedY + Block.length);
				glVertex2f(Block.selectedX,
						Block.selectedY);
				glEnd();
			}

			Player.centerPlayer();
			for (Entity e : Main.world.getEntities())
				e.draw();

			// draw debug menu, if necessary
			if (Main.debug){
				float height = 16f;
				drawString("fps: " + fps, 10, 10, height, true);
				drawString("delta (ms): " +
						String.format("%.3f", Timing.displayDelta / 1000000), 10, 40, height, true);
				drawString("x: " +
						String.format("%.3f", Main.player.getX()), 10, 70, height, true);
				drawString("y: " +
						String.format("%.3f", Main.player.getY()), 10, 100, height, true);
				drawString("light level: " + String.format("%.3f", Player.light * Block.maxLight), 10, 130, height, true);
				drawString("ticks: " + TickManager.getTicks(), 10, 160, height, true);
				int mb = 1024 * 1024;
				Runtime runtime = Runtime.getRuntime();
				drawString(runtime.totalMemory() / mb + "mb allocated memory: " +
						(runtime.totalMemory() - runtime.freeMemory()) / mb + "mb used, " +
						runtime.freeMemory() / mb + "mb free", 10, 190, height, true);
			}

			/*if(Console.enabled)
				Console.draw();*/

			//Display.sync(60);
			Display.update();
		}
		Main.closed = true;
	}

	public static void initializeChars() throws IOException {
		InputStream is = NumUtil.class.getClassLoader().getResourceAsStream(
				"textures/chars.png");
		//InputStream newIs = ImageUtil.asInputStream(ImageUtil.scaleImage(
		//		ImageIO.read(is), MineFlat.world.getChunkLength(),
		//		MineFlat.world.getChunkLength())); // in case I decide to resize it later on

		Main.charTexture = ImageUtil.createTextureFromStream(is);

		specialChars.put('!', 0f);
		specialChars.put('?', 1f);
		specialChars.put('.', 2f);
		specialChars.put(',', 3f);
		specialChars.put(':', 4f);
		specialChars.put('-', 5f);
		specialChars.put('+', 6f);
		specialChars.put('(', 7f);
		specialChars.put(')', 8f);
		specialChars.put('µ', 9f);
	}

	public static void drawString(String str, float x, float y, float height, boolean shadow){
		float wm = 42f + 2f / 3f;
		float hm = 4f;
		float width = height * charWHRatio;
		glPushMatrix();
		glEnable(GL_BLEND);
		glBindTexture(GL_TEXTURE_2D, Main.charTexture);
		for (int i = 0; i <= (shadow ? 1 : 0); i++){
			if (i == 0 && shadow) {
				glColor3f(0f, 0f, 0f);
				x -= shadowOffset;
				y -= shadowOffset;
			}
			else if (i == 1){
				glColor3f(1f, 1f, 1f);
				x += shadowOffset;
				y += shadowOffset;
			}
			else
				glColor3f(1f, 1f, 1f);
			glBegin(GL_QUADS);
			float pos = 0f;
			for (char c : str.toCharArray()){
				float tx = 25f, ty = 3f;
				if (Character.isLetter(c)){
					if (Character.isUpperCase(c)){
						tx = c - 'A';
						ty = 0;
					}
					else {
						tx = c - 'a';
						ty = 0; // temporary until I remember to add lowercase characters to the image
					}
				}
				else if (NumUtil.isInt(Character.toString(c))){
					tx = Float.parseFloat((Character.toString(c)));
					ty = 2f;
				}
				else if (c == ' '){
					pos += 1;
					continue;
				}
				else {
					ty = 3f;
					if (specialChars.containsKey((Character)c))
						tx = (float)specialChars.get((Character)c);
					else
						tx = 25f;
				}
				glTexCoord2f(tx / wm, (ty / hm));
				glVertex2f(x + pos * width, y);
				glTexCoord2f((tx + 1f) / wm, (ty / hm));
				glVertex2f(x + pos * width + width, y);
				glTexCoord2f((tx + 1f) / wm, (ty + 1f) / hm);
				glVertex2f(x + pos * width + width, y + height);
				glTexCoord2f(tx / wm, (ty + 1f) / hm);
				glVertex2f(x + pos * width, y + height);
				if (c != '.' && c != ',' && c != ':')
					pos += 1f + (height / 32f) * interCharSpace;
				else
					pos += (1f / 6f) + interCharSpace;
			}
		}
		glEnd();
		glDisable(GL_BLEND);
		glBindTexture(GL_TEXTURE_2D, 0);
		glPopMatrix();
	}

	public static int getStringLength(String str, float height){
		float pos = 0f;
		for (char c : str.toCharArray()){
			if (c == ' '){
				pos += 1;
				continue;
			}
			if (c != '.' && c != ',' && c != ':')
				pos += 1f + (height / 32f) * interCharSpace;
			else
				pos += (1f / 6f) + interCharSpace;
		}
		return (int)Math.ceil(pos);
	}

	/*@SuppressWarnings("unchecked")
	public static void initializeFont(){
		float size = 16F;
		Font awtFont = new Font("Courier New", Font.PLAIN, (int)size);
		font = new UnicodeFont(awtFont.deriveFont(0, size));
		font.addAsciiGlyphs();
		ColorEffect e = new ColorEffect();
		e.setColor(Color.BLACK);
		font.getEffects().add(e);
		try {
			font.loadGlyphs();
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
	}*/

}
