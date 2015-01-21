package user.task;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import user.login.MyUser;
import android.os.Handler;
import basic.net.ApiDefine;
import basic.net.ApiRequest;
import basic.task.BaseTask;

/**
 * @author song
 * @version 创建时间：2014-11-27 下午7:37:03 类说明 默认登录
 */
public class IMEILoginTask extends BaseTask {
	String imei;

	public IMEILoginTask(Handler tHandler, String imei) {
		this.init(tHandler);
		this.imei = imei;
	}

	@Override
	protected String request() {
		String url = ApiDefine.DOMAIN + ApiDefine.QUICK_LOGIN;
		Map<String, String> params = new HashMap<String, String>();
		params.put("imei", imei);
		params.put("machine", "" + 1);

		return ApiRequest.postRequest(url, params);
	}

	@Override
	protected boolean paramsFail() {
		if (imei == null)
			return true;

		return false;
	}

	@Override
	protected boolean parseResult(JSONObject jresult) {
		MyUser user = MyUser.getInstance();
		user.imei = imei;
		user.loginSet(jresult.optJSONObject("userinfo"));
		if (user.userId > 0) {
			handler.sendEmptyMessage(ApiDefine.GET_SUCCESS);
			return true;
		} else
			return false;
	}

}
