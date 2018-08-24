package my.com.Util;

public class StringUtil {
	/**
	 * 注册用户名限制
	 * @param name
	 * @return true 通过，false不通过
	 */
	public static boolean nameLimit(String name) {
		return name != null && !name.contains(" ") && name.length() > 5 && name.length() < 13;
	}
	/**
	 * 密码限制
	 * @param name
	 * @return true 通过，false不通过
	 */
	public int passLimit(String pass,String pass1) {
       if (pass==null||pass.replace(" ", "").equals("")) {
		return 1;//密码不能为空
	}else if (pass.contains(" ")) {
		return 2;//密码不能包含空格
	}else if (pass.length()<4||pass.length()>30) {
		return 3;//密码至少6位字符；
	}else if (!pass.equals(pass1)) {
		return 4;//密码不一致；
	}else if (!mixture(pass)) {
		return 5;//密码必须包含数字和字母
	}
       return 5;
	}
	public boolean mixture(String pass){
		char[] str="qwertyuioplkjhgfdsazxcvbnm".toCharArray();
		char[]num="1234567890".toCharArray();
		for (int i = 0; i < num.length; i++) {
			if (!pass.contains(num[i]+"")) {
				return false;
			}
			}
		for (int i = 0; i < str.length; i++) {
			if (!pass.contains(str[i]+"")) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNullOrEmpty(String str){
		return null == str || str.trim().equals(""); 
	}
}
