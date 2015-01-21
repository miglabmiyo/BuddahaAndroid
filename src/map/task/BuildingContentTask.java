package map.task;

import map.entity.BuildingInfo;

import org.json.JSONObject;

import user.login.MyUser;
import android.os.Handler;
import basic.net.ApiDefine;
import basic.net.ApiRequest;
import basic.task.BaseTask;

/**
 * @author song
 * @version 创建时间：2014-12-18 下午10:27:06 类说明 获取建筑详情
 */
public class BuildingContentTask extends BaseTask {
	BuildingInfo building;

	public BuildingContentTask(Handler tHandler, BuildingInfo building) {
		this.init(tHandler);
		this.building = building;
	}

	@Override
	protected String request() throws Exception {
		String url = ApiDefine.DOMAIN + ApiDefine.BUILD_CONTENT;
		String params = MyUser.getApiBasicParams() + "&bid=" + building.id
				+ "&latitude=" + building.latitude + "&longitude="
				+ building.longitude;

		return ApiRequest.getRequest(url + params);
	}
	
	@Override
	protected boolean parseResult(JSONObject jresult) {
		building.parseSummary(jresult);
		handler.sendMessage(handler.obtainMessage(
				ApiDefine.GET_SUCCESS, building));
		return true;
	}

	@Override
	protected boolean paramsFail() {
		if(building == null)
			return true;
		
		return false;
	}

}