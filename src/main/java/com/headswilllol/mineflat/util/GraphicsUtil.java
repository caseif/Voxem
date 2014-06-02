package com.headswilllol.mineflat.util;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.headswilllol.mineflat.Block;
import com.headswilllol.mineflat.Location;
import com.headswilllol.mineflat.Material;

public class GraphicsUtil {
	
	public static HashMap<Material, BufferedImage> textures = new HashMap<Material, BufferedImage>();
	public static HashMap<Material, Location> texCoords = new HashMap<Material, Location>();
	public static int atlas;
	public static float atlasSize;

	public static void addTexture(Material m){
		try {
			InputStream is = GraphicsUtil.class.getClassLoader().getResourceAsStream(
					"textures/block/" + m.toString().toLowerCase() + ".png");
			InputStream newIs = ImageUtil.asInputStream(ImageUtil.scaleImage(
					ImageIO.read(is), Block.length, Block.length));
			BufferedImage b = ImageIO.read(newIs);
			textures.put(m, b);
		}
		catch (Exception ex){
			System.err.println("Exception occurred while preparing texture for material " + m.toString());
			ex.printStackTrace();
		}
	}
}
