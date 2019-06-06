package my.com.util;

import java.util.Random;

public class RandomUtil {
	private static String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";     
	public static String getRandomString(int length) { //length表示生成字符串的长度  
	    Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < length; i++) {     
	        int number = random.nextInt(base.length());     
	        sb.append(base.charAt(number));     
	    }     
	    return sb.toString();     
	 }
	
	
	public static String getRandomCode(int n) {
		String noteCode = "";
		Random random = new Random();
		int number = 0;
		for (int i = 0; i < n; i++) {
			number = random.nextInt(10);
			noteCode += Integer.toString(number);
		}

		return noteCode;
	}
}
