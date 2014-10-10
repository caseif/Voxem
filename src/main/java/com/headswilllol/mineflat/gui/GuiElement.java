package com.headswilllol.mineflat.gui;

import com.headswilllol.mineflat.vector.Vector2i;

public abstract class GuiElement {

	protected String id;
	protected Vector2i position;

	public String getId(){
		return id;
	}

	public Vector2i getPosition(){
		if (position == null)
			System.out.println("wot");
		return position;
	}

	public void setPosition(Vector2i position){
		this.position = position;
	}

	public abstract void draw();

}
