package my.com.util;

import java.util.UUID;

public abstract class UUIDUtil {

	/**
	 * 获取UUID
	 * 
	 * @return
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public static void main(String[] str) {
/*		Double zz = 0d;
		Double a = null;

		zz = a;
		zz += 1D;
		System.out.println(zz);*/
	  System.out.println(UUIDUtil.getUUID());
	}

}
