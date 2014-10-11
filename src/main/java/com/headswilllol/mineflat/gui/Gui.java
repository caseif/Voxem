package com.headswilllol.mineflat.gui;

import com.headswilllol.mineflat.vector.Vector2i;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Gui {

	private String id;
	private HashMap<String, GuiElement> elements = new HashMap<String, GuiElement>();

	private boolean active = false;

	public Collection<GuiElement> getElements(){
		return elements.values();
	}

	public void addElement(GuiElement element){
		elements.put(element.getId(), element);
	}

	public GuiElement getElement(String id){
		if (elements.get(id) != null) {
			return elements.get(id);
		}
		else {
			for (GuiElement ge : elements.values()){
				if (ge instanceof ContainerElement){
					if (((ContainerElement)ge).getElement(id) != null){
						return ((ContainerElement)ge).getElement(id);
					}
				}
			}
		}
		return null;
	}

	public void removeElement(String id){
		elements.remove(id);
	}

	public void checkMousePos(){
		if (isActive()) {
			for (GuiElement e : getElements()) {
				if (e.isActive()) {
					if (e instanceof Button) {
						if (((Button)e).contains(new Vector2i(Mouse.getX(), Display.getHeight() - Mouse.getY()))) {
							((Button)e).click();
						}
					}
					else if (e instanceof ContainerElement){
						((ContainerElement)e).checkButtons();
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
			for (GuiElement e : this.elements.values()) {
				e.draw();
			}
		}
	}

}
