/*
 * Voxem
 * Copyright (c) 2014-2015, Maxim Roncacé <caseif@caseif.net>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.caseif.voxem.util;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glColorPointer;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoordPointer;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import net.caseif.voxem.GraphicsHandler;
import net.caseif.voxem.Main;
import net.caseif.voxem.Material;
import net.caseif.voxem.Texture;
import net.caseif.voxem.world.Biome;
import net.caseif.voxem.world.Block;
import net.caseif.voxem.world.Chunk;
import net.caseif.voxem.world.Level;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// I'm slowly realizing this class does extremely weird things when I'm not looking
public class VboUtil {

    public static int bufferHandle;
    public static float[] vertexArray;
    public static FloatBuffer vertexData = null;
    public static final HashMap<Integer, Float[]> chunkArrays = new HashMap<Integer, Float[]>();
    public static boolean rebindArray = false;

    /**
     * Initializes VBO support in the OpenGL instance.
     */
    public static void initialize() {
        //System.out.println("[VboUtil] initialize");
        ImageUtil.createAtlas();
        IntBuffer buffer = BufferUtils.createIntBuffer(1);
        glGenBuffers(buffer);
        bufferHandle = buffer.get(0);
        updateArray();
        prepareBindArray();
        for (int i = 0; i < 15; i++) {
            bindArray();
        }
    }

    /**
     * Updates the entire VBO. This method may cause a severe drop in FPS while running.
     */
    public static void updateArray() {
        //System.out.println("[VboUtil] update");
        if (Main.world != null) {
            for (Level l : Main.world.getLevels()) {
                for (int c : l.chunks.keySet())
                    updateChunkArray(l, c);
            }
        }
        recreateArray();
    }

    /**
     * Updates the portion of the vertex array containing data for the specified chunk.
     *
     * @param level the level containing the chunk.
     * @param chunk The chunk to update in the VBO.
     */
    public static void updateChunkArray(Level level, int chunk) {
        //System.out.println("[VboUtil] update chunk " + chunk);
        synchronized (chunkArrays) {
            Chunk c = level.getChunk(chunk);
            if (c != null) {
                List<Float> cValues = new ArrayList<>();
                for (int x = 0; x < Main.world.getChunkLength(); x++) {
                    for (int y = 0; y < Main.world.getChunkHeight(); y++) {
                        Block b = c.getBlock(x, y);
                        if (b.getType() != Material.AIR) {
                            Texture t = Texture.getTexture(b.getType());
                            float tX = t.getAtlasX();
                            float tY = t.getAtlasY();

                            // this whole bit takes care of smooth lighting
                            List<Integer> s = new ArrayList<>();
                            // 1
                            if (b.getLevel().getBlock(b.getX() - 1, b.getY() - 1).isPresent())
                                s.add(b.getLevel().getBlock(b.getX() - 1, b.getY() - 1).get().getLightLevel());
                            else
                                s.add(Block.maxLight);
                            // 2
                            if (b.getLevel().getBlock(b.getX(), b.getY() - 1).isPresent())
                                s.add(b.getLevel().getBlock(b.getX(), b.getY() - 1).get().getLightLevel());
                            else
                                s.add(Block.maxLight);
                            // 3
                            if (b.getLevel().getBlock(b.getX() + 1, b.getY() - 1).isPresent())
                                s.add(b.getLevel().getBlock(b.getX() + 1, b.getY() - 1).get().getLightLevel());
                            else
                                s.add(Block.maxLight);
                            // 4
                            if (b.getLevel().getBlock(b.getX() - 1, b.getY()).isPresent())
                                s.add(b.getLevel().getBlock(b.getX() - 1, b.getY()).get().getLightLevel());
                            else
                                s.add(Block.maxLight);
                            // 5
                            if (b.getLevel().getBlock(b.getX() + 1, b.getY()).isPresent())
                                s.add(b.getLevel().getBlock(b.getX() + 1, b.getY()).get().getLightLevel());
                            else
                                s.add(Block.maxLight);
                            // 6
                            if (b.getLevel().getBlock(b.getX() - 1, b.getY() + 1).isPresent())
                                s.add(b.getLevel().getBlock(b.getX() - 1, b.getY() + 1).get().getLightLevel());
                            else
                                s.add(Block.maxLight);
                            // 7
                            if (b.getLevel().getBlock(b.getX(), b.getY() + 1).isPresent())
                                s.add(b.getLevel().getBlock(b.getX(), b.getY() + 1).get().getLightLevel());
                            else
                                s.add(Block.maxLight);
                            // 8
                            if (b.getLevel().getBlock(b.getX() + 1, b.getY() + 1).isPresent())
                                s.add(b.getLevel().getBlock(b.getX() + 1, b.getY() + 1).get().getLightLevel());
                            else
                                s.add(Block.maxLight);

                            float l1 = (float) (b.getLightLevel() + s.get(0) + s.get(1) + s.get(3)) /
                                    4 / Block.maxLight;
                            float l2 = (float) (b.getLightLevel() + s.get(1) + s.get(2) + s.get(4)) /
                                    4 / Block.maxLight;
                            float l3 = (float) (b.getLightLevel() + s.get(4) + s.get(6) + s.get(7)) /
                                    4 / Block.maxLight;
                            float l4 = (float) (b.getLightLevel() + s.get(3) + s.get(5) + s.get(6)) /
                                    4 / Block.maxLight;

                            if (c.getBiome() == Biome.SNOWY_HILLS && b.getType() == Material.GRASS) {
                                Texture t2 = Texture.getTexture(Material.SNOW_GRASS);
                                tX = t2.getAtlasX();
                                tY = t2.getAtlasY();
                            }

                            // top left
                            // vertex
                            cValues.add((float) b.getLocation().getPixelX());
                            cValues.add((float) b.getLocation().getPixelY());
                            // light
                            for (int i = 0; i < 3; i++)
                                cValues.add(l1); // rgb
                            cValues.add(1f); // alpha
                            // texture
                            cValues.add(tX);
                            cValues.add(tY);

                            // top right
                            // vertex
                            cValues.add((float) b.getLocation().getPixelX() +
                                    Block.length);
                            cValues.add((float) b.getLocation().getPixelY());
                            // light
                            for (int i = 0; i < 3; i++)
                                cValues.add(l2); // rgb
                            cValues.add(1f); // alpha
                            // texture
                            cValues.add(tX + 1 / (Texture.atlasSize / Block.length));
                            cValues.add(tY);

                            // bottom right
                            // vertex
                            cValues.add((float) b.getLocation().getPixelX() +
                                    Block.length);
                            cValues.add((float) b.getLocation().getPixelY() +
                                    Block.length);
                            // light
                            for (int i = 0; i < 3; i++)
                                cValues.add(l3); // rgb
                            cValues.add(1f); // alpha
                            // texture
                            cValues.add(tX + 1 / (Texture.atlasSize / Block.length));
                            //cValues.add(tY + 1 / (Texture.atlasSize / Block.length));
                            cValues.add(1f);

                            // bottom left
                            // vertex
                            cValues.add((float) b.getLocation().getPixelX());
                            cValues.add((float) b.getLocation().getPixelY() +
                                    Block.length);
                            // light
                            for (int i = 0; i < 3; i++)
                                cValues.add(l4); // rgb
                            cValues.add(1f); // alpha
                            // texture
                            cValues.add(tX);
                            //cValues.add(tY + 1 / ((float)Texture.atlasSize / Block.length));
                            cValues.add(1f);

                            if (b.getType() != Material.AIR
                                    && (b.getY() == 0 || Block.isAir(b.getLevel().getBlock(b.getX(), b.getY() - 1)))) {

                                //TODO: make this more flexible (less awful)
                                if (b.getType() == Material.GRASS) {
                                    Texture t2 = Texture.getTexture(c.getBiome() == Biome.SNOWY_HILLS ?
                                            Material.SNOW :
                                            Material.GRASS_TOP);
                                    tX = t2.getAtlasX();
                                    tY = t2.getAtlasY();
                                } else if (b.getType() == Material.LOG) {
                                    Texture t2 = Texture.getTexture(Material.LOG_TOP);
                                    tX = t2.getAtlasX();
                                    tY = t2.getAtlasY();
                                } else if (b.getType() == Material.PUMPKIN) {
                                    Texture t2 = Texture.getTexture(Material.PUMPKIN_TOP);
                                    tX = t2.getAtlasX();
                                    tY = t2.getAtlasY();
                                }

                                // front left
                                // vertex
                                cValues.add((float) b.getLocation().getPixelX());
                                cValues.add((float) b.getLocation().getPixelY());
                                // light
                                for (int i = 0; i < 3; i++)
                                    cValues.add(l1 - Block.horShadow > Block.minLight / (float) Block.maxLight ?
                                            l1 - Block.horShadow : Block.minLight / (float) Block.maxLight); // rgb
                                cValues.add(1f); // alpha
                                // texture
                                cValues.add(tX);
                                //cValues.add(tY + 1 / ((float)Texture.atlasSize / Block.length));
                                cValues.add(1f);

                                // front right
                                // vertex
                                cValues.add((float) b.getLocation().getPixelX() +
                                        Block.length);
                                cValues.add((float) b.getLocation().getPixelY());
                                // light
                                for (int i = 0; i < 3; i++)
                                    cValues.add(l2 - Block.horShadow > Block.minLight / (float) Block.maxLight ?
                                            l2 - Block.horShadow : Block.minLight / (float) Block.maxLight); // rgb
                                cValues.add(1f); // alpha
                                // texture
                                cValues.add(tX + 1 / (Texture.atlasSize / Block.length));
                                //cValues.add(tY + 1 / (Texture.atlasSize / Block.length));
                                cValues.add(1f);

                                // back right
                                // vertex
                                cValues.add((float) b.getLocation().getPixelX() +
                                        Block.length);
                                cValues.add((float) b.getLocation().getPixelY() -
                                        Block.length / Block.horAngle);
                                // light
                                for (int i = 0; i < 3; i++)
                                    cValues.add(l2 - Block.horShadow > Block.minLight / (float) Block.maxLight ?
                                            l2 - Block.horShadow : Block.minLight / (float) Block.maxLight); // rgb
                                cValues.add(1f); // alpha
                                // texture
                                cValues.add(tX + 1 / (Texture.atlasSize / Block.length));
                                cValues.add(tY);

                                // back left
                                // vertex
                                cValues.add((float) b.getLocation().getPixelX());
                                cValues.add((float) b.getLocation().getPixelY() -
                                        Block.length / Block.horAngle);
                                // light
                                for (int i = 0; i < 3; i++)
                                    cValues.add(l1 - Block.horShadow > Block.minLight / (float) Block.maxLight ?
                                            l1 - Block.horShadow : Block.minLight / (float) Block.maxLight); // rgb
                                cValues.add(1f); // alpha
                                // texture
                                cValues.add(tX);
                                cValues.add(tY);

                            }
                        }
                    }
                }

                chunkArrays.remove(c.getIndex());
                Float[] cArray = cValues.toArray(new Float[cValues.size()]);
                chunkArrays.put(c.getIndex(), cArray);
            } else
                chunkArrays.remove(chunk);
        }

        recreateArray();
    }

    /**
     * Prepares the data to bind to the primary VBO
     */
    public static void prepareBindArray() {
        //System.out.println("[VboUtil] prepare");
        vertexData = (FloatBuffer) BufferUtils
                .createFloatBuffer(vertexArray.length).flip();
        vertexData.limit(vertexData.capacity());
        vertexData.put(vertexArray);
        vertexData.rewind();
        rebindArray = true;
    }

    /**
     * Rebinds the VBO to the OpenGL instance, effectively redrawing it.
     */
    public static void bindArray() {
        //System.out.println("[VboUtil] bind");
        if (vertexData != null) {
            glBindBuffer(GL_ARRAY_BUFFER, bufferHandle);
            glBufferData(GL_ARRAY_BUFFER, vertexData, GL_DYNAMIC_DRAW);
            vertexData = null;
        }
        rebindArray = false;
    }

    /**
     * Splices all chunk arrays into a single VBO.
     */
    public static void recreateArray() {
        //System.out.println("[VboUtil] recreate");
        synchronized (chunkArrays) {
            int length = 0;
            for (Float[] f : chunkArrays.values())
                length += f.length;
            float[] newArray = new float[length];
            int elements = 0;
            for (Float[] f : chunkArrays.values()) {
                for (int i = 0; i < f.length; i++)
                    newArray[elements + i] = f[i];
                elements += f.length;
            }
            vertexArray = newArray;
        }
    }

    /**
     * Renders the VBO to the screen. This does not rebind it, and so block updates will not be
     * visible until VboUtil.bindArray() is called.
     */
    public static void render() {
        glPushMatrix();
        glTranslatef(GraphicsHandler.xOffset, GraphicsHandler.yOffset, 0);
        glBindTexture(GL_TEXTURE_2D, Texture.atlas);
        glVertexPointer(2, GL_FLOAT, 32, 0);
        glColorPointer(4, GL_FLOAT, 32, 8);
        glTexCoordPointer(2, GL_FLOAT, 32, 24);
        glDrawArrays(GL_QUADS, 0, vertexArray.length / 8);
        glBindTexture(GL_TEXTURE_2D, 0);
        glPopMatrix();
    }

}
