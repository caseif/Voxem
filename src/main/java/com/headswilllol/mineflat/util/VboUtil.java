package com.headswilllol.mineflat.util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.BufferUtils;

import com.headswilllol.mineflat.Block;
import com.headswilllol.mineflat.Chunk;
import com.headswilllol.mineflat.GraphicsHandler;
import com.headswilllol.mineflat.Level;
import com.headswilllol.mineflat.Main;
import com.headswilllol.mineflat.Material;

public class VboUtil {

	public static int bufferHandle;
	public static float[] vertexArray;
	public static FloatBuffer vertexData = null;
	public static final HashMap<Integer, Float[]> chunkArrays = new HashMap<Integer, Float[]>();
	public static boolean rebindArray = false;

	/**
	 * Initializes VBO support in the OpenGL instance.
	 */
	public static void initialize(){
		ImageUtil.createAtlas();
		IntBuffer buffer = BufferUtils.createIntBuffer(1);
		glGenBuffers(buffer);
		bufferHandle = buffer.get(0);
		updateArray();
		prepareBindArray();
		for (int i = 0; i < 15; i++){
			bindArray();
		}
	}

	/**
	 * Updates the entire VBO. This method may cause a severe drop in FPS while running.
	 */
	public static void updateArray(){
		for (Level l : Main.world.getLevels()){
			for (int c : Main.player.getLevel().chunks.keySet())
				updateChunkArray(l, c);
		}
		recreateArray();
	}

	/**
	 * Updates the portion of the vertex array containing data for the specified chunk.
	 * @param level the level containing the chunk.
	 * @param chunk The chunk to update in the VBO.
	 */
	public static void updateChunkArray(Level level, int chunk){
		Chunk c = level.getChunk(chunk);
		if (c != null){
			List<Float> cValues = new ArrayList<Float>();
			for (int x = 0; x < Main.world.getChunkLength(); x++){
				for (int y = 0; y < Main.world.getChunkHeight(); y++){
					Block b = c.getBlock(x, y);
					if (!Block.isAir(b)){
						float tX = GraphicsUtil.texCoords.get(b.getType()).getX();
						float tY = GraphicsUtil.texCoords.get(b.getType()).getY();

						// this whole bit takes care of smooth lighting
						List<Integer> s = new ArrayList<Integer>();
						// 1
						if (Block.getBlock(b.getLevel(), b.getX() - 1, b.getY() - 1) != null)
							s.add(Block.getBlock(b.getLevel(), b.getX() - 1, b.getY() - 1).getLightLevel());
						else
							s.add(Block.maxLight);
						// 2
						if (Block.getBlock(b.getLevel(), b.getX(), b.getY() - 1) != null)
							s.add(Block.getBlock(b.getLevel(), b.getX(), b.getY() - 1).getLightLevel());
						else
							s.add(Block.maxLight);
						// 3
						if (Block.getBlock(b.getLevel(), b.getX() + 1, b.getY() - 1) != null)
							s.add(Block.getBlock(b.getLevel(), b.getX() + 1, b.getY() - 1).getLightLevel());
						else
							s.add(Block.maxLight);
						// 4
						if (Block.getBlock(b.getLevel(), b.getX() - 1, b.getY()) != null)
							s.add(Block.getBlock(b.getLevel(), b.getX() - 1, b.getY()).getLightLevel());
						else
							s.add(Block.maxLight);
						// 5
						if (Block.getBlock(b.getLevel(), b.getX() + 1, b.getY()) != null)
							s.add(Block.getBlock(b.getLevel(), b.getX() + 1, b.getY()).getLightLevel());
						else
							s.add(Block.maxLight);
						// 6
						if (Block.getBlock(b.getLevel(), b.getX() - 1, b.getY() + 1) != null)
							s.add(Block.getBlock(b.getLevel(), b.getX() - 1, b.getY() + 1).getLightLevel());
						else
							s.add(Block.maxLight);
						// 7
						if (Block.getBlock(b.getLevel(), b.getX(), b.getY() + 1) != null)
							s.add(Block.getBlock(b.getLevel(), b.getX(), b.getY() + 1).getLightLevel());
						else
							s.add(Block.maxLight);
						// 8
						if (Block.getBlock(b.getLevel(), b.getX() + 1, b.getY() + 1) != null)
							s.add(Block.getBlock(b.getLevel(), b.getX() + 1, b.getY() + 1).getLightLevel());
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
						cValues.add((float)b.getLocation().getPixelX());
						cValues.add((float)b.getLocation().getPixelY());
						// light
						for (int i = 0; i < 3; i++)
							cValues.add(l1); // rgb
						cValues.add(1f); // alpha
						// texture
						cValues.add(tX);
						cValues.add(tY);

						// top right
						// vertex
						cValues.add((float)b.getLocation().getPixelX() +
								Block.length);
						cValues.add((float)b.getLocation().getPixelY());
						// light
						for (int i = 0; i < 3; i++)
							cValues.add(l2); // rgb
						cValues.add(1f); // alpha
						// texture
						cValues.add(tX + 1 / (GraphicsUtil.atlasSize / Block.length));
						cValues.add(tY);

						// bottom right
						// vertex
						cValues.add((float)b.getLocation().getPixelX() +
								Block.length);
						cValues.add((float)b.getLocation().getPixelY() +
								Block.length);
						// light
						for (int i = 0; i < 3; i++)
							cValues.add(l3); // rgb
						cValues.add(1f); // alpha
						// texture
						cValues.add(tX + 1 / (GraphicsUtil.atlasSize / Block.length));
						//cValues.add(tY + 1 / (GraphicsUtil.atlasSize / Block.length));
						cValues.add(1f);

						// bottom left
						// vertex
						cValues.add((float)b.getLocation().getPixelX());
						cValues.add((float)b.getLocation().getPixelY() +
								Block.length);
						// light
						for (int i = 0; i < 3; i++)
							cValues.add(l4); // rgb
						cValues.add(1f); // alpha
						// texture
						cValues.add(tX);
						//cValues.add(tY + 1 / ((float)GraphicsUtil.atlasSize / Block.length));
						cValues.add(1f);

						if (!Block.isAir(b) && (b.getY() == 0 || Block.getBlock(b.getLevel(), b.getX(), b.getY() - 1).getType() == Material.AIR)){

							if (b.getType() == Material.GRASS){
								tX = GraphicsUtil.texCoords.get(Material.GRASS_TOP).getX();
								tY = GraphicsUtil.texCoords.get(Material.GRASS_TOP).getY();
							}
							else if (b.getType() == Material.LOG){
								tX = GraphicsUtil.texCoords.get(Material.LOG_TOP).getX();
								tY = GraphicsUtil.texCoords.get(Material.LOG_TOP).getY();
							}

							// front left
							// vertex
							cValues.add((float)b.getLocation().getPixelX());
							cValues.add((float)b.getLocation().getPixelY());
							// light
							for (int i = 0; i < 3; i++)
								cValues.add(l1 - Block.horShadow > Block.minLight / (float)Block.maxLight ?
										l1 - Block.horShadow : Block.minLight / (float)Block.maxLight); // rgb
							cValues.add(1f); // alpha
							// texture
							cValues.add(tX);
							//cValues.add(tY + 1 / ((float)GraphicsUtil.atlasSize / Block.length));
							cValues.add(1f);

							// front right
							// vertex
							cValues.add((float)b.getLocation().getPixelX() +
									Block.length);
							cValues.add((float)b.getLocation().getPixelY());
							// light
							for (int i = 0; i < 3; i++)
								cValues.add(l2 - Block.horShadow > Block.minLight / (float)Block.maxLight ?
										l2 - Block.horShadow : Block.minLight / (float)Block.maxLight); // rgb
							cValues.add(1f); // alpha
							// texture
							cValues.add(tX + 1 / (GraphicsUtil.atlasSize / Block.length));
							//cValues.add(tY + 1 / (GraphicsUtil.atlasSize / Block.length));
							cValues.add(1f);

							// back right
							// vertex
							cValues.add((float)b.getLocation().getPixelX() +
									Block.length);
							cValues.add((float)b.getLocation().getPixelY() -
									Block.length / Block.horAngle);
							// light
							for (int i = 0; i < 3; i++)
								cValues.add(l2 - Block.horShadow > Block.minLight / (float)Block.maxLight ?
										l2 - Block.horShadow : Block.minLight / (float)Block.maxLight); // rgb
							cValues.add(1f); // alpha
							// texture
							cValues.add(tX + 1 / (GraphicsUtil.atlasSize / Block.length));
							cValues.add(tY);

							// back left
							// vertex
							cValues.add((float)b.getLocation().getPixelX());
							cValues.add((float)b.getLocation().getPixelY() -
									Block.length / Block.horAngle);
							// light
							for (int i = 0; i < 3; i++)
								cValues.add(l1 - Block.horShadow > Block.minLight / (float)Block.maxLight ?
										l1 - Block.horShadow : Block.minLight / (float)Block.maxLight); // rgb
							cValues.add(1f); // alpha
							// texture
							cValues.add(tX);
							cValues.add(tY);

						}
					}
				}
			}

			chunkArrays.remove(c.getNum());
			Float[] cArray = cValues.toArray(new Float[cValues.size()]);
			chunkArrays.put(c.getNum(), cArray);

			recreateArray();
		}
	}

	/**
	 * Prepares the data to bind to the primary VBO
	 */
	public static void prepareBindArray(){
		vertexData = (FloatBuffer)BufferUtils
				.createFloatBuffer(vertexArray.length).flip();
		vertexData.limit(vertexData.capacity());
		vertexData.put(vertexArray);
		vertexData.rewind();
		rebindArray = true;
	}

	/**
	 * Rebinds the VBO to the OpenGL instance, effectively redrawing it.
	 */
	public static void bindArray(){
		if (vertexData != null){
			glBindBuffer(GL_ARRAY_BUFFER, bufferHandle);
			glBufferData(GL_ARRAY_BUFFER, vertexData, GL_DYNAMIC_DRAW);
			vertexData = null;
		}
		rebindArray = false;
	}

	/**
	 * Splices all chunk arrays into a single VBO.
	 */
	public static void recreateArray(){
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
	}

	/**
	 * Renders the VBO to the screen. This does not rebind it, and so block updates will not be
	 * visible until VboUtil.bindArray() is called.
	 */
	public static void render(){
		glPushMatrix();
		glTranslatef(GraphicsHandler.xOffset, GraphicsHandler.yOffset, 0);
		glBindTexture(GL_TEXTURE_2D, GraphicsUtil.atlas);
		glVertexPointer(2, GL_FLOAT, 32, 0);
		glColorPointer(4, GL_FLOAT, 32, 8);
		glTexCoordPointer(2, GL_FLOAT, 32, 24);
		glDrawArrays(GL_QUADS, 0, vertexArray.length / 8);
		glBindTexture(GL_TEXTURE_2D, 0);
		glPopMatrix();
	}

}
