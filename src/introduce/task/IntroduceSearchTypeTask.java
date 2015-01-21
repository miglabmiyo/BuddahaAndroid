package introduce.task;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import user.login.MyUser;
import android.os.Handler;
import basic.net.ApiDefine;
import basic.net.ApiRequest;
import basic.task.BaseTask;
import book.entity.BookInfo;

/**
 * @author 赵龙权
 * @version 创建时间：2015-1-12 下午2:19:11
 * 类说明	介绍页面佛教历史，宗教思想，艺术浏览分类
 */
public class IntroduceSearchTypeTask extends BaseTask{

	int type; //1佛教历史 ,2宗教思想, 3书画, 4工艺品

	public IntroduceSearchTypeTask(Handler tHandler, int type) {
		this.init(tHandler);
		this.type = type;
	}

	@Override
	protected String request() throws Exception {
		String url = ApiDefine.DOMAIN + ApiDefine.INTRODUCE_BOOK_TYPE;
		String params = MyUser.getApiBasicParams()+ "&btype=" + type;

		return ApiRequest.getRequest(url + params);
	}

	@Override
	protected boolean parseResult(JSONObject jresult) {
		Object[] objs = new Object[1];
		String[] str = { "list" };
		ArrayList<BookInfo> list;
		JSONArray array;
		for (int i = 0; i < objs.length; i++) {
			array = jresult.optJSONArray(str[i]);
			list = new ArrayList<BookInfo>();
			if (array != null && array.length() > 0) {
				for (int j = 0; j < array.length(); j++) {
					list.add(new BookInfo(array.optJSONObject(j)));
				}
			}
			objs[i] = list;
		}
		
		if(type == 1 || type == 3){
			handler.sendMessage(handler.obtainMessage(ApiDefine.GET_INTRODUCE_HISTORY_SUCCESS, objs));
		}
		if(type == 2 || type == 4){
			handler.sendMessage(handler.obtainMessage(ApiDefine.GET_INTRODUCE_THOUGHT_SUCCESS, objs));
		}

//		handler.sendMessage(handler.obtainMessage(ApiDefine.GET_SUCCESS, objs));
		return true;
	}

	
	@Override
	protected boolean paramsFail() {
		if(type <= 0 || type > 4)
			return true;
		
		return false;
	}

}
