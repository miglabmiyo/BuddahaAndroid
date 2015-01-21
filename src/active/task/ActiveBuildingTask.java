package active.task;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import user.login.MyUser;
import map.entity.BuildingInfo;
import active.entity.ActiveInfo;
import android.os.Handler;
import basic.net.ApiDefine;
import basic.net.ApiRequest;
import basic.task.BaseTask;

/**
 * @author 赵龙权
 * @version 创建时间：2015-1-7 下午3:07:00
 * 类说明	获取活动详情
 */
public class ActiveBuildingTask extends BaseTask{

	private int id;
	ActiveInfo activeInfo;
	
	public ActiveBuildingTask(Handler tHandler, ActiveInfo activeInfo) {
		this.init(tHandler);
		this.activeInfo = activeInfo;
	}

	@Override
	protected String request() throws Exception {
		String url = ApiDefine.DOMAIN + ApiDefine.ACTIVE_CONTENT;
//		String params = MyUser.getApiBasicParams() + "&aid=" + activeInfo.id;
		String params = MyUser.getApiBasicParams() + "&aid=" + activeInfo.id
				+ "&latitude=" + activeInfo.latitude + "&longitude="
				+ activeInfo.longitude;
		return ApiRequest.getRequest(url + params);
		
	}
	
	@Override
	protected boolean parseResult(JSONObject jresult) {
		activeInfo.activeSummary(jresult);
		handler.sendMessage(handler.obtainMessage(
				ApiDefine.GET_SUCCESS, activeInfo));
		return true;
	}

	@Override
	protected boolean paramsFail() {
		if(activeInfo == null)
			return true;
		
		return false;
	}
	
}
