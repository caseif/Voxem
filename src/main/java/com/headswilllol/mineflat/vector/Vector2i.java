package com.headswilllol.mineflat.vector;

/**
 * Represents a vector with two int elements.
 */
public class Vector2i implements Vector2 {

	protected int x;
	protected int y;

	public Vector2i(int x, int y){
		this.x = x;
		this.y = y;
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public void setX(int x){
		this.x = x;
	}

	public void setY(int y){
		this.y = y;
	}

	public Vector2i add(Vector2i vector){
	return add(vector.getX(), vector.getY());
	}

	public Vector2i add(int x, int y){
		return new Vector2i(this.x + x, this.y + y);
	}

	public Vector2i subtract(Vector2i vector){
		return subtract(vector.getX(), vector.getY());
	}

	public Vector2i subtract(int x, int y){
		return new Vector2i(this.x - x, this.y - y);
	}

	public boolean equals(Object o){
		if (o instanceof Vector2i){
			Vector2i v = (Vector2i)o;
			return v.getX() == this.x && v.getY() == this.y;
		}
		return false;
	}

	@Override
	public Vector2i clone(){
		return new Vector2i(x, y);
	}

}
