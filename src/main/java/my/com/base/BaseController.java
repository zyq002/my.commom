package my.com.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.remote.entity.dto.user.UserDto;

public abstract class BaseController {

	public Logger logger = Logger.getLogger(BaseController.class);

	@Autowired
	HttpServletRequest request;

	@Autowired
	HttpServletResponse response;

	/**
	 * http相关
	 * 
	 */
	protected String base() {
		return request.getContextPath();
	}

	protected HttpServletRequest request() {
		return request;
	}

	protected HttpServletResponse response() {
		return response;
	}

	/**
	 * 获取用户IP
	 * 
	 */
	public String ip() {
		return null;
	}

	/**
	 * 获取用户信息
	 * 
	 */
	public UserDto user() {
		return null;
	}

	/**
	 * Cookie相关
	 * 
	 */
	protected void addCookie(String name, String value, int age) {

	}

	protected String getCookie(String name) {
		return null;
	}

	/**
	 * Session 相关操作 getSession();putSession();removeSession();
	 * 
	 */
	protected void getSession() {
		request.getSession();
	}

	protected void removeSession(String name) {
		request.getSession().removeAttribute(name);
		;
	}

	protected void putSession(String key, Object value) {
		request.getSession().setAttribute(key, value);
	}
}