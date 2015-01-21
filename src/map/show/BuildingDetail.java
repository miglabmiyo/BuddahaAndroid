package map.show;

import java.util.ArrayList;
import java.util.List;

import map.task.BuildingContentTask;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import basic.io.ImageUtil;
import basic.net.ApiDefine;
import basic.util.MyLog;

import com.miglab.buddha.R;

/**
 * @author song
 * @version 创建时间：2014-12-21 下午5:35:47 类说明 建筑详情
 */
public class BuildingDetail extends MapActivity {
	ImageView iv_icon;
	TextView tv_phone, tv_detail;
	View ly_address, ly_phone;

	@Override
	protected void init() {
		this.setContentView(R.layout.ac_map_detail);
		this.setSimpleTitle(R.string.detail);
		initBuildingInfo();
		if (building != null) {
			setView();
			setListener();
			if (!TextUtils.isEmpty(building.pic))
				setIcon();
		} else {
			MyLog.e(TAG, "building is null");
		}
	}

	void setView() {
		iv_icon = (ImageView) findViewById(R.id.item_icon);
		((TextView) findViewById(R.id.item_name)).setText(building.name);
		((TextView) findViewById(R.id.item_content)).setText(building.address);
		((TextView) findViewById(R.id.item_distance))
				.setText(building.distance < 10000 ? (building.distance + "m")
						: (building.distance / 1000 + "km"));
		((TextView) findViewById(R.id.item_name11)).setText(building.address);
		tv_phone = (TextView) findViewById(R.id.item_name12);
		if (!TextUtils.isEmpty(building.phone))
			tv_phone.setText(building.phone);
		tv_detail = (TextView) findViewById(R.id.tv_detail);
		if (!TextUtils.isEmpty(building.summary))
			tv_detail.setText(building.summary);
		ly_address = findViewById(R.id.ly_address);
		ly_phone = findViewById(R.id.ly_phone);

		if (TextUtils.isEmpty(building.phone)
				|| TextUtils.isEmpty(building.summary)) {
			new BuildingContentTask(h, building).execute();
		}
	}

	void setListener() {
		ly_address.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				routePlan();
			}
		});

		ly_phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				callTel();
			}
		});

	}

	void setIcon() {
		List<String> images = new ArrayList<String>();
		images.add(building.pic);
		ImageUtil.showIconImage(this, h, images, ApiDefine.FRESH_IMAGE,
				false);
	}

	/** h里的操作 */
	@Override
	protected void doHandler(Message msg) {
		try {
			int nCommand = msg.what;
			switch (nCommand) {
			case ApiDefine.FRESH_IMAGE:
				if (iv_icon != null) {
					ImageUtil.setIcon(iv_icon, building.pic,
							BuildingDetail.this, ImageUtil.ICON_PATH);
				}
				break;
			case ApiDefine.GET_SUCCESS:
				if (!TextUtils.isEmpty(building.phone))
					tv_phone.setText(building.phone);
				if (!TextUtils.isEmpty(building.summary))
					tv_detail.setText(building.summary);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			MyLog.showException(e);
		}
	}

	void callTel() {
		if (this.canClicked()) {
			// 用intent启动拨打电话
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ building.phone));
			startActivity(intent);
		}
	}

	void routePlan() {
		if (this.canClicked()) {
			Intent i = new Intent(this, POIRoute.class);
			i.putExtra("dstLatitude", building.latitude);
			i.putExtra("dstLongtitude", building.longitude);
			i.putExtra("latitude", latitude);
			i.putExtra("longtitude", longtitude);
			this.startActivity(i);
		}

	}
}