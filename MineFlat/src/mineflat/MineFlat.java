package mineflat;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import mineflat.util.BufferUtil;
import mineflat.util.ImageUtil;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;


public class MineFlat {
	
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
		
		new Block(Material.DIRT, 15, 15);
		new Block(Material.DIRT, -15, -15);
		new Block(Material.DIRT, 150, 150);
		new Block(Material.DIRT, -150, -150);
		
		while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			
			Block.draw();
			
			Display.sync(60);

			Display.update();
		}
		
	}
	
}
