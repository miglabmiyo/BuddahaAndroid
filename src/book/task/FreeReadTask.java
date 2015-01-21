package book.task;

import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import basic.net.ApiDefine;
import basic.net.ApiRequest;
import basic.util.MyLog;
import book.entity.ChapterInfo;

/**
 * @author song
 * @version 创建时间：2015-1-10 下午4:17:38 类说明
 */
public class FreeReadTask extends AsyncTask<Void, Void, Void> {
	String TAG = "FreeReadTask";
	String url;
	Handler handler;

	public FreeReadTask(Handler tHandler, String url) {
		this.handler = tHandler;
		this.url = url;
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
			String request = ApiRequest.getText(url);
			// MyLog.v(TAG, "request: " + request);
			if (TextUtils.isEmpty(request)) { // 超时
				handler.sendEmptyMessage(ApiDefine.ERROR_TIMEOUT);
				return null;
			}

			ChapterInfo chapter = new ChapterInfo();
			chapter.name = getContent(request, "Chapter");
			chapter.content = getContent(request, "Content");

			handler.sendMessage(handler.obtainMessage(ApiDefine.GET_SUCCESS, 1,
					0, chapter));

		} catch (Exception e1) {
			handler.sendEmptyMessage(ApiDefine.ERROR_TIMEOUT);
			MyLog.showException(e1);
		}

		return null;
	}

	String getContent(String total, String label) {
		if (TextUtils.isEmpty(total) || TextUtils.isEmpty(label)
				|| total.length() <= label.length())
			return null;

		String text = null;
		String start = "<" + label + ">";
		String end = "</" + label + ">";
		if (total.contains(start)) {
			text = total.substring(total.indexOf(start) + start.length(),
					total.indexOf(end));
		}
		return text;
	}
}
