package com.headswilllol.mineflat.gui;

import com.headswilllol.mineflat.util.Alignment;
import com.headswilllol.mineflat.vector.Vector2i;
import com.headswilllol.mineflat.vector.Vector4f;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.lwjgl.opengl.GL11.*;

public class Button extends InteractiveElement {

	private Vector2i size;
	private String text;
	private Vector4f color;
	private Vector4f hoverColor;

	private Method handler;

	private Runnable function;

	private TextElement te;

	protected Button(String id, Vector2i position, Vector2i size, String text, Vector4f color, Vector4f hoverColor){
		this.id = id;
		this.position = position;
		this.size = size;
		this.text = text;
		te = new TextElement(id + "-text", new Vector2i(size.getX() / 2, size.getY() / 2 - 8), text, 16);
		te.setAlignment(Alignment.CENTER);
		te.setParent(this);

		this.color = color;
		this.hoverColor = hoverColor != null ? hoverColor : color;
	}

	public Button(String id, Vector2i position, Vector2i size, String text, Vector4f color, Vector4f hoverColor,
	              Runnable clickFunction){
		this(id, position, size, text, color, hoverColor);
		this.function = clickFunction;
	}

	public Button(String id, Vector2i position, Vector2i size, String text, Vector4f color, Vector4f hoverColor,
	              Method clickFunction){
		this(id, position, size, text, color, hoverColor);
		this.handler = clickFunction;
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

	@Override
	public void setActive(boolean active){
		super.setActive(active);
		this.te.setActive(active);
	}

	public void draw(){
		if (isActive()) {
			if (this.contains(new Vector2i(Mouse.getX(), Display.getHeight() - Mouse.getY())))
				glColor4f(hoverColor.getX(), hoverColor.getY(), hoverColor.getZ(), hoverColor.getW());
			else
				glColor4f(color.getX(), color.getY(), color.getZ(), color.getW());
			glBegin(GL_QUADS);
			glVertex2f(getAbsolutePosition().getX(), getAbsolutePosition().getY());
			glVertex2f(getAbsolutePosition().getX() + size.getX(), getAbsolutePosition().getY());
			glVertex2f(getAbsolutePosition().getX() + size.getX(), getAbsolutePosition().getY() + size.getY());
			glVertex2f(getAbsolutePosition().getX(), getAbsolutePosition().getY() + size.getY());
			glEnd();
			te.draw();
		}
	}

	public boolean contains(Vector2i point){
		if (point.getX() >= this.getAbsolutePosition().getX() && point.getX() <= this.getAbsolutePosition().getX() + this.getSize().getX() &&
				point.getY() >= this.getAbsolutePosition().getY() && point.getY() <= this.getAbsolutePosition().getY() + this.getSize().getY()) {
			return true;
		}
		return false;
	}

	@Override
	public void interact(){
		if (hasMouseReleased && isActive()){
			if (function != null)
				function.run();
			else if (handler != null){
				try {
					handler.invoke(null);
				}
				catch (IllegalAccessException | InvocationTargetException ex){
					System.err.println("Failed to interact with button \"" + id + "\"");
					ex.printStackTrace();
				}
			}
			hasMouseReleased = false;
		}
	}

	public void interact(Object... params){
		//TODO: params
		interact();
	}

}
