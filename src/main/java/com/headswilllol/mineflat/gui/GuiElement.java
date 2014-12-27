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
		if (point.getX() >= this.getAbsolutePosition().getX() && point.getX() <= this.getAbsolutePosition().getX() + this.getSize().getX() &&
				point.getY() >= this.getAbsolutePosition().getY() && point.getY() <= this.getAbsolutePosition().getY() + this.getSize().getY()) {
			return true;
		}
		return false;
	}

	public abstract void draw();

}
