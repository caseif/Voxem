package mineflat.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EventManager {

	public static List<ListenerData> listeners = new ArrayList<ListenerData>();

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
