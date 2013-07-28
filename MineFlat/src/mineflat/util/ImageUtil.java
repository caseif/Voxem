package mineflat.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import mineflat.Block;
import mineflat.Location;
import mineflat.Material;

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
		int finalSize = (int)Math.sqrt(MiscUtil.nextPowerOfTwo((BlockUtil.textures.size()
				* Block.length * Block.length)));
		BufferedImage atlas = new BufferedImage(finalSize, finalSize, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = atlas.createGraphics();
		int y = 0;
		for (int i = 0; i < BlockUtil.textures.size(); i++){
			int x = i * Block.length;
			if (x + Block.length > finalSize){
				x = 0;
				y += Block.length;
			}
			g.drawImage(BlockUtil.textures.get(BlockUtil.textures.keySet().toArray()[i]), x, y, null);
			BlockUtil.texCoords.put((Material)(BlockUtil.textures.keySet().toArray()[i]),
					new Location((float)x / finalSize, (float)y / finalSize));
		}
		atlas = scaleImage(atlas, finalSize, finalSize);
		try {BlockUtil.atlas = BufferedImageUtil.getTexture("", atlas);}
		catch (Exception ex){ex.printStackTrace();}
	}

}
