package book.show;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import basic.io.ImageUtil;
import basic.net.ApiDefine;
import basic.show.BaseActivity;
import basic.show.Home;
import basic.util.MyLog;
import book.entity.BookDetailInfo;
import book.entity.BookInfo;
import book.task.BookShelfTask;

import com.miglab.buddha.R;

/**
 * @author song
 * @version 创建时间：2015-1-6 下午7:10:03 类说明 书架
 */
public class BookShelf extends BaseActivity implements OnItemClickListener{
	ShelfGridView lv_result;
	BookAdapter adapter;
	ArrayList<BookDetailInfo> mlist = new ArrayList<BookDetailInfo>();

	protected void init() {
		this.setContentView(R.layout.ac_bookshelf);
		initTitle();

		new BookShelfTask(h).execute();

		lv_result = (ShelfGridView) findViewById(R.id.book_shelf);
		adapter = new BookAdapter(this, mlist);
		lv_result.setAdapter(adapter);
		lv_result.setOnItemClickListener(this);
	}

	void initTitle() {
		this.setSimpleTitle(R.string.book_shelf);
		TextView right = (TextView) this.findViewById(R.id.title_right);
		right.setText(R.string.bookmall);
		right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				next(Home.class);
			}
		});

	}

	/** handler里的操作 */
	protected void doHandler(Message msg) {
		try {
			int nCommand = msg.what;
			switch (nCommand) {
			case ApiDefine.GET_SUCCESS:
				MyLog.d(TAG, "GET_SUCCESS");
				if (msg.obj != null) {
					ArrayList<BookDetailInfo> list = (ArrayList<BookDetailInfo>) msg.obj;
					if (list != null && !list.isEmpty()) {
						setResult(list);
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

	void setResult(ArrayList<BookDetailInfo> list) {
		mlist.clear();
		if (list != null && list.size() > 0) {
			mlist.addAll(list);
			adapter.notifyDataSetChanged();
			updateImage();
		}
	}

	class BookAdapter extends BaseAdapter {
		Context con;
		ArrayList<BookDetailInfo> list = new ArrayList<BookDetailInfo>();

		public BookAdapter(Context con, ArrayList<BookDetailInfo> list) {
			this.con = con;
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list == null ? 0 : list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return list == null ? null : list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			BookHolder holder;
			if (view == null) {
				holder = new BookHolder();
				view = holder.parent;
				view.setTag(holder);
			} else {
				holder = (BookHolder) view.getTag();
			}
			if (position < list.size()) {
				holder.setData(list.get(position));
			}
			return view;
		}

		class BookHolder {
			View parent;
			ImageView iv;
			String imgUrl;

			public BookHolder() {
				parent = LayoutInflater.from(con).inflate(
						R.layout.item_bookshelf, null);
				iv = (ImageView) parent.findViewById(R.id.item_icon);
			}

			void setData(BookInfo book) {
				if (book == null || TextUtils.isEmpty(book.pic))
					return;

				if (imgUrl == null || !imgUrl.equals(book.pic)) {
					imgUrl = book.pic;
					if (!ImageUtil
							.setIcon(iv, imgUrl, con, ImageUtil.ICON_PATH))
						iv.setImageResource(R.drawable.book_a);
				}
			}
		}

	}

	void updateImage() {
		MyLog.v(TAG, "start image");
		List<String> images = new ArrayList<String>();
		if (mlist != null && !mlist.isEmpty())
			for (BookInfo info : mlist) {
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
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if(this.canClicked() && arg2 < mlist.size()){
			BookDetailInfo book = mlist.get(arg2);
			Intent i = new Intent(this, BookReader.class);
			i.putExtra("book", book);
			i.putExtra("readtype", 1);
			this.startActivity(i);
		}
	}

}
