package book.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONObject;

import user.login.MyUser;
import android.os.Handler;
import android.text.TextUtils;
import basic.net.ApiDefine;
import basic.net.ApiRequest;
import basic.task.BaseTask;
import book.entity.BookDetailInfo;
import book.entity.ChapterInfo;

/**
 * @author song
 * @version 创建时间：2015-1-7 下午7:43:07 类说明
 */
public class GetChapterTask extends BaseTask {
	BookDetailInfo bookInfo;

	public GetChapterTask(Handler tHandler, BookDetailInfo bookInfo) {
		this.init(tHandler);
		this.bookInfo = bookInfo;
	}

	@Override
	protected boolean paramsFail() {
		if (bookInfo == null || bookInfo.id <= 0
				|| TextUtils.isEmpty(bookInfo.booktoken))
			return true;

		return false;
	}

	@Override
	protected String request() throws Exception {
		String url = ApiDefine.DOMAIN + ApiDefine.CHAPTER_LIST;
		String params = MyUser.getApiBasicParams() + "&booktoken="
				+ bookInfo.booktoken + "&bookid=" + bookInfo.id;

		return ApiRequest.getRequest(url + params);
	}

	@Override
	protected boolean parseResult(JSONObject jresult) {
		ArrayList<ChapterInfo> list = new ArrayList<ChapterInfo>();
		JSONArray array = jresult.optJSONArray("list");
		for (int i = 0; i < array.length(); i++) {
			list.add(new ChapterInfo(array.optJSONObject(i)));
		}

		if (list.size() > 0) {
			Collections.sort(list, new Comparator<ChapterInfo>() {
				@Override
				public int compare(ChapterInfo lhs, ChapterInfo rhs) {
					// return (int) (rhs.lastModified()-lhs.lastModified());
					if (lhs.id > lhs.id) {
						return 1;
					} else if (lhs.id == lhs.id) {
						return 0;
					} else {
						return -1;
					}
				}
			});
		}

		handler.sendMessage(handler.obtainMessage(ApiDefine.GET_SUCCESS, 2, 0,
				list));
		return true;
	}

}
