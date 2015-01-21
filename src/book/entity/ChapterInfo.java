package book.entity;

import java.io.Serializable;

import org.json.JSONObject;

/**
 * @author song
 * @version 创建时间：2015-1-7 下午7:45:00 类说明 章节信息
 */
public class ChapterInfo implements Serializable {
	private static final long serialVersionUID = -7721272905419875496L;
	public int bookid;
	public int id;
	public String name;
	public String url;
	public String content;
	public int pageCount;
	public boolean isReaded; // 是否已读过
	public boolean isLoaded; // 是否已加载

	public ChapterInfo() {
	}

	public ChapterInfo(JSONObject json) {
		if (json != null) {
			id = json.optInt("id");
			name = json.optString("name");
			url = json.optString("url");
			bookid = json.optInt("bookid");
		}
	}

	@Override
	public String toString() {
		return "ChapterInfo [bookid="
				+ bookid
				+ ", id="
				+ id
				+ ", name="
				+ name
				+ ", url="
				+ url
				+ ", content="
				+ (content == null ? "null"
						: (content.substring(0, 20) + "...")) + "]";
	}
}
