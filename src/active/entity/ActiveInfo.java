package active.entity;

import java.io.Serializable;

import org.json.JSONObject;

/**
 * @author 赵龙权
 * @version 创建时间：2015-1-7 上午2:12:25
 * 类说明
 */
public class ActiveInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	public int id;
	public String name;
	public String pic;
	public String address;
	public double latitude, longitude;
	public String phone, summary;

	public ActiveInfo(JSONObject json) {
		if (json != null) {
			id = json.optInt("id");
			name = json.optString("name");
			pic = json.optString("pic");
//			address = json.optString("address");
//			latitude = json.optDouble("latitude");
//			longitude = json.optDouble("longitude");
//			phone = json.optString("phone");
//			summary = json.optString("summary");
		}
	}
	
	public void activeSummary(JSONObject json){
		if(json != null){
			JSONObject location = json.optJSONObject("location");
			if(location != null){
				address = location.optString("address");
				latitude = location.optDouble("latitude");
				longitude = location.optDouble("longitude");
			}
			
			JSONObject content = json.optJSONObject("summary");
			if(content != null){
				phone = content.optString("phone");
				summary = content.optString("summary");
			}
		}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
	
	
}
