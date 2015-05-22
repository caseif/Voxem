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
package net.caseif.voxem.event.player;

import net.caseif.voxem.world.Location;
import net.caseif.voxem.entity.living.player.Player;
import net.caseif.voxem.event.Cancellable;
import net.caseif.voxem.event.Event;
import net.caseif.voxem.event.human.HumanMoveEvent;

/**
 * Fired when a player moves from one point in space to another.
 */
public class PlayerMoveEvent extends HumanMoveEvent implements Cancellable {
	
	/**
	 * Fired when the player moves from one point in space to another.
	 * @param player The player involved in this event.
	 * @param to The location the player is moving to.
	 * @param from The location of the player before this event was fired.
	 */
	public PlayerMoveEvent(Player player, Location to, Location from){
		super(player, to, from);
		Event.fireEvent((HumanMoveEvent)this);
	}
	
}
