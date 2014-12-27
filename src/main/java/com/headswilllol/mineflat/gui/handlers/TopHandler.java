package com.headswilllol.mineflat.gui.handlers;

import com.headswilllol.mineflat.Main;
import com.headswilllol.mineflat.gui.ContainerElement;
import com.headswilllol.mineflat.gui.GuiFactory;

public class TopHandler {

	public static void playGame(){
		((ContainerElement)GuiFactory.guis.get("main").getChild("contentPanel")).getChild("top").setActive(false);
		((ContainerElement)GuiFactory.guis.get("main").getChild("contentPanel")).getChild("spMenu").setActive(true);
	}

	public static void quitGame(){
		Main.closed = true;
	}

}
