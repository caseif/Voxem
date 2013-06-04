package mineflat.util;

import java.util.Random;

public class NoiseUtil {

	public static float noise(){
		Random r = new Random();
		float mult = -1;
		if (r.nextInt(1) == 1)
			mult = 1;
		return r.nextFloat() * mult;
	}

	public static float smoothNoise(float x){
		return (noise() / 2 + noise() / 4  +  noise() / 4);
	}

	public static float interpolate(float a, float b, float x){
		double ft = x * Math.PI;
		float f = (float)((1 - Math.cos(ft)) * .5);
		return  a * (1 - f) + b * f;
	}

	public static float interpolatedNoise(float x){

		float integerX = x;
		float frX = x - integerX;

		float v1 = smoothNoise(integerX);
		float v2 = smoothNoise(integerX + 1);

		return interpolate(v1 , v2 , frX);

	}


	public static int perlin(float x){

		float total = 0;
		float p = 1/8;
		float n = 7;

		for (float i = 0; i <= n; i++){

			float frequency = (float)Math.pow(2, (double)i);
			float amplitude = (float)Math.pow((double)p, (double)i);

			total = Math.abs(total + interpolatedNoise(x * frequency) * amplitude * 16);

		}

		return (int)total;

	}
}
