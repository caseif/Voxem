package mineflat.util;

import org.lwjgl.Sys;

public class MiscUtil {

	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	public static int nextPowerOfTwo(int i){
		String binary = Long.toBinaryString(i);
		int power = binary.length() - binary.indexOf("1");
		return (int)Math.pow(2, power);
	}
	
}
