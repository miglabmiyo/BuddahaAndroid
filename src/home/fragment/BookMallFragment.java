package home.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import basic.net.ApiDefine;
import basic.show.BaseFragment;
import basic.util.MyLog;
import book.entity.BookGridAdapter;
import book.entity.BookInfo;
import book.entity.BookListAdapter;
import book.show.BookDetail;
import book.show.BookListPage;
import book.show.ScrollListView;
import book.task.BookMallTask;

import com.miglab.buddha.R;

/**
 * @author song
 * @version 创建时间：2014-12-27 下午5:41:55 类说明 书城
 */
public class BookMallFragment extends BaseFragment {
	BaseView[] views = new BaseView[4];

	@Override
	protected void setLayout() {
		rootResource = R.layout.fm_bookmall;
	}

	@Override
	protected void initView() {
		new BookMallTask(h).execute();

		initFourMenu();
		views[0] = new BookListView(R.id.ly_books_recommend,
				R.string.hot_recommend);
		views[1] = new BookGridView(R.id.ly_books_jing, R.string.jing);
		views[2] = new BookGridView(R.id.ly_books_yigui, R.string.yigui);
		views[3] = new BookGridView(R.id.ly_books_lun, R.string.lun);
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

	View[] menus = new View[4];

	void initFourMenu() {
		int[] ids = { R.id.book_menu1, R.id.book_menu2, R.id.book_menu3,
				R.id.book_menu4 };
		View parent = vRoot.findViewById(R.id.ly_books_menus);
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
					Intent intent = new Intent(ac, BookListPage.class);
					intent.putExtra("type", i);
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
					.findViewById(R.id.tv_books_title);
			title.setText(nameRes);
			lv = (ScrollListView) parent.findViewById(R.id.lv_books);
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
						R.layout.item_booklist);
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
					Intent i = new Intent(ac, BookDetail.class);
					i.putExtra("book", book);
					ac.startActivity(i);
				}
			}
		}

	};

	class BookGridView extends BaseView {

		public ArrayList<BookInfo> list;
		GridView gv;

		public BookGridView(int parentId, int nameRes) {
			View parent = vRoot.findViewById(parentId);
			TextView title = (TextView) parent
					.findViewById(R.id.tv_books_title);
			title.setText(nameRes);
			gv = (GridView) parent.findViewById(R.id.gv_books);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onDataUpdated(Object obj) {
			super.onDataUpdated(obj);
			if (obj == null)
				return;

			list = (ArrayList<BookInfo>) obj;
			if (list != null && !list.isEmpty()) {
				BookGridAdapter adapter = new BookGridAdapter(ac, list);
				gv.setAdapter(adapter);
				gv.setOnItemClickListener(bookClicker);
			}
		}

	}
}
