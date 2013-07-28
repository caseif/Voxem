package mineflat.util;

public class MiscUtil {

	public static long getTime(){
		//return (Sys.getTime() * 1000) / Sys.getTimerResolution();
		return System.nanoTime() / 1000000;
	}
	
	public static long getTimeResolution(){
		//return Sys.getTimerResolution();
		return 1000;
	}
	
	public static int nextPowerOfTwo(int i){
		String binary = Long.toBinaryString(i);
		int power = binary.length() - binary.indexOf("1");
		return (int)Math.pow(2, power);
	}
	
}
