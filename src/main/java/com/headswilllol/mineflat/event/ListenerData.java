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
