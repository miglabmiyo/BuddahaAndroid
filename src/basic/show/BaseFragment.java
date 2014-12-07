package basic.show;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import basic.util.MyLog;

/**
 * @author song
 * @version 创建时间：2014-11-29 下午5:45:15 类说明 加上通用设定
 */
public class BaseFragment extends Fragment {
	public BaseFragment instance = null;
	public String TAG = "BaseFragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		TAG = this.getClass().getSimpleName();

		MyLog.i(TAG, "onCreate.");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		instance = null;

		MyLog.i(TAG, "onDestroy.");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MyLog.i(TAG, "onResume.");
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	public void refreshView() {

	}

	public void onNewIntent(Intent intent) {

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
}
