package com.headswilllol.mineflat.vector;

/**
 * Represents a vector with 3 float elements.
 */
public class Vector3f implements Vector3 {

	protected float x;
	protected float y;
	protected float z;

	public Vector3f(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public float getX(){
		return x;
	}

	public float getY(){
		return y;
	}

	public float getZ(){
		return z;
	}

	public void setX(float x){
		this.x = x;
	}

	public void setY(float y){
		this.y = y;
	}

	public void setZ(float z){
		this.z = z;
	}

	public Vector3f add(Vector3f vector){
		return add(vector.getX(), vector.getY(), vector.getZ());
	}

	public Vector3f add(float x, float y, float z){
		return new Vector3f(this.x + x, this.y + y, this.z + z);
	}

	public Vector3f subtract(Vector3f vector){
		return subtract(vector.getX(), vector.getY(), vector.getZ());
	}

	public Vector3f subtract(float x, float y, float z){
		return new Vector3f(this.x - x, this.y - y, this.z + z);
	}

	public boolean equals(Object o){
		if (o instanceof Vector3f){
			Vector3f v = (Vector3f)o;
			return v.getX() == this.x && v.getY() == this.y && this.z == z;
		}
		return false;
	}

	@Override
	public Vector3f clone(){
		return new Vector3f(x, y, z);
	}

	@Override
	public String toString(){
		return "Vector3f{" + x + ", " + y + ", " + z + "}";
	}

}
