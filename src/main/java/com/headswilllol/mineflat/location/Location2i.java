package com.headswilllol.mineflat.location;

public class Location2i {

	protected int x;
	protected int y;

	public Location2i(int x, int y){
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
