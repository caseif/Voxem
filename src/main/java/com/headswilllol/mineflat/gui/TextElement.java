package com.headswilllol.mineflat.gui;

import com.headswilllol.mineflat.GraphicsHandler;
import com.headswilllol.mineflat.util.Alignment;
import com.headswilllol.mineflat.vector.Vector2i;

public class TextElement extends GuiElement {

	private String text;
	private boolean dropShadow;
	private Alignment alignment = Alignment.LEFT;

	public TextElement(String id, Vector2i position, String text, int height, boolean dropShadow){
		super.id = id;
		super.position = position;
		this.text = text;
		this.size = new Vector2i(GraphicsHandler.getStringLength(text, height), height);
		this.dropShadow = dropShadow;
	}

	public TextElement(String id, Vector2i position, String text, int height){
		this(id, position, text, height, false);
	}

	public String getText(){
		return text;
	}

	public void setText(String text){
		this.text = text;
	}

	public Alignment getAlignment(){
		return alignment;
	}

	public void setAlignment(Alignment alignment){
		this.alignment = alignment;
	}

	public void draw(){
		if (isActive()) {
			switch (alignment) {
				case CENTER:
					GraphicsHandler.drawString(text, getAbsolutePosition().getX() - size.getX() / 2,
							getAbsolutePosition().getY(), size.getY(), dropShadow);
					break;
				//case RIGHT:
				//	//TODO: actually implement this
				//	break;
				default:
					GraphicsHandler.drawString(text, getAbsolutePosition().getX(), getAbsolutePosition().getY(),
							size.getY(), dropShadow);
					break;
			}
		}
	}

}
