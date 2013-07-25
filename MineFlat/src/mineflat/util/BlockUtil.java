package mineflat.util;

import java.io.InputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import mineflat.Block;
import mineflat.Chunk;
import mineflat.Location;
import mineflat.Material;

public class BlockUtil {

	public static HashMap<Material, Texture> textures = new HashMap<Material, Texture>();

	public static void addTexture(Material m){
		try {
			InputStream is = BlockUtil.class.getClassLoader().getResourceAsStream(
					"textures/" + m.toString().toLowerCase() + ".png");
			InputStream newIs = ImageUtil.asInputStream(ImageUtil.scaleImage(
					ImageIO.read(is), Block.length, Block.length));
			Texture t = TextureLoader.getTexture("PNG", newIs);
			textures.put(m, t);
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

}
