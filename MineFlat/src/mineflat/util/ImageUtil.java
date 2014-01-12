package mineflat.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import mineflat.GraphicsHandler;
import mineflat.Location;
import mineflat.Material;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.util.BufferedImageUtil;

public class ImageUtil {

	public static BufferedImage scaleImage(BufferedImage img, int width, int height){
		BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)newImg.getGraphics();
		g.scale(((double)width / (double)img.getWidth()), ((double)height / (double)img.getHeight()));
		g.drawImage(img, 0, 0, null);
		g.dispose();
		return newImg;
	}
	
	public static InputStream asInputStream(BufferedImage bi) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bi, "png", baos);
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return is;
	}
	
	public static void createAtlas(){
		int finalSize = NumUtil.nextPowerOfTwo((int)Math.sqrt(GraphicsUtil.textures.size() *
				Math.pow(GraphicsHandler.texSize, 2)));
		BufferedImage atlas = new BufferedImage(finalSize, finalSize, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = atlas.createGraphics();
		int y = 0;
		for (int i = 0; i < GraphicsUtil.textures.size(); i++){
			int x = i * GraphicsHandler.texSize - (finalSize * (y / GraphicsHandler.texSize));
			if (x >= finalSize){
				x = 0;
				y += GraphicsHandler.texSize;
			}
			g.drawImage(GraphicsUtil.textures.get(GraphicsUtil.textures.keySet().toArray()[i]), x, y, null);
			GraphicsUtil.texCoords.put((Material)(GraphicsUtil.textures.keySet().toArray()[i]),
					new Location((float)x / finalSize, (float)y / finalSize));
		}
		atlas = scaleImage(atlas, finalSize, finalSize);
		try {GraphicsUtil.atlas = BufferedImageUtil.getTexture("", atlas, GL11.GL_NEAREST);}
		catch (Exception ex){ex.printStackTrace();}
	}

}
