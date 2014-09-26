package com.headswilllol.mineflat;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;

public class Button {

	private static int nextHandle = 0;
	private static final List<Button> buttons = new ArrayList<Button>();
	
	private int x;
	private int y;
	private int width;
	private int height;
	private String text;
	private final int handle;
	private boolean active = true;

	public Button(int x, int y, int width, int height, String text){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
		this.handle = nextHandle;
		nextHandle += 1;
	}
	
	public int getX(){
		return x;
	}

	public void setX(int x){
		this.x = x;
	}

	public int getY(){
		return y;
	}

	public void setY(int y){
		this.y = y;
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
		glVertex2f(x, y);
		glVertex2f(x + width, y);
		glVertex2f(x + width, y + height);
		glVertex2f(x, y + height);
		glEnd();
	}
	
	public static Button getButton(int handle){
		for (Button b : buttons)
			if (b.getHandle() == handle)
				return b;
		return null;
	}
	
}
