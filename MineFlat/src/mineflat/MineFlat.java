package mineflat;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import mineflat.noise.SimplexNoiseGenerator;
import mineflat.util.BlockUtil;
import mineflat.util.BufferUtil;
import mineflat.util.ChunkUtil;
import mineflat.util.ImageUtil;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

/**
 * Copyright (c) 2013 Maxim Roncacé
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

	public static Player player = new Player(new Location(0, 0));

	public static SimplexNoiseGenerator noise = new SimplexNoiseGenerator(System.currentTimeMillis());

	/**
	 * The level of variation the terrain should have
	 */
	public static final int terrainVariation = 5;
	
	/**
	 * The number of chunks adjacent to the player's that should be generated/loaded
	 */
	public static final int renderDistance = 6;
	
	public static void main(String[] args){

		try {
			Display.setDisplayMode(new DisplayMode(Display.getDesktopDisplayMode().getWidth() - 20, Display.getDesktopDisplayMode().getHeight() - 100));
			Display.setTitle("MineFlat");
			ByteBuffer[] icons = null;
			if (System.getProperty("os.name").startsWith("Windows")){
				icons = new ByteBuffer[2];
				BufferedImage icon1 = ImageUtil.scaleImage(ImageIO.read(MineFlat.class.getClassLoader().getResourceAsStream("images/icon.png")), 16, 16);
				BufferedImage icon2 = ImageUtil.scaleImage(ImageIO.read(MineFlat.class.getClassLoader().getResourceAsStream("images/icon.png")), 32, 32);;
				icons[0] = BufferUtil.asByteBuffer(icon1);
				icons[1] = BufferUtil.asByteBuffer(icon2);
			}
			else if (System.getProperty("os.name").startsWith("Mac")){
				icons = new ByteBuffer[1];
				BufferedImage icon = ImageUtil.scaleImage(ImageIO.read(MineFlat.class.getClassLoader().getResourceAsStream("images/icon.png")), 128, 128);
				icons[0] = BufferUtil.asByteBuffer(icon);
			}
			else {
				icons = new ByteBuffer[1];
				BufferedImage icon = ImageUtil.scaleImage(ImageIO.read(MineFlat.class.getClassLoader().getResourceAsStream("images/icon.png")), 32, 32);
				icons[0] = BufferUtil.asByteBuffer(icon);
			}
			Display.setIcon(icons);
			Display.create();
		}
		catch (Exception ex){
			ex.printStackTrace();
		}

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);

		glEnable(GL_TEXTURE_2D);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glClearColor(0.3f, 0.3f, 0.8f, 1f);

		Block.initialize();

		BlockUtil.addTexture(Material.DIRT);

		while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){

			ChunkUtil.generateChunks();
			
			Block.draw();

			Display.sync(60);

			Display.update();
			
		}

	}

}
