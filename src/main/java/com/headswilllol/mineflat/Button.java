package com.headswilllol.mineflat;

import com.headswilllol.mineflat.vector.Vector2i;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Button {

	private static int nextHandle = 0;
	private static final List<Button> buttons = new ArrayList<Button>();
	
	private Vector2i position;
	private int width;
	private int height;
	private String text;
	private final int handle;
	private boolean active = true;

	public Button(Vector2i position, int width, int height, String text){
		this.position = position;
		this.width = width;
		this.height = height;
		this.text = text;
		this.handle = nextHandle;
		nextHandle += 1;
	}

	public Vector2i getPosition(){
		return position;
	}

	public void setPosition(Vector2i position){
		this.position = position;
	}

	public int getWidth(){
		return width;
	}

	public void setWidth(int width){
		this.width = width;
	}

	public int getHeight(){
		return height;
	}

	public void setHeight(int height){
		this.height = height;
	}

	public String getText(){
		return text;
	}

	public void setText(String text){
		this.text = text;
	}
	
	public int getHandle(){
		return handle;
	}
	
	public boolean isActive(){
		return active;
	}
	
	public void setActive(boolean active){
		this.active = active;
	}
	
	public void draw(){
		glColor3f(0.5f, 0.5f, 0.5f);
		glBegin(GL_QUADS);
		glVertex2f(position.getX(), position.getY());
		glVertex2f(position.getX() + width, position.getY());
		glVertex2f(position.getX() + width, position.getY() + height);
		glVertex2f(position.getX(), position.getY() + height);
		glEnd();
	}
	
	public static Button getButton(int handle){
		for (Button b : buttons)
			if (b.getHandle() == handle)
				return b;
		return null;
	}
	
}
