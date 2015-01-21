package book.task;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import user.login.MyUser;
import android.os.Handler;
import basic.net.ApiDefine;
import basic.net.ApiRequest;
import basic.task.BaseTask;
import book.entity.BookDetailInfo;


/**
 * @author song
 * @version 创建时间：2015-1-6 下午7:30:17
 * 类说明 书架
 */
public class BookShelfTask extends BaseTask {

	public BookShelfTask(Handler tHandler) {
		this.init(tHandler);
	}

	@Override
	protected String request() throws Exception {
		String url = ApiDefine.DOMAIN + ApiDefine.MY_BOOK_LIST;
		String params = MyUser.getApiBasicParams();

		return ApiRequest.getRequest(url + params);
	}

	@Override
	protected boolean parseResult(JSONObject jresult) {
		ArrayList<BookDetailInfo> list = new ArrayList<BookDetailInfo>();
		JSONArray array = jresult.optJSONArray("list");
		if(array != null && array.length() > 0){
			for (int j = 0; j < array.length(); j++) {
				list.add(new BookDetailInfo(array.optJSONObject(j)));
			}
		}

		handler.sendMessage(handler.obtainMessage(ApiDefine.GET_SUCCESS, list));
		return true;
	}
	
}
