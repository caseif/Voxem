package mineflat.util;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;

import mineflat.MineFlat;

public class MiscUtil {

	private static final float charWHRatio = 3f / 4f;

	// space between characters when height is 32px
	private static final float interCharSpace = 0.1f;
	
	// offset of shadows (duh)
	private static final float shadowOffset = 1;

	// why the hell did I think it was a good idea to have this return a long?
	public static long getTime(){
		//return (Sys.getTime() * 1000) / Sys.getTimerResolution();
		return System.nanoTime();
	}

	public static float getTimeResolution(){
		//return Sys.getTimerResolution();
		return 1000000000;
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

	public static void drawString(String text, float x, float y, float height, boolean shadow){
		float wm = 42f + 2f / 3f;
		float hm = 4f;
		float width = height * charWHRatio;
		glPushMatrix();
		glEnable(GL_BLEND);
		glBindTexture(GL_TEXTURE_2D, MineFlat.charTexture.getTextureID());
		for (int i = 0; i <= (shadow ? 1 : 0); i++){
			if (i == 0 && shadow) {
				glColor3f(0f, 0f, 0f);
				x -= shadowOffset;
				y -= shadowOffset;
			}
			else if (i == 1){
				glColor3f(1f, 1f, 1f);
				x += shadowOffset;
				y += shadowOffset;
			}
			else
				glColor3f(1f, 1f, 1f);
			glBegin(GL_QUADS);
			float pos = 0f;
			for (char c : text.toCharArray()){
				float tx = 25f, ty = 3f;
				if (Character.isAlphabetic(c)){
					if (Character.isUpperCase(c)){
						tx = c - 'A';
						ty = 0;
					}
					else {
						tx = c - 'a';
						ty = 0; // temporary until I remember to add lowercase characters to the image
					}
				}
				else if (isInt(Character.toString(c))){
					tx = Float.parseFloat((Character.toString(c)));
					ty = 2f;
				}
				else if (c == ' '){
					pos += 1;
					continue;
				}
				else {
					ty = 3f;
					switch (c){
					case '!': tx = 0f; break;
					case '?': tx = 1f; break;
					case '.': tx = 2f; break;
					case ',': tx = 3f; break;
					case ':': tx = 4f; break;
					case '-': tx = 5f; break;
					case '+': tx = 6f; break;
					default: tx = 25f; break;
					}
				}
				glTexCoord2f(tx / wm, (ty / hm));
				glVertex2f(x + pos * width, y);
				glTexCoord2f((tx + 1f) / wm, (ty / hm));
				glVertex2f(x + pos * width + width, y);
				glTexCoord2f((tx + 1f) / wm, (ty + 1f) / hm);
				glVertex2f(x + pos * width + width, y + height);
				glTexCoord2f(tx / wm, (ty + 1f) / hm);
				glVertex2f(x + pos * width, y + height);
				if (c != '.' && c != ',' && c != ':')
					pos += 1f + (height / 32f) * interCharSpace;
				else
					pos += (1f / 6f) + interCharSpace;
			}
		}
		glEnd();
		glDisable(GL_BLEND);
		glBindTexture(GL_TEXTURE_2D, 0);
		glPopMatrix();
	}

	public static boolean isInt(String s){
		try { Integer.parseInt(s); return true; }
		catch (NumberFormatException ex){ return false; }
	}

}
