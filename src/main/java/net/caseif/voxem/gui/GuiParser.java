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

import net.caseif.voxem.GraphicsHandler;
import net.caseif.voxem.util.MiscUtil;
import net.caseif.voxem.vector.Vector2i;
import net.caseif.voxem.vector.Vector4f;

import com.google.common.base.Optional;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.lwjgl.opengl.Display;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GuiParser {

    private static final List<GuiElement> elementsToCenter = new ArrayList<>();

    /**
     * Parses a JSON file into a {@link GuiElement} object.
     *
     * @param path the path of the file to parse
     * @return the constructed {@link GuiElement}
     */
    public static GuiElement parseFile(String path) {
        InputStream is = GuiParser.class.getResourceAsStream(path);
        if (is != null) {
            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse(new InputStreamReader(is)).getAsJsonObject();
            JsonObject menus = json.getAsJsonObject("menus");
            GuiElement gui = parseElement("gui", menus, Optional.<GuiElement>absent());
            //TODO: this is a terrible way of accomplishing this and should be reconsidered
            for (GuiElement e : elementsToCenter) {
                e.setPosition(new Vector2i(((e.getParent().isPresent() ?
                        e.getParent().get().getSize().getX() :
                        Display.getWidth()) / 2 - (e.getSize().getX()) / 2), e.getPosition().getY()));
            }
            return gui;
        }
        throw new IllegalArgumentException("Cannot find resource " + path + "!");
    }

    private static GuiElement parseElement(String id, JsonObject json, Optional<GuiElement> parent) {
        GuiElement element = null; // only remains null if this element can't be parsed
        int height;
        if (json.has("height")) {
            if (json.get("height").getAsString().endsWith("%")) {
                // height is defined as percentage of parent height
                height = (int) (Integer.parseInt(json.get("height").getAsString().replace("%", "")) / 100f *
                        (parent.isPresent() ? parent.get().getSize().getY() : Display.getHeight()));
            } else {
                // height is standard pixel count
                height = json.get("height").getAsInt();
            }
        } else {
            if (parent.isPresent()) {
                height = parent.get().getSize().getY(); // use parent element height
            } else {
                height = Display.getHeight(); // default to full window height
            }
        }
        if (height < 0)
            height = Display.getHeight() + height;

        int width;
        if (json.has("width")) {
            if (json.get("width").getAsString().endsWith("%")) {
                // width is defined as percentage of parent height
                width = (int) (Integer.parseInt(json.get("width").getAsString().replace("%", "")) / 100f *
                        (parent.isPresent() ? parent.get().getSize().getX() : Display.getWidth()));
            } else {
                // width is standard pixel count
                width = json.get("width").getAsInt();
            }
        } else if (json.has("type") && json.get("type").getAsString().equalsIgnoreCase("text") && json.has("text")) {
            width = GraphicsHandler.getStringLength(json.get("text").getAsString(), height);
        } else {
            if (parent.isPresent()) {
                width = parent.get().getSize().getX(); // use parent element width
            } else {
                width = Display.getWidth(); // default to full window width
            }
        }
        if (width < 0)
            width = Display.getWidth() + width;

        int x = 0; // default to x=0 (relative to parent)
        if (json.has("x")) {
            String jsonX = json.get("x").getAsString();
            if (jsonX.equalsIgnoreCase("center")) {
                x = Integer.MAX_VALUE;
            } else
                x = Integer.parseInt(jsonX);
        }
        if (x < 0) // position is relative to right
            x = (parent.isPresent() ? parent.get().getSize().getX() : Display.getWidth()) + x;

        int y = json.has("y") ? json.get("y").getAsInt() : 0; // if not present, default to y=0 (relative to parent)
        if (y < 0) // position is relative to bottom
            y = (parent.isPresent() ? parent.get().getSize().getY() : Display.getHeight()) + y;

        Vector4f color = json.has("color") ?
                MiscUtil.hexToRGBA(json.get("color").getAsString()) : // use defined color
                MiscUtil.hexToRGBA("#FFF0"); // default to white transparent

        String text = json.has("text") ? json.get("text").getAsString() : ""; // default to empty string

        Class<?> handlerClass = null;
        if (json.has("handlerClass")) {
            try {
                handlerClass = Class.forName(json.get("handlerClass").getAsString());
            } catch (ClassNotFoundException ex) {
                System.err.println("Cannot resolve handler class for element " + id + "!");
                ex.printStackTrace();
            }
        } else if (parent.isPresent())
            handlerClass = parent.get().getHandlerClass().isPresent() ? parent.get().getHandlerClass().get() : null;

        if (json.has("type")) {
            String type = json.get("type").getAsString();
            switch (type) { // check defined type
                case "text": // text element
                    // instantiate the new TextElement
                    element = new TextElement(id, new Vector2i(x, y), text, height,
                            json.has("shadow") && json.get("shadow").getAsBoolean());
                    break;
                case "button": // button element
                    Vector4f hover = MiscUtil.hexToRGBA(json.get("hoverColor").getAsString());
                    if (handlerClass != null) {
                        if (json.has("handlerMethod")) {
                            String mName = json.get("handlerMethod").getAsString();
                            JsonArray params = json.getAsJsonArray("handlerParams");
                            try {
                                Method m = handlerClass.getMethod(mName);
                                //TODO: params
                                element = new Button(id, new Vector2i(x, y), new Vector2i(width, height),
                                        text, color, hover, m);
                            } catch (NoSuchMethodException ex) {
                                System.err.println("Failed to access handler method " + mName + " for element " + id);
                            }
                        } else
                            System.err.println("No handler method for element " + id + "!");
                    } else {
                        System.err.println("No handler class for element " + id + "!");
                    }
                    break;
                case "panel": // container element
                    element = new GuiElement(id, new Vector2i(x, y), new Vector2i(width, height), color);
                    break;
                default:
                    // unrecognized element, ignore it
                    System.err.println("Unrecognized element type \"" + type + "\"");
                    // default to container element
                    element = new GuiElement(id, new Vector2i(x, y), new Vector2i(width, height), color);
                    break;
            }
        } else {
            // default to container element
            element = new GuiElement(id, new Vector2i(x, y), new Vector2i(width, height), color);
        }
        if (element != null) {
            if (x == Integer.MAX_VALUE)
                elementsToCenter.add(element);
            element.setHandlerClass(handlerClass);
            for (Map.Entry<String, JsonElement> e : json.entrySet()) { // iterate subelements
                if (e.getValue().isJsonObject()) { // assert it's not a value for the current element
                    // recursively parse subelements
                    element.addChild(parseElement(e.getKey(), e.getValue().getAsJsonObject(),
                            Optional.of(element)));
                }
            }
        }
        return element;
    }

}
