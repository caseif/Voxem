package mineflat.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

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

}
