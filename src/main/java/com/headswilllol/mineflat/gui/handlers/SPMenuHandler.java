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
package com.headswilllol.mineflat.gui.handlers;

import com.headswilllol.mineflat.GameState;
import com.headswilllol.mineflat.Main;
import com.headswilllol.mineflat.entity.Player;
import com.headswilllol.mineflat.gui.GuiFactory;
import com.headswilllol.mineflat.util.FileUtil;
import com.headswilllol.mineflat.world.Location;
import com.headswilllol.mineflat.world.SaveManager;
import com.headswilllol.mineflat.world.World;
import com.headswilllol.mineflat.world.generator.Terrain;

import java.io.File;

public class SPMenuHandler {

	public static void createWorld(){
		//Main.world = new World(((TextField)createWorldPanel.getElement("worldNameField")).getText(), 8, 16, 128);
		File save = new File(
				FileUtil.getAppDataFolder() + File.separator + ".mineflat" + File.separator + "saves", "world"
		);
		if (save.exists()){ //TODO: disallow world creation
			save.delete();
			System.out.println("Deleted existing world");
		}
		Main.world = new World("world", 8, 16, 128);
		Main.world.creationTime = System.currentTimeMillis() / 1000L;
		Main.world.addLevel(0);
		Main.player = new Player(new Location(Main.world.getLevel(0), 0, 0));
		Main.world.getLevel(0).addEntity(Main.player);
		Terrain.generateTerrain();
		SaveManager.saveWorldToMemory(Main.world);

		GuiFactory.guis.get("main").setActive(false);

		SaveManager.prepareWorld();
		Main.state = GameState.INGAME;
	}

	public static void back(){
		GuiFactory.guis.get("main").getChild("contentPanel").getChild("spMenu").setActive(false);
		GuiFactory.guis.get("main").getChild("contentPanel").getChild("top").setActive(true);
	}

}
