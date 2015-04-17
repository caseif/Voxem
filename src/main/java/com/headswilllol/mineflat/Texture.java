/*
 * MineFlat
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
package com.headswilllol.mineflat;

import com.headswilllol.mineflat.world.Location;
import com.headswilllol.mineflat.vector.Vector2f;
import com.headswilllol.mineflat.util.ImageUtil;
import com.headswilllol.mineflat.world.Block;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;

public class Texture {

	public static final HashMap<Material, Texture[]> textures = new HashMap<>();
	public static int atlas;
	public static float atlasSize;

	private Material material;
	private int data;
	private BufferedImage image;
	private Vector2f atlasLoc;

	public Texture(Material material, int data, BufferedImage image, Vector2f atlasLoc){
		this.material = material;
		this.data = data;
		this.image = image;
		this.atlasLoc = atlasLoc;
	}

	public Texture(Material material, int data, BufferedImage image, int atlasX, int atlastY){
		this(material, data, image, new Location(null, -1, -1));
	}

	public Texture(Material material, int data, BufferedImage image){
		this(material, data, image, -1, -1);
	}

	public Material getMaterial(){
		return material;
	}

	public int getData(){
		return data;
	}

	public BufferedImage getImage(){
		return image;
	}

	public Vector2f getAtlasLocation(){
		return atlasLoc;
	}

	public float getAtlasX(){
		return atlasLoc.getX();
	}

	public float getAtlasY(){
		return atlasLoc.getY();
	}

	public void setAtlasLocation(Vector2f location){
		this.atlasLoc = location;
	}

	public void setAtlasX(float x){
		this.atlasLoc.setX(x);
	}

	public void setAtlasY(float y){
		this.atlasLoc.setY(y);
	}

	public static void addTexture(Material m){
		try {
			Texture[] tArray = new Texture[m.getTextures().length];
			int i = 0;
			for (String t : m.getTextures()) {
				InputStream is = Texture.class.getResourceAsStream("/textures/block/" + m.getTexture(i) + ".png");
				if (is == null)
					is = Texture.class.getResourceAsStream("/textures/block/missing_texture.png");
				InputStream newIs = ImageUtil.asInputStream(ImageUtil.scaleImage(
						ImageIO.read(is), Block.length, Block.length));
				BufferedImage b = ImageIO.read(newIs);
				tArray[i] = new Texture(m, i, b, 0, 0);
				i += 1;
			}
			textures.put(m, tArray);

		}
		catch (Exception ex){
			System.err.println("Exception occurred while preparing texture for material " + m.toString());
			ex.printStackTrace();
		}
	}

	public static Texture getTexture(Material material, int data){
		if (textures.get(material).length >= data - 1)
			return textures.get(material)[data];
		return textures.get(material)[0];
	}

	public static Texture getTexture(Material material){
		return getTexture(material, 0);
	}

	public static Texture getTexture(Block block){
		 return getTexture(block.getType(), block.getData());
	}

}
