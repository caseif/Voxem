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
package net.caseif.voxem.gui;

import net.caseif.voxem.util.Alignment;
import net.caseif.voxem.vector.Vector2i;
import net.caseif.voxem.vector.Vector4f;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Button extends InteractiveElement {

    private Vector4f hoverColor;

    private Method handler;

    private Runnable function;

    private TextElement te;

    protected Button(String id, Vector2i position, Vector2i size, String text, Vector4f color, Vector4f hoverColor) {
        super(id, position, size, color);

        te = new TextElement(id + "-text", new Vector2i(size.getX() / 2, size.getY() / 2 - 8), text, 16);
        te.setAlignment(Alignment.CENTER);
        te.setParent(this);

        this.hoverColor = hoverColor != null ? hoverColor : color;
    }

    public Button(String id, Vector2i position, Vector2i size, String text, Vector4f color, Vector4f hoverColor,
                  Runnable clickFunction) {
        this(id, position, size, text, color, hoverColor);
        this.function = clickFunction;
    }

    public Button(String id, Vector2i position, Vector2i size, String text, Vector4f color, Vector4f hoverColor,
                  Method clickFunction) {
        this(id, position, size, text, color, hoverColor);
        this.handler = clickFunction;
    }

    public Vector2i getSize() {
        return size;
    }

    public void setSize(Vector2i size) {
        this.size = size;
    }

    public String getText() {
        return te.getText();
    }

    public void setText(String text) {
        te.setText(text);
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        this.te.setActive(active);
    }

    @Override
    public void draw() {
        Vector4f oldColor = this.color;
        if (this.contains(new Vector2i(Mouse.getX(), Display.getHeight() - Mouse.getY())))
            setColor(hoverColor);
        super.draw();
        setColor(oldColor);
        if (isActive()) {
            te.draw();
        }
    }

    public boolean contains(Vector2i point) {
        return point.getX() >= this.getAbsolutePosition().getX() &&
                point.getX() <= this.getAbsolutePosition().getX() + this.getSize().getX() &&
                point.getY() >= this.getAbsolutePosition().getY() &&
                point.getY() <= this.getAbsolutePosition().getY() + this.getSize().getY();
    }

    @Override
    public void interact() {
        if (hasMouseReleased && isActive()) {
            if (function != null)
                function.run();
            else if (handler != null) {
                try {
                    handler.invoke(null);
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    System.err.println("Failed to interact with button \"" + id + "\"");
                    ex.printStackTrace();
                }
            }
            hasMouseReleased = false;
        }
    }

    public void interact(Object... params) {
        //TODO: params
        interact();
    }

}
