package my.com.util;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;

import com.google.common.base.Throwables;

public class DES3 {
	private static final Cipher DES_CIPHER;

	static {
		try {
			DES_CIPHER = Cipher.getInstance("DESede/ECB/PKCS5Padding");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw Throwables.propagate(e);
		}
	}

	public static String encryptDES(String encryptString, String encryptKey) {
		try {
			DES_CIPHER.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes("UTF-8"), "DESede"));

			byte[] encryptedData = DES_CIPHER.doFinal(encryptString.getBytes("UTF-8"));
			String hexData = Hex.encodeHexString(encryptedData).toUpperCase();
			return StringUtils.leftPad(String.valueOf(hexData.length()), 6, '0') + hexData;
		} catch (Throwable e) {
			throw Throwables.propagate(e);
		}
	}

	public static String decryptDES(String decryptString, String decryptKey) {
		try {
			DES_CIPHER.init(Cipher.DECRYPT_MODE, encrypt3DESECB(decryptKey.getBytes(), null));
			byte[] decryptedData = DES_CIPHER.doFinal(Hex.decodeHex(decryptString.substring(6).toCharArray()));
			return new String(decryptedData, "UTF-8");
		} catch (Throwable e) {
			throw Throwables.propagate(e);
		}
	}

	public static String base64(String encodedText) throws UnsupportedEncodingException {
		final Base64.Decoder decoder = Base64.getDecoder();
		return new String(decoder.decode(encodedText), "utf-8");
	}

	public static SecretKeySpec encrypt3DESECB(byte[] keyBytes, byte[] dataBytes) {
		SecretKeySpec newKey = null;
		try {
			if (keyBytes.length == 16) { // short key ? .. extend to 24 byte key
				byte[] tmpKey = new byte[24];
				System.arraycopy(keyBytes, 0, tmpKey, 0, 16);
				System.arraycopy(keyBytes, 0, tmpKey, 16, 8);
				keyBytes = tmpKey;
			}
			newKey = new SecretKeySpec(keyBytes, "DESede");
		} catch (Exception e) {
		}
		return newKey;
	}

	public static void main(String[] agr) {
		String encodedText = "9Uf2LhSqGF1Y+mrW2YrXuMXhm5J6+zHTxIAvs0Job5ayUkYsvm0RHb0DxIOcQbvuN"
				+ "H0S4DPQIhwb23/W8KHezmLBqam79Qrx"
				+ "9oqLHUZcgdVlG6gDu4/QGroN0MGGjziryDS9+yqyjLcLvMmebTlmFpt2u5a2ULFarpScKYOht"
				+ "lhUOi81XtaUIPINy+uPenvGP49ddodFlg8K5h7Mul5l"
				+ "KEg2WBMvVFBvAlNJB02SVl4d4h/EY/gXujPy+FR0X9dn9oqLHUZcgdWlx/gzclSAU1JCCiW"
				+ "dKjdZ+Yrh/wY8t62yP9mUJMj8ASIG7Ab8UEa51oBf"
				+ "JFtiB1g06YvBj57CjmVS51Bg/by7euTp2bFuu2BaOUdfaTdcq04FPXZD6xDbnJZDWjo4uozPOS"
				+ "D6liE7w6mREcRjUeR5eBHJQsCu9/2l3iQkZSRwSFO"
				+ "jwFdN8weESPa2csMX/DlwkQHulIAHWHYEZsKzzXBQ5BwiFhh0MtYx44QFWX2f2d181mC"
				+ "MMqyYXuhvbBGvorxfrJBDuRTncseR0eG7B7Ogl8hc6"
				+ "rreZwKouCsmNI6xDUPLmsRNFLFqyseRCnh66OXxnp4vs4XeeZn9Zc0ZEmqf+NyiR5aRyBNa"
				+ "8GS3J6kvi7CJpsaaI3WzRTkBXiuVjt0z/nGStN24nFMHgBZ1NNNnBA0insZpMgERRu3tn9s6jQ==";
		try {
			String str = base64(encodedText);
			System.out.println(str);
			String key = "310103198704176033";
			String ndnd = "{\"type\":\"1\",\"customerIdentityNo\":\"310103198704176033\","
					+ "\"customerName\":\"TEST01\","
					+ "\"phone\":\"15021269056\",”cardNo”:”1001010100”,”vid”:”100000000”}";
			String key1 = "􀁛􀁛􀁛􀁛􀁛􀁛􀁛􀁛􀁛";
			System.out.println(encryptDES(ndnd,  key));
			// System.out.println(decryptDES(str, key));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
