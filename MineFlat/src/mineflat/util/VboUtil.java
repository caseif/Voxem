package mineflat.util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mineflat.Block;
import mineflat.Chunk;
import mineflat.GraphicsHandler;
import mineflat.Material;
import mineflat.MineFlat;

import org.lwjgl.BufferUtils;

public class VboUtil {

	public static int bufferHandle;
	public static float[] vertexArray;
	public static FloatBuffer vertexData = null;
	public static HashMap<Integer, Float[]> chunkArrays = new HashMap<Integer, Float[]>();
	public static boolean rebindArray = false;

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
		for (Chunk c : MineFlat.world.chunks)
			updateChunkArray(c.getNum());
		recreateArray();
	}

	/**
	 * Updates the portion of the vertex array containing data for the specified chunk.
	 * @param chunk The chunk to update in the VBO.
	 */
	public static void updateChunkArray(int chunk){
		Chunk c = MineFlat.world.getChunk(chunk);
		if (c != null){
			long time = System.currentTimeMillis();
			List<Float> cValues = new ArrayList<Float>();
			for (int x = 0; x < MineFlat.world.getChunkLength(); x++){
				for (int y = 0; y < MineFlat.world.getChunkHeight(); y++){
					Block b = c.getBlock(x, y);
					if (Block.isSolid(b)){
						float tX = Float.valueOf(GraphicsUtil.texCoords.get(b.getType()).getX());
						float tY = Float.valueOf(GraphicsUtil.texCoords.get(b.getType()).getY());

						// this whole bit takes care of smooth lighting
						List<Integer> s = new ArrayList<Integer>();
						// 1
						if (Block.getBlock(b.getX() - 1, b.getY() - 1) != null)
							s.add(Block.getBlock(b.getX() - 1, b.getY() - 1).getLightLevel());
						else
							s.add(Block.maxLight);
						// 2
						if (Block.getBlock(b.getX(), b.getY() - 1) != null)
							s.add(Block.getBlock(b.getX(), b.getY() - 1).getLightLevel());
						else
							s.add(Block.maxLight);
						// 3
						if (Block.getBlock(b.getX() + 1, b.getY() - 1) != null)
							s.add(Block.getBlock(b.getX() + 1, b.getY() - 1).getLightLevel());
						else
							s.add(Block.maxLight);
						// 4
						if (Block.getBlock(b.getX() - 1, b.getY()) != null)
							s.add(Block.getBlock(b.getX() - 1, b.getY()).getLightLevel());
						else
							s.add(Block.maxLight);
						// 5
						if (Block.getBlock(b.getX() + 1, b.getY()) != null)
							s.add(Block.getBlock(b.getX() + 1, b.getY()).getLightLevel());
						else
							s.add(Block.maxLight);
						// 6
						if (Block.getBlock(b.getX() - 1, b.getY() + 1) != null)
							s.add(Block.getBlock(b.getX() - 1, b.getY() + 1).getLightLevel());
						else
							s.add(Block.maxLight);
						// 7
						if (Block.getBlock(b.getX(), b.getY() + 1) != null)
							s.add(Block.getBlock(b.getX(), b.getY() + 1).getLightLevel());
						else
							s.add(Block.maxLight);
						// 8
						if (Block.getBlock(b.getX() + 1, b.getY() + 1) != null)
							s.add(Block.getBlock(b.getX() + 1, b.getY() + 1).getLightLevel());
						else
							s.add(Block.maxLight);

						float l1 = (float)(b.getLightLevel() + s.get(0) + s.get(1) + s.get(3))
								/ 4 / Block.maxLight;
						float l2 = (float)(b.getLightLevel() + s.get(1) + s.get(2) + s.get(4))
								/ 4 / Block.maxLight;
						float l3 = (float)(b.getLightLevel() + s.get(4) + s.get(6) + s.get(7))
								/ 4 / Block.maxLight;
						float l4 = (float)(b.getLightLevel() + s.get(3) + s.get(5) + s.get(6))
								/ 4 / Block.maxLight;

						// top left
						// vertex
						cValues.add(Float.valueOf((float)b.getLocation().getPixelX()));
						cValues.add(Float.valueOf((float)b.getLocation().getPixelY()));
						// light
						for (int i = 0; i < 3; i++)
							cValues.add(l1);
						// texture
						cValues.add(tX);
						cValues.add(tY);

						// top right
						// vertex
						cValues.add(Float.valueOf((float)b.getLocation().getPixelX()) +
								Block.length);
						cValues.add(Float.valueOf((float)b.getLocation().getPixelY()));
						// light
						for (int i = 0; i < 3; i++)
							cValues.add(l2);
						// texture
						cValues.add(tX + 1 / ((float)GraphicsUtil.atlas.getImageWidth() /
								GraphicsHandler.texSize));
						cValues.add(tY);

						// bottom right
						// vertex
						cValues.add(Float.valueOf((float)b.getLocation().getPixelX()) +
								Block.length);
						cValues.add(Float.valueOf((float)b.getLocation().getPixelY()) +
								Block.length);
						// light
						for (int i = 0; i < 3; i++)
							cValues.add(l3);
						// texture
						cValues.add(tX + 1 / ((float)GraphicsUtil.atlas.getImageWidth() /
								GraphicsHandler.texSize));
						cValues.add(tY + 1 / ((float)GraphicsUtil.atlas.getImageWidth() /
								GraphicsHandler.texSize));

						// bottom left
						// vertex
						cValues.add(Float.valueOf((float)b.getLocation().getPixelX()));
						cValues.add(Float.valueOf((float)b.getLocation().getPixelY()) +
								Block.length);
						// light
						for (int i = 0; i < 3; i++)
							cValues.add(l4);
						// texture
						cValues.add(tX);
						cValues.add(tY + 1 / ((float)GraphicsUtil.atlas.getImageWidth() /
								GraphicsHandler.texSize));
						
						if (b.getY() == 0 || Block.getBlock(b.getX(), b.getY() - 1).getType() == Material.AIR){
							
							if (b.getType() == Material.GRASS){
								tX = Float.valueOf(GraphicsUtil.texCoords.get(Material.GRASS_TOP).getX());
								tY = Float.valueOf(GraphicsUtil.texCoords.get(Material.GRASS_TOP).getY());
							}
							
							// front left
							// vertex
							cValues.add(Float.valueOf((float)b.getLocation().getPixelX()));
							cValues.add(Float.valueOf((float)b.getLocation().getPixelY()));
							// light
							for (int i = 0; i < 3; i++)
								cValues.add(l1 - Block.horShadow);
							// texture
							cValues.add(tX);
							cValues.add(tY + 1 / ((float)GraphicsUtil.atlas.getImageWidth() /
									GraphicsHandler.texSize));

							// front right
							// vertex
							cValues.add(Float.valueOf((float)b.getLocation().getPixelX()) +
									Block.length);
							cValues.add(Float.valueOf((float)b.getLocation().getPixelY()));
							// light
							for (int i = 0; i < 3; i++)
								cValues.add(l2 - Block.horShadow);
							// texture
							cValues.add(tX + 1 / ((float)GraphicsUtil.atlas.getImageWidth() /
									GraphicsHandler.texSize));
							cValues.add(tY + 1 / ((float)GraphicsUtil.atlas.getImageWidth() /
									GraphicsHandler.texSize));

							// back right
							// vertex
							cValues.add(Float.valueOf((float)b.getLocation().getPixelX()) +
									Block.length);
							cValues.add(Float.valueOf((float)b.getLocation().getPixelY()) -
									Block.length / Block.horAngle);
							// light
							for (int i = 0; i < 3; i++)
								cValues.add(l2 - Block.horShadow);
							// texture
							cValues.add(tX + 1 / ((float)GraphicsUtil.atlas.getImageWidth() /
									GraphicsHandler.texSize));
							cValues.add(tY);

							// back left
							// vertex
							cValues.add(Float.valueOf((float)b.getLocation().getPixelX()));
							cValues.add(Float.valueOf((float)b.getLocation().getPixelY()) -
									Block.length / Block.horAngle);
							// light
							for (int i = 0; i < 3; i++)
								cValues.add(l1 - Block.horShadow);
							// texture
							cValues.add(tX);
							cValues.add(tY);

						}
					}
				}
			}

			chunkArrays.remove(c.getNum());
			Float[] cArray = cValues.toArray(new Float[]{});
			chunkArrays.put(c.getNum(), cArray);
			System.out.println("updateChunkArray: " + (System.currentTimeMillis() - time));

			recreateArray();
		}
	}

	/**
	 * Prepares the data to bind to the primary VBO
	 */
	public static void prepareBindArray(){
		long time = System.currentTimeMillis();
		vertexData = (FloatBuffer)BufferUtils
				.createFloatBuffer(vertexArray.length).flip();
		vertexData.limit(vertexData.capacity());
		vertexData.put(vertexArray);
		vertexData.rewind();
		rebindArray = true;
		System.out.println("prepareBindArray: " + (System.currentTimeMillis() - time));
	}

	/**
	 * Rebinds the VBO to the OpenGL instance, effectively redrawing it.
	 */
	public static void bindArray(){
		if (vertexData != null){
			glBindBuffer(GL_ARRAY_BUFFER, bufferHandle);
			glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
			vertexData = null;
		}
		rebindArray = false;
	}

	/**
	 * Splices all chunk arrays into a single VBO.
	 */
	public static void recreateArray(){
		long timeBefore = System.currentTimeMillis();
		int length = 0;
		for (Float[] f : chunkArrays.values())
			length += f.length;
		float[] newArray = new float[length];
		int elements = 0;
		for (Float[] f : chunkArrays.values()){
			for (int i = 0; i < f.length; i++)
				newArray[elements + i] = f[i];
			elements += f.length;
		}
		vertexArray = newArray;
		System.out.println("recreateArray: " + (System.currentTimeMillis() - timeBefore));
	}

	/**
	 * Renders the VBO to the screen. This does not rebind it, and so block updates will not be
	 * visible until VboUtil.bindArray() is called.
	 */
	public static void render(){
		glPushMatrix();
		glTranslatef(GraphicsHandler.xOffset, GraphicsHandler.yOffset, 0);
		glBindTexture(GL_TEXTURE_2D, GraphicsUtil.atlas.getTextureID());
		glVertexPointer(2, GL_FLOAT, 28, 0);
		glColorPointer(3, GL_FLOAT, 28, 8);
		glTexCoordPointer(2, GL_FLOAT, 28, 20);
		glDrawArrays(GL_QUADS, 0, vertexArray.length / 7);
		glBindTexture(GL_TEXTURE_2D, 0);
		glPopMatrix();
	}

}
