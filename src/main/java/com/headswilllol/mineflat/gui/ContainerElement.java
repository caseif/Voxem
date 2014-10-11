package com.headswilllol.mineflat.gui;

import com.headswilllol.mineflat.vector.Vector2i;
import com.headswilllol.mineflat.vector.Vector4f;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class ContainerElement extends GuiElement {

	private Vector2i size;

	private HashMap<String, GuiElement> elements = new HashMap<String, GuiElement>();

	private Vector4f color;

	public ContainerElement(String id, Vector2i position, Vector2i size, Vector4f color){
		super.id = id;
		this.position = position;
		this.size = size;
		this.color = color;
	}

	public Vector2i getSize(){
		return size;
	}

	@Override
	public void setActive(boolean active){
		super.setActive(active);
		for (GuiElement ge : elements.values()){
			ge.setActive(active);
		}
	}

	public GuiElement getElement(String id){
		if (elements.get(id) != null)
			return elements.get(id);
		for (GuiElement ge : elements.values()){
			if (ge instanceof ContainerElement){
				if (((ContainerElement)ge).getElement(id) != null){
					return ((ContainerElement)ge).getElement(id);
				}
			}
		}
		return null;
	}

	public void addElement(GuiElement ge){
		ge.setParent(this);
		elements.put(ge.getId(), ge);
	}

	public void removeElement(String id){
		elements.remove(id);
	}

	public void checkButtons(){
		for (GuiElement ge : elements.values()){
			if (ge instanceof Button){
				if (((Button)ge).contains(new Vector2i(Mouse.getX(), Display.getHeight() - Mouse.getY()))){
					((Button)ge).click();
				}
			}
			else if (ge instanceof ContainerElement){
				((ContainerElement)ge).checkButtons();
			}
		}
	}

	public void draw(){
		if (isActive()) {
			GL11.glColor4f(color.getX(), color.getY(), color.getZ(), color.getW());
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2i(position.getX(), position.getY());
			GL11.glVertex2i(position.getX() + size.getX(), position.getY());
			GL11.glVertex2i(position.getX() + size.getX(), position.getY() + size.getY());
			GL11.glVertex2i(position.getX(), position.getY() + size.getY());
			GL11.glEnd();
			for (GuiElement ge : elements.values()) {
				ge.draw();
			}
		}
	}

}
