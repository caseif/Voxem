package mineflat.event.input;

import mineflat.event.Event;

public class KeyPressEvent extends Event {
	
	private int key;
	private char c;
	
	public KeyPressEvent(int key, char c){
		this.key = key;
		this.c = c;
	}
	
	public int getKey(){
		return key;
	}
	
	public char getChar(){
		return c;
	}
	
}
