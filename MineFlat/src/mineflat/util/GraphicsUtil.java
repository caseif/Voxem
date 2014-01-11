package mineflat.util;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;

import mineflat.Location;
import mineflat.Material;

import org.newdawn.slick.opengl.Texture;

public class GraphicsUtil {
	
	public static HashMap<Material, BufferedImage> textures = new HashMap<Material, BufferedImage>();
	public static HashMap<Material, Location> texCoords = new HashMap<Material, Location>();
	public static Texture atlas;

	public static void addTexture(Material m){
		try {
			InputStream is = GraphicsUtil.class.getClassLoader().getResourceAsStream(
					"textures/" + m.toString().toLowerCase() + ".png");
			InputStream newIs = ImageUtil.asInputStream(ImageUtil.scaleImage(
					ImageIO.read(is), 16, 16));
			BufferedImage b = ImageIO.read(newIs);
			textures.put(m, b);
		}
		catch (Exception ex){
			System.err.println("Exception occurred while preparing texture for material " + m.toString());
			ex.printStackTrace();
		}
	}
}
