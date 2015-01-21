package basic.show;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import basic.util.AppStatus;
import basic.util.MyLog;

import com.miglab.buddha.R;

/**
 * @author song
 * @version 创建时间：2013-4-26 下午4:42:56 类说明 加上通用设定
 */
public class BaseActivity extends Activity {
	protected BaseActivity instance = null;
	protected String TAG = "BaseActivity";
	protected long clickTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppStatus.register(this);// 将房间Activity加入activityList
		instance = this;
		TAG = this.getClass().getSimpleName();
		MyLog.d(TAG, "onCreate");

		init();
	}

	/** 初始化 */
	protected void init() {
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		instance = null;
		AppStatus.unregister(this);
		MyLog.d(TAG, "onDestroy");
	}

	@Override
	protected void onPause() {
		super.onPause();
		MyLog.d(TAG, "onPause");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MyLog.d(TAG, "onResume");
		AppStatus.resume(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		MyLog.d(TAG, "onStart");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		MyLog.d(TAG, "onActivityResult");
	}

	/** 监听手机按键 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) {
			back();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	/** 退出Activity */
	protected void back() {
		this.finish();
	}

	/**
	 * 设定title，title的布局需使用ly_title_simple
	 * 
	 * @param titleStringResource
	 *            标题的内容资源
	 */
	protected void setSimpleTitle(int titleStringResource) {
		TextView title = (TextView) this.findViewById(R.id.title_content);
		title.setText(titleStringResource);

		ImageView back = (ImageView) this.findViewById(R.id.title_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				back();
			}
		});
	}

	/** 可以点击 */
	protected boolean canClicked() {
		if (System.currentTimeMillis() - clickTime > 500) {
			clickTime = System.currentTimeMillis();
			return true;
		}

		return false;
	}

	/** 跳转到下个Activity */
	protected void next(Class<?> cls) {
		startActivity(new Intent(this, cls));
		finish();
	}

	/** 跳转到下个Activity */
	protected void nextWithoutFinish(Class<?> cls) {
		startActivity(new Intent(this, cls));
	}

	/** UI线程 */
	protected Handler h = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg != null) {
				doHandler(msg);
			}
		}

	};

	/** handler里的操作 */
	protected void doHandler(Message msg) {
	}

}