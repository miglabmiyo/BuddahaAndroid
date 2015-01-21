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
 * @version 创建时间：2014-12-18 下午10:17:58 类说明 获取推荐建筑
 */
public class RecommendBuildingTask extends BaseTask {
	String latitude, longitude;

	public RecommendBuildingTask(Handler tHandler, double latitude,
			double longitude) {
		this.init(tHandler);
		this.latitude = String.valueOf(latitude);
		this.longitude = String.valueOf(longitude);
	}

	@Override
	protected String request() throws Exception {
		String url = ApiDefine.DOMAIN + ApiDefine.RECOMMEND_BUILD;
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
