package com.headswilllol.mineflat;

import java.io.File;
import java.net.MalformedURLException;

import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;

public class SoundManager {

	static SoundSystem soundSystem = null;

	public static void initialize(){
		try {
			SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
			SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
		}
		catch (SoundSystemException ex){
			System.err.println("An exception occurred while linking the Codec-JOrbis plugin");
		}
		soundSystem = new SoundSystem();

		try {
			if (!Main.class.getProtectionDomain().getCodeSource().getLocation().getPath().contains("git")) // this makes my life a bit easier while testing
				soundSystem.backgroundMusic("Infinity",
						new File(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile().getPath() +
								"/audio/soundtrack/infinity.ogg").toURI().toURL(), "ogg", true);
			else
				soundSystem.backgroundMusic("Infinity",
						new File("C:/Users/Maxim/AppData/Roaming/.mineflat/resources/audio/soundtrack/infinity.ogg").toURI().toURL(), "ogg", true);
		}
		catch (MalformedURLException ex){
			ex.printStackTrace();
			System.err.println("Failed to initialize audio stream!");
		}
	}

}
