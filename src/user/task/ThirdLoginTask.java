package user.task;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import user.login.MyUser;
import android.os.Handler;
import android.text.TextUtils;
import basic.net.ApiDefine;
import basic.net.ApiRequest;
import basic.task.BaseTask;

/**
 * @author song
 * @version 创建时间：2014-12-7 下午1:51:56 类说明 第三方登录
 */
public class ThirdLoginTask extends BaseTask {

	public ThirdLoginTask(Handler tHandler) {
		this.init(tHandler);
	}

	@Override
	protected String request() {
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

	@Override
	protected boolean parseResult(JSONObject jresult) {
		MyUser user = MyUser.getInstance();
		user.loginSet(jresult.optJSONObject("userinfo"));
		if (user.userId > 0) {
			handler.sendEmptyMessage(ApiDefine.GET_SUCCESS);
			return true;
		}else
			return false;
	}
}
