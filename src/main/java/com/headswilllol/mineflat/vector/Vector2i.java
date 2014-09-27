package com.headswilllol.mineflat.vector;

/**
 * Represents a vector with two integer elements.
 */
public class Vector2i extends Vector2 {

	protected int x;
	protected int y;

	public Vector2i(int x, int y){
		this.x = x;
		this.y = y;
	}

	public float getX(){
		return x;
	}

	public float getY(){
		return y;
	}

	public void setX(int x){
		this.x = x;
	}

	public void setY(int y){
		this.y = y;
	}

}
