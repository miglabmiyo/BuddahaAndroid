package book.show;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.GridView;
import basic.util.MyLog;

import com.miglab.buddha.R;

/**
 * @author song
 * @version 创建时间：2015-1-6 下午6:32:53 类说明 书架列表
 */
public class ShelfGridView extends GridView {

	private Bitmap background;
	private int numColumns = 3;

	public ShelfGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		background = BitmapFactory.decodeResource(getResources(),
				R.drawable.book_shelf);
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		int count = getChildCount() > 0 ? (getChildCount() / 3
				+ getChildCount() % numColumns > 0 ? 1 : 0) : 1;

		DisplayMetrics dm = getResources().getDisplayMetrics();
		int top = getChildCount() > 0 ? (getChildAt(0).getBottom())
				: (int) (113 * dm.density + 0.5f);
		int left = (int) (15 * dm.density + 0.5f); // 左边空出15dip
		int right = getWidth() - (int) (15 * dm.density + 0.5f); // 右边空出15dip
		int height = (background.getHeight() * (right - left) / background
				.getWidth()) + 2;

		MyLog.d("ShelfGridView getChildCount:" + getChildCount() + ",count:"
				+ count);
		MyLog.d("ShelfGridView left:" + left + ",getWidth():" + getWidth()
				+ ",right:" + right + ",top:" + top + ", height:" + height);
		Rect dst = new Rect(left, top, right, top + height);
		for (int i = 0; i < count; i++) {
			// Rect src = new Rect(0, 0, background.getWidth(),
			// background.getHeight());
			if (dst != null)
				canvas.drawBitmap(background, null, dst, null);
			
			if (i + 1 != count) {
				dst.top = this.getChildAt((i + 1) * numColumns).getBottom();
			}
		}

		super.dispatchDraw(canvas);
	}
}
