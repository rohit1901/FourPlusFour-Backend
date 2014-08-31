package dto;

import java.sql.Blob;

public class usercredential {
	private String userid;
	private String password;
	private String source;
	private Blob userInfo;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Blob getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(Blob userInfo) {
		this.userInfo = userInfo;
	}

}
