/*
 * Voxem
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
package net.caseif.voxem.gui;

import net.caseif.voxem.GameState;
import net.caseif.voxem.Main;
import net.caseif.voxem.util.FileUtil;
import net.caseif.voxem.vector.Vector2i;
import net.caseif.voxem.vector.Vector4f;
import net.caseif.voxem.world.SaveManager;

import java.io.File;
import java.util.HashMap;

/**
 * Utility class used for creating GUI interfaces in Visual Basic to track the killer's IP.
 */
public class GuiFactory {

	public static HashMap<String, GuiElement> guis = new HashMap<>();

	public static void construct(){
		constructMainGui();
		constructDebugGui();
	}

	public static void constructMainGui(){
		GuiElement mainMenu = GuiParser.parseFile("/gui/gui.json");
		mainMenu.getChild("titleText").setActive(true);
		mainMenu.getChild("main").getChild("contentPanel").getChild("top").setActive(true);
		guis.put("main", mainMenu);
		constructLoadWorldMenu();
	}

	public static void constructLoadWorldMenu(){
		final GuiElement worldList = guis.get("main").getChild("contentPanel").getChild("spMenu")
				.getChild("loadWorld");
		worldList.setActive(false);
		int buttons = 0;
		File saveFolder = new File(FileUtil.getAppDataFolder() + File.separator + ".voxem", "saves");
		if (saveFolder.exists()){
			for (final File f : saveFolder.listFiles()){
				Button lB = new Button(
						"loadWorld-" + f.getName(),
						new Vector2i(worldList.getSize().getX() / 2 - 200, 75 * (buttons)),
						new Vector2i(400, 50),
						f.getName(),
						new Vector4f(0.5f, 0.5f, 0.5f, 1f),
						new Vector4f(0.8f, 0.4f, 0.4f, 1f),
						new Runnable() {
					public void run(){
						try {
							SaveManager.loadWorld(f.getName()); // TODO: load world name from JSON file
						}
						catch (Exception ex){
							System.err.println("Exception occurred while loading world \"" + f.getName() +
									"\" from disk! The save file may be invalid or corrupt.");
							ex.printStackTrace();
						}

						SaveManager.prepareWorld();

						guis.get("main").setActive(false);

						Main.state = GameState.INGAME;
					}
				});
				lB.setActive(false);
				worldList.addChild(lB);
				buttons += 1;
			}
		}
	}

	public static void constructDebugGui(){
		final GuiElement debugPanel = new GuiElement("debugPanel",
				new Vector2i(0, 0),
				new Vector2i(0, 280),
				new Vector4f(0.2f, 0.2f, 0.2f, 0.3f));
		int height = 16;
		debugPanel.addChild(new TextElement("fps", new Vector2i(10, 10), "fps: ???", height, true));
		debugPanel.addChild(new TextElement("delta", new Vector2i(10, 40), "delta (ms): ???", height, true));
		debugPanel.addChild(new TextElement("playerX", new Vector2i(10, 70), "x: ???", height, true));
		debugPanel.addChild(new TextElement("playerY", new Vector2i(10, 100), "y: ???", height, true));
		debugPanel.addChild(new TextElement("playerChunk", new Vector2i(10, 130), "chunk: ???", height, true));
		debugPanel.addChild(new TextElement("playerG", new Vector2i(10, 160), "g: ???", height, true));
		debugPanel.addChild(new TextElement("playerLight", new Vector2i(10, 190), "light level: ???", height, true));
		debugPanel.addChild(new TextElement("ticks", new Vector2i(10, 220), "ticks: ???", height, true));
		debugPanel.addChild(new TextElement("memory", new Vector2i(10, 250), "??? mb allocated memory", height, true));
		guis.put("debug", debugPanel);
	}

}
