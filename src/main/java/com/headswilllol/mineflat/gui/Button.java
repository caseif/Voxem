package com.headswilllol.mineflat.gui;

import com.headswilllol.mineflat.GraphicsHandler;
import com.headswilllol.mineflat.vector.Vector2i;
import com.headswilllol.mineflat.vector.Vector3f;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL11.*;

public class Button extends GuiElement {

	private Vector2i size;
	private String text;
	private Vector3f color;
	private Vector3f hoverColor;
	private boolean active = true;

	private TextElement te;

	private Runnable function;

	public Button(String id, Vector2i position, Vector2i size, String text, Vector3f color, Vector3f hoverColor, Runnable clickFunction){
		this.id = id;
		this.position = position;
		this.size = size;
		this.text = text;
		this.te = new TextElement(new Vector2i(position.getX() + size.getX() / 2, position.getY() + size.getY() / 2 - 8), text, 16);
		this.te.setAlignment(Alignment.CENTER);
		this.color = color;
		this.hoverColor = hoverColor;
		this.function = clickFunction;
	}

	public Vector2i getSize(){
		return size;
	}

	public void setSize(Vector2i size){
		this.size = size;
	}

	public String getText(){
		return text;
	}

	public void setText(String text){
		this.text = text;
	}

	public boolean isActive(){
		return active;
	}

	public void setActive(boolean active){
		this.active = active;
	}

	public void draw(){
		if (this.contains(new Vector2i(Mouse.getX(), Display.getHeight() - Mouse.getY())))
			glColor3f(hoverColor.getX(), hoverColor.getY(), hoverColor.getZ());
		else
			glColor3f(color.getX(), color.getY(), color.getZ());
		glBegin(GL_QUADS);
		glVertex2f(position.getX(), position.getY());
		glVertex2f(position.getX() + size.getX(), position.getY());
		glVertex2f(position.getX() + size.getX(), position.getY() + size.getY());
		glVertex2f(position.getX(), position.getY() + size.getY());
		glEnd();
		te.draw();
	}

	public boolean contains(Vector2i point){
		//System.out.println(point.getX() + ", " + point.getY() + ", " + getPosition().getX() + ", " + getPosition().getY());
		if (point.getX() >= this.getPosition().getX() && point.getX() <= this.getPosition().getX() + this.getSize().getX() &&
				point.getY() >= this.getPosition().getY() && point.getY() <= this.getPosition().getY() + this.getSize().getY())
			return true;
		return false;
	}

	public void click(){
		function.run();
	}

}
