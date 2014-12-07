package user.login;

import org.json.JSONObject;

/**
 * @author song
 * @version 创建时间：2014-11-27 下午8:13:59 类说明
 */
public class MyUser extends Account {
	public long userId; //自己平台的id
	public String requestToken;
	
	static MyUser user = new MyUser();

	private MyUser() {
	};

	public static MyUser getInstance() {
		return user;
	}

	public void loginSet(JSONObject json) {
		if (json != null) {
			userId = json.optLong("uid");
			requestToken = json.optString("token");
			location = json.optString("address");
			
		}
	}

	public boolean isLogin() {
		return userId > 0 && type > 0;
	}
}
