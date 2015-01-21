package user.login;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import basic.util.AppStatus;
import basic.util.MyLog;

/**
 * @author song
 * @version 创建时间：2014-11-27 下午8:13:59 类说明
 */
public class MyUser extends Account {
	public long userId; // 自己平台的id
	public String requestToken;
	public long time;

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
			time = json.optLong("time");
			location = json.optString("address");
			if (type == 0)
				nickname = json.optString("nickname");
			writeRecord();
		}
	}

	public boolean isLogin() {
		return userId > 0 && type > 0;
	}

	public static String getApiBasicParams() {
		return "?uid=" + user.userId + "&token=" + user.requestToken;
	}

	public void readRecord(Context con) {
		if (userId <= 0 && con != null) {
			SharedPreferences sharedPrefs = con.getSharedPreferences("myuser",
					Context.MODE_PRIVATE);
			if (sharedPrefs != null) {
				userId = sharedPrefs.getLong("userId", 0);
				requestToken = sharedPrefs.getString("requestToken", "");
				type = sharedPrefs.getInt("type", 0);
				id = sharedPrefs.getString("id", "");
				token = sharedPrefs.getString("token", "");
				nickname = sharedPrefs.getString("nickname", "");
				gender = sharedPrefs.getInt("gender", 0);
				headUrl = sharedPrefs.getString("headUrl", "");
				birthday = sharedPrefs.getInt("birthday", 0);
				location = sharedPrefs.getString("location", "");
				imei = sharedPrefs.getString("imei", "");
				expires = sharedPrefs.getLong("expires", 0);

				MyLog.d("读账户--" + toString());
			}
		}
	}

	public void writeRecord() {
		if (userId > 0 && !TextUtils.isEmpty(requestToken)
				&& AppStatus.frontActivity != null) {
			SharedPreferences sharedPrefs = AppStatus.frontActivity
					.getSharedPreferences("myuser", Context.MODE_PRIVATE);

			SharedPreferences.Editor editor = sharedPrefs.edit();
			editor.putLong("userId", userId);
			editor.putString("requestToken", requestToken);
			editor.putInt("type", type);
			editor.putString("id", id);
			editor.putString("token", token);
			editor.putString("nickname", nickname);
			editor.putInt("gender", gender);
			editor.putString("headUrl", headUrl);
			editor.putInt("birthday", birthday);
			editor.putString("location", location);
			editor.putString("imei", imei);
			editor.putLong("expires", expires);
			editor.commit();

			MyLog.d("写账户--" + toString());
		}
	}

	@Override
	public String toString() {
		return "MyUser [userId=" + userId + ", requestToken=" + requestToken
				+ ", time=" + time + ", type=" + type + ", id=" + id
				+ ", token=" + token + ", nickname=" + nickname + ", gender="
				+ gender + ", headUrl=" + headUrl + ", birthday=" + birthday
				+ ", location=" + location + ", imei=" + imei + ", expires="
				+ expires + "]";
	}

}
