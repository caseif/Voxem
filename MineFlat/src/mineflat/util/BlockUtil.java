package mineflat.util;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.newdawn.slick.opengl.Texture;

import mineflat.Block;
import mineflat.Chunk;
import mineflat.Location;
import mineflat.Material;

public class BlockUtil {

	public static HashMap<Material, BufferedImage> textures = new HashMap<Material, BufferedImage>();
	public static HashMap<Material, Location> texCoords = new HashMap<Material, Location>();
	public static Texture atlas;

	public static void addTexture(Material m){
		try {
			InputStream is = BlockUtil.class.getClassLoader().getResourceAsStream(
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

	public static int getTop(int x){
		for (int yy = 0; yy < 128; yy++){
			if (new Location(x, yy).getBlock() != null){
				return yy;
			}
		}
		return -1;
	}

	public static Block getBlock(int x, int y){
		Chunk c = ChunkUtil.getChunk(new Location(x, y).getChunk());
		if (c != null)
			return c.getBlock(Math.abs(x % 16), y);
		return null;
	}
	
	public static boolean isBlockEmpty(Block b){
		if (b == null)
			return true;
		else if (b.getType() == Material.AIR)
			return true;
		return false;
	}

}
