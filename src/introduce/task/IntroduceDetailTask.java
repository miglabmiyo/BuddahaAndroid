package introduce.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import user.login.MyUser;
import active.entity.ActiveInfo;
import android.os.Handler;
import basic.net.ApiDefine;
import basic.net.ApiRequest;
import basic.task.BaseTask;
import book.entity.BookInfo;

/**
 * @author 赵龙权
 * @version 创建时间：2015-1-9 下午3:34:57
 * 类说明	介绍佛教历史，宗教思想列表书籍详情
 */
public class IntroduceDetailTask extends BaseTask{

	BookInfo bookInfo;
	
	public IntroduceDetailTask(Handler tHandler, BookInfo bookInfo) {
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
		String url = ApiDefine.DOMAIN + ApiDefine.INTRODUCE_HISATHR;
		String params = MyUser.getApiBasicParams() + "&introid=" + bookInfo.id;

		return ApiRequest.getRequest(url + params);
	}

	@Override
	protected boolean parseResult(JSONObject jresult) {
		
		Map<String, Object> objs = new HashMap<String, Object>();
		JSONObject js = jresult.optJSONObject("summary");
		if (js != null) {
			String[] str = { "pic", "chapter", "summary" };
			for (int i = 0; i < str.length; i++) {
				objs.put(str[i], js.opt(str[i]));
			}
		}
		
		Object[] obj = new Object[1];
		String[] str = { "list" };
		ArrayList<BookInfo> list;
		JSONArray array;
		for (int i = 0; i < obj.length; i++) {
			array = jresult.optJSONArray(str[i]);
			list = new ArrayList<BookInfo>();
			if (array != null && array.length() > 0) {
				for (int j = 0; j < array.length(); j++) {
					list.add(new BookInfo(array.optJSONObject(j)));
				}
			}
			obj[i] = list;
		}

		handler.sendMessage(handler.obtainMessage(ApiDefine.GET_SUCCESS_OBJ, objs));
		handler.sendMessage(handler.obtainMessage(ApiDefine.GET_SUCCESS_LIST, obj));
		
//		handler.sendMessage(handler.obtainMessage(ApiDefine.GET_SUCCESS, objs));
//		handler.sendMessage(handler.obtainMessage(ApiDefine.GET_SUCCESS, obj));
		return true;
	}
	
}
