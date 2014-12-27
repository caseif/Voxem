package com.headswilllol.mineflat.gui.handlers;

import com.headswilllol.mineflat.GameState;
import com.headswilllol.mineflat.Main;
import com.headswilllol.mineflat.entity.Player;
import com.headswilllol.mineflat.gui.ContainerElement;
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
		((ContainerElement)GuiFactory.guis.get("main").getChild("contentPanel")).getChild("spMenu").setActive(false);
		((ContainerElement)GuiFactory.guis.get("main").getChild("contentPanel")).getChild("top").setActive(true);
	}

}
