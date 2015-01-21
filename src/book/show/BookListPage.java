package book.show;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import basic.io.ImageUtil;
import basic.net.ApiDefine;
import basic.show.BaseActivity;
import basic.util.MyLog;
import book.entity.BookInfo;
import book.entity.BookListAdapter;
import book.task.SearchBookListTask;

import com.miglab.buddha.R;

/**
 * @author song
 * @version 创建时间：2014-12-27 下午10:51:32 
 * 类说明 书城--书籍列表
 */
public class BookListPage extends BaseActivity {
	ListView lv_result;
	BookListAdapter adapter;
	ArrayList<BookInfo> mlist = new ArrayList<BookInfo>();
	ArrayList<BookInfo> newlist, hotlist;
	int type;
	boolean isNew;
	TextView menu_new, menu_hot;
	final static int COLOR_SEL = 0xff799692, COLOR_UNSEL = 0xfff5f5f5;

	protected void init() {
		this.setContentView(R.layout.ac_book_list);
		type = getIntent().getIntExtra("type", 0);
		int[] res = { R.string.jing, R.string.yigui, R.string.lun,
				R.string.modern };
		this.setSimpleTitle(res[type]);
		initMenu();
		initTypeResult();
	}

	void initMenu() {
		menu_new = (TextView) findViewById(R.id.book_sel1);
		menu_hot = (TextView) findViewById(R.id.book_sel2);

		OnClickListener menuClicker = new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				selectMenu(arg0);
			}

		};

		menu_new.setOnClickListener(menuClicker);
		menu_hot.setOnClickListener(menuClicker);
		
	}

	void initTypeResult() {
		lv_result = (ListView) findViewById(R.id.lv_result);

		adapter = new BookListAdapter(this, mlist, R.layout.item_booklist2);
		lv_result.setAdapter(adapter);

		OnItemClickListener bookClicker = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (BookListPage.this.canClicked()) {
					BaseAdapter adapter = (BaseAdapter) arg0.getAdapter();
					if (adapter != null) {
						Object obj = adapter.getItem(arg2);
						if (obj != null) {
							BookInfo book = (BookInfo) obj;
							selectBook(book);
						}
					}
				}

			}

		};
		lv_result.setOnItemClickListener(bookClicker);

		new SearchBookListTask(h, ++type).execute();

		selectMenu(menu_new);
	}

	void selectBook(BookInfo book) {
		if (book == null)
			return;

		Intent i = new Intent(this, BookDetail.class);
		i.putExtra("book", book);
		this.startActivity(i);
	}

	void selectMenu(View v) {
		if (v == null)
			return;

		if (v == menu_new) {
			isNew = true;
			menu_new.setBackgroundColor(COLOR_SEL);
			menu_hot.setBackgroundColor(COLOR_UNSEL);
			setResults(newlist);
		} else if (v == menu_hot) {
			isNew = false;
			menu_hot.setBackgroundColor(COLOR_SEL);
			menu_new.setBackgroundColor(COLOR_UNSEL);
			setResults(hotlist);
		}
	}

	/** handler里的操作 */
	protected void doHandler(Message msg) {
		try {
			int nCommand = msg.what;
			switch (nCommand) {
			case ApiDefine.GET_SUCCESS:
				MyLog.d(TAG, "GET_SUCCESS");
				if (msg.obj != null) {
					Object[] objs = (Object[]) msg.obj;
					if (objs != null) {
						newlist = (ArrayList<BookInfo>) objs[0];
						hotlist = (ArrayList<BookInfo>) objs[1];
						setResults(isNew ? newlist : hotlist);
						updateImage();
					}
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

	void setResults(ArrayList<BookInfo> list) {
		MyLog.i(TAG, "setResults");
		if (list != null && !list.isEmpty()) {
			MyLog.i(TAG, "list size: " + list.size());

			mlist.clear();
			mlist.addAll(list);
			adapter.notifyDataSetChanged();
		}
	}

	void updateImage() {
		MyLog.v(TAG, "start image");
		List<String> images = new ArrayList<String>();
		if (newlist != null && !newlist.isEmpty())
			for (BookInfo info : newlist) {
				MyLog.v(TAG, "info: " + info);
				if (!TextUtils.isEmpty(info.pic)) {
					MyLog.i(TAG, "info.pic: " + info.pic);
					images.add(info.pic);
				}
			}

		if (hotlist != null && !hotlist.isEmpty())
			for (BookInfo info : hotlist) {
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
