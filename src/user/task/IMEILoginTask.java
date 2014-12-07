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
 * @version 创建时间：2014-11-27 下午7:37:03 类说明 默认登录
 */
public class IMEILoginTask extends AsyncTask<Void, Void, Void> {
	String TAG = "IMEILoginTask";
	Handler handler;
	String imei;

	public IMEILoginTask(Handler tHandler, String imei) {
		this.handler = tHandler;
		this.imei = imei;
	}

	@Override
	protected Void doInBackground(Void... params) {
		MyLog.v(TAG, "imei: " + imei + ", handler: " + handler);
		if (imei == null || handler == null)
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
						if (jresult != null) {
							MyUser user = MyUser.getInstance();
							user.loginSet(jresult
									.optJSONObject("userinfo"));
							user.imei = imei;
							MyLog.i(TAG, "user -- " + user.toString());
//							if (AppStatus.frontActivity != null) {
//								SharedPreferences sharedPrefs = AppStatus.frontActivity
//										.getSharedPreferences("home_content",
//												Context.MODE_PRIVATE);
//
//								SharedPreferences.Editor editor = sharedPrefs
//										.edit();
//								editor.putString("advert", jresult
//										.optJSONArray("advert").toString());
//								editor.putString("news",
//										jresult.optJSONObject("news")
//												.toString());
//								editor.putString("books",
//										jresult.optJSONArray("books")
//												.toString());
//								editor.putString("activities", jresult
//										.optJSONArray("activities").toString());
//								editor.commit();
//							}
							
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
			handler.sendEmptyMessage(ApiDefine.ERROR_TIMEOUT);
			MyLog.showException(e1);
		}

		return null;
	}

	String request() {
		String url = ApiDefine.DOMAIN + ApiDefine.QUICK_LOGIN;
		Map<String, String> params = new HashMap<String, String>();
		params.put("imei", imei);
		params.put("machine", "" + 1);

		return ApiRequest.postRequest(url, params);
	}

}
