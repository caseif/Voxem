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

import java.util.Optional;

// I actually at one point considered writing the GUI module in Visual Basic, but I wouldn't have been able to
// implement anything directly
public abstract class GuiElement {

	protected String id;
	protected Vector2i position;
	protected Vector2i size;

	protected Optional<Class<?>> handlerClass = Optional.empty();

	protected Optional<GuiElement> parent = Optional.empty();

	protected boolean active = false;

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
		this.active = active;
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

	public abstract void draw();

}
