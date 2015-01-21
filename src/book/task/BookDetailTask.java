package book.task;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import user.login.MyUser;
import android.os.Handler;
import basic.net.ApiDefine;
import basic.net.ApiRequest;
import basic.task.BaseTask;
import book.entity.BookDetailInfo;
import book.entity.BookInfo;

/**
 * @author song
 * @version 创建时间：2015-1-5 下午8:05:57 类说明
 */
public class BookDetailTask extends BaseTask {
	BookInfo bookInfo;
	BookDetailInfo detailInfo;

	public BookDetailTask(Handler tHandler, BookInfo bookInfo) {
		this.init(tHandler);
		this.bookInfo = bookInfo;
	}

	@Override
	protected boolean paramsFail() {
		if (bookInfo == null || bookInfo.id <= 0)
			return true;

		return false;
	}

	@Override
	protected String request() throws Exception {
		String url = ApiDefine.DOMAIN + ApiDefine.BOOK_DETAIL;
		String params = MyUser.getApiBasicParams() + "&bookid=" + bookInfo.id;

		return ApiRequest.getRequest(url + params);
	}

	@Override
	protected boolean parseResult(JSONObject jresult) {
		JSONObject js = jresult.optJSONObject("summary");
		if (js != null) {
			BookDetailInfo detailInfo = new BookDetailInfo(bookInfo, js);

			js = jresult.optJSONObject("user");
			if (js != null) {
				detailInfo.saveNum = js.optInt("issave");
			}

			JSONArray arrays = jresult.optJSONArray("label");
			if (arrays != null) {
				int len = arrays.length();
				if (len > 0) {
					String[] labels = new String[len];
					for (int i = 0; i < len; i++) {
						labels[i] = arrays.optString(i);
					}
					detailInfo.labels = labels;
				}

			}

			handler.sendMessage(handler.obtainMessage(ApiDefine.GET_SUCCESS, 1, 0, 
					detailInfo));
			return true;
		}

		return false;

	}

}
