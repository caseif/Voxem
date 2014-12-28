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
package com.headswilllol.mineflat.event.player;

import com.headswilllol.mineflat.entity.Human;
import com.headswilllol.mineflat.entity.Player;
import com.headswilllol.mineflat.event.Event;
import com.headswilllol.mineflat.event.human.HumanEvent;
import com.headswilllol.mineflat.event.human.HumanMoveEvent;

public class PlayerEvent extends HumanEvent {

	/**
	 * The player involved in this event.
	 */
	protected Player entity;

	@Override
	public Player getEntity(){
		return entity;
	}

	public void setEntity(Player entity){
		this.entity = entity;
	}

	@Override
	public void setEntity(Human entity){
		if (entity instanceof Player)
			super.setEntity(entity);
		else
			throw new IllegalArgumentException("Entity must be a player!");
	}
	
}
