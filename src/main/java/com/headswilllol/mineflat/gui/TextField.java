/*
 * MineFlat
 * Copyright (c) 2014, Maxim Roncacé <mproncace@gmail.com>
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
package com.headswilllol.mineflat.gui;

import com.headswilllol.mineflat.GraphicsHandler;
import com.headswilllol.mineflat.vector.Vector2i;
import com.headswilllol.mineflat.vector.Vector4f;
import org.lwjgl.opengl.GL11;

public class TextField extends InteractiveElement {

	private static TextField selected;

	private String text = "";
	private Vector4f bgColor;
	private Vector4f textColor;

	private int column;

	public TextField(String id, Vector2i position, Vector2i size, Vector4f bgColor, Vector4f textColor){
		super.id = id;
		super.position = position;
		super.size = size;
		this.bgColor = bgColor;
		this.textColor = textColor;
	}

	public String getText(){
		return text;
	}

	public void setText(String text){
		this.text = text;
	}

	public Vector4f getBackgroundColor(){
		return bgColor;
	}

	public void setBackgroundColor(Vector4f color){
		this.bgColor = color;
	}

	public Vector4f getTextColor(){
		return textColor;
	}

	public void setTextColor(Vector4f color){
		this.textColor = color;
	}

	public void draw(){
		if (isActive()) {
			GL11.glColor4f(bgColor.getX(), bgColor.getY(), bgColor.getZ(), bgColor.getW());
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2i(getAbsolutePosition().getX(), getAbsolutePosition().getY());
			GL11.glVertex2i(getAbsolutePosition().getX() + getSize().getX(), getAbsolutePosition().getY());
			GL11.glVertex2i(
					getAbsolutePosition().getX() + getSize().getX(), getAbsolutePosition().getY() + getSize().getY()
			);
			GL11.glVertex2i(getAbsolutePosition().getX(), getAbsolutePosition().getY() + getSize().getY());
			GL11.glEnd();
			GL11.glColor4f(textColor.getX(), textColor.getY(), textColor.getZ(), textColor.getW());
			GraphicsHandler.drawString(getText(), getAbsolutePosition().getX() + 3,
					getAbsolutePosition().getY() + 3, getSize().getY() - 6, false);
			if (selected == this){
				GL11.glBegin(GL11.GL_QUADS);
				int /* I got 'em foaming from the mouth and I just hit 'em with the */ baseX =
						getAbsolutePosition().getX() +
						GraphicsHandler.getStringLength(getText().substring(0, column + 1), getSize().getY() - 6) + 1;
				int baseY = getAbsolutePosition().getY() + 1;
				GL11.glVertex2i(baseX, baseY);
				GL11.glVertex2i(baseX + 2, baseY);
				GL11.glVertex2i(baseX + 2, baseY + getSize().getY() - 2);
				GL11.glVertex2i(baseX, baseY + getSize().getY() - 2);
				GL11.glEnd();
			}
		}
	}

	public void interact(){
		setSelectedField(this);
	}

	public static TextField getSelectedField(){
		return selected;
	}

	public static void setSelectedField(TextField selected){
		TextField.selected = selected;
	}

}
