/*
 * MineFlat
 * Copyright (c) 2014, Maxim Roncacé <mproncace@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.headswilllol.mineflat.gui;

import com.headswilllol.mineflat.vector.Vector2i;
import com.headswilllol.mineflat.vector.Vector4f;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

// I actually at one point considered writing the GUI module in Visual Basic, but I wouldn't have been able to
// implement anything directly
public class GuiElement {

	private HashMap<String, GuiElement> children = new HashMap<>();

	protected String id;
	protected Vector2i position;
	protected Vector2i size;
	protected Vector4f color;

	protected Optional<Class<?>> handlerClass = Optional.empty();

	protected Optional<GuiElement> parent = Optional.empty();

	protected boolean active = false;

	public GuiElement(String id, Vector2i position, Vector2i size, Vector4f color){
		this.id = id;
		this.position = position;
		this.size = size;
		this.color = color;
	}

	public GuiElement(String id, Vector2i position, Vector2i size){
		this(id, position, size, new Vector4f(0f, 0f, 0f, 0f));
	}

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
		return parent.isPresent() ? parent.get().getAbsolutePosition().add(position) : getPosition();
	}

	public Vector2i getSize(){
		return size;
	}

	public void setSize(Vector2i size){
		this.size = size;
	}

	public boolean isActive(){
		return active;
	}

	public void setActive(boolean active){
		setActive(active, true);
	}

	public void setActive(boolean active, boolean recursive){
		this.active = active;
		if (recursive){
			for (GuiElement ge : children.values()){
				ge.setActive(active);
			}
		}
		if (active && getParent().isPresent()){
			System.out.println(id + " - " + getParent().get().getId());
			getParent().get().setActive(active, false);
		}
	}

	public Vector4f getColor(){
		return this.color;
	}

	public void setColor(Vector4f color){
		this.color = color;
	}

	public Optional<GuiElement> getParent(){
		return parent;
	}

	public void setParent(GuiElement element){
		this.parent = element != null ? Optional.of(element) : Optional.<GuiElement>empty();
	}

	public Optional<Class<?>> getHandlerClass(){
		if (handlerClass.isPresent() || !parent.isPresent())
			return handlerClass;
		else
			return parent.get().getHandlerClass();
	}

	public void setHandlerClass(Class<?> clazz){
		handlerClass = clazz != null ? Optional.<Class<?>>of(clazz) : Optional.<Class<?>>empty();
	}

	public void setHandlerClass(String className){
		try {
			handlerClass = className != null ?
					Optional.<Class<?>>of(Class.forName(className)) :
					Optional.<Class<?>>empty();
		}
		catch (ClassNotFoundException ex){
			handlerClass = Optional.empty();
			System.err.println("Invalid handler class \"" + className + "\" for GUI element " + id  + "!");
		}
	}

	public boolean contains(Vector2i point){
		return point.getX() >= this.getAbsolutePosition().getX() &&
				point.getX() <= this.getAbsolutePosition().getX() + this.getSize().getX() &&
				point.getY() >= this.getAbsolutePosition().getY() &&
				point.getY() <= this.getAbsolutePosition().getY() + this.getSize().getY();
	}

	public Collection<GuiElement> getChildren(){
		return children.values();
	}

	public GuiElement getChild(String id){
		if (children.get(id) != null)
			return children.get(id);
		for (GuiElement ge : children.values()){
			if (ge.getChild(id) != null){
				return ge.getChild(id);
			}
		}
		return null;
	}

	public void addChild(GuiElement ge){
		ge.setParent(this);
		children.put(ge.getId(), ge);
	}

	public void removeChild(String id){
		children.remove(id);
	}

	public void checkInteraction(){
		if (this instanceof InteractiveElement){
			if (this.contains(new Vector2i(Mouse.getX(), Display.getHeight() - Mouse.getY()))){
				((InteractiveElement)this).interact();
			}
		}
		for (GuiElement ge : children.values()){
			ge.checkInteraction();
		}
	}

	public void checkMousePos(){
		if (isActive()) {
			if (this instanceof InteractiveElement) {
				if (this.contains(new Vector2i(Mouse.getX(), Display.getHeight() - Mouse.getY()))) {
					((InteractiveElement)this).interact();
				}
			}
			for (GuiElement e : getChildren()) {
				e.checkInteraction();
			}
		}
	}

	public void draw(){
		if (isActive()){
			GL11.glColor4f(color.getX(), color.getY(), color.getZ(), color.getW());
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2i(getAbsolutePosition().getX(), getAbsolutePosition().getY());
			GL11.glVertex2i(getAbsolutePosition().getX() + size.getX(), getAbsolutePosition().getY());
			GL11.glVertex2i(getAbsolutePosition().getX() + size.getX(), getAbsolutePosition().getY() + size.getY());
			GL11.glVertex2i(getAbsolutePosition().getX(), getAbsolutePosition().getY() + size.getY());
			GL11.glEnd();
			for (GuiElement ge : children.values()){
				ge.draw();
			}
		}
	}

}
