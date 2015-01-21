package introduce.show;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import introduce.task.IntroduceArtDetailTask;
import com.miglab.buddha.R;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import basic.io.ImageUtil;
import basic.net.ApiDefine;
import basic.show.BaseActivity;
import basic.util.MyLog;
import basic.util.ToolUtil;
import book.entity.BookInfo;

/**
 * @author 赵龙权
 * @version 创建时间：2015-1-12 上午10:17:44
 * 类说明	艺术浏览详细界面
 */
public class IntroduceArtBookDetail extends BaseActivity{

	BookInfo bookInfo;
	ImageView iv_art;
	TextView title, tv_content;
	int id;
	String name;
	
	protected void init() {
		this.setContentView(R.layout.ac_introduce_art_detail);
//		bookInfo = (BookInfo) getIntent().getSerializableExtra("book");
		Intent i = this.getIntent();
		  if (i != null) {
			  Bundle b = i.getExtras();
			  if (b != null) {
				  id = b.getInt("id");//接收值
				  name = b.getString("name");
			  }
		  }

		initTitle();
		if (id > 0) {
			initInfo();
		}
	}
	
	void initTitle() {
		title = (TextView) this.findViewById(R.id.title_content);
		title.setText(name);

	}

	void initInfo() {
		iv_art = (ImageView) findViewById(R.id.iv_art);
		tv_content = (TextView) findViewById(R.id.tv_content);

		new IntroduceArtDetailTask(h, id).execute();
		
		setListener();
	}

	@Override
	protected void doHandler(Message msg) {
		try {
			int nCommand = msg.what;
			switch (nCommand) {
			case ApiDefine.GET_SUCCESS:
				MyLog.d(TAG, "GET_SUCCESS");
				if (msg.obj != null) {
					bookInfo = (BookInfo)msg.obj;
					tv_content.setText(bookInfo.summary);
					if (!TextUtils.isEmpty(bookInfo.pic) && !setImage()) {
						updateImage();
					}
				}
				break;
			default:
				break;
			}
		} catch (Exception e) { 
			MyLog.showException(e);
		}
	}
	
	boolean setImage() {
		return ImageUtil.setIcon(iv_art, bookInfo.pic, this,
				ImageUtil.ICON_PATH);
	}
	
	void updateImage() {
		MyLog.v(TAG, "start image");
		List<String> images = new ArrayList<String>();
		images.add(bookInfo.pic);
		if (!images.isEmpty())
			ImageUtil.showIconImage(this, h, images, ApiDefine.FRESH_IMAGE,
					false);
	}
	
	private void setListener() {
		// TODO Auto-generated method stub
		iv_art.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				tv_content.setVisibility(View.VISIBLE);
			}
		});
		
		tv_content.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				tv_content.setVisibility(View.GONE);
			}
		});
	}
	
}
