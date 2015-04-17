/*
 * MineFlat
 * Copyright (c) 2014-2015, Maxim Roncacé <caseif@caseif.net>
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
