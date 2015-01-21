package map.entity;

import java.util.ArrayList;

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

import com.miglab.buddha.R;

/**
 * @author song
 * @version 创建时间：2014-12-21 下午10:53:22
 * 类说明
 */
public class BuildingAdapter extends BaseAdapter {
	Context con;
	ArrayList<BuildingInfo> list;
	String path;
	Bitmap bitmap;

	public BuildingAdapter(Context con, ArrayList<BuildingInfo> list) {
		this.con = con;
		this.list = list;
		if (list == null)
			this.list = new ArrayList<BuildingInfo>();

		path = ImageUtil.initImagePath(con, ImageUtil.ICON_PATH);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		RecommendHolder holder;
		if (view == null) {
			holder = new RecommendHolder();
			view = holder.parent;
		} else {
			holder = (RecommendHolder) view.getTag();
		}
		BuildingInfo info = list.get(position);
		if (holder != null && info != null) {
			holder.initData(info);
		}
		return view;
	}

	class RecommendHolder {
		View parent;
		ImageView iv_icon;
		TextView tv_name, tv_address, tv_distance;

		public RecommendHolder() {
			initView();
		}

		void initView() {
			parent = LayoutInflater.from(con).inflate(
					R.layout.item_map_distance, null);
			iv_icon = (ImageView) parent.findViewById(R.id.item_icon);
			tv_name = (TextView) parent.findViewById(R.id.item_name);
			tv_address = (TextView) parent.findViewById(R.id.item_content);
			tv_distance = (TextView) parent
					.findViewById(R.id.item_distance);
			parent.setTag(this);
		}

		void initData(BuildingInfo info) {
			if (info == null)
				return;
			// image
			setImage(info.pic);

			tv_name.setText(info.name);
			tv_address.setText(info.address);
			tv_distance
					.setText(info.distance < 10000 ? (info.distance + "m")
							: (info.distance / 1000 + "km"));
		}

		void setImage(String url) {
			boolean isSetImage = false;
			if (url != null && url.length() > 0) {
				isSetImage = ImageUtil.setIcon(iv_icon, ToolUtil.md5(url),
						path, bitmap);
			}

			if (!isSetImage)
				iv_icon.setImageResource(R.drawable.banner);
		}
	}
}
