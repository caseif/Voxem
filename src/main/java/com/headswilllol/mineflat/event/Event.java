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
package com.headswilllol.mineflat.event;

import java.lang.reflect.Method;

public class Event {

	/**
	 * Fires an event which all registered listener classes will be notified of.
	 * @param e The event which will be fired. Only methods listening to the event type provided will be notified.
	 */
	public static void fireEvent(Event e){
		for (ListenerData ep : EventManager.listeners){
			Method m = ep.getMethod();
			for (String event : ep.getEvent().split("\\.")){
				if (event.equals(e.getClass().getSimpleName())){
					try {
						m.invoke(ep.getListener(), e);
					}
					catch (Exception ex){
						ex.printStackTrace();
						System.err.println("Failed to invoke method " + m.getName() +
								" for event " + e.getClass().getSimpleName());
					}
				}
			}
		}
	}

}
