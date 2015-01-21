package book.entity;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import basic.util.MyLog;
import book.show.ReadView;
import book.show.ReadView.ReadViewListener;

import com.miglab.buddha.R;

/**
 * @author song
 * @version 创建时间：2015-1-7 下午5:52:22 类说明
 */
public class ReaderAdapter extends PageAdapter implements ReadViewListener {
	Context context;
	ArrayList<ChapterInfo> chapters = new ArrayList<ChapterInfo>();
	ArrayList<PageInfo> pages = new ArrayList<PageInfo>();
	ArrayList<Integer> chapterPageStarts = new ArrayList<Integer>();
	ReadView readView;
	View[] parents = new View[3];
	boolean isInited;
	int width, pageLines;
	String bookname;

	public ReaderAdapter(Context context, String bookname,
			ArrayList<ChapterInfo> chapters) {
		this.context = context;
		this.bookname = bookname;
		this.chapters = chapters;
	}

	public View getView(int index) {
		View view = LayoutInflater.from(context).inflate(R.layout.item_reader,
				null);
		setView(view, R.id.tv_bookname, bookname);
		if (index < parents.length) {
			parents[index] = view;
		}
		return view;
	}

	public int getCount() {
		return pages.size() > 0 ? pages.size() : chapters.size();
	}

	public void addContent(View view, int position) {
		position--;
		if (position < 0 || position >= getCount())
			return;

		if (chapters.size() > 0) {
			ReadView tv = (ReadView) view.findViewById(R.id.content);
			if (pages.size() > 0 && position < pages.size()) {
				PageInfo page = pages.get(position);
				if (page != null) {
					setText(tv, page);
					if (page.chapter != null) {
						setView(view, R.id.tv_chapter, page.chapter.name);
						if (page.chapter.pageCount > 0)
							setView(view, R.id.tv_pageNum, (position + 1) + "");
					}
					return;
				}
			}

			tv.setText(chapters.get(0).content);
		}
	}

	public void setPageNum(ReadView view, ChapterInfo chapter) {
		if (view == null)
			return;
		MyLog.v("setPageNum");

		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		width = dm.widthPixels - 2 * 30 * (int) (dm.density + 0.5f);
		int height = dm.heightPixels - 2 * 30 * (int) (dm.density + 0.5f);
		int lineHeight = view.getLineHeight();
		pageLines = height / lineHeight - (int) (dm.density + 0.5f);
		MyLog.v("Paint pageLines: " + pageLines);

		addPage(view, chapter, width, pageLines);
	}

	public void loadView() {
		if (parents[0] == null)
			return;

		if (readView == null)
			readView = (ReadView) parents[0].findViewById(R.id.content);

		MyLog.v("loadView readView: " + readView);
		readView.init(this);
		if (chapters.size() > 0) {
			ChapterInfo chapter = chapters.get(0);
			// MyLog.v("setPreView chapter: " + chapter);
			readView.setText(chapter, 0, 0, 0);
			setPageNum(readView, chapter);
		}
	}

	// 本方法 分行又分页
	public static ArrayList<Integer> getPageContentStringInfo(Paint m_paint,
			String content, int pageLines, float pageWidth) {

		char ch;
		float w = 0;
		int lineNum = 0;
		// float[] widths = new float[1];
		float width;
		ArrayList<Integer> contentList = new ArrayList<Integer>();
		// 内容长度
		for (int i = 0; i < content.length(); i++) {
			ch = content.charAt(i);
			String srt = String.valueOf(ch);
			width = m_paint.measureText(srt);// .getTextWidths(srt, widths);
			if (ch == '\n') {
				// 如果遇到断行符
				lineNum++;
				w = 0;
			} else {
				// 遇到字符
				w += width;// (int) (Math.ceil(widths[0]));
				// 当长度小于宽度时
				if (w > pageWidth) {
					lineNum++;
					i--;
					w = 0;
				} else {
					if (i == (content.length() - 1)) {
						lineNum++;
					}
				}
			}
			if (lineNum == pageLines || i == (content.length() - 1)) {
				contentList.add(i + 1);
				lineNum = 0;
			}
		}
		return contentList;
	}

	public void updateData(int index) {
		ChapterInfo chapter = chapters.get(index);
		addPage(readView, chapter, width, pageLines);
	}

	public void onResize(ReadView view, ChapterInfo chapter, int chapterIndex,
			int pageIndex, int start, int charNum, int remainNum) {
		if (readView == view) {
			MyLog.i("ReaderAdapter", "onResize chapter:" + chapter
					+ ", chapterIndex:" + chapterIndex + ", pageIndex:"
					+ pageIndex + ", start:" + start + ", charNum:" + charNum
					+ ", remainNum:" + remainNum);

			if (chapter != null) {
				PageInfo page = new PageInfo();

				page.chapter = chapter;
				page.start = start;
				page.end = start + charNum;
				pages.add(page);

				if (remainNum > 0) {
					readView.setText(chapter, chapterIndex, ++pageIndex,
							page.end);
				} else {
					chapterIndex++;
					if (chapters.size() >= chapterIndex) {
						ChapterInfo c = chapters.get(chapterIndex);
						readView.setText(c, chapterIndex, ++pageIndex, page.end);
					} else { // 加载完了
						if (pages.size() > 0) {
							addContent(parents[1], 1);
						}
						if (pages.size() > 1) {
							addContent(parents[2], 2);
						}
					}
				}

			}
		}
	}

	void setText(ReadView view, PageInfo page) {
		ChapterInfo chapter = page.chapter;
		view.setText(chapter.content.substring(page.start, page.end));
	}

	void setView(View parent, int id, String str) {
		if (parent != null && id != 0 && !TextUtils.isEmpty(str)) {
			TextView tv = (TextView) parent.findViewById(id);
			tv.setText(str);
		}
	}

	@Override
	public void onResize(ReadView view, int width, int height, int lineHeight) {
		// TODO 自动生成的方法存根

		if (readView == view && !isInited) {
			MyLog.i("ReaderAdapter", "onResize view:" + view + ", width:"
					+ width + ", height:" + height + ", lineHeight:"
					+ lineHeight);
			this.width = width;
			pageLines = height / lineHeight - 1;
			MyLog.v("Paint pageLines: " + pageLines);
			pages.clear();
			for (int i = 0; i < chapters.size(); i++) {
				ChapterInfo chapter = chapters.get(i);
				addPage(readView, chapter, this.width, pageLines);
			}

			if (pages.size() > 0 && parents[1] != null) {
				addContent(parents[1], 1);
			}
			if (pages.size() > 1 && parents[2] != null) {
				addContent(parents[2], 2);
			}

			isInited = true;
		}
	}

	public void addPage(ReadView view, ChapterInfo chapter, int width,
			int pageLines) {
		if (chapter == null || TextUtils.isEmpty(chapter.content))
			return;

		Paint p = view.getPaint();
		ArrayList<Integer> starts = getPageContentStringInfo(p,
				chapter.content, pageLines, width);
		chapter.pageCount = starts.size();
		starts.add(0, 0);
		MyLog.v("addPage starts: " + starts);
		int size = starts.size();
		int max = size - 1;
		int total = chapter.content.length();
		MyLog.v("addPage total: " + total);
		int start;
		for (int i = 0; i < size; i++) {
			start = starts.get(i);
			if (start < total) {
				PageInfo page = new PageInfo();
				page.chapter = chapter;
				page.start = start;
				if (i == max)
					page.end = chapter.content.length();
				else
					page.end = starts.get(i + 1);

				pages.add(page);
			} else {
				chapter.pageCount = i + 1;
			}

		}

		for (int i = 0; i < parents.length; i++) {
			View v = parents[i];
			setView(v, R.id.tv_pageTotal, "/" + pages.size());
		}
	}
}
