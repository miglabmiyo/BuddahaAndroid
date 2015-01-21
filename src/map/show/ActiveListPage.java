package map.show;

import active.entity.ActiveInfo;
import active.task.ActiveBuildingTask;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import basic.io.ImageUtil;
import basic.net.ApiDefine;
import basic.util.MyLog;
import basic.util.ToolUtil;
import com.miglab.buddha.R;

/**
 * @author 赵龙权
 * @version 创建时间：2015-1-7 下午3:24:58
 * 类说明	活动详情
 */
public class ActiveListPage extends MapActivity{

	ImageView iv_icon, iv_share;
	TextView tv_phone, tv_detail, item_name11;
	View ly_address, ly_phone;
	ActiveInfo active;
	String path;
	Bitmap bitmap;
	private int width;
	private int bitmapWidth; //图片的宽度
	private int bitmapHeight; //图片的高度
	
	protected void init() {
		this.setContentView(R.layout.ac_active_detail);
		this.setSimpleTitle(R.string.detail);
		
		initScreen();
		active = (ActiveInfo) getIntent().getSerializableExtra("active");
		new ActiveBuildingTask(h, active).execute();
		
		if (active != null && active.id > 0) {
			setView();
			setListener();
			if (!TextUtils.isEmpty(active.pic))
				setImage(active.pic);
		} else {
			MyLog.e(TAG, "active is null");
		}
	}
	
	private void initScreen() {
		path = ImageUtil.initImagePath(ActiveListPage.this, ImageUtil.ICON_PATH);
		//获取屏幕的宽高来设定图片的大小
		WindowManager wm = (WindowManager) ActiveListPage.this
				.getSystemService(Context.WINDOW_SERVICE);
	    width = wm.getDefaultDisplay().getWidth();
	    bitmapWidth = width;
	    bitmapHeight = (int)(width/2.86);
	}

	private void setView() {
		// TODO Auto-generated method stub
		((TextView) findViewById(R.id.item_name11)).setText(active.address);
		if (!TextUtils.isEmpty(active.phone))
			tv_phone.setText(active.phone);
		if (!TextUtils.isEmpty(active.summary))
			tv_detail.setText(active.summary);
		iv_icon = (ImageView) findViewById(R.id.item_icon);
		tv_phone = (TextView) findViewById(R.id.item_name12);
		ly_address = findViewById(R.id.ly_address);
		ly_phone = findViewById(R.id.ly_phone);
		iv_share = (ImageView) findViewById(R.id.title_share);
		tv_detail = (TextView) findViewById(R.id.tv_detail);
		item_name11 = (TextView) findViewById(R.id.item_name11);
	}

	private void setListener() {
		// TODO Auto-generated method stub
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
		
		iv_share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MyLog.e(TAG, "active to share");
			}
		});
	}
	
	void setImage(String url) {
		boolean isSetImage = false;
		if (url != null && url.length() > 0) {
			isSetImage = ImageUtil.setActiveIcon(iv_icon, ToolUtil.md5(url),
					path, bitmap, bitmapWidth, bitmapHeight);
		}

		if (!isSetImage)
			iv_icon.setImageResource(R.drawable.banner);
	}

	/** h里的操作 */
	protected void doHandler(Message msg) {
		try {
			int nCommand = msg.what;
			switch (nCommand) {
			case ApiDefine.FRESH_IMAGE:
				setImage(active.pic);
				break;
			case ApiDefine.GET_SUCCESS:
				MyLog.e(TAG, "ActiveInfo latitude-->" + active.latitude);
				MyLog.e(TAG, "ActiveInfo longitude-->" + active.longitude);
				tv_phone.setText(active.phone);
				tv_detail.setText(active.summary);
				item_name11.setText(active.address);
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
					+ active.phone));
			startActivity(intent);
		}
	}

	void routePlan() {
		if (this.canClicked()) {
			Intent i = new Intent(this, POIRoute.class);
			i.putExtra("dstLatitude", active.latitude);
			i.putExtra("dstLongtitude", active.longitude);
			i.putExtra("latitude", latitude);
			i.putExtra("longtitude", longtitude);

			MyLog.e(TAG, "ActiveInfo latitude routePlan()-->" + active.latitude);
			MyLog.e(TAG, "ActiveInfo longitude routePlan()-->" + active.longitude);
			this.startActivity(i);
		}

	}
}
