package mineflat.util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import mineflat.GraphicsHandler;
import mineflat.Location;
import mineflat.Material;

import org.lwjgl.BufferUtils;

import de.matthiasmann.twl.utils.PNGDecoder;

public class ImageUtil {

	public static BufferedImage scaleImage(BufferedImage img, int width, int height){
		//TODO: Change argument to InputStream and move platform-dependent code here
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
		//TODO: Rewrite to use something other than BufferedImage
		int finalSize = NumUtil.nextPowerOfTwo((int)Math.sqrt(GraphicsUtil.textures.size() *
				Math.pow(GraphicsHandler.texSize, 2)));
		BufferedImage atlas = new BufferedImage(finalSize, finalSize, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = atlas.createGraphics();
		int width = atlas.getWidth();
		int height = atlas.getHeight();
		int y = 0;
		for (int i = 0; i < GraphicsUtil.textures.size(); i++){
			int x = i * GraphicsHandler.texSize - (height * (y / GraphicsHandler.texSize));
			if (x >= width){
				x = 0;
				y += GraphicsHandler.texSize;
			}
			g.drawImage(GraphicsUtil.textures.get(GraphicsUtil.textures.keySet().toArray()[i]), x, y, null);
			GraphicsUtil.texCoords.put((Material)(GraphicsUtil.textures.keySet().toArray()[i]),
					new Location((float)x / width, (float)y / height));
		}
		GraphicsUtil.atlasSize = finalSize;

		/*try {
			File outputfile = new File("atlas.png");
			ImageIO.write(atlas, "png", outputfile);
		}
		catch (IOException ex){
			ex.printStackTrace();
		}*/

		try {
			GraphicsUtil.atlas = ImageUtil.createTextureFromStream(ImageUtil.asInputStream(atlas));
		}
		catch (Exception ex){
			ex.printStackTrace();
			System.err.println("Failed to load block atlas as texture");
			System.exit(-1);
		}

	}

	public static int createTextureFromStream(InputStream stream){
		try {
			PNGDecoder decoder = new PNGDecoder(stream);
			ByteBuffer buffer = BufferUtils.createByteBuffer(decoder.getWidth() * decoder.getHeight() * 4);
			decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
			buffer.flip();

			int handle = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, handle);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
			glBindTexture(GL_TEXTURE_2D, 0);

			return handle;
		}
		catch (IOException ex){
			ex.printStackTrace();
			System.err.println("Failed to load texture!");
			System.exit(-1);
		}

		return -1;
	}

}
