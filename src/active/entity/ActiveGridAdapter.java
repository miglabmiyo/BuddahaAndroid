package active.entity;

import java.util.ArrayList;
import com.miglab.buddha.R;
import basic.io.ImageUtil;
import basic.util.MyLog;
import basic.util.ToolUtil;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author 赵龙权
 * @version 创建时间：2015-1-8 上午10:33:29
 * 类说明
 */
public class ActiveGridAdapter extends BaseAdapter{

	ArrayList<ActiveInfo> list;
	Context con;
	String path;
	Bitmap bitmap;
	private int width;
	private int bitmapWidth; //图片的宽度
	private int bitmapHeight; //图片的高度
	
	public ActiveGridAdapter(Context con, ArrayList<ActiveInfo> list) {
		super();
		this.con = con;
		if (list != null)
			this.list = list;

		path = ImageUtil.initImagePath(con, ImageUtil.ICON_PATH);
		init();
	}
	
	private void init() {
		path = ImageUtil.initImagePath(con, ImageUtil.ICON_PATH);
		//获取屏幕的宽高来设定图片的大小
		WindowManager wm = (WindowManager) con
				.getSystemService(Context.WINDOW_SERVICE);
	    width = wm.getDefaultDisplay().getWidth();
	    bitmapWidth = (int)(width-30)/2;
	    bitmapHeight = (int)((width-30)/2/1.48);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list != null ? list.size() : 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list != null ? list.get(arg0) : null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		ActiveGridHolder holder;
		if (view == null) {
			holder = new ActiveGridHolder();
			view = holder.parent;
		} else {
			holder = (ActiveGridHolder) view.getTag();
		}
		ActiveInfo info = list.get(position);
		if (holder != null && info != null) {
			holder.initData(info);
		}
		return view;
	}
	
	class ActiveGridHolder{
		View parent;
		ImageView iv_icon;
		TextView tv_name;
		RelativeLayout.LayoutParams lParams;
		
		public void init() {
		    //没有下载到图片的时候默认图片的大小显示
		    lParams = (RelativeLayout.LayoutParams)iv_icon.getLayoutParams();
			lParams.width = bitmapWidth;
			lParams.height = bitmapHeight;
			iv_icon.setLayoutParams(lParams);
		}
		
		public ActiveGridHolder(){
			parent = LayoutInflater.from(con).inflate(
					R.layout.item_active, null);
			iv_icon = (ImageView) parent.findViewById(R.id.iv_icon);
			tv_name = (TextView) parent.findViewById(R.id.tv_name);
			parent.setTag(this);
		}
		
		void initData(ActiveInfo info) {
			if (info == null)
				return;
			// image
			setImage(info.pic);

			tv_name.setText(info.name);
		}

		void setImage(String url) {
			boolean isSetImage = false;
			if (url != null && url.length() > 0) {
				isSetImage = ImageUtil.setActiveIcon(iv_icon, ToolUtil.md5(url),
						path, bitmap, bitmapWidth, bitmapHeight);
			}

			if (!isSetImage)
				iv_icon.setImageResource(R.drawable.book_a);
		}
	
	}

}
