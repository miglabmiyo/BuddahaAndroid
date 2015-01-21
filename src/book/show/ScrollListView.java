package book.show;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @author song
 * @version 创建时间：2015-1-4 下午7:07:52
 * 类说明 ScrollView下嵌套的listview
 */
public class ScrollListView extends ListView{

	public ScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
                MeasureSpec.AT_MOST);  
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
	
}
