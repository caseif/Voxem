package com.headswilllol.mineflat.event.input;

import com.headswilllol.mineflat.event.Event;

public class KeyPressEvent extends Event {
	
	private final int key;
	private final char c;
	
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
