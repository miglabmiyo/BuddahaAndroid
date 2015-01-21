package map.show;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import map.entity.BuildingAdapter;
import map.entity.BuildingInfo;
import map.task.RecommendBuildingTask;
import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import basic.io.ImageUtil;
import basic.net.ApiDefine;
import basic.util.MyLog;

import com.miglab.buddha.R;

/**
 * @author song
 * @version 创建时间：2014-12-15 下午4:47:39 类说明
 */
public class ArroundHome extends MapActivity {
	ListView lv_recommends;
	BuildingAdapter adapter;
	ArrayList<BuildingInfo> mlist = new ArrayList<BuildingInfo>();

	@Override
	protected void init() {
		this.setContentView(R.layout.ac_map_arround);
		this.setSimpleTitle(R.string.arround);
		initSelections();
		initRecommends();
	}

	void initSelections() {
		GridView gv_selections = (GridView) findViewById(R.id.gv_selections);

		// 生成动态数组，并且转入数据
		ArrayList<HashMap<String, Object>> lstResource = new ArrayList<HashMap<String, Object>>();
		int[] res = { R.drawable.around_simiao, R.string.simiao,
				R.drawable.around_book, R.string.bookstore,
				R.drawable.around_food, R.string.sushiguan,
				R.drawable.around_fojv, R.string.fojuguan,
				R.drawable.around_close, R.string.fuzhuang };
		for (int i = 0; i < res.length; i += 2) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("image", res[i]);
			map.put("text", getString(res[i + 1]));
			lstResource.add(map);
		}

		// 生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
		SimpleAdapter adapter = new SimpleAdapter(this, lstResource,// 数据来源
				R.layout.item_arround_selection,// item的XML实现
				// 动态数组与Item对应的子项
				new String[] { "image", "text" },
				// Item的XML文件里面的一个ImageView,一个TextView的ID
				new int[] { R.id.iv_selection, R.id.tv_selection });

		gv_selections.setAdapter(adapter);
		gv_selections.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO 自动生成的方法存根
				select(arg2);
			}
		});
	}

	void select(int index) {
		if (index < 0 || index >= 5)
			return;

		if (canClicked()) {
			MyLog.d(TAG, "点击了 " + index);

			Intent i = new Intent(this, BuildingSearch.class);
			i.putExtra("latitude", latitude);
			i.putExtra("longtitude", longtitude);
			i.putExtra("type", index + 1);
			this.startActivity(i);
		}
	}

	void initRecommends() {
		lv_recommends = (ListView) findViewById(R.id.lv_recommends);

		adapter = new BuildingAdapter(this, mlist);
		lv_recommends.setAdapter(adapter);

		lv_recommends.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				selectRecommend(arg2);
			}

		});

		new RecommendBuildingTask(h, latitude, longtitude).execute();
	}

	/** handler里的操作 */
	@Override
	protected void doHandler(Message msg) {
		try {
			int nCommand = msg.what;
			switch (nCommand) {
			case ApiDefine.GET_SUCCESS:
				MyLog.d(TAG, "GET_SUCCESS");
				if (msg.obj != null) {
					setRecommends((ArrayList<BuildingInfo>) msg.obj);
				}
				break;
			case ApiDefine.FRESH_IMAGE:
				MyLog.d(TAG, "FRESH_IMAGE");
				if (adapter != null) {
					adapter.notifyDataSetChanged();
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			MyLog.showException(e);
		}
	}

	void setRecommends(ArrayList<BuildingInfo> list) {
		MyLog.i(TAG, "setRecommends");
		if (list != null && !list.isEmpty()) {
			MyLog.i(TAG, "list size: " + list.size());

			mlist.clear();
			mlist.addAll(list);
			adapter.notifyDataSetChanged();

			MyLog.v(TAG, "start image");
			List<String> images = new ArrayList<String>();
			for (BuildingInfo info : mlist) {
				MyLog.v(TAG, "info: " + info);
				if (!TextUtils.isEmpty(info.pic)) {
					MyLog.i(TAG, "info.pic: " + info.pic);
					images.add(info.pic);
				}
			}

			MyLog.v(TAG, "images size: " + images.size());
			if (!images.isEmpty())
				ImageUtil.showIconImage(this, h, images, ApiDefine.FRESH_IMAGE,
						false);

		}
	}

	void selectRecommend(int position) {
		if (position < 0 || mlist.isEmpty() || position >= mlist.size())
			return;

		if (this.canClicked()) {
			building = mlist.get(position);
			gotoNext(BuildingDetail.class);
		}
	}

}
