package my.com.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Constant {

	private static Log logger = LogFactory.getLog(Constant.class);

	public static final String SYSTEM_STATUS_CODE_SUCCESS = "200";

	public static final String SYSTEM_STATUS_CODE_ERROR = "500";

	public static final String SYSTEM_STATUS_CODE_EXCEPTION = "400";

	public static final String SYSTEM_STATUS_CODE_AUTHORITY = "401";

	public static final String SYSTEM_MESSAGE_SUCCESS = "SUCCESS";
	public static final String SYSTEM_MESSAGE_AUTHORITY = "权限异常";
	public static final String SYSTEM_MESSAGE_EXCEPTION = "参数异常";
	public static final String SYSTEM_MESSAGE_ERROR = "接口异常";
}
