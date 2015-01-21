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
 * @version 创建时间：2014-12-27 下午6:46:02 类说明
 */
public class BookMallTask extends BaseTask {

	public BookMallTask(Handler tHandler) {
		this.init(tHandler);
	}

	@Override
	protected String request() throws Exception {
		String url = ApiDefine.DOMAIN + ApiDefine.BOOKMALL_HOME;
		String params = MyUser.getApiBasicParams();

		return ApiRequest.getRequest(url + params);
	}

	@Override
	protected boolean parseResult(JSONObject jresult) {
		Object[] objs = new Object[4];
		String[] str = { "hot", "scriptures", "ritual",
				"theory" };
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
	
}
