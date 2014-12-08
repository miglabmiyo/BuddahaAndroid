package home.task;

import java.net.SocketTimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import user.login.MyUser;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import basic.net.ApiDefine;
import basic.net.ApiRequest;
import basic.util.AppStatus;
import basic.util.MyLog;

/**
 * @author song
 * @version 创建时间：2014-12-8 下午4:50:25 类说明 获取首页数据
 */
public class RecommendBuddhaTask extends AsyncTask<Void, Void, Void> {
	String TAG = "RecommendBuddhaTask";
	Handler handler;

	public RecommendBuddhaTask(Handler tHandler) {
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
						if (jresult != null) {
							if (AppStatus.frontActivity != null) {
								SharedPreferences sharedPrefs = AppStatus.frontActivity
										.getSharedPreferences("home_content",
												Context.MODE_PRIVATE);

								SharedPreferences.Editor editor = sharedPrefs
										.edit();
								editor.putString("advert", jresult
										.optJSONArray("advert").toString());
								editor.putString("news",
										jresult.optJSONObject("news")
												.toString());
								editor.putString("books",
										jresult.optJSONArray("books")
												.toString());
								editor.putString("activities", jresult
										.optJSONArray("activities").toString());
								editor.commit();
							}

							handler.sendEmptyMessage(ApiDefine.GET_SUCCESS);
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

		} catch (SocketTimeoutException se) {
			handler.sendEmptyMessage(ApiDefine.ERROR_TIMEOUT);

		} catch (Exception e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}

		return null;
	}

	String request() throws Exception {
		String url = ApiDefine.DOMAIN + ApiDefine.HOME_INFO;
		MyUser user = MyUser.getInstance();
		String params = "?uid=" + user.userId + "&token=" + user.requestToken;

		return ApiRequest.getRequest(url + params);
	}

}