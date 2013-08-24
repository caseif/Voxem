package mineflat.util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import mineflat.Block;
import mineflat.Chunk;
import mineflat.MineFlat;

import org.lwjgl.BufferUtils;

public class VboUtil {

	public static int bufferHandle;
	public static float[] vertexArray;
	public static HashMap<Integer, Float[]> chunkArrays = new HashMap<Integer, Float[]>();

	/**
	 * Initializes VBO support in the OpenGL instance.
	 */
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

	/**
	 * Updates the entire VBO. This method may cause a severe drop in FPS while running.
	 */
	public static void updateArray(){
		for (Chunk c : Chunk.chunks){
			List<Float> cValues = new ArrayList<Float>();
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
						cValues.add(Float.valueOf((float)b.getLocation().getPixelX()));
						cValues.add(Float.valueOf((float)b.getLocation().getPixelY()));
						// light
						cValues.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						cValues.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						cValues.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						// texture
						cValues.add(tX);
						cValues.add(tY);

						// top right
						// vertex
						cValues.add(Float.valueOf((float)b.getLocation().getPixelX()) +
								Block.length);
						cValues.add(Float.valueOf((float)b.getLocation().getPixelY()));
						// light
						cValues.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						cValues.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						cValues.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						// texture
						cValues.add(tX + 1 / ((float)BlockUtil.atlas.getImageWidth() / 16));
						cValues.add(tY);

						// bottom right
						// vertex
						cValues.add(Float.valueOf((float)b.getLocation().getPixelX()) +
								Block.length);
						cValues.add(Float.valueOf((float)b.getLocation().getPixelY()) +
								Block.length);
						// light
						cValues.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						cValues.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						cValues.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						// texture
						cValues.add(tX + 1 / ((float)BlockUtil.atlas.getImageWidth() / 16));
						cValues.add(tY + 1 / ((float)BlockUtil.atlas.getImageWidth() / 16));

						// bottom left
						// vertex
						cValues.add(Float.valueOf((float)b.getLocation().getPixelX()));
						cValues.add(Float.valueOf((float)b.getLocation().getPixelY()) +
								Block.length);
						// light
						cValues.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						cValues.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						cValues.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						// texture
						cValues.add(tX);
						cValues.add(tY + 1 / ((float)BlockUtil.atlas.getImageWidth() / 16));
						b.updateLight();
					}
				}
			}

			chunkArrays.remove(c.getNum());
			Float[] cArray = cValues.toArray(new Float[]{});
			chunkArrays.put(c.getNum(), cArray);
		}

		recreateArray();
		bindArray();

	}

	/**
	 * Updates the portion of the vertex array containing data for the specified chunk.
	 * @param chunk The chunk to update in the VBO.
	 */
	public static void updateChunkArray(int chunk){
		Chunk c = ChunkUtil.getChunk(chunk);
		if (c != null){
			List<Float> cValues = new ArrayList<Float>();
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
						cValues.add(Float.valueOf((float)b.getLocation().getPixelX()));
						cValues.add(Float.valueOf((float)b.getLocation().getPixelY()));
						// light
						cValues.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						cValues.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						cValues.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						// texture
						cValues.add(tX);
						cValues.add(tY);

						// top right
						// vertex
						cValues.add(Float.valueOf((float)b.getLocation().getPixelX()) +
								Block.length);
						cValues.add(Float.valueOf((float)b.getLocation().getPixelY()));
						// light
						cValues.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						cValues.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						cValues.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						// texture
						cValues.add(tX + 1 / ((float)BlockUtil.atlas.getImageWidth() / 16));
						cValues.add(tY);

						// bottom right
						// vertex
						cValues.add(Float.valueOf((float)b.getLocation().getPixelX()) +
								Block.length);
						cValues.add(Float.valueOf((float)b.getLocation().getPixelY()) +
								Block.length);
						// light
						cValues.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						cValues.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						cValues.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						// texture
						cValues.add(tX + 1 / ((float)BlockUtil.atlas.getImageWidth() / 16));
						cValues.add(tY + 1 / ((float)BlockUtil.atlas.getImageWidth() / 16));

						// bottom left
						// vertex
						cValues.add(Float.valueOf((float)b.getLocation().getPixelX()));
						cValues.add(Float.valueOf((float)b.getLocation().getPixelY()) +
								Block.length);
						// light
						cValues.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						cValues.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						cValues.add(Float.valueOf((float)b.getLightLevel() / Block.maxLight));
						// texture
						cValues.add(tX);
						cValues.add(tY + 1 / ((float)BlockUtil.atlas.getImageWidth() / 16));
						b.updateLight();
					}
				}
			}

			chunkArrays.remove(c.getNum());
			Float[] cArray = cValues.toArray(new Float[]{});
			chunkArrays.put(c.getNum(), cArray);

			recreateArray();
			bindArray();
		}
	}

	/**
	 * Rebinds the VBO to the OpenGL instance, effectively redrawing it.
	 */
	public static void bindArray(){
		FloatBuffer vertexData = (FloatBuffer)BufferUtils
				.createFloatBuffer(vertexArray.length).flip();
		vertexData.limit(vertexData.capacity());
		vertexData.put(vertexArray);
		vertexData.rewind();
		glBindBuffer(GL_ARRAY_BUFFER, bufferHandle);
		glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
	}

	/**
	 * Syndicates all chunk arrays into a single VBO.
	 */
	public static void recreateArray(){
		List<Float> newList = new ArrayList<Float>();
		for (int i : chunkArrays.keySet())
			newList.addAll(Arrays.asList(chunkArrays.get(i)));
		vertexArray = new float[newList.size()];
		for (int i = 0; i < newList.size(); i++)
			vertexArray[i] = newList.get(i) != null ? newList.get(i) : Float.NaN;
	}

	/**
	 * Renders the VBO to the screen. This does not rebind it, and so block updates will not be
	 * taken into account until VboUtil.bindArray() is called.
	 */
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
