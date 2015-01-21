package basic.task;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import basic.net.ApiDefine;
import basic.util.MyLog;

/**
 * @author song
 * @version 创建时间：2014-12-31 上午11:34:32 类说明
 */
public class BaseTask extends AsyncTask<Void, Void, Void>{
	protected String TAG = "BaseTask";
	protected Handler handler;

	protected void init(Handler handler) {
		this.handler = handler;
		TAG = this.getClass().getSimpleName();
	}

	@Override
	protected Void doInBackground(Void... params) {
		MyLog.v(TAG, "handler: " + handler);
		if (handler == null)
			return null;
		
		if (paramsFail()){
			handler.sendEmptyMessage(ApiDefine.ERROR_PARAMS);
			return null;
		}
		
		try {
			String request = request();
			MyLog.v(TAG, "request: " + request);
			if (TextUtils.isEmpty(request)) { // 超时
				handler.sendEmptyMessage(ApiDefine.ERROR_TIMEOUT);
				return null;
			}

			int code = 0;
			String errorMsg = ApiDefine.ERRORMSG_UNKNOWN;
			try {
				JSONObject json = new JSONObject(request);
				if (json != null) {
					code = json.optInt("status");
					MyLog.v(TAG, "status: " + code);
					if (code == 1) {
						JSONObject jresult = json.optJSONObject("result");
						if (jresult != null && parseResult(jresult)) {
							return null;
						}
						MyLog.e(TAG, "result end jresult: " + jresult);
					}
				}

			} catch (JSONException e) {
				MyLog.showException(e);
			}

			handler.sendMessage(handler.obtainMessage(
					code == 0 ? ApiDefine.ERROR_UNKNOWN : code, errorMsg));

		} catch (Exception e1) {
			handler.sendEmptyMessage(ApiDefine.ERROR_TIMEOUT);
			MyLog.showException(e1);
		}

		return null;
	}

	protected String request() throws Exception {
		return "";
	}

	protected boolean paramsFail(){
		return false;
	}
	
	protected boolean parseResult(JSONObject jresult){
		return false;
	}

}
