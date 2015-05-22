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
package net.caseif.voxem.threading;

public class Task {

	private static int nextId = 0;

	private int id;
	private Runnable runnable;
	private long timeScheduled;
	private int delay;

	Task(Runnable runnable, int delay){
		this.runnable = runnable;
		this.delay = delay;
		this.timeScheduled = System.currentTimeMillis();
		this.id = nextId;
		nextId += 1;
	}

	Task(Runnable runnable){
		this(runnable, 0);
	}

	public int getTaskId(){
		return id;
	}

	public Runnable getRunnable(){
		return runnable;
	}

	public long getTimeScheduled(){
		return timeScheduled;
	}

	public int getDelay(){
		return delay;
	}

}
