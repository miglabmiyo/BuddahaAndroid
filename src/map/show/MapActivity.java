package map.show;

import map.entity.BuildingInfo;
import android.content.Intent;
import android.os.Bundle;
import basic.show.BaseActivity;
import basic.util.MyLog;

/**
 * @author song
 * @version 创建时间：2014-12-22 下午9:25:24 类说明
 */
public class MapActivity extends BaseActivity {
	BuildingInfo building;
	double latitude, longtitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		initMyLating();
		super.onCreate(savedInstanceState);
	}

	protected void initMyLating() {
		latitude = getIntent().getDoubleExtra("latitude", 0);
		longtitude = getIntent().getDoubleExtra("longtitude", 0);
		MyLog.i(TAG, "latitude: " + latitude + ", longtitude:" + latitude);
	}

	void initBuildingInfo() {
		building = (BuildingInfo) getIntent().getSerializableExtra("build");
	}

	void gotoNext(Class<?> ac) {
		Intent i = new Intent(this, ac);
		i.putExtra("build", building);
		i.putExtra("latitude", latitude);
		i.putExtra("longtitude", longtitude);
		this.startActivity(i);
	}
}
