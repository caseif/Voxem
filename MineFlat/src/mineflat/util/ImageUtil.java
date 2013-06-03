package mineflat.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ImageUtil {

	public static BufferedImage scaleImage(BufferedImage img, int width, int height){
		BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)newImg.getGraphics();
		g.scale(((double)width / (double)img.getWidth()), ((double)height / (double)img.getHeight()));
		g.drawImage(img, 0, 0, null);
		g.dispose();
		return newImg;
	}

}
