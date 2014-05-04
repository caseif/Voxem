package com.headswilllol.mineflat.event.input;

import com.headswilllol.mineflat.Button;
import com.headswilllol.mineflat.event.Event;

public class ButtonClickEvent extends Event {
	
	private int handle;
	
	public ButtonClickEvent(int handle){
		this.handle = handle;
	}
	
	/**
	 * @return The handle of the button involved in this event
	 */
	public int getHandle(){
		return handle;
	}
	
	/**
	 * @return The button involved in this event
	 */
	public Button getButton(){
		return Button.getButton(handle);
	}

}
