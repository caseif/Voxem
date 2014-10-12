package com.headswilllol.mineflat;

import com.headswilllol.mineflat.entity.Entity;
import com.headswilllol.mineflat.entity.LivingEntity;
import com.headswilllol.mineflat.entity.Mob;
import com.headswilllol.mineflat.entity.Player;
import com.headswilllol.mineflat.gui.Gui;
import com.headswilllol.mineflat.threading.Scheduler;
import com.headswilllol.mineflat.util.Timing;
import com.headswilllol.mineflat.world.Block;
import com.headswilllol.mineflat.world.SaveManager;
import com.headswilllol.mineflat.world.World;
import org.lwjgl.opengl.*;
import org.lwjgl.opengl.DisplayMode;

/**
 * @author Maxim Roncacé
 *
 * @License
 *
 * Copyright (c) 2014 Maxim Roncacé
 *
 * THE WORK IS PROVIDED UNDER THE TERMS OF THIS CREATIVE COMMONS PUBLIC LICENSE
 * ("CCPL" OR "LICENSE"). THE WORK IS PROTECTED BY COPYRIGHT AND/OR OTHER APPLICABLE LAW. ANY USE
 * OF THE WORK OTHER THAN AS AUTHORIZED UNDER THIS LICENSE OR COPYRIGHT LAW IS PROHIBITED.
 *
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND AGREE TO BE BOUND BY THE TERMS
 * OF THIS LICENSE. TO THE EXTENT THIS LICENSE MAY BE CONSIDERED TO BE A CONTRACT, THE LICENSOR
 * GRANTS YOU THE RIGHTS CONTAINED HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND
 * CONDITIONS.
 *
 * The full text of this license can be found in the root directory of this JAR file under the file
 * "LICENSE"
 */

public class Main {

	public static World world = null;

	/**
	 * The player of the game, or rather, their virtual doppelganger
	 */
	public static Player player = null;

	public static boolean closed = false;

	public static final Object lock = new Object();

	public static GameState state = GameState.MAIN_MENU;

	public static int charTexture;
	public static int charArmTexture;

	public static boolean debug = false;

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
		catch (NoClassDefFoundError ex) {
			ex.printStackTrace();
			launchedProperly = false;
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
		//Console.initialize();
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
					if (e instanceof LivingEntity)
						(e).manageMovement();
					else
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
