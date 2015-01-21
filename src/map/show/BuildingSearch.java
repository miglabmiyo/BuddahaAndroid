package map.show;

import java.util.ArrayList;
import java.util.List;

import map.entity.BuildingAdapter;
import map.entity.BuildingInfo;
import map.task.SearchTypeBuildingTask;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import basic.io.ImageUtil;
import basic.net.ApiDefine;
import basic.util.MyLog;

import com.miglab.buddha.R;

/**
 * @author song
 * @version 创建时间：2014-12-21 下午10:46:50 类说明 搜索建筑
 */
public class BuildingSearch extends MapActivity {
	ListView lv_result;
	BuildingAdapter adapter;
	ArrayList<BuildingInfo> mlist = new ArrayList<BuildingInfo>();
	int type;

	@Override
	protected void init() {
		this.setContentView(R.layout.ac_map_search);
		this.setSimpleTitle(R.string.search);
		initTypeResult();
	}

	void initTypeResult() {
		lv_result = (ListView) findViewById(R.id.lv_result);

		adapter = new BuildingAdapter(this, mlist);
		lv_result.setAdapter(adapter);

		lv_result.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				select(arg2);
			}

		});

		type = getIntent().getIntExtra("type", 0);
		new SearchTypeBuildingTask(h, type, latitude, longtitude).execute();
	}

	void select(int position) {
		if (position < 0 || mlist.isEmpty() || position >= mlist.size())
			return;

		if (this.canClicked()) {
			building = mlist.get(position);
			gotoNext(BuildingDetail.class);
		}
	}

	/** h里的操作 */
	@Override
	protected void doHandler(Message msg) {

		try {
			int nCommand = msg.what;
			switch (nCommand) {
			case ApiDefine.GET_SUCCESS:
				MyLog.d(TAG, "GET_SUCCESS");
				if (msg.obj != null) {
					setResults((ArrayList<BuildingInfo>) msg.obj);
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

	void setResults(ArrayList<BuildingInfo> list) {
		MyLog.i(TAG, "setResults");
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

	class SearchMenu {

	}
}
