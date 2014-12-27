package com.headswilllol.mineflat.util;

import com.headswilllol.mineflat.vector.Vector4f;

public class MiscUtil {

	public static Vector4f hexToRGBA(String hex){
		if (hex.startsWith("#"))
			hex = hex.replace("#", "");
		int r = 0;
		int g = 0;
		int b = 0;
		int a = 0;
		if (hex.length() == 3 || hex.length() == 4){
			r = Integer.parseInt(hex.substring(0, 1) + hex.substring(0, 1), 16);
			g = Integer.parseInt(hex.substring(1, 2) + hex.substring(1, 2), 16);
			b = Integer.parseInt(hex.substring(2, 3) + hex.substring(2, 3), 16);
			if (hex.length() == 4)
				a = Integer.parseInt(hex.substring(3, 4) + hex.substring(3, 4));
			else
				a = 255;
			return new Vector4f(r / 255f, g / 255f, b / 255f, a / 255f);
		}
		else if (hex.length() == 6 || hex.length() == 8){
			r = Integer.parseInt(hex.substring(0, 1) + hex.substring(1, 2), 16);
			g = Integer.parseInt(hex.substring(2, 3) + hex.substring(3, 4), 16);
			b = Integer.parseInt(hex.substring(4, 5) + hex.substring(5, 6), 16);
			if (hex.length() == 8)
				a = Integer.parseInt(hex.substring(6, 7) + hex.substring(7, 8));
			else
				a = 255;
			return new Vector4f(r / 255f, g / 255f, b / 255f, a / 255f);
		}
		else
			throw new IllegalArgumentException("Invalid hex color!");
	}

}
