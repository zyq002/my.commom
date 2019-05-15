package my.com.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
	private Long id;
	private String userName;
	private String realName;
	private String status;
	private String userCode;
	private String iphone;

	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", realName=" + realName + ", status=" + status
				+ ", userCode=" + userCode + ", iphone=" + iphone + "]";
	}

}
