package book.task;

import org.json.JSONObject;

import user.login.MyUser;
import android.os.Handler;
import android.text.TextUtils;
import basic.net.ApiDefine;
import basic.net.ApiRequest;
import basic.task.BaseTask;

/**
 * @author song
 * @version 创建时间：2015-1-15 下午5:12:15 类说明
 */
public class AddToShelfTask extends BaseTask {
	int bookId;

	public AddToShelfTask(Handler tHandler, int bookId) {
		this.init(tHandler);
		this.bookId = bookId;
	}

	@Override
	protected boolean paramsFail() {
		if (bookId <= 0)
			return true;

		return false;
	}

	@Override
	protected String request() throws Exception {
		String url = ApiDefine.DOMAIN + ApiDefine.ADD_TO_SHELF;
		String params = MyUser.getApiBasicParams() + "&bookid=" + bookId;

		return ApiRequest.getRequest(url + params);
	}

	@Override
	protected boolean parseResult(JSONObject jresult) {
		String book_token = jresult.optString("book_token");
		if (!TextUtils.isEmpty(book_token)) {
			handler.sendMessage(handler.obtainMessage(ApiDefine.GET_SUCCESS, 2,
					0, book_token));
			return true;
		}

		return false;

	}

}
