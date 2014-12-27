package com.headswilllol.mineflat.gui;

import com.headswilllol.mineflat.vector.Vector2i;
import com.headswilllol.mineflat.vector.Vector4f;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.util.Collection;
import java.util.HashMap;

//TODO: phase out this class so all elements are containers
public class ContainerElement extends GuiElement {

	private HashMap<String, GuiElement> elements = new HashMap<String, GuiElement>();

	private Vector4f color;

	public ContainerElement(String id, Vector2i position, Vector2i size, Vector4f color){
		super.id = id;
		this.position = position;
		this.size = size;
		this.color = color;
	}

	@Override
	public void setActive(boolean active){
		setActive(active, true);
	}

	public void setActive(boolean active, boolean recursive){
		super.setActive(active);
		if (recursive){
			for (GuiElement ge : elements.values()){
				ge.setActive(active);
			}
		}
		if (active && getParent().isPresent()){
			if (getParent().get() instanceof ContainerElement)
				((ContainerElement)getParent().get()).setActive(active, false);
			else
				getParent().get().setActive(active);
		}
	}

	public Collection<GuiElement> getChildren(){
		return elements.values();
	}

	public GuiElement getChild(String id){
		if (elements.get(id) != null)
			return elements.get(id);
		for (GuiElement ge : elements.values()){
			if (ge instanceof ContainerElement){
				if (((ContainerElement)ge).getChild(id) != null){
					return ((ContainerElement)ge).getChild(id);
				}
			}
		}
		return null;
	}

	public void addChild(GuiElement ge){
		ge.setParent(this);
		elements.put(ge.getId(), ge);
	}

	public void removeChild(String id){
		elements.remove(id);
	}

	public void checkInteraction(){
		for (GuiElement ge : elements.values()){
			if (ge instanceof InteractiveElement){
				if (ge.contains(new Vector2i(Mouse.getX(), Display.getHeight() - Mouse.getY()))){
					((InteractiveElement)ge).interact();
				}
			}
			else if (ge instanceof ContainerElement){
				((ContainerElement)ge).checkInteraction();
			}
		}
	}

	public void checkMousePos(){
		if (isActive()) {
			for (GuiElement e : getChildren()) {
				if (e.isActive()) {
					if (e instanceof InteractiveElement) {
						if (e.contains(new Vector2i(Mouse.getX(), Display.getHeight() - Mouse.getY()))) {
							((InteractiveElement)e).interact();
						}
					}
					else if (e instanceof ContainerElement){
						((ContainerElement)e).checkInteraction();
					}
				}
			}
		}
	}

	public void draw(){
		if (isActive()){
			GL11.glColor4f(color.getX(), color.getY(), color.getZ(), color.getW());
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2i(position.getX(), position.getY());
			GL11.glVertex2i(position.getX() + size.getX(), position.getY());
			GL11.glVertex2i(position.getX() + size.getX(), position.getY() + size.getY());
			GL11.glVertex2i(position.getX(), position.getY() + size.getY());
			GL11.glEnd();
			for (GuiElement ge : elements.values()){
				ge.draw();
			}
		}
	}

}
