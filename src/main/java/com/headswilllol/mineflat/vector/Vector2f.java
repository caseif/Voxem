package com.headswilllol.mineflat.vector;

/**
 * Represents a vector with 2 float elements.
 */
public class Vector2f implements Vector2 {

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

	public Vector2f add(Vector2f vector){
		return add(vector.getX(), vector.getY());
	}

	public Vector2f add(float x, float y){
		return new Vector2f(this.x + x, this.y + y);
	}

	public Vector2f subtract(Vector2f vector){
		return subtract(vector.getX(), vector.getY());
	}

	public Vector2f subtract(float x, float y){
		return new Vector2f(this.x - x, this.y - y);
	}

	public boolean equals(Object o){
		if (o instanceof Vector2f){
			Vector2f v = (Vector2f)o;
			return v.getX() == this.x && v.getY() == this.y;
		}
		return false;
	}

	@Override
	public Vector2f clone(){
		return new Vector2f(x, y);
	}

}
