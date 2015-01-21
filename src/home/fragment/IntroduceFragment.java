package home.fragment;

import introduce.show.IntroduceArtBookDetail;
import introduce.show.IntroduceBookDetail;
import introduce.show.IntroduceListPage;
import introduce.task.IntroduceHomeTask;

import java.util.ArrayList;
import com.miglab.buddha.R;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import basic.net.ApiDefine;
import basic.show.BaseFragment;
import basic.util.MyLog;
import book.entity.BookInfo;
import book.entity.BookListAdapter;
import book.show.ScrollListView;
import book.task.BookMallTask;

/**
 * @author 赵龙权
 * @version 创建时间：2015-1-7 下午5:09:23
 * 类说明	介绍
 */
public class IntroduceFragment extends BaseFragment{

	BaseView[] views = new BaseView[3];

	@Override
	protected void setLayout() {
		rootResource = R.layout.fm_introduce;
	}

	@Override
	protected void initView() {
//		new BookMallTask(h).execute();
		new IntroduceHomeTask(h).execute();

		initThreeMenu();
		views[0] = new BookListView(R.id.ly_introduce_history, R.string.history_list);
		views[1] = new BookListView(R.id.ly_introduce_thought, R.string.thought_list);
		views[2] = new BookListView(R.id.ly_introduce_browse, R.string.browse_list);
	}
	
	@Override
	protected void doHandler(Message msg) {
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
	
	View[] menus = new View[3];

	void initThreeMenu() {
		int[] ids = { R.id.introduce_menu1, R.id.introduce_menu2, R.id.introduce_menu3 };
		View parent = vRoot.findViewById(R.id.ly_introduce_menus);
		OnClickListener l = new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				clickMenu(arg0);
			}

		};

		for (int i = 0; i < ids.length; i++) {
			menus[i] = parent.findViewById(ids[i]);
			menus[i].setOnClickListener(l);
		}
	}
	
	void clickMenu(View v) {
		if (this.canClicked()) {
			for (int i = 0; i < menus.length; i++) {
				if (menus[i] == v) {
					int type;
					type = i + 1;
					Intent intent = new Intent(ac, IntroduceListPage.class);
//					intent.putExtra("type", i);
					intent.putExtra("type", type);
					ac.startActivity(intent);
					return;
				}
			}
		}
	}
	
	class BookListView extends BaseView {

		public ArrayList<BookInfo> list;
		ScrollListView lv;

		public BookListView(int parentId, int nameRes) {
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

			list = (ArrayList<BookInfo>) obj;
			if (list != null && !list.isEmpty()) {
				BookListAdapter adapter = new BookListAdapter(ac, list,
						R.layout.item_activelist);
				lv.setAdapter(adapter);
				lv.setOnItemClickListener(bookClicker);
			}
		}

	}

	OnItemClickListener bookClicker = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			BaseAdapter adapter = (BaseAdapter) arg0.getAdapter();
			if (adapter != null) {
				Object obj = adapter.getItem(arg2);
				if (obj != null) {
					BookInfo book = (BookInfo) obj;
					if(book.type == 1 || book.type ==2){
						Intent i = new Intent(ac, IntroduceBookDetail.class);
						i.putExtra("book", book);
						ac.startActivity(i);
					}else{
						Intent i = new Intent(ac, IntroduceArtBookDetail.class);
						Bundle bundle = new Bundle();
						bundle.putInt("id", book.id);
						bundle.putString("name", book.name);
						i.putExtras(bundle);
						ac.startActivity(i);
					}
					
//					Intent i = new Intent(ac, IntroduceBookDetail.class);
//					i.putExtra("book", book);
//					ac.startActivity(i);
				}
			}
		}

	};
	
}
