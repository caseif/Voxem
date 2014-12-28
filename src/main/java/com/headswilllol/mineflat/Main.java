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

import com.headswilllol.mineflat.entity.Entity;
import com.headswilllol.mineflat.entity.living.Mob;
import com.headswilllol.mineflat.entity.living.player.Player;
import com.headswilllol.mineflat.threading.Scheduler;
import com.headswilllol.mineflat.util.Timing;
import com.headswilllol.mineflat.world.Block;
import com.headswilllol.mineflat.world.SaveManager;
import com.headswilllol.mineflat.world.TickManager;
import com.headswilllol.mineflat.world.World;
import org.lwjgl.opengl.*;
import org.lwjgl.opengl.DisplayMode;

public class Main {

	public static World world = null;

	public static Player player;

	public static boolean closed = false;

	public static final Object lock = new Object();

	public static GameState state = GameState.MAIN_MENU;

	public static int charTexture;

	public static void main(String[] args){

		boolean launchedProperly = true;
		try {
			// verify libraries are present and LWJGL is in path
			for (DisplayMode mode : Display.getAvailableDisplayModes()){
				if (mode.getWidth() == Display.getDesktopDisplayMode().getWidth() &&
						mode.getHeight() == Display.getDesktopDisplayMode()
								.getHeight() && mode.isFullscreenCapable()){
					Display.setDisplayMode(mode);
					break;
				}
			}
			Display.destroy();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			launchedProperly = false;
		}
		if (!launchedProperly){
			new DummyMain();
			return;
		}

		Thread t = new Thread(new GraphicsHandler());
		t.start();

		InputManager.initialize();
		Mob.initialize();
		SoundManager.initialize();

		while (!closed){
			Timing.calculateDelta();
			Scheduler.checkTasks();
			InputManager.manage();
			if (world != null && player != null) {
				Player.calculateLight();
				TickManager.checkForTick();
				for (Entity e : player.getLevel().getEntities())
					e.manageMovement();
				Block.updateSelectedBlock();
			}
			Timing.throttleCpu();
		}
		SoundManager.soundSystem.cleanup();
		if (world != null) {
			SaveManager.writeWorldToDisk(world);
		}

	}

}
