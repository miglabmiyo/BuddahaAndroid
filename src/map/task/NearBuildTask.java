package map.task;

import java.util.ArrayList;

import map.entity.BuildingInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import user.login.MyUser;
import android.os.Handler;
import basic.net.ApiDefine;
import basic.net.ApiRequest;
import basic.task.BaseTask;
import basic.util.MyLog;

/**
 * @author song
 * @version 创建时间：2014-12-18 下午9:24:53 类说明 获取附近建筑物
 */
public class NearBuildTask extends BaseTask {
	double latitude, longitude;

	public NearBuildTask(Handler tHandler, double latitude, double longitude) {
		this.init(tHandler);
		this.latitude = latitude;
		this.longitude = longitude;
	}

	protected String request() throws Exception {
		String url = ApiDefine.DOMAIN + ApiDefine.NEAR_BUILD;
		String params = MyUser.getApiBasicParams() + "&latitude=" + latitude
				+ "&longitude=" + longitude;

		return ApiRequest.getRequest(url + params);
	}
	
	@Override
	protected boolean parseResult(JSONObject jresult) {
		JSONArray array = jresult.optJSONArray("nearbuild");
		if (array != null && array.length() > 0) {
			ArrayList<BuildingInfo> list = new ArrayList<BuildingInfo>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject item = array.optJSONObject(i);
				if (item != null) {
					list.add(new BuildingInfo(item));
				}
			}
			if (!list.isEmpty()) {
				handler.sendMessage(handler.obtainMessage(
						ApiDefine.GET_SUCCESS, list));
				return true;
			}
			MyLog.e(TAG, "生成list, 列表为空");
		}
		MyLog.e(TAG, "获取Array为空");
		return false;
	}
}
