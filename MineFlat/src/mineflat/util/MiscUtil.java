package mineflat.util;

import org.lwjgl.Sys;

public class MiscUtil {

	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
}
