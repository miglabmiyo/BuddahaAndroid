package home.fragment;

import java.util.ArrayList;

import map.show.ActiveListPage;

import com.miglab.buddha.R;

import active.entity.ActiveGridAdapter;
import active.entity.ActiveInfo;
import active.entity.ActiveListAdapter;
import active.task.ActiveListTask;
import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import basic.net.ApiDefine;
import basic.show.BaseFragment;
import basic.util.MyLog;
import book.show.ScrollListView;

/**
 * @author 赵龙权
 * @version 创建时间：2015-1-6 下午7:18:22
 * 类说明	活动
 */
public class ActiveFragment extends BaseFragment{

	BaseView[] views = new BaseView[2];
	
	@Override
	protected void setLayout() {
		// TODO Auto-generated method stub
		rootResource = R.layout.fm_active;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		new ActiveListTask(h).execute();
		views[0] = new ActiveGridView(R.id.ly_active_menus);
		views[1] = new ActiveListView(R.id.ly_active_other, R.string.active_list);
	}

	@Override
	protected void doHandler(Message msg) {
		// TODO Auto-generated method stub
		try {
			int nCommand = msg.what;
			switch (nCommand) {
			case ApiDefine.GET_SUCCESS:
				MyLog.d(TAG, "GET_SUCCESS");
				if (msg.obj != null) {
					Object[] objs = (Object[]) msg.obj;
					if (objs != null && objs.length >= views.length)
						for (int i = 0; i < views.length; i++) {
							views[i].onDataUpdated(objs[i]);
						}
				}

				break;
			default:
				break;
			}
		} catch (Exception e) {
			MyLog.showException(e);
		}
	}
	
	class ActiveListView extends BaseView {

		public ArrayList<ActiveInfo> list;
		ScrollListView lv;

		public ActiveListView(int parentId, int nameRes) {
			View parent = vRoot.findViewById(parentId);
			TextView title = (TextView) parent
					.findViewById(R.id.tv_active_title);
			title.setText(nameRes);
			lv = (ScrollListView) parent.findViewById(R.id.lv_actives);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onDataUpdated(Object obj) {
			super.onDataUpdated(obj);
			if (obj == null)
				return;

			list = (ArrayList<ActiveInfo>) obj;
			if (list != null && !list.isEmpty()) {
				ActiveListAdapter adapter = new ActiveListAdapter(ac, list,
						R.layout.item_activelist);
				lv.setAdapter(adapter);
				lv.setOnItemClickListener(activeClicker);
			}
		}

	}
	
	OnItemClickListener activeClicker = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			BaseAdapter adapter = (BaseAdapter) arg0.getAdapter();
			if (adapter != null) {
				Object obj = adapter.getItem(arg2);
				if (obj != null) {
					ActiveInfo active = (ActiveInfo) obj;
					Intent i = new Intent(ac, ActiveListPage.class);
					i.putExtra("active", active);
					ac.startActivity(i);
				}
			}
		}

	};
	
	class ActiveGridView extends BaseView {

		public ArrayList<ActiveInfo> list;
		GridView gv;

		public ActiveGridView(int parentId) {
			View parent = vRoot.findViewById(parentId);
			gv = (GridView) parent.findViewById(R.id.gv_actives);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onDataUpdated(Object obj) {
			super.onDataUpdated(obj);
			if (obj == null)
				return;

			list = (ArrayList<ActiveInfo>) obj;
			if (list != null && !list.isEmpty()) {
				ActiveGridAdapter adapter = new ActiveGridAdapter(ac, list);
				gv.setAdapter(adapter);
				gv.setOnItemClickListener(activeClicker);
			}
		}

	}

}
