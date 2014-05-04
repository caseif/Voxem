package com.headswilllol.mineflat;

import java.util.concurrent.locks.LockSupport;

public class Timing {
	
	public static long timeResolution = 1000000000L;

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

	private static long starttime = System.currentTimeMillis();

	// why the hell did I think it was a good idea to have this return a long?
	public static long getTime(){
		return System.nanoTime();
	}
	
	public static void calculateDelta(){
		time = getTime();
		delta = time - lastTime;
		lastTime = time;
	}
	
	public static void throttleCpu(){
		starttime += (1000.0 / 60); 
		LockSupport.parkNanos((long)(Math.max(0, starttime - System.currentTimeMillis()) * 1000000));
	}

}
