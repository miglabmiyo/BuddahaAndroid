package book.show;

import android.content.Context;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import basic.util.MyLog;
import book.entity.ChapterInfo;

/**
 * @author song
 * @version 创建时间：2015-1-13 下午6:23:20 类说明
 */
public class ReadView extends TextView {
	ReadViewListener l;
	int chapterIndex = 0, pageIndex = 0, start = 0;
	ChapterInfo chapter = null;

	public ReadView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ReadView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ReadView(Context context) {
		super(context);
	}

	public void init(ReadViewListener listener) {
		this.l = listener;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		MyLog.d("ReadView", "onLayout changed:" + changed);
		// MyLog.d("ReadView", "onLayout left:" + left);
		// MyLog.d("ReadView", "onLayout top:" + top);
		// MyLog.d("ReadView", "onLayout right:" + right);
		// MyLog.d("ReadView", "onLayout bottom:" + bottom);
		resize();
	}

	public void setText(ChapterInfo chapter, int chapterIndex, int pageIndex,
			int start) {
		MyLog.v("setText chapter: " + chapter + ", chapterIndex: "
				+ chapterIndex + ", pageIndex: " + pageIndex + ", start: "
				+ start);
		if (chapter != null && !TextUtils.isEmpty(chapter.content)
				&& chapterIndex >= 0 && pageIndex >= 0 && start >= 0) {
			this.chapter = chapter;
			this.start = start;
			this.chapterIndex = chapterIndex;
			this.pageIndex = pageIndex;
			setText(chapter.content.substring(start));
		}
	}

	/**
	 * 取出当前页无法显示的字
	 * 
	 * @return 去掉的字数
	 */
	public int resize() {
		MyLog.d("ReadView", "resize");
		CharSequence oldContent = getText();
		// MyLog.d("ReadView", "resize oldContent: " + oldContent);
		int charNum = getCharNum();
		MyLog.v("ReadView", "resize charNum:" + charNum);
		if (charNum > 0) {
			CharSequence newContent = oldContent.subSequence(0, charNum);
			int remainNum = oldContent.length() - newContent.length();
			// setText(newContent);
			return remainNum;
		} else {
			return 0;
		}
	}

	/**
	 * 获取当前页总字数
	 */
	public int getCharNum() {
		Layout layout = getLayout();
		if (layout != null) {
			int linenum = getLineNum();
			MyLog.v("ReadView", "getCharNum linenum:" + linenum);
			return layout.getLineEnd(linenum);
		} else
			return 0;
	}

	/**
	 * 获取当前页总行数
	 */
	public int getLineNum() {
		Layout layout = getLayout();
		int topOfLastLine = getHeight() - getPaddingTop() - getPaddingBottom()
				- getLineHeight();
		MyLog.d("ReadView", "getLineNum layout:" + layout);
		MyLog.v("ReadView", "getLineNum getHeight:" + getHeight());
		MyLog.v("ReadView", "getLineNum getWidth:" + this.getWidth());
		MyLog.v("ReadView", "getLineNum getLineHeight:" + getLineHeight());
		MyLog.v("ReadView", "getLineNum getLineCount:" + this.getLineCount());
		if (layout != null) {
			if (l != null) {
				l.onResize(this, getWidth(),  getHeight(), getLineHeight());
			}
			return layout.getLineForVertical(topOfLastLine);
		} else
			return 0;
	}

	public interface ReadViewListener {
		void onResize(ReadView view, int width, int height, int lineHeight);
	}

}
