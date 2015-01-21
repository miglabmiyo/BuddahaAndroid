package book.entity;

import java.io.Serializable;

import org.json.JSONObject;

/**
 * @author song
 * @version 创建时间：2014-12-27 下午7:14:07 类说明 书籍简要信息
 */
public class BookInfo implements Serializable  {

	private static final long serialVersionUID = 589613165478828057L;
	public int id;
	public String name;
	public String pic;
	public String summary;
	public int type;
	public int chapter;
	public String url;
	public int index;

	public BookInfo(){}
	
	public BookInfo(JSONObject json) {
		if (json != null) {
			id = json.optInt("id");
			name = json.optString("name");
			pic = json.optString("pic");
			summary = json.optString("summary");
			type = json.optInt("type");
			
			chapter = json.optInt("chapter");
			url = json.optString("url");
			index = json.optInt("index");
		}
	}

	@Override
	public String toString() {
		return "BookInfo [id=" + id + ", name=" + name + ", pic=" + pic
				+ ", summary=" + summary + ", type=" + type + ", chapter="
				+ chapter + ", url=" + url + ", index=" + index + "]";
	}
}
