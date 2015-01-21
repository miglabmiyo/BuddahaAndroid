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
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import basic.io.ImageUtil;
import basic.net.ApiDefine;
import basic.show.BaseActivity;
import basic.util.MyLog;
import basic.util.ShowUtil;
import book.entity.BookDetailInfo;
import book.entity.BookInfo;
import book.task.AddToShelfTask;
import book.task.BookDetailTask;

import com.miglab.buddha.R;

/**
 * @author song
 * @version 创建时间：2015-1-5 上午11:33:40 类说明 书城--书籍详情
 */
public class BookDetail extends BaseActivity {
	BookInfo bookInfo;
	BookDetailInfo detailInfo;
	ImageView iv_icon;
	boolean isAddShelf;

	@Override
	protected void init() {
		this.setContentView(R.layout.ac_book_detail);
		initTitle();
		
		Intent i = getIntent();
		if(i.hasExtra("book")){
			bookInfo = (BookInfo) i.getSerializableExtra("book");
			if (bookInfo != null && bookInfo.id > 0) {
				initInfo();
			}
		}		
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO 自动生成的方法存根
		super.onNewIntent(intent);
		if(intent.hasExtra("book")){
			BookInfo info = (BookInfo) intent.getSerializableExtra("book");;
		}
		
	}



	void initTitle() {
		this.setSimpleTitle(R.string.book_detail);
		TextView right = (TextView) this.findViewById(R.id.title_right);
		right.setText(R.string.book_shelf);
		right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				nextWithoutFinish(BookShelf.class);
			}
		});

	}

	void initInfo() {
		iv_icon = (ImageView) findViewById(R.id.iv_icon);
		TextView name = (TextView) findViewById(R.id.tv_bookname);
		name.setText(bookInfo.name);

		new BookDetailTask(h, bookInfo).execute();

		if (!TextUtils.isEmpty(bookInfo.pic) && !setImage()) {
			updateImage();
		}

		// 免费试读
		findViewById(R.id.book_sel1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				freeRead();
			}
		});

		// 保存书架
		findViewById(R.id.book_sel2).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				putIntoShelf();
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
					if (msg.arg1 == 1) { // 获取书籍详细信息
						detailInfo = (BookDetailInfo) msg.obj;
						if (detailInfo != null) {
							setResult();
						}
					} else if (msg.arg1 == 2) { // 加入书架
						String token = (String) msg.obj;
						if (!TextUtils.isEmpty(token) && detailInfo != null) {
							detailInfo.booktoken = token;
							findViewById(R.id.book_sel2).setEnabled(false);
							ShowUtil.showToast(this, "加入书架成功！");
						}
					}
				}
				break;
			case ApiDefine.FRESH_IMAGE:
				MyLog.d(TAG, "FRESH_IMAGE");
				setImage();
				break;
			default:
				break;
			}
		} catch (Exception e) {
			MyLog.showException(e);
		}
	}

	void setResult() {
		if (detailInfo == null)
			return;

		String text = "";
		TextView tv = null;
		String[] texts = { "作者：？", "出版时间：？", "共？章", "？" };
		Object[] contents = { detailInfo.author, detailInfo.pubtime,
				detailInfo.chapter, detailInfo.summary };
		int[] res = { R.id.tv_author, R.id.tv_date, R.id.tv_bookchapter,
				R.id.tv_booksummary };
		for (int i = 0; i < res.length; i++) {
			tv = (TextView) findViewById(res[i]);
			text = "" + contents[i];
			if (!TextUtils.isEmpty(text))
				text = texts[i].replace("？", text);
			tv.setText(text);
		}

		texts = detailInfo.labels;
		if (texts != null && texts.length > 0) {
			GridView gv = (GridView) findViewById(R.id.gv_bookmark);
			LabelAdapter adapter = new LabelAdapter(this, texts);
			gv.setAdapter(adapter);
		}

	}

	class LabelAdapter extends BaseAdapter {
		Context con;
		String[] list;
		View[] views;

		public LabelAdapter(Context con, String[] list) {
			this.con = con;
			this.list = list;

			if (list != null) {
				views = new View[list.length];
			}
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list == null ? 0 : list.length;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return list == null ? null : list[arg0];
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			if (views != null && position < views.length) {
				if (views[position] == null) {
					View v = LayoutInflater.from(con).inflate(
							R.layout.item_book_label, null);
					views[position] = v;
				}
				view = views[position];

				if (list != null && position < list.length) {
					TextView tv = (TextView) view.findViewById(R.id.item_label);
					tv.setText(list[position]);
				}

			}
			return view;
		}

		@Override
		public void notifyDataSetChanged() {
			if (list == null)
				return;

			if (views == null || views.length != list.length) {
				views = new View[list.length];
			}
			super.notifyDataSetChanged();
		}

	}

	boolean setImage() {
		return ImageUtil.setIcon(iv_icon, bookInfo.pic, this,
				ImageUtil.ICON_PATH);
	}

	void updateImage() {
		MyLog.v(TAG, "start image");
		List<String> images = new ArrayList<String>();
		images.add(bookInfo.pic);

		// MyLog.v(TAG, "images size: " + images.size());
		if (!images.isEmpty())
			ImageUtil.showIconImage(this, h, images, ApiDefine.FRESH_IMAGE,
					false);
	}

	void freeRead() {
		if (this.canClicked() && detailInfo != null
				&& !TextUtils.isEmpty(detailInfo.freeRead)) {
			Intent i = new Intent(this, BookReader.class);
			i.putExtra("book", detailInfo);
			i.putExtra("readtype", 0);
			startActivity(i);
		}
	}

	void putIntoShelf() {
		if (this.canClicked()) {
			if (!isAddShelf)
				new AddToShelfTask(h, bookInfo.id).execute();
			else
				ShowUtil.showToast(this, "已经加入书架了！");
		}
	}
}