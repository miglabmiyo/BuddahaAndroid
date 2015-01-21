package book.entity;

import java.util.Arrays;

import org.json.JSONObject;

/**
 * @author song
 * @version 创建时间：2015-1-10 下午4:36:48 类说明 书籍详细信息
 */
public class BookDetailInfo extends BookInfo {
	private static final long serialVersionUID = -589415933455273709L;

	public String author;
	public String pubtime;
	public String freeRead;
	public String fullRead;
	public String booktoken;
	public int saveNum;
	public String[] labels;

	public BookDetailInfo(BookInfo info, JSONObject json) {
		if (info != null) {
			id = info.id;
			name = info.name;
			pic = info.pic;
			type = info.type;
		}

		if (json != null) {
			author = json.optString("author");
			pubtime = json.optString("pubtime");
			chapter = json.optInt("chapter");
			summary = json.optString("summary");
			freeRead = json.optString("free");
			fullRead = json.optString("full");
		}
	}

	public BookDetailInfo(JSONObject json) {
		super(json);

		if (json != null) {
			booktoken = json.optString("booktoken");
		}
	}

	@Override
	public String toString() {
		return "BookDetailInfo [id=" + id + ", name=" + name + ", pic=" + pic
				+ ", summary=" + summary + ", type=" + type + ", chapter="
				+ chapter + ", url=" + url + ", index=" + index + ", author="
				+ author + ", pubtime=" + pubtime + ", freeRead=" + freeRead
				+ ", fullRead=" + fullRead + ", booktoken=" + booktoken
				+ ", saveNum=" + saveNum + ", labels="
				+ Arrays.toString(labels) + "]";
	}

}
