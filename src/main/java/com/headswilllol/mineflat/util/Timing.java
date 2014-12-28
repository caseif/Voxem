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
package com.headswilllol.mineflat.util;

import java.util.concurrent.locks.LockSupport;

public class Timing {
	
	public static final long TIME_RESOLUTION = 1000000000L;

	/**
	 * The variable used to determine the duration of each iteration so as to move ingame objects
	 * at a constant speed
	 */
	public static float delta = 0;
	public static double displayDelta = 0;

	/**
	 * Used in the calculation of delta
	 */
	public static long time = getTime();

	/**
	 * Used in the calculation of delta
	 */
	public static long lastTime = getTime();

	private static long starttime = getTime();

	public static long getTime(){
		return System.nanoTime();
	}
	
	public static void calculateDelta(){
		time = getTime();
		delta = time - lastTime;
		lastTime = time;
	}
	
	public static void throttleCpu(){
		starttime += ((1000 / 60) * TIME_RESOLUTION / 1000);
		LockSupport.parkNanos((Math.max(0, starttime - getTime() + 1)));
	}

}
