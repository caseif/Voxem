package com.headswilllol.mineflat.util;

import java.io.File;

import com.headswilllol.mineflat.Main;

public class FileUtil {

	public static String getAppDataFolder(){
		String OS = System.getProperty("os.name").toUpperCase();
		if (OS.contains("WIN"))
			return System.getenv("APPDATA");
		else if (OS.contains("MAC"))
			return System.getProperty("user.home") + "/Library/Application Support";
		try {return new File(Main.class.getProtectionDomain()
				.getCodeSource().getLocation().toURI().getPath()).getParent();}
		catch (Exception ex){ex.printStackTrace();}
		return System.getProperty("user.dir");
	}
	
}
