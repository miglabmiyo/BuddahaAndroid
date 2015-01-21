package home.fragment;

import java.util.ArrayList;

import map.entity.BuildingInfo;
import map.show.ArroundHome;
import map.show.BuildingDetail;
import map.task.NearBuildTask;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import basic.net.ApiDefine;
import basic.show.BaseFragment;
import basic.util.MyLog;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.miglab.buddha.R;

/**
 * @author song
 * @version 创建时间：2014-12-11 下午5:23:14 类说明
 */
public class MapFragment extends BaseFragment {
	MapView mMapView = null;
	BaiduMap mBaiduMap;
	// 定位相关
	LocationClient mLocClient;
	boolean isFirstLoc = true;// 是否首次定位
	double latitude, longtitude;
	
	@Override
	protected void setLayout() {
		rootResource = R.layout.fm_map;
	}

	@Override
	protected void initData() {
		initMap();
		initArround();
	}

	void initMap() {
		// 获取地图控件引用
		mMapView = (MapView) vRoot.findViewById(R.id.bmapView);
		initBuildInfo();

		// 定位
		mBaiduMap = mMapView.getMap();
	}

	void initArround() {
		View ly_arround = vRoot.findViewById(R.id.ly_arround);
		ly_arround.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (MapFragment.this.canClicked()) {
					Intent i = new Intent(ac, ArroundHome.class);
					i.putExtra("latitude", latitude);
					i.putExtra("longtitude", longtitude);
					ac.startActivity(i);
				}
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
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
	}

	@Override
	public void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		if (mMapView != null)
			mMapView.onResume();

		if(isFirstLoc){
			// 开启定位图层
			mBaiduMap.setMyLocationEnabled(true);
			mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
					LocationMode.FOLLOWING, true, null));
			// 定位初始化
			mLocClient = new LocationClient(ac);
			mLocClient.registerLocationListener(new MyLocationListenner());
			LocationClientOption option = new LocationClientOption();
			option.setOpenGps(true);// 打开gps
			option.setCoorType("bd09ll"); // 设置坐标类型
			option.setScanSpan(5000);

			mLocClient.setLocOption(option);
			mLocClient.start();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		if (mMapView != null)
			mMapView.onPause();
	}

	/**
	 * 定位SDK监听函数
	 */
	class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
//			MyLog.d(TAG, "MyLocationListenner onReceiveLocation location: "
//					+ location);
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
				latitude = location.getLatitude();
				longtitude = location.getLongitude();

				LatLng ll = new LatLng(latitude, longtitude);
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);

				new NearBuildTask(h, latitude, longtitude).execute();
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
//			MyLog.d(TAG, "MyLocationListenner onReceivePoi  poiLocation: "
//					+ poiLocation);
		}
	}

	@Override
	protected void doHandler(Message msg) {
		try {
			int nCommand = msg.what;
			switch (nCommand) {
			case ApiDefine.GET_SUCCESS:
				MyLog.d(TAG, "GET_SUCCESS");
				if (msg.obj != null && msg.obj instanceof ArrayList<?>) {
					ArrayList<BuildingInfo> list = (ArrayList<BuildingInfo>) msg.obj;
					showList(list);
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			MyLog.showException(e);
		}
	}
	
	void showList(ArrayList<BuildingInfo> list) {
		MyLog.v(TAG, "showList");
		if (list != null && !list.isEmpty()) {
			MyLog.v(TAG, "showList list不为空");
			for (BuildingInfo buildingInfo : list) {
				showMarker(buildingInfo);
			}
		}
	}

	// 0 自己 1 寺庙 2 书店 3素食馆 4 佛具 5 服装
	int[] res = { R.drawable.map_my_id, R.drawable.map_simiao_id,
			R.drawable.map_book_id, R.drawable.map_food_id,
			R.drawable.map_fozhu_id, R.drawable.map_close_id };
	BitmapDescriptor[] bmRes = new BitmapDescriptor[res.length];
	View v_marker_info;
	TextView tv_marker_name;

	void showMarker(BuildingInfo build) {
		MyLog.v(TAG, "showMarker build: " + build);
		for (int i = 0; i < bmRes.length; i++) {
			bmRes[i] = BitmapDescriptorFactory.fromResource(res[i]);
		}
		
		if (build != null && build.latitude > 0 && build.longitude > 0) {
			// 定义Maker坐标点
			LatLng point = new LatLng(build.latitude, build.longitude);
			// 构建MarkerOption，用于在地图上添加Marker
			Bundle info = new Bundle();
			info.putSerializable("build", build);
			OverlayOptions option = new MarkerOptions().position(point)
					.icon(bmRes[build.type]).title(build.name).extraInfo(info);
			// 在地图上添加Marker，并显示
			mBaiduMap.addOverlay(option);
		}

		v_marker_info = LayoutInflater.from(ac).inflate(
				R.layout.ly_map_marker_showinfo, null);
		tv_marker_name = (TextView) v_marker_info
				.findViewById(R.id.marker_name);

		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				return showBuildInfo(marker);
			}

		});

		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng arg0) {
				MyLog.v(TAG, "onMapClick");
				dismissBuildInfo();
			}

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				MyLog.d(TAG, "onMapPoiClick");
				return false;
			}
		});
	}

	View ly_build_info;
	ImageView iv_build_icon;
	TextView tv_build_name, tv_build_address;
	// 0 自己 1 寺庙 2 书店 3素食馆 4 佛具 5 服装
	int[] imgs = { R.drawable.map_my_id, R.drawable.around_simiao,
			R.drawable.around_book, R.drawable.around_food,
			R.drawable.around_fojv, R.drawable.around_close };
	BuildingInfo currentClickBuilding;

	void initBuildInfo() {
		ly_build_info = vRoot.findViewById(R.id.ly_build_info);
		iv_build_icon = (ImageView) vRoot.findViewById(R.id.iv_build_icon);
		tv_build_name = (TextView) vRoot.findViewById(R.id.tv_build_name);
		tv_build_address = (TextView) vRoot.findViewById(R.id.tv_build_address);

//		ly_build_info.setOnClickListener(infoClicker);
		ly_build_info.setVisibility(View.GONE);
	}

	boolean showBuildInfo(Marker marker) {
		if (this.canClicked() && marker != null) {
			MyLog.d(TAG, "showBuildInfo " + marker.getTitle());
			Bundle info = marker.getExtraInfo();
			if (info.containsKey("build")) {
				BuildingInfo build = (BuildingInfo) info
						.getSerializable("build");
				if (build != null && build.id > 0) {
					currentClickBuilding = build;
					tv_marker_name.setText(build.name);
					// tv_marker_name.setOnClickListener(infoClicker);
					LatLng l = new LatLng(build.latitude, build.longitude);
					InfoWindow infoshow = new InfoWindow(
							BitmapDescriptorFactory.fromView(v_marker_info), l,
							-50, infoClicker);
					mBaiduMap.showInfoWindow(infoshow);
					return true;
				}
			}
		}

		return false;
	}

	InfoWindow.OnInfoWindowClickListener infoClicker = new InfoWindow.OnInfoWindowClickListener() {

		@Override
		public void onInfoWindowClick() {
			// TODO Auto-generated method stub
			gotoDetailInfo();
		}

	};

	void gotoDetailInfo() {
		if (this.canClicked() && currentClickBuilding != null) {
			Intent i = new Intent(ac, BuildingDetail.class);
			i.putExtra("build", currentClickBuilding);
			i.putExtra("latitude", latitude);
			i.putExtra("longtitude", longtitude);
			ac.startActivity(i);
		}
	}

	void dismissBuildInfo() {
		mBaiduMap.hideInfoWindow();
	}
}
