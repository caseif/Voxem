package mineflat;

import static org.lwjgl.input.Keyboard.getEventCharacter;
import static org.lwjgl.input.Keyboard.getEventKey;
import static org.lwjgl.input.Keyboard.getEventKeyState;
//import static org.lwjgl.input.Keyboard.isRepeatEvent;
import static org.lwjgl.input.Keyboard.next;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

public class Console {

	public static boolean enabled = false;
	public static boolean focused = false;
	public static String currentText = "";
	
	public static List<Integer> disallowedSymbols = new ArrayList<Integer>();

	@SuppressWarnings("deprecation")
	public static void initialize(){
		disallowedSymbols.add(Keyboard.KEY_BACK);
		disallowedSymbols.add(Keyboard.KEY_CAPITAL);
		disallowedSymbols.add(Keyboard.KEY_CLEAR);
		disallowedSymbols.add(Keyboard.KEY_CONVERT);
		disallowedSymbols.add(Keyboard.KEY_DOWN);
		disallowedSymbols.add(Keyboard.KEY_DELETE);
		disallowedSymbols.add(Keyboard.KEY_END);
		disallowedSymbols.add(Keyboard.KEY_ESCAPE);
		disallowedSymbols.add(Keyboard.KEY_FUNCTION);
		disallowedSymbols.add(Keyboard.KEY_F1);
		disallowedSymbols.add(Keyboard.KEY_F2);
		disallowedSymbols.add(Keyboard.KEY_F3);
		disallowedSymbols.add(Keyboard.KEY_F4);
		disallowedSymbols.add(Keyboard.KEY_F5);
		disallowedSymbols.add(Keyboard.KEY_F6);
		disallowedSymbols.add(Keyboard.KEY_F7);
		disallowedSymbols.add(Keyboard.KEY_F8);
		disallowedSymbols.add(Keyboard.KEY_F9);
		disallowedSymbols.add(Keyboard.KEY_F10);
		disallowedSymbols.add(Keyboard.KEY_F11);
		disallowedSymbols.add(Keyboard.KEY_F12);
		disallowedSymbols.add(Keyboard.KEY_F13);
		disallowedSymbols.add(Keyboard.KEY_F14);
		disallowedSymbols.add(Keyboard.KEY_F15);
		disallowedSymbols.add(Keyboard.KEY_F16);
		disallowedSymbols.add(Keyboard.KEY_F17);
		disallowedSymbols.add(Keyboard.KEY_F18);
		disallowedSymbols.add(Keyboard.KEY_F19);
		disallowedSymbols.add(Keyboard.KEY_GRAVE);
		disallowedSymbols.add(Keyboard.KEY_HOME);
		disallowedSymbols.add(Keyboard.KEY_INSERT);
		disallowedSymbols.add(Keyboard.KEY_LCONTROL);
		disallowedSymbols.add(Keyboard.KEY_LEFT);
		disallowedSymbols.add(Keyboard.KEY_LMENU);
		disallowedSymbols.add(Keyboard.KEY_LMETA);
		disallowedSymbols.add(Keyboard.KEY_LSHIFT);
		disallowedSymbols.add(Keyboard.KEY_LWIN);
		disallowedSymbols.add(Keyboard.KEY_NUMLOCK);
		disallowedSymbols.add(Keyboard.KEY_PAUSE);
		disallowedSymbols.add(Keyboard.KEY_POWER);
		disallowedSymbols.add(Keyboard.KEY_RCONTROL);
		disallowedSymbols.add(Keyboard.KEY_RETURN);
		disallowedSymbols.add(Keyboard.KEY_RIGHT);
		disallowedSymbols.add(Keyboard.KEY_RMETA);
		disallowedSymbols.add(Keyboard.KEY_RMENU);
		disallowedSymbols.add(Keyboard.KEY_RSHIFT);
		disallowedSymbols.add(Keyboard.KEY_RWIN);
		disallowedSymbols.add(Keyboard.KEY_SCROLL);
		disallowedSymbols.add(Keyboard.KEY_SLEEP);
		disallowedSymbols.add(Keyboard.KEY_STOP);
		disallowedSymbols.add(Keyboard.KEY_SYSRQ);
		disallowedSymbols.add(Keyboard.KEY_TAB);
		disallowedSymbols.add(Keyboard.KEY_UP);
		
	}
	
	public static void pollTextInput(){
		if (Console.enabled){
			while (next()){
				int key = getEventKey();
				boolean down = getEventKeyState();
				//boolean repeat = isRepeatEvent(); // I might need this at some point...
				char c = getEventCharacter();
				if (down){
					if (!disallowedSymbols.contains(key)){
						currentText += c;
					}
					else if (key == Keyboard.KEY_BACK && currentText.length() > 0)
						currentText = currentText.substring(0, currentText.length() - 1);
				}
			}
		}
	}

	public static void draw(){
		//Console log
		glEnable(GL_BLEND);
		glBegin(GL_QUADS);
		glColor4f(.5f,.5f, .5f, .32f);
		glVertex2f(20, 20);
		glVertex2f(Display.getWidth() - 20, 20);
		glVertex2f(Display.getWidth()-20, 300);
		glVertex2f(20, 300);
		glEnd();

		// text entry section
		glEnable(GL_BLEND);
		glBegin(GL_QUADS);
		if (focused)
			glColor4f(.7f,.7f, .7f, .85f);
		else
			glColor4f(.6f,.6f, .6f, .85f);
		glVertex2f(30, 265);
		glVertex2f(Display.getWidth() - 30, 265);
		glVertex2f(Display.getWidth()-30, 290);
		glVertex2f(30, 290);
		glEnd();

		// cursor
		if (focused && System.currentTimeMillis() % 1000 <= 500){
			glBegin(GL_LINES);
			glColor3f(0f, 0f, 0f);
			glVertex2f(37 + GraphicsHandler.font.getWidth(currentText), 267);
			glVertex2f(37 + GraphicsHandler.font.getWidth(currentText), 288);
			glEnd();
		}

		// string in text box
		GraphicsHandler.font.drawString(35, 267, currentText);

	}
}
