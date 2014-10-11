package com.headswilllol.mineflat.gui;

import com.headswilllol.mineflat.vector.Vector2i;

public abstract class GuiElement {

	protected String id;
	protected Vector2i position;

	protected GuiElement parent;

	protected boolean active = true;

	public String getId(){
		return id;
	}

	public Vector2i getPosition(){
		return position;
	}

	public void setPosition(Vector2i position){
		this.position = position;
	}

	public Vector2i getAbsolutePosition(){
		return parent != null ? parent.getAbsolutePosition().add(position) : position;
	}

	public boolean isActive(){
		return active;
	}

	public void setActive(boolean active){
		this.active = active;
	}

	public GuiElement getParent(){
		return parent;
	}

	public void setParent (GuiElement element){
		this.parent = element;
	}

	public abstract void draw();

}
