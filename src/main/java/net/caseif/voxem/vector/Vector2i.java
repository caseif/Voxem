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

	@Override
	public String toString(){
		return "Vector2i{" + x + ", " + y + "}";
	}

}
