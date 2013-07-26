package mineflat.event;

import java.lang.reflect.Method;

public class Event {

	/**
	 * Fires an event which all registered listener classes will be notified of.
	 * @param e The event which will be fired. Only methods listening to the event type provided will be notified.
	 */
	public static void fireEvent(Event e){
		for (ListenerData ep : EventManager.listeners){
			Method m = ep.getMethod();
			String[] eventArray = ep.getEvent().split("\\.");
			for (int i = 0; i < eventArray.length; i++){
				if (eventArray[i].equals(e.getClass().getSimpleName())){
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
