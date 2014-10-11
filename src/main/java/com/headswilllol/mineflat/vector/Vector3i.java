package com.headswilllol.mineflat.vector;

/**
 * Represents a vector with 3 int elements.
 */
public class Vector3i implements Vector3 {

	protected int x;
	protected int y;
	protected int z;

	public Vector3i(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public int getZ(){
		return z;
	}

	public void setX(int x){
		this.x = x;
	}

	public void setY(int y){
		this.y = y;
	}

	public void setZ(int z){
		this.z = z;
	}

	public Vector3i add(Vector3i vector){
		return add(vector.getX(), vector.getY(), vector.getZ());
	}

	public Vector3i add(int x, int y, int z){
		return new Vector3i(this.x + x, this.y + y, this.z + z);
	}

	public Vector3i subtract(Vector3i vector){
		return subtract(vector.getX(), vector.getY(), vector.getZ());
	}

	public Vector3i subtract(int x, int y, int z){
		return new Vector3i(this.x - x, this.y - y, this.z + z);
	}

	public boolean equals(Object o){
		if (o instanceof Vector3i){
			Vector3i v = (Vector3i)o;
			return v.getX() == this.x && v.getY() == this.y && this.z == z;
		}
		return false;
	}

	@Override
	public Vector3i clone(){
		return new Vector3i(x, y, z);
	}

	@Override
	public String toString(){
		return "Vector3i{" + x + ", " + y + ", " + z + "}";
	}

}
