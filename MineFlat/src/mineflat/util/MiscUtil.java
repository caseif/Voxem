package mineflat.util;

import java.io.File;

import mineflat.MineFlat;

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

	public static byte[] hexToByte(String hex){
		String s;
		byte[] b = new byte[hex.length() / 2];
		int i;
		for (i = 0; i < hex.length() / 2; i++) {
			s = hex.substring(i * 2, i * 2 + 2);
			b[i] = (byte)(Integer.parseInt(s, 16) & 0xff);
		}
		return b;
	}

	public static String getAppDataFolder(){
		String OS = System.getProperty("os.name").toUpperCase();
		if (OS.contains("WIN"))
			return System.getenv("APPDATA");
		else if (OS.contains("MAC"))
			return System.getProperty("user.home") + "/Library/Application Support";
		try {return new File(MineFlat.class.getProtectionDomain()
				.getCodeSource().getLocation().toURI().getPath()).getParent();}
		catch (Exception ex){ex.printStackTrace();}
		return System.getProperty("user.dir");
	}

}
