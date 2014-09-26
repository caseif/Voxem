package com.headswilllol.mineflat;

import com.headswilllol.mineflat.entity.Entity;
import com.headswilllol.mineflat.entity.LivingEntity;
import com.headswilllol.mineflat.entity.Mob;
import com.headswilllol.mineflat.entity.Player;
import org.lwjgl.opengl.*;
import org.lwjgl.opengl.DisplayMode;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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

	public static World world;

	/**
	 * The player of the game, or rather, their virtual doppelganger
	 */
	public static Player player;

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
			DisplayMode[] modes = Display.getAvailableDisplayModes();
			for (int i = 0; i < modes.length; i++){
				if (modes[i].getWidth() == Display.getDesktopDisplayMode().getWidth() &&
						modes[i].getHeight() == Display.getDesktopDisplayMode()
								.getHeight() && modes[i].isFullscreenCapable()){
					Display.setDisplayMode(modes[i]);
					break;
				}
			}
			Display.destroy();
		}
		catch (NoClassDefFoundError ex) {
			launchedProperly = false;
		}
		catch (Exception ex) {
			launchedProperly = false;
		}
		if (!launchedProperly){
			new DummyMain();
			return;
		}

		SaveManager.loadWorld("world");

		if (world == null){
			world = new World("world", 8, 16, 128);
			world.addLevel(0);
			player = new Player(new Location(world.getLevel(0), 0, 0));
			world.getLevel(0).addEntity(player);
			Terrain.generateTerrain();
		}
		else {
			player = new Player(new Location(world.getLevel(0), 0, 0));
			world.getLevel(0).addEntity(player);
		}
		
		
		//SaveManager.loadWorld("world");
		
		InputManager.initialize();
		//Console.initialize();
		Mob.initialize();
		SoundManager.initialize();
		Thread t = new Thread(new GraphicsHandler());
		t.start();

		while (!closed){
			Timing.calculateDelta();
			InputManager.manage();
			Player.calculateLight();
			TickManager.checkForTick();
			for (Entity e : world.getLevel(0).getEntities())
				if (e instanceof LivingEntity)
					((LivingEntity)e).manageMovement();
				else
					e.manageMovement();
			Block.updateSelectedBlock();
			Timing.throttleCpu();
		}
		SoundManager.soundSystem.cleanup();
		SaveManager.saveWorld();

	}

}
