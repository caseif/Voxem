package com.headswilllol.mineflat.vector;

/**
 * Represents a vector with 4 float elements.
 */
public class Vector4f implements Vector4 {

	protected float x;
	protected float y;
	protected float z;
	protected float w;

	public Vector4f(float x, float y, float z, float w){
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
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

	public float getW(){
		return w;
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

	public void setW(float w){
		this.w = w;
	}

	public Vector4f add(Vector4f vector){
		return add(vector.getX(), vector.getY(), vector.getZ(), vector.getW());
	}

	public Vector4f add(float x, float y, float z, float w){
		return new Vector4f(this.x + x, this.y + y, this.z + z, this.w + w);
	}

	public Vector4f subtract(Vector4f vector){
		return subtract(vector.getX(), vector.getY(), vector.getZ(), vector.getW());
	}

	public Vector4f subtract(float x, float y, float z, float w){
		return new Vector4f(this.x - x, this.y - y, this.z + z, this.w + w);
	}

	public boolean equals(Object o){
		if (o instanceof Vector4f){
			Vector4f v = (Vector4f)o;
			return v.getX() == this.x && v.getY() == this.y && this.z == z && v.getW() == w;
		}
		return false;
	}

	@Override
	public Vector4f clone(){
		return new Vector4f(x, y, z, w);
	}

	@Override
	public String toString(){
		return "Vector4f{" + x + ", " + y + ", " + z + ", " + w + "}";
	}

}
