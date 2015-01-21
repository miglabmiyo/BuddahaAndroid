package book.entity;

import android.view.View;

/**
 * @author song
 * @version 创建时间：2015-1-7 下午5:18:25 类说明
 */
public abstract class PageAdapter {
	/**
	 * @return 页面view
	 */
	public abstract View getView(int index);

	public abstract int getCount();

	/**
	 * 将内容添加到view中
	 * 
	 * @param view
	 *            包含内容的view
	 * @param position
	 *            第position页
	 */
	public abstract void addContent(View view, int position);

}
