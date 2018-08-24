package my.com.Util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Constant {

	private static Log logger = LogFactory.getLog(Constant.class);
	 
	public static String  LOGINEXCEPTION ="网络异常登入失败！";
	
	 
	private static Properties webConfigProperties = null;

	static {
		InputStream is = null;
		try {
			webConfigProperties = new Properties();
			is = Constant.class.getClassLoader().getResourceAsStream("config/common.properties");
			webConfigProperties.load(is);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	/**
	 * 读取文件路径
	 * 
	 * @throws IOException
	 */
	public static String readFilePath(String key) {
		return webConfigProperties.getProperty(key);
	}

	public static void main(String[] agr) {

	}

}
