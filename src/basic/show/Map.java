package basic.show;

import basic.util.MyLog;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.miglab.buddha.R;

/**
 * @author song
 * @version 创建时间：2014-12-12 下午2:28:12 类说明
 */
public class Map extends BaseActivity {

	MapView mMapView = null;
	BaiduMap mBaiduMap;
	// 定位相关
	LocationClient mLocClient;
	boolean isFirstLoc = true;// 是否首次定位

	/** 初始化 */
	protected void init() {
		setContentView(R.layout.fm_map);
		
		// 获取地图控件引用
		mMapView = (MapView) this.findViewById(R.id.bmapView);
		MyLog.v(TAG, "init 1");
		// 定位
		mBaiduMap = mMapView.getMap();
		MyLog.v(TAG, "init 2");
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		MyLog.v(TAG, "init 3");
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				LocationMode.FOLLOWING, true, null));
		MyLog.v(TAG, "init 4");
		// 定位初始化
		mLocClient = new LocationClient(this);
		MyLog.v(TAG, "init 5");
		mLocClient.registerLocationListener(new MyLocationListenner());
		MyLog.v(TAG, "init 6");
		LocationClientOption option = new LocationClientOption();
		MyLog.v(TAG, "init 7");
		option.setOpenGps(true);// 打开gps
		MyLog.v(TAG, "init 8");
		option.setCoorType("bd09ll"); // 设置坐标类型
		MyLog.v(TAG, "init 9");
		option.setScanSpan(1000);
		MyLog.v(TAG, "init 10");
		mLocClient.setLocOption(option);
		MyLog.v(TAG, "init 11");
		mLocClient.start();
		MyLog.v(TAG, "init 12");
	}

	/**
	 * 定位SDK监听函数
	 */
	class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			MyLog.d(TAG, "MyLocationListenner onReceiveLocation location:"
					+ location);
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
			MyLog.d(TAG, "MyLocationListenner onReceivePoi");
		}
	}

	@Override
	protected void onPause() {
		if (mMapView != null)
			mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (mMapView != null)
			mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 关闭定位图层
		if (mBaiduMap != null)
			mBaiduMap.setMyLocationEnabled(false);

		if (mMapView != null) {
			mMapView.onDestroy();
			mMapView = null;
		}

		// 退出时销毁定位
		if (mLocClient != null && mLocClient.isStarted())
			mLocClient.stop();

		super.onDestroy();
	}

}
