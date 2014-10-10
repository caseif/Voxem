package com.headswilllol.mineflat.event.input;

import com.headswilllol.mineflat.gui.Button;
import com.headswilllol.mineflat.event.Event;

public class ButtonClickEvent extends Event {
	
	private final Button button;
	
	public ButtonClickEvent(Button button){
		this.button = button;
	}
	
	/**
	 * @return The button involved in this event
	 */
	public Button getButton(){
		return button;
	}

}
