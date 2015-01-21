package introduce.show;

import introduce.task.IntroduceSearchTypeTask;
import java.util.ArrayList;
import java.util.List;
import com.miglab.buddha.R;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import basic.io.ImageUtil;
import basic.net.ApiDefine;
import basic.show.BaseActivity;
import basic.util.DrawableClickEditText;
import basic.util.MyLog;
import book.entity.BookInfo;
import book.entity.BookListAdapter;
import book.show.BookDetail;

/**
 * @author 赵龙权
 * @version 创建时间：2015-1-9 上午10:28:16
 * 类说明	介绍二级界面（佛教历史和宗教思想）
 */
public class IntroduceListPage extends BaseActivity{

	ListView lv_result;
	BookListAdapter adapter;
	ArrayList<BookInfo> mlist = new ArrayList<BookInfo>();
	ArrayList<BookInfo> historyList, thoughtList; //佛教历史，宗教思想
	int type;
	boolean isNew;
	TextView menu_history, menu_though;
	final static int COLOR_SEL = 0xff799692, COLOR_UNSEL = 0xfff5f5f5;
//	private Button btn_search;
//	private View searchbar;
//	private DrawableClickEditText ed_content;

	protected void init() {
		this.setContentView(R.layout.ac_book_list);
		type = getIntent().getIntExtra("type", 0);
		int[] res = { R.string.history_thought_list, R.string.history_thought_list,
						R.string.browse_list};
		this.setSimpleTitle(res[type - 1]);
		initMenu();
		initTypeResult();
	}

	void initMenu() {
//		searchbar = (View) findViewById(R.id.searchbook_searchbar);
//		searchbar.setVisibility(View.VISIBLE);
//		btn_search = (Button) findViewById(R.id.search_button);
//		ed_content = (DrawableClickEditText) findViewById(R.id.search_input);
//		ed_content.setHint(R.string.search);
		
		menu_history = (TextView) findViewById(R.id.book_sel1);
		menu_though = (TextView) findViewById(R.id.book_sel2);
		if(type == 1 || type == 3 ){
			selectMenu(menu_history);
		}
		if(type == 2 || type == 4){
			selectMenu(menu_though);
		}
		if(type == 1 || type == 2){
			menu_history.setText(R.string.history_list);
			menu_though.setText(R.string.thought_list);
		}else{
			menu_history.setText(R.string.shuhua_list);
			menu_though.setText(R.string.artware_list);
		}

		OnClickListener menuClicker = new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				selectMenu(arg0);
			}
		};

		menu_history.setOnClickListener(menuClicker);
		menu_though.setOnClickListener(menuClicker);
		
//		setListener();
	}
	
//	private void setListener() {
//		// TODO Auto-generated method stub
//		ed_content.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before,
//					int count) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//				// TODO Auto-generated method stub
//				if (s != null && s.length() > 0) {
//					changeSearchView(true);
//				} else {
//					changeSearchView(false);
//				}
//			}
//		});
//
//		// 搜索
//		btn_search.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//	}

	void initTypeResult() {
		lv_result = (ListView) findViewById(R.id.lv_result);

		adapter = new BookListAdapter(this, mlist, R.layout.item_booklist2);
		lv_result.setAdapter(adapter);

		OnItemClickListener bookClicker = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (IntroduceListPage.this.canClicked()) {
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
		
		if(type == 1 || type == 2){
			new IntroduceSearchTypeTask(h, 1).execute();
			new IntroduceSearchTypeTask(h, 2).execute();
		}
		if(type == 3 || type == 4){
			new IntroduceSearchTypeTask(h, 3).execute();
			new IntroduceSearchTypeTask(h, 4).execute();
		}
		
//		new IntroduceSearchTypeTask(h, type).execute();
//		selectMenu(menu_history);
	}

	void selectBook(BookInfo book) {
		if (book == null)
			return;
		
		if(type == 1 || type == 2 ){
			Intent i = new Intent(this, BookDetail.class);
			i.putExtra("book", book);
			this.startActivity(i);
		}else{
			Intent i = new Intent(this, IntroduceArtBookDetail.class);
			Bundle bundle = new Bundle();
			bundle.putInt("id", book.id);
			bundle.putString("name", book.name);
			i.putExtras(bundle);
			this.startActivity(i);
		}
	}

	void selectMenu(View v) {
		if (v == null)
			return;

		if (v == menu_history) {
			isNew = true;
			menu_history.setBackgroundColor(COLOR_SEL);
			menu_though.setBackgroundColor(COLOR_UNSEL);
			setResults(historyList);
		} else if (v == menu_though) {
			isNew = false;
			menu_though.setBackgroundColor(COLOR_SEL);
			menu_history.setBackgroundColor(COLOR_UNSEL);
			setResults(thoughtList);
		}
	}
	
//	private void changeSearchView(boolean isSearch) {
//		if (isSearch) {
//			btn_search.setVisibility(View.VISIBLE);
//		} else {
//			btn_search.setVisibility(View.GONE);
//		}
//	}

	/** handler里的操作 */
	protected void doHandler(Message msg) {
		try {
			int nCommand = msg.what;
			switch (nCommand) {
			case ApiDefine.GET_INTRODUCE_HISTORY_SUCCESS:
				MyLog.d(TAG, "GET_SUCCESS");
				if (msg.obj != null) {
					Object[] objs = (Object[]) msg.obj;
					if (objs != null) {
						historyList = (ArrayList<BookInfo>) objs[0];
						setResults(historyList);
						updateImage();
					}
				}
				break;
			case ApiDefine.GET_INTRODUCE_THOUGHT_SUCCESS:
				MyLog.d(TAG, "GET_SUCCESS");
				if (msg.obj != null) {
					Object[] objs = (Object[]) msg.obj;
					if (objs != null) {
						thoughtList = (ArrayList<BookInfo>) objs[0];
						setResults(thoughtList);
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
		if (historyList != null && !historyList.isEmpty())
			for (BookInfo info : historyList) {
				MyLog.v(TAG, "info: " + info);
				if (!TextUtils.isEmpty(info.pic)) {
					MyLog.i(TAG, "info.pic: " + info.pic);
					images.add(info.pic);
				}
			}

		if (thoughtList != null && !thoughtList.isEmpty())
			for (BookInfo info : thoughtList) {
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
