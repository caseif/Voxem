package com.headswilllol.mineflat.gui;

import com.headswilllol.mineflat.GraphicsHandler;
import com.headswilllol.mineflat.vector.Vector2i;

public class TextElement extends GuiElement {

	private String text;
	private int height;
	private boolean dropShadow;
	private Alignment alignment = Alignment.LEFT;

	public TextElement(Vector2i position, String text, int height, boolean dropShadow){
		super.position = position;
		this.text = text;
		this.height = height;
		this.dropShadow = dropShadow;
	}

	public TextElement(Vector2i position, String text, int height){
		this(position, text, height, false);
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
		switch (alignment){
			case CENTER:
				GraphicsHandler.drawString(text, position.getX() - GraphicsHandler.getStringLength(text, height) / 2,
						position.getY(), height, false);
				break;
			case RIGHT:
				//TODO: actually implement this
				break;
			default:
				GraphicsHandler.drawString(text, position.getX(), position.getY(), height, false);
				break;
		}
	}

}
