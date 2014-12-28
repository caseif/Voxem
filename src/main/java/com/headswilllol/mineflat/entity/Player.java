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
package com.headswilllol.mineflat.entity;

import com.headswilllol.mineflat.event.Event;
import com.headswilllol.mineflat.event.player.PlayerMoveEvent;
import com.headswilllol.mineflat.world.Location;
import org.lwjgl.opengl.Display;

import com.headswilllol.mineflat.world.Block;
import com.headswilllol.mineflat.GraphicsHandler;
import com.headswilllol.mineflat.Main;
import com.headswilllol.mineflat.Material;

/**
 * Represents a human in the world which the current instance of the game is in control of.
 */
public class Player extends Human {

	/**
	 * Distance from center of screen at which the screen will begin scrolling when the player reaches it
	 */
	public static final float scrollTolerance = 0.25f;

	public static float light = 1f;

	public Player(Location location){
		super(location);
		this.setType(EntityType.PLAYER);
	}

	@Override
	public void setX(float x){
		Location old = getLocation();
		super.setX(x);
		Event.fireEvent(new PlayerMoveEvent(this, getLocation(), old));
	}

	@Override
	public void setY(float y){
		Location old = getLocation();
		super.setY(y);
		Event.fireEvent(new PlayerMoveEvent(this, getLocation(), old));
	}

	public static void calculateLight() {
		if (Main.player != null) {
			float firstLight;
			float secondLight;
			light = 1f;
			Block top = Main.player.getLocation().subtract(0, 1).getBlock();
			Block bottom = Main.player.getLocation().getBlock();
			if (top != null && bottom != null) {
				float topLight = top.getLightLevel() / (float) Block.maxLight;
				float bottomLight = bottom.getLightLevel() / (float) Block.maxLight;
				float topBias = ((Main.player.getY()) % 1);
				float bottomBias = 1 - topBias;
				firstLight = (topLight * topBias + bottomLight * bottomBias);
				int beside = Main.player.getX() % 1 < 0.5f ? -1 : 1;
				Block top2 = Main.player.getLocation().subtract(beside, 1).getBlock();
				Block bottom2 = Main.player.getLocation().subtract(beside, 0).getBlock();
				if (top2 != null && bottom2 != null && top2.getType() == Material.AIR) {
					float topLight2 = top2.getLightLevel() / (float) Block.maxLight;
					float bottomLight2 = bottom2.getLightLevel() / (float) Block.maxLight;
					secondLight = (topLight2 * topBias + bottomLight2 * bottomBias);
					float sideBias = Math.abs(0.5f - Math.abs(Main.player.getX() % 1));
					float centerBias = 1 - sideBias;
					light = firstLight * centerBias + secondLight * sideBias;
				}
				else
					light = firstLight;
			}
			else if (Main.player.getY() > Main.player.getLevel().getWorld().getChunkHeight() - 1)
				light = 0f;

			if (light <= 2f / (float) Block.maxLight)
				light = 2f / (float) Block.maxLight;
		}
	}

	public static void centerPlayer(){
		if (Main.player.getLocation().getPixelX() <
				Display.getWidth() / 2 - GraphicsHandler.xOffset -
						(int)(Display.getWidth() / 2 * scrollTolerance))
			GraphicsHandler.xOffset =
					Display.getWidth() / 2 - Main.player.getLocation().getPixelX() -
							(int)(Display.getWidth() / 2 * scrollTolerance);
		else if (Main.player.getLocation().getPixelX() >
				Display.getWidth() / 2 - GraphicsHandler.xOffset + (int)(Display.getWidth() / 2 * scrollTolerance))
			GraphicsHandler.xOffset =
					Display.getWidth() / 2 - Main.player.getLocation().getPixelX() +
							(int)(Display.getWidth() / 2 * scrollTolerance);

		if (Main.player.getLocation().getPixelY() <
				Display.getHeight() / 2 - GraphicsHandler.yOffset -
						(int)(Display.getHeight() / 2 * scrollTolerance))
			GraphicsHandler.yOffset =
					Display.getHeight() / 2 - Main.player.getLocation().getPixelY() -
							(int)(Display.getHeight() / 2 * scrollTolerance);
		else if (Main.player.getLocation().getPixelY() >
				Display.getHeight() / 2 - GraphicsHandler.yOffset + (int)(Display.getHeight() / 2 * scrollTolerance))
			GraphicsHandler.yOffset =
					Display.getHeight() / 2 - Main.player.getLocation().getPixelY() +
							(int)(Display.getHeight() / 2 * scrollTolerance);

	}

}
