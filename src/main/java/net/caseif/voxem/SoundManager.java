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
package net.caseif.voxem;

import java.io.File;
import java.net.MalformedURLException;

import net.caseif.voxem.util.FileUtil;
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
			soundSystem.backgroundMusic("Infinity",
					new File(FileUtil.getAppDataFolder() +
							"/.voxem/resources/audio/soundtrack", "infinity.ogg").toURI().toURL(), "ogg", true);
		}
		catch (MalformedURLException ex){
			ex.printStackTrace();
			System.err.println("Failed to initialize audio stream!");
		}
	}

	public static void generateMeme(){
		try {
			soundSystem.stop("Infinity");
			soundSystem.backgroundMusic("Sandstorm",
					new File(FileUtil.getAppDataFolder() +
							"/.voxem/resources/audio/soundtrack", "dss.ogg").toURI().toURL(), "ogg", true);
		}
		catch (MalformedURLException ex){
			ex.printStackTrace();
			System.err.println("Failed to initialize audio stream!");
		}
	}

}
