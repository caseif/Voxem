/*
 * MineFlat
 * Copyright (c) 2014, Maxim Roncacé <mproncace@gmail.com>
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

public enum Material {

	AIR("missing_texture"), // shouldn't be rendered anyway. if it is, something's wrong.
	DIRT,
	GRASS,
	GRASS_TOP,
	SNOW_GRASS,
	SNOW,
	LOG("log_oak"),
	LOG_TOP("log_oak_top"),
	LEAVES("leaves_oak"),
	WOOD("wood_oak"),
	STONE,
	BEDROCK,
	COAL_ORE,
	IRON_ORE,
	GOLD_ORE,
	DIAMOND_ORE,
	PUMPKIN,
	PUMPKIN_TOP;

	private String[] textures;

	//TODO: add durability argument
	Material(String[] textures){
		this.textures = textures;
	}

	Material(String texture){
		this.textures = new String[]{texture};
	}

	Material(){
		this.textures = new String[]{this.toString().toLowerCase()};
	}

	public String[] getTextures(){
		return textures;
	}

	public String getTexture(int dataValue){
		return textures.length >= dataValue - 1 ? textures[dataValue] : textures[0];
	}
}
