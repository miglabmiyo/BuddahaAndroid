package active.entity;

import java.util.ArrayList;
import com.miglab.buddha.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import basic.io.ImageUtil;
import basic.util.ToolUtil;

/**
 * @author 赵龙权
 * @version 创建时间：2015-1-7 上午2:00:40
 * 类说明
 */
public class ActiveListAdapter extends BaseAdapter{

	ArrayList<ActiveInfo> list;
	Context con;
	int parentId;
	String path;
	Bitmap bitmap;
	
	public ActiveListAdapter(Context con, ArrayList<ActiveInfo> list, int parentId) {
		super();
		this.con = con;
		if (list != null)
			this.list = list;
		this.parentId = parentId;

		path = ImageUtil.initImagePath(con, ImageUtil.ICON_PATH);
	}

	@Override
	public int getCount() {
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
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ActiveListHolder holder;
		if (view == null) {
			holder = new ActiveListHolder();
			view = holder.parent;
		} else {
			holder = (ActiveListHolder) view.getTag();
		}
		ActiveInfo info = list.get(position);
		if (holder != null && info != null) {
			holder.initData(info);
		}
		return view;
	}
	
	class ActiveListHolder{
		View parent;
		ImageView iv_icon;
		TextView tv_name, tv_content;
		
		public ActiveListHolder(){
			parent = LayoutInflater.from(con).inflate(
					R.layout.item_activelist, null);
			iv_icon = (ImageView) parent.findViewById(R.id.item_icon);
			tv_name = (TextView) parent.findViewById(R.id.item_name);
			tv_content = (TextView) parent.findViewById(R.id.item_content);
			parent.setTag(this);
		}
		
		void initData(ActiveInfo info) {
			if (info == null)
				return;
			// image
			setImage(info.pic);
			tv_name.setText(info.name);
			tv_content.setText(info.summary);
		}

		void setImage(String url) {
			boolean isSetImage = false;
			if (url != null && url.length() > 0) {
				isSetImage = ImageUtil.setIcon(iv_icon, ToolUtil.md5(url),
						path, bitmap);
			}

			if (!isSetImage)
				iv_icon.setImageResource(R.drawable.book_a);
		}
	
	}
	
}
