package book.task;

import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import basic.net.ApiDefine;
import basic.net.ApiRequest;
import basic.util.MyLog;

/**
 * @author song
 * @version 创建时间：2015-1-10 下午4:17:38 类说明
 */
public class ReadChapterTask extends AsyncTask<Void, Void, Void> {
	String TAG = "ReadChapterTask";
	String url;
	Handler handler;
	int index;

	public ReadChapterTask(Handler tHandler, String url, int index) {
		this.handler = tHandler;
		this.url = url;
		this.index = index;
	}

	public void setData(String url, int index) {
		this.url = url;
		this.index = index;
	}

	@Override
	protected Void doInBackground(Void... params) {
		MyLog.v(TAG, "handler: " + handler);
		if (handler == null)
			return null;

		if (TextUtils.isEmpty(url)) {
			handler.sendEmptyMessage(ApiDefine.ERROR_PARAMS);
			return null;
		}

		try {
			String request = ApiRequest.getRequest(url);
			// MyLog.v(TAG, "request: " + request);
			if (TextUtils.isEmpty(request)) { // 超时
				handler.sendEmptyMessage(ApiDefine.ERROR_TIMEOUT);
				return null;
			}

			if (!request.contains("404 Not Found")) {
				handler.sendMessage(handler.obtainMessage(
						ApiDefine.GET_SUCCESS, 3, index, request));
			}

		} catch (Exception e1) {
			handler.sendEmptyMessage(ApiDefine.ERROR_TIMEOUT);
			MyLog.showException(e1);
		}

		return null;
	}
}
