package active.task;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import user.login.MyUser;
import active.entity.ActiveInfo;
import android.os.Handler;
import basic.net.ApiDefine;
import basic.net.ApiRequest;
import basic.task.BaseTask;
import basic.util.MyLog;

/**
 * @author 赵龙权
 * @version 创建时间：2015-1-7 上午2:25:38
 * 类说明	活动首页
 */
public class ActiveListTask extends BaseTask{

	public ActiveListTask(Handler tHandler) {
		this.init(tHandler);
	}

	@Override
	protected String request() throws Exception {
		String url = ApiDefine.DOMAIN + ApiDefine.ACTIVE_LIST;
		String params = MyUser.getApiBasicParams();

		return ApiRequest.getRequest(url + params);
	}

	@Override
	protected boolean parseResult(JSONObject jresult) {
		Object[] objs = new Object[2];
		String[] str = { "common", "recomm" };
		ArrayList<ActiveInfo> list;
		JSONArray array;
		for (int i = 0; i < objs.length; i++) {
			array = jresult.optJSONArray(str[i]);
			list = new ArrayList<ActiveInfo>();
			if (array != null && array.length() > 0) {
				for (int j = 0; j < array.length(); j++) {
					list.add(new ActiveInfo(array.optJSONObject(j)));
				}
			}
			objs[i] = list;
		}

		handler.sendMessage(handler.obtainMessage(ApiDefine.GET_SUCCESS, objs));
		return true;
	}
}
