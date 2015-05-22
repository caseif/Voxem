/*
 * Voxem
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
package net.caseif.voxem.vector;

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
