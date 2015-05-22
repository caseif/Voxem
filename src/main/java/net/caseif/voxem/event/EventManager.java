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
package net.caseif.voxem.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EventManager {

	public static final List<ListenerData> listeners = new ArrayList<ListenerData>();

	/**
	 * Registers a given class as an event listener.
	 * This is mandatory in order for its methods to be notified of events.
	 * @param l The class implementing Listener which should be notified of events
	 */
	public static void registerEventListener(Listener l){
		for (Method m : l.getClass().getMethods()){
			if (m.isAnnotationPresent(EventHandler.class)){
				for (Class<?> cl : m.getParameterTypes()){
					if (cl.getSuperclass() != null){
						if (cl.getSuperclass() == Event.class ||
								(cl.getSuperclass().getSuperclass() != null && 
								cl.getSuperclass().getSuperclass() == Event.class)){
							String[] params = new String[m.getParameterTypes().length];
							for (int i = 0; i < m.getParameterTypes().length; i++)
								params[i] = m.getParameterTypes()[i].getPackage().getName() +
								"." + m.getParameterTypes()[i].getSimpleName();
							ListenerData pair = new ListenerData(
									l, m.getName(), cl.getPackage().getName() + "." +
											cl.getSimpleName(), params);
							listeners.add(pair);
							break;
						}
						else
							System.err.println("The event handler method " + m.getName() +
									" in class " + l.getClass().getSimpleName() +
									" could not be registered" +
									" as an event listener because " + cl.getSimpleName() +
									" does not extend Event");
					}
					else
						System.err.println("The event handler method " + m.getName() +
								" in class " + l.getClass().getSimpleName() +
								" could not be registered" +
								" as an event listener because " + cl.getSimpleName() +
								" is an invalid type (primitive/interface/void)");
				}
			}
		}
	}

}
