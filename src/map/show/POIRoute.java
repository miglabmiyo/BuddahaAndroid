package map.show;

import java.util.List;

import android.content.Intent;
import android.view.View;
import basic.util.MyLog;
import basic.util.ShowUtil;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.miglab.buddha.R;

/**
 * @author song
 * @version 创建时间：2014-12-12 下午2:28:12 类说明 线路规划
 */
public class POIRoute extends MapActivity implements
		BaiduMap.OnMapClickListener, OnGetRoutePlanResultListener {

	MapView mMapView = null;
	BaiduMap mBaiduMap;

	/** 初始化 */
	@Override
	protected void init() {
		setContentView(R.layout.ac_map_search_goto);
		// 获取地图控件引用
		mMapView = (MapView) this.findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// 线路规划初始化
		initRoute();
	}

	@Override
	public void onMapClick(LatLng arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onMapPoiClick(MapPoi arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	// 搜索相关
	RoutePlanSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	View[] v_methods = new View[3];
	TransitRoutePlanOption busPlan;
	DrivingRoutePlanOption drivePlan;
	WalkingRoutePlanOption walkPlan;
	PlanNode stNode, enNode;
	int nodeIndex = -1;// 节点索引,供浏览节点时使用
	RouteLine route = null;

	void initRoute() {
//		initBuildingInfo();
		Intent i = this.getIntent();
		if(i.hasExtra("dstLatitude")){
			MyLog.v(TAG, "i hasExtra dstLatitude");
			double dstLatitude = 0, dstLongtitude = 0; //目标坐标
			dstLatitude = i.getDoubleExtra("dstLatitude", 0);
			dstLongtitude = i.getDoubleExtra("dstLongtitude", 0);
			MyLog.e(TAG, "dstLatitude-->" + dstLatitude );
			MyLog.e(TAG, "dstLongtitude-->" + dstLongtitude );
			stNode = PlanNode.withLocation(new LatLng(latitude, longtitude));
			enNode = PlanNode.withLocation(new LatLng(dstLatitude,
					dstLongtitude));
			busPlan = new TransitRoutePlanOption();
			drivePlan = new DrivingRoutePlanOption();
			walkPlan = new WalkingRoutePlanOption();
			MyLog.v(TAG, "三个方式初始化");

			// 初始化搜索模块，注册事件监听
			mSearch = RoutePlanSearch.newInstance();
			mSearch.setOnGetRoutePlanResultListener(this);
			MyLog.v(TAG, "初始化搜索模块，注册事件监听");

			// 初始化View
			v_methods[0] = this.findViewById(R.id.tv_sel1);
			v_methods[1] = this.findViewById(R.id.tv_sel2);
			v_methods[2] = this.findViewById(R.id.tv_sel3);

			View.OnClickListener l = new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					changeRoutePlanMethod(v);
				}

			};
			for (View v : v_methods) {
				v.setOnClickListener(l);
			}

			changeRoutePlanMethod(v_methods[0]);

		}

	}

	void changeRoutePlanMethod(View v) {
		if (this.canClicked()) {
			mBaiduMap.clear();
			if (v == v_methods[0]) {
				mSearch.transitSearch(busPlan.from(stNode).to(enNode)
						.city("杭州"));
			} else if (v == v_methods[1]) {
				mSearch.drivingSearch(drivePlan.from(stNode).to(enNode));
			} else if (v == v_methods[2]) {
				mSearch.walkingSearch(walkPlan.from(stNode).to(enNode));
			}
		}
	}

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		MyLog.v(TAG, "自驾结果：" + result);

		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			ShowUtil.showToast(POIRoute.this, "抱歉，未找到结果");
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			MyLog.e(TAG, "起终点或途经点地址有岐义，通过以下接口获取建议查询信息.");
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			nodeIndex = -1;
			route = result.getRouteLines().get(0);
			DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
			getRouteLineInfo(2);

		}
	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult result) {
		MyLog.v(TAG, "公交结果：" + result);
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			ShowUtil.showToast(POIRoute.this, "抱歉，未找到结果");
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			MyLog.e(TAG, "起终点或途经点地址有岐义，通过以下接口获取建议查询信息.");
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			nodeIndex = -1;
			route = result.getRouteLines().get(0);
			TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
			getRouteLineInfo(1);
		}

	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult result) {
		MyLog.v(TAG, "步行结果：" + result);
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			ShowUtil.showToast(POIRoute.this, "抱歉，未找到结果");
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			MyLog.e(TAG, "起终点或途经点地址有岐义，通过以下接口获取建议查询信息.");
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			nodeIndex = -1;
			route = result.getRouteLines().get(0);
			WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
			getRouteLineInfo(3);
		}
	}

	// 定制RouteOverly
	private class MyTransitRouteOverlay extends TransitRouteOverlay {

		public MyTransitRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			// return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
			return null;
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			// return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
			return null;

		}
	}

	private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

		public MyDrivingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);

		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
		}
	}

	private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

		public MyWalkingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
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
		if (mMapView != null) {
			mMapView.onDestroy();
			mMapView = null;
		}

		if (mSearch != null)
			mSearch.destroy();
		;

		super.onDestroy();
	}

	// type 1公交，2自驾，3步行
	void getRouteLineInfo(int type) {
		if (route != null) {
			MyLog.d(TAG, "route.getDistance(): " + route.getDistance());
			MyLog.d(TAG, "route.getDuration(): " + route.getDuration());
			MyLog.d(TAG, "route.getTitle(): " + route.getTitle());
			MyLog.d(TAG, "route.getStarting(): " + route.getStarting());
			MyLog.d(TAG, "route.getTerminal(): " + route.getTerminal());
			MyLog.d(TAG, "route.getAllStep(): " + route.getAllStep());

			if (type == 1) {
				List<TransitRouteLine.TransitStep> list = route.getAllStep();
				if (list != null && !list.isEmpty()) {
					for (int i = 0; i < list.size(); i++) {
						TransitRouteLine.TransitStep step = list.get(i);
						MyLog.v(TAG, "i : " + i);
						MyLog.v(TAG,
								"step.getDistance(): " + step.getDistance());
						MyLog.v(TAG,
								"step.getDuration(): " + step.getDuration());
						MyLog.v(TAG,
								"step.getInstructions(): "
										+ step.getInstructions());
						MyLog.v(TAG, "step.getEntrace(): " + step.getEntrace());
						MyLog.v(TAG, "step.getExit(): " + step.getExit());
						MyLog.v(TAG,
								"step.getStepType(): " + step.getStepType());
						MyLog.v(TAG,
								"step.getVehicleInfo(): "
										+ step.getVehicleInfo());
//						MyLog.v(TAG,
//								"step.getWayPoints(): " + step.getWayPoints());

					}
				}

			}
		}
	}
}
