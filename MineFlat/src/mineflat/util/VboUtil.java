package mineflat.util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import mineflat.Block;
import mineflat.Chunk;
import mineflat.MineFlat;

import org.lwjgl.BufferUtils;

public class VboUtil {

	public static int bufferHandle;
	public static float[] vertexArray;

	public static void initialize(){
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_COLOR_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		ImageUtil.createAtlas();
		IntBuffer buffer = BufferUtils.createIntBuffer(1);
		glGenBuffers(buffer);
		bufferHandle = buffer.get(0);
		updateArray();
	}

	public static void updateArray(){
		List<Float> values = new ArrayList<Float>();
		for (Chunk c : Chunk.chunks){
			//TODO: Render each chunk into a separate array, then add to
			//the main array when a chunk is loaded, unloaded, or modified.
			for (int x = 0; x < 16; x++){
				for (int y = 0; y < 128; y++){
					Block b = c.getBlock(x, y);
					if (b != null){
						float tX = Float.valueOf(BlockUtil.texCoords
								.get(b.getType())
								.getX());
						float tY = Float.valueOf(BlockUtil.texCoords.get(b.getType()).getY());

						// top left
						// vertex
						values.add(Float.valueOf((float)b.getLocation().getPixelX()));
						values.add(Float.valueOf((float)b.getLocation().getPixelY()));
						// light
						values.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						values.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						values.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						// texture
						values.add(tX);
						values.add(tY);

						// top right
						// vertex
						values.add(Float.valueOf((float)b.getLocation().getPixelX()) +
								Block.length);
						values.add(Float.valueOf((float)b.getLocation().getPixelY()));
						// light
						values.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						values.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						values.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						// texture
						values.add(tX + 1 / ((float)BlockUtil.atlas.getImageWidth() / 16));
						values.add(tY);

						// bottom right
						// vertex
						values.add(Float.valueOf((float)b.getLocation().getPixelX()) +
								Block.length);
						values.add(Float.valueOf((float)b.getLocation().getPixelY()) +
								Block.length);
						// light
						values.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						values.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						values.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						// texture
						values.add(tX + 1 / ((float)BlockUtil.atlas.getImageWidth() / 16));
						values.add(tY + 1 / ((float)BlockUtil.atlas.getImageWidth() / 16));

						// bottom left
						// vertex
						values.add(Float.valueOf((float)b.getLocation().getPixelX()));
						values.add(Float.valueOf((float)b.getLocation().getPixelY()) +
								Block.length);
						// light
						values.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						values.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						values.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						// texture
						values.add(tX);
						values.add(tY + 1 / ((float)BlockUtil.atlas.getImageWidth() / 16));
						b.updateLight();
					}
				}
			}
		}

		vertexArray = new float[values.size()];
		for (int i = 0; i < values.size(); i++) {
			Float f = values.get(i);
			vertexArray[i] = (f != null ? f : Float.NaN);
		}
		
		FloatBuffer vertexData = (FloatBuffer)BufferUtils
				.createFloatBuffer(vertexArray.length).flip();
		vertexData.limit(vertexData.capacity());
		vertexData.put(vertexArray);
		vertexData.rewind();
		glBindBuffer(GL_ARRAY_BUFFER, bufferHandle);
		glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);

	}

	public static void render(){
		glPushMatrix();
		glTranslatef(MineFlat.xOffset, MineFlat.yOffset, 0);
		glBindTexture(GL_TEXTURE_2D, BlockUtil.atlas.getTextureID());
		glVertexPointer(2, GL_FLOAT, 28, 0);
		glColorPointer(3, GL_FLOAT, 28, 8);
		glTexCoordPointer(2, GL_FLOAT, 28, 20);
		glDrawArrays(GL_QUADS, 0, vertexArray.length / 7);
		glBindTexture(GL_TEXTURE_2D, 0);
		glPopMatrix();
	}

}
