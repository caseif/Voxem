package com.headswilllol.mineflat.gui;

import com.headswilllol.mineflat.vector.Vector2i;

import java.util.ArrayList;
import java.util.Collection;

public class Gui {

	private String id;
	private Collection<GuiElement> elements = new ArrayList<GuiElement>();

	private boolean active = false;

	public void addElement(GuiElement element){
		elements.add(element);
	}

	public void checkMousePos(Vector2i mousePos){
		if (isActive()) {
			for (GuiElement e : elements) {
				if (e instanceof Button) {
					if (((Button)e).contains(mousePos)) {
						((Button)e).click();
					}
				}
			}
		}
	}

	public boolean isActive(){
		return active;
	}

	public void setActive(boolean active){
		this.active = active;
	}

	public void draw() {
		if (isActive()) {
			for (GuiElement e : this.elements) {
				e.draw();
			}
		}
	}

}
