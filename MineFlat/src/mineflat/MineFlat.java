package mineflat;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import mineflat.noise.SimplexNoiseGenerator;
import mineflat.util.BlockUtil;
import mineflat.util.BufferUtil;
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

			Block.draw();

			Display.sync(60);

			Display.update();

			for (int i = player.getLocation().getChunk() - 8; i <= player.getLocation().getChunk() + 8; i++){
				if (!Chunk.isGenerated(i)){
					Chunk c = new Chunk(i);
					System.out.println("Generating chunk " + i);
					for (int b = 0; b < 16; b++){
						System.out.println("Generating block " + (b + 1) + " in chunk");
						System.out.println(noise.noise(Chunk.getActualX(i, b)));
						System.out.println(noise.noise(Chunk.getActualX(i, b)) / 2);
						System.out.println((noise.noise(Chunk.getActualX(i, b)) / 2 + 0.5));
						int h = (int)((noise.noise(Chunk.getActualX(i, b)) / 2 + 0.5) * terrainVariation);
						System.out.println("Height is " + h);
						for (int y = h; y < 128; y++){
							c.setBlock(Material.DIRT, b, y);
							new Block(Material.DIRT, new Location(Chunk.getActualX(c.getNum(), b), y));
						}
					}
					/*Chunk c = new Chunk(i);
					int h0 = 0;
					int h1 = 0;
					Random r = new Random();
					if (Chunk.isGenerated(i - 1)){
						int diff = r.nextInt(1);
						if (diff == 0 && BlockUtil.getTop(Chunk.getActualX(i - 1, 0)) > 0)
							diff = -1;
						h0 = BlockUtil.getTop(Chunk.getActualX(i - 1, 15)) + diff;
						h0 = BlockUtil.getTop((i - 1) * 16 + 15);
					}
					if (Chunk.isGenerated(i + 1)){
						int diff = r.nextInt(1);
						if (diff == 0 && BlockUtil.getTop(Chunk.getActualX(i + 1, 0)) > 0)
							diff = -1;
						h1 = BlockUtil.getTop(Chunk.getActualX(i + 1, 0)) + diff;
						h1 = BlockUtil.getTop(Chunk.getActualX(i + 1, 0));
					}
					if (h0 == 0)
						h0 = r.nextInt(6);
					if (h1 == 0)
						h1 = r.nextInt(6);
					c.setBlock(Material.DIRT, 0, h0);
					c.setBlock(Material.DIRT, 15, h1);
					h1 = h0;
					for (int y = h0; y < 128; y++){
						c.setBlock(Material.DIRT, 0, y);
						new Block(Material.DIRT, c.getNum() * 16, y);
					}
					for (int y = h1; y < 128; y++){
						c.setBlock(Material.DIRT, 15, y);
						new Block(Material.DIRT, c.getNum() * 16 + 15, y);
					}

					//int x0 = c.getNum() * 16 + c.getNum() % (r.nextInt(3) + 1);
					//int x1 = c.getNum() * 16 + 15 + c.getNum() % (r.nextInt(3) + 1);
					int x0 = 0;
					int x1 = 15;
					for (int x = 1; x < 15; x++){
						int t = (x - x0) / (x1 - x0);
						t = t * t * (3 - 2 * t);
						int h = h0 + t * h1;
						h = (int)(0.5 * h * (2 * x) + 0.25 * h * (4 * x) + 0.125 * h * (8 * x));
						h /= 5;
						h = (int)(0.5 * h * (2 * x) + 0.25 * h * (4 * x) + 0.125 * h * (8 * x));
						h /= 100;
						c.setBlock(Material.DIRT, x, h);
						new Block(Material.DIRT, new Location(x, h));
						for (int y = h; y < 128; y++){
							c.setBlock(Material.DIRT, x, y);
							new Block(Material.DIRT, new Location(Chunk.getActualX(c.getNum(), x), y));
						}
					}*/
				}
			}

		}

	}

}
