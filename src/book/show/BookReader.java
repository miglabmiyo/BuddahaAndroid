package book.show;

import java.util.ArrayList;

import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import basic.net.ApiDefine;
import basic.show.BaseActivity;
import basic.show.LoadingDialog;
import basic.util.MyLog;
import book.entity.BookDetailInfo;
import book.entity.ChapterInfo;
import book.entity.ReaderAdapter;
import book.task.FreeReadTask;
import book.task.GetChapterTask;
import book.task.ReadChapterTask;

import com.miglab.buddha.R;

/**
 * @author song
 * @version 创建时间：2015-1-7 下午6:57:23 类说明 阅读器
 */
public class BookReader extends BaseActivity {
	BookDetailInfo book;
	int readType = 0; // 0是免费试读，1是章节阅读
	ReaderView reader;
	ReaderAdapter adapter;
	ArrayList<ChapterInfo> chapters = new ArrayList<ChapterInfo>();
	int curChapter = 0;
	LoadingDialog loading;

	@Override
	protected void init() {
		this.setContentView(R.layout.ac_book_reader);
		book = (BookDetailInfo) getIntent().getSerializableExtra("book");
		readType = getIntent().getIntExtra("readtype", 0);
		MyLog.d(TAG, "readType：" + readType + ", bookInfo: " + book);
		if (book != null) {
			if (readType == 0) // 免费阅读
				new FreeReadTask(h, book.freeRead).execute();
			else
				// 章节阅读
				new GetChapterTask(h, book).execute();
		}

		initReader();
	}

	@Override
	protected void doHandler(Message msg) {
		// TODO 自动生成的方法存根
		super.doHandler(msg);
		if (msg.what == ApiDefine.GET_SUCCESS) {
			MyLog.d(TAG, "GET_SUCCESS");
			if (msg.obj != null) {
				if (msg.arg1 == 1) { // 免费阅读
					ChapterInfo chapter = (ChapterInfo) msg.obj;
					MyLog.d(TAG, "chapter: " + chapter);
					if (chapter != null) {
						chapters.add(chapter);
						loadChapter(chapter, 0);
					}
				} else if (msg.arg1 == 2) { // 章节阅读
					ArrayList<ChapterInfo> list = (ArrayList<ChapterInfo>) msg.obj;
					MyLog.d(TAG, "list: " + list);
					parseChapters(list);
				} else if (msg.arg1 == 3) { // 加载单章
					String content = (String) msg.obj;
					if (!TextUtils.isEmpty(content) && !chapters.isEmpty()) {
						ChapterInfo one = chapters.get(msg.arg2);
						one.content = content;
						if (one != null) {
							loadChapter(one, msg.arg2);
						}
					} else {
						setError();
					}
				}
			}
		} else if (msg.what == 1) {
			if (msg.arg1 > 0 && msg.arg1 < chapters.size()) {
				ChapterInfo c = chapters.get(msg.arg1);
				if (c != null) {
					new ReadChapterTask(h, c.url, msg.arg1).execute();
					// readTask.setData(c.url, msg.arg1);
					// readTask.execute();
				}
			}
		} else {
			setError();
		}
	}

	void initReader() {
		reader = (ReaderView) findViewById(R.id.reader);
		loading = new LoadingDialog(this);
		loading.start();
	}

	void loadChapter(ChapterInfo chapter, int index) {
		if (chapter == null || TextUtils.isEmpty(chapter.content)) {
			setError();
			return;
		}

		chapter.isLoaded = true;

		if (adapter == null) {
			adapter = new ReaderAdapter(this, book.name, chapters);
			reader.setAdapter(adapter);
		} else {
			adapter.updateData(index);
		}
		h.sendMessageDelayed(h.obtainMessage(1, ++index, 0), 500);

		if (loading != null)
			loading.stop();
	}

	void parseChapters(ArrayList<ChapterInfo> list) {
		if (list != null && !list.isEmpty()) {
			chapters.clear();
			chapters.addAll(list);
			ChapterInfo one = chapters.get(0);
			new ReadChapterTask(h, one.url, 0).execute();
		} else {
			setError();
		}
	}

	void setError() {
		this.findViewById(R.id.tv_empty).setVisibility(View.VISIBLE);
		if (loading != null)
			loading.stop();
	}

}
