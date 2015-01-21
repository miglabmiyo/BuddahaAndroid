package home.task;

import org.json.JSONObject;

import user.login.MyUser;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import basic.net.ApiDefine;
import basic.net.ApiRequest;
import basic.task.BaseTask;
import basic.util.AppStatus;

/**
 * @author song
 * @version 创建时间：2014-12-8 下午4:50:25 类说明 获取首页数据
 */
public class RecommendBuddhaTask extends BaseTask {

	public RecommendBuddhaTask(Handler tHandler) {
		this.init(tHandler);
	}

	@Override
	protected String request() throws Exception {
		String url = ApiDefine.DOMAIN + ApiDefine.HOME_INFO;
		String params = MyUser.getApiBasicParams();

		return ApiRequest.getRequest(url + params);
	}

	@Override
	protected boolean parseResult(JSONObject jresult) {
		if (AppStatus.frontActivity != null) {
			SharedPreferences sharedPrefs = AppStatus.frontActivity
					.getSharedPreferences("home_content", Context.MODE_PRIVATE);

			SharedPreferences.Editor editor = sharedPrefs.edit();
			editor.putString("advert", jresult.optJSONArray("advert")
					.toString());
			editor.putString("news", jresult.optJSONObject("news").toString());
			editor.putString("books", jresult.optJSONArray("books").toString());
			editor.putString("activities", jresult.optJSONArray("activities")
					.toString());
			editor.commit();
			handler.sendEmptyMessage(ApiDefine.GET_SUCCESS);
			return true;
		}

		return false;

	}

}