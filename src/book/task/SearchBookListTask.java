package book.task;

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
 * @author song
 * @version 创建时间：2014-12-28 下午9:28:24
 * 类说明
 */
public class SearchBookListTask extends BaseTask{
	int type; //1 佛经 2 佛律 3佛论 4 近代佛著

	public SearchBookListTask(Handler tHandler, int type) {
		this.init(tHandler);
		this.type = type;
	}

	@Override
	protected String request() throws Exception {
		String url = ApiDefine.DOMAIN + ApiDefine.BOOKMALL_TYPE;
		String params = MyUser.getApiBasicParams()+ "&btype=" + type;

		return ApiRequest.getRequest(url + params);
	}

	@Override
	protected boolean parseResult(JSONObject jresult) {
		Object[] objs = new Object[2];
		String[] str = { "hot", "new" };
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

		handler.sendMessage(handler.obtainMessage(ApiDefine.GET_SUCCESS, objs));
		return true;
	}

	
	@Override
	protected boolean paramsFail() {
		if(type <= 0 || type > 4)
			return true;
		
		return false;
	}

}