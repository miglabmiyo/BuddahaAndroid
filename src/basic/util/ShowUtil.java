package basic.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * @author song
 * @version 创建时间：2013-6-17 下午7:41:15 <br>
 *          类说明 显示提示工具类
 */
public class ShowUtil {

	public synchronized static void showToast(Context con, String content) {
		MyLog.v("显示提示toast---" + content);
		if(con != null && !TextUtils.isEmpty(content)){
			Toast.makeText(con, content, Toast.LENGTH_SHORT).show();
		}
	}

	public synchronized static void showToast(Context con, int resource) {
		MyLog.v("显示提示toast---" + con.getString(resource));
		if (con != null)
			Toast.makeText(con, resource, Toast.LENGTH_SHORT).show();
	}

	public synchronized static void showLongToast(Context con, String content) {
		MyLog.v("显示提示toast-Long--" + content);
		if(con != null && !TextUtils.isEmpty(content)){
			Toast.makeText(con, content, Toast.LENGTH_LONG).show();
		}
	}

	public synchronized static void showLongToast(Context con, int resource) {
		if (con != null) {
			MyLog.v("显示提示toast-Long--" + con.getString(resource));
			Toast.makeText(con, resource, Toast.LENGTH_LONG).show();
		}
	}

}
