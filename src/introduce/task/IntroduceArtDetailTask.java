package introduce.task;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import user.login.MyUser;
import android.os.Handler;
import basic.net.ApiDefine;
import basic.net.ApiRequest;
import basic.task.BaseTask;
import book.entity.BookInfo;

/**
 * @author 赵龙权
 * @version 创建时间：2015-1-12 上午10:42:24
 * 类说明	艺术浏览详情
 */
public class IntroduceArtDetailTask extends BaseTask{

	BookInfo bookInfo;
	int id;
	
	public IntroduceArtDetailTask(Handler tHandler, int id) {
		this.init(tHandler);
		this.id = id;
	}
	
	@Override
	protected boolean paramsFail() {
		if (id <= 0)
			return true;

		return false;
	}

	@Override
	protected String request() throws Exception {
		String url = ApiDefine.DOMAIN + ApiDefine.INTRODUCE_ART;
		String params = MyUser.getApiBasicParams() + "&introid=" + id;

		return ApiRequest.getRequest(url + params);
	}

	@Override
	protected boolean parseResult(JSONObject jresult) {
		
		JSONObject js = jresult.optJSONObject("summary");
		if (js != null) {
			bookInfo = new BookInfo(js);
		}

		handler.sendMessage(handler.obtainMessage(ApiDefine.GET_SUCCESS, bookInfo));
		return true;
	}
}
