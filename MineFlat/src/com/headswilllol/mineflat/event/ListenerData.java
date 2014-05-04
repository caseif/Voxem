package com.headswilllol.mineflat.event;

import java.lang.reflect.Method;

/**
 * Stores data about each method which listens to an event.
 */
public class ListenerData {

	public Listener listener;
	public String method;
	public String event;
	public String[] params;
	
	/**
	 * Stores data about each method which listens to an event.
	 * @param listener The instance of the listener class which registered the event.
	 * @param method The name of the method which will listen to the event.
	 * @param event The path of the class which is equivalent to the event being fired.
	 * @param params The parameter types which the method requires (this differs from event in that the method may technically listen to multiple events or simply require arbitrary variables).
	 */
	public ListenerData(Listener listener, String method, String event, String[] params){
		this.listener = listener;
		this.method = method;
		this.event = event;
		this.params = params;
	}
	
	public String[] getParams() {
		return params;
	}

	public void setParams(String[] params) {
		this.params = params;
	}

	public Listener getListener(){
		return listener;
	}
	
	public void setListener(Listener listener){
		this.listener = listener;
	}
	
	public String getMethodName(){
		return method;
	}
	
	public void setMethodName(String method){
		this.method = method;
	}
	
	public String getEvent(){
		return event;
	}
	
	public void setEvent(String event){
		this.event = event;
	}
	
	public Class<?> getEventClass(){
		try {
			return Class.forName(event);
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	public void setMethod(String method) {
		this.method = method;
	}

	public Method getMethod(){
		try {
			Class<?>[] c = new Class<?>[params.length];
			for (int i = 0; i < params.length; i++)
				c[i] = Class.forName(params[i]);
			return getListener().getClass().getMethod(method, c);
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
}
