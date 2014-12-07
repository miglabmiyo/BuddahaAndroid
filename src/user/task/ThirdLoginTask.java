package user.task;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import user.login.MyUser;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import basic.net.ApiDefine;
import basic.net.ApiRequest;
import basic.util.MyLog;

/**
 * @author song
 * @version 创建时间：2014-12-7 下午1:51:56 类说明 第三方登录
 */
public class ThirdLoginTask extends AsyncTask<Void, Void, Void> {
	String TAG = "ThirdLoginTask";
	Handler handler;

	public ThirdLoginTask(Handler tHandler) {
		this.handler = tHandler;
	}

	@Override
	protected Void doInBackground(Void... params) {
		MyLog.v(TAG, "doInBackground handler: " + handler);
		if (handler == null)
			return null;

		try {
			String request = request();
			MyLog.v(TAG, "request: " + request);
			if (TextUtils.isEmpty(request)) { // 超时
				handler.sendEmptyMessage(ApiDefine.ERROR_TIMEOUT);
				return null;
			}

			int code = 0;
			String errorMsg = ApiDefine.ERRORMSG_UNKNOWN;
			try {
				JSONObject json = new JSONObject(request);
				if (json != null) {
					code = json.optInt("status");
					MyLog.v(TAG, "status: " + code);
					if (code == 1) {
						JSONObject jresult = json.optJSONObject("result");
						MyLog.v(TAG, "jresult: " + jresult);
						if (jresult != null) {
							MyUser user = MyUser.getInstance();
							user.loginSet(jresult.optJSONObject("userinfo"));
							handler.sendEmptyMessage(ApiDefine.LOGIN_SUCCESS);
							return null;
						}

					}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			handler.sendMessage(handler.obtainMessage(
					code == 0 ? ApiDefine.ERROR_UNKNOWN : code, errorMsg));

		} catch (Exception e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}

		return null;
	}

	String request() {
		String url = ApiDefine.DOMAIN + ApiDefine.OTHER_LOGIN;
		MyUser user = MyUser.getInstance();
		Map<String, String> params = new HashMap<String, String>();
		params.put("machine", "" + 1);
		params.put("nickname", user.nickname);
		params.put("source", "" + user.type);
		params.put("session", "" + user.id);
		if (!TextUtils.isEmpty(user.imei))
			params.put("imei", user.imei);
		params.put("sex", "" + user.gender);
		if (user.birthday > 0)
			params.put("birthday", "" + user.birthday);
		if (!TextUtils.isEmpty(user.location))
			params.put("location", user.location);
		if (!TextUtils.isEmpty(user.headUrl))
			params.put("head", user.headUrl);

		return ApiRequest.postRequest(url, params);
	}

}
