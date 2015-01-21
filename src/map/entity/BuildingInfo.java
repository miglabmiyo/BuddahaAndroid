package map.entity;

import java.io.Serializable;

import org.json.JSONObject;

/**
 * @author song
 * @version 创建时间：2014-12-18 下午9:34:48
 * 类说明 建筑详情
 */
public class BuildingInfo implements Serializable {

	private static final long serialVersionUID = -3690958207326342810L;
	public long id;
	public String name;
	public int type;
	public String pic;
	public String address, city;
	public double latitude, longitude;
	public long distance;
	public String phone, summary;
	public int collect;
	
	public BuildingInfo(JSONObject json){
		if(json != null){
			JSONObject basic = json.optJSONObject("basic");
			if(basic != null){
				id = basic.optLong("id");
				name = basic.optString("name");
				type = basic.optInt("type");
				pic = basic.optString("pic");
			}

			JSONObject location = json.optJSONObject("location");
			if(location != null){
				address = location.optString("address");
				city = location.optString("city");
				latitude = location.optDouble("latitude");
				longitude = location.optDouble("longitude");
				distance = location.optLong("distance");
			}
		}
	}

	public void parseSummary(JSONObject json){
		if(json != null){
			JSONObject content = json.optJSONObject("summary");
			if(content != null){
				phone = content.optString("phone");
				summary = content.optString("summary");
			}

			JSONObject user = json.optJSONObject("user");
			if(user != null){
				collect = user.optInt("collect");
			}
		}
	}

	@Override
	public String toString() {
		return "BuildingInfo [id=" + id + ", name=" + name + ", type=" + type
				+ ", pic=" + pic + ", address=" + address + ", city=" + city
				+ ", latitude=" + latitude + ", longitude=" + longitude
				+ ", distance=" + distance + ", phone=" + phone + ", summary="
				+ summary + ", collect=" + collect + "]";
	}
	
}
