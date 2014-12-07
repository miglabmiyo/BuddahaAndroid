package basic.show;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import basic.util.AppStatus;
import basic.util.MyLog;

public class BaseFragmentActivity extends FragmentActivity {
	public BaseFragmentActivity instance = null;
	public String TAG = "BaseFragmentActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppStatus.register(this);// 将房间Activity加入activityList
		instance = this;

		TAG = this.getClass().getSimpleName();

		MyLog.d(TAG, "onCreate");
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

	protected void back() {
		this.finish();
	}
}
