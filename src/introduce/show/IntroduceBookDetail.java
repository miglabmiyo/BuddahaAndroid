package introduce.show;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import introduce.entity.IntroduceHomeAdapter;
import introduce.task.IntroduceDetailTask;
import com.miglab.buddha.R;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import basic.io.ImageUtil;
import basic.net.ApiDefine;
import basic.show.BaseActivity;
import basic.util.MyLog;
import book.entity.BookInfo;
import book.entity.BookListAdapter;

/**
 * @author 赵龙权
 * @version 创建时间：2015-1-9 上午11:49:52
 * 类说明	佛教历史，宗教思想列表书籍详情
 */
public class IntroduceBookDetail extends BaseActivity{

	BookInfo bookInfo;
	ImageView iv_icon;
	TextView title;
	ListView lv_chatper;
	private List<BookInfo> mlist = new ArrayList<BookInfo>();
	IntroduceHomeAdapter adapter;
	ArrayList<BookInfo> chapterList; //章节
	
	protected void init() {
		this.setContentView(R.layout.ac_introduce_detail);
		bookInfo = (BookInfo) getIntent().getSerializableExtra("book");

		initTitle();
		if (bookInfo != null && bookInfo.id > 0) {
			initInfo();
		}
	}
	
	void initTitle() {
		title = (TextView) this.findViewById(R.id.title_content);
		title.setText(bookInfo.name);
		lv_chatper = (ListView) this.findViewById(R.id.lv_intro_content);

	}

	void initInfo() {
		iv_icon = (ImageView) findViewById(R.id.iv_introduce);
		TextView name = (TextView) findViewById(R.id.tv_name);
		name.setText(bookInfo.name);

		new IntroduceDetailTask(h, bookInfo).execute();

		if (!TextUtils.isEmpty(bookInfo.pic) && !setImage()) {
			updateImage();
		}
		
		adapter = new IntroduceHomeAdapter(this, mlist);
		lv_chatper.setAdapter(adapter);
		
		lv_chatper.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	@Override
	protected void doHandler(Message msg) {
		try {
			int nCommand = msg.what;
			switch (nCommand) {
			case ApiDefine.GET_SUCCESS_OBJ:
				MyLog.d(TAG, "GET_SUCCESS");
				if (msg.obj != null) {
					Map<String, Object> objs = (Map<String, Object>) msg.obj;
					if (objs != null) {
						setResult(objs);
					}
				}

				break;
			case ApiDefine.GET_SUCCESS_LIST:
				MyLog.d(TAG, "GET_SUCCESS");
				if (msg.obj != null) {
					Object[] obj = (Object[]) msg.obj;
					if (obj != null) {
						chapterList = (ArrayList<BookInfo>) obj[0];
						showIntroduceBookListView(chapterList);
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
	
	void setResult(Map<String, Object> objs) {
		String text = "";
		TextView tv = null;
		String[] index = { "chapter", "summary" };
		String[] texts = { "共？章", "？" };
		int[] res = { R.id.tv_chapter, R.id.tv_booksummary };
		for (int i = 0; i < res.length; i++) {
			tv = (TextView) findViewById(res[i]);
			text = "";
			if (i == 0) {
				text = "" + (Integer) objs.get(index[i]);
			} else {
				text = (String) objs.get(index[i]);
			}
			if (!TextUtils.isEmpty(text))
				text = texts[i].replace("？", text);
			tv.setText(text);
		}

	}

	private void showIntroduceBookListView(List<BookInfo> list) {
		// TODO Auto-generated method stub
		if (list != null && !list.isEmpty()) {
			MyLog.i(TAG, "list size: " + list.size());

			mlist.clear();
			mlist.addAll(list);
			adapter.notifyDataSetChanged();
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
		if (!images.isEmpty())
			ImageUtil.showIconImage(this, h, images, ApiDefine.FRESH_IMAGE,
					false);
	}
	
}
