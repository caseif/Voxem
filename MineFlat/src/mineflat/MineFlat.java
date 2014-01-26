package mineflat;

import mineflat.entity.Player;

import org.newdawn.slick.opengl.Texture;

/**
 * @author Maxim Roncacé
 * 
 * @License
 *
 * Copyright (c) 2013 Maxim Roncacé
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

public class MineFlat {

	/**
	 * The player of the game, or rather, their virtual doppelganger
	 */
	public static Player player = new Player(32, 0);

	public static boolean closed = false;

	public static final Object lock = new Object();

	public static GameState state = GameState.MAIN_MENU;

	public static Texture charTexture;

	public static boolean debug = false;
	
	/**
	 * The currently loaded world
	 */
	public static World world;

	public static void main(String[] args){
		
		world = new World("world", 8, 16, 128);

		Terrain.generateTerrain();
		InputManager.initialize();
		Console.initialize();
		Thread t = new Thread(new GraphicsHandler());
		t.start();

		while (!closed){
			Timing.calculateDelta();
			InputManager.manage();
			player.manageMovement();
			Block.updateSelectedBlock();
			Timing.throttleCpu();
		}
		SaveManager.saveWorld();

	}

}
