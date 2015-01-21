package basic.show;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import basic.util.MyLog;

/**
 * @author song
 * @version 创建时间：2014-11-29 下午5:45:15 类说明 加上通用设定
 */
public class BaseFragment extends Fragment {
	public BaseFragment instance = null;
	public String TAG = "BaseFragment";
	protected long clickTime;
	protected View vRoot;
	protected Activity ac;
	protected int rootResource;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		TAG = this.getClass().getSimpleName();
		ac = this.getActivity();

		MyLog.i(TAG, "onCreate.");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setLayout();
		vRoot = inflater.inflate(rootResource, container, false);
		initView();
		return vRoot;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initData();
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

	protected void setLayout() {
	}

	protected void initView() {
	}

	protected void initData() {
	}

	public void refreshView() {
	}

	protected boolean canClicked() {
		if (System.currentTimeMillis() - clickTime > 300) {
			clickTime = System.currentTimeMillis();
			return true;
		}

		return false;
	}

	/** 跳转到下个Activity */
	protected void next(Class<?> cls) {
		startActivity(new Intent(ac, cls));
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
