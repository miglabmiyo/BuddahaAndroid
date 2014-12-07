package basic.show;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.miglab.buddha.R;

/**
 * @author song
 * @version 创建时间：2014-11-24 下午4:45:37 类说明 起始页
 */
public class Start extends BaseActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_start);

		// MobclickAgent.updateOnlineConfig(this);
		// MobclickAgent.onError(this);

		init();
	}

	/** 初始化 */
	private void init() {
		h.sendMessageDelayed(h.obtainMessage(5), 1000);
	}

	/** UI线程 */
	Handler h = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 5) {
				next();
			}
		}
	};

	private void next() {
		startActivity(new Intent(this, Login.class));
		finish();
	}

}