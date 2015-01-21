package basic.task;

import org.json.JSONObject;

import android.os.Handler;

/**
 * @author song
 * @version 创建时间：2015-1-4 下午2:48:06
 * 类说明
 */
public interface BaseTaskHelper {
	void init(Handler handler);
	boolean paramsFail();
	String request();
	boolean parseResult(JSONObject jresult);
}
