package mineflat.util;

public class NumUtil {

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

	public static boolean isInt(String s){
		try { Integer.parseInt(s); return true; }
		catch (NumberFormatException ex){ return false; }
	}

}
