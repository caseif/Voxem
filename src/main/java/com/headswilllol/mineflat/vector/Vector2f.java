package com.headswilllol.mineflat.vector;

/**
 * Represents a vector with 2 float elements.
 */
public class Vector2f extends Vector2 {

	protected float x;
	protected float y;

	public Vector2f(float x, float y){
		this.x = x;
		this.y = y;
	}

	public float getX(){
		return x;
	}

	public float getY(){
		return y;
	}

	public void setX(float x){
		this.x = x;
	}

	public void setY(float y){
		this.y = y;
	}

}
