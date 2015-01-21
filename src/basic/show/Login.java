package basic.show;

import user.login.MyLogin;
import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import basic.net.ApiDefine;
import basic.util.MyLog;
import basic.util.ShowUtil;

import com.miglab.buddha.R;

/**
 * @author song
 * @version 创建时间：2014-12-5 下午4:56:43 类说明
 */
public class Login extends BaseActivity {
	MyLogin myLogin;
	View[] items = new View[4];

	@Override
	protected void init() {
		this.setContentView(R.layout.ac_login);
		setView();

		myLogin = new MyLogin(this, h);
	}

	void setView() {
		this.setSimpleTitle(R.string.login);
		items[0] = findViewById(R.id.qq);
		items[1] = findViewById(R.id.sina);
		items[2] = findViewById(R.id.weixin);
		items[3] = findViewById(R.id.guestlogin);
		OnClickListener l = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				chooseSelect(v);
			}

		};
		for (View v : items) {
			v.setOnClickListener(l);
		}
	}

	void chooseSelect(View v) {
		if (myLogin != null && canClicked()) {
			if (v == items[0]) {
				myLogin.qqLogin();
			} else if (v == items[1]) {
				myLogin.weiboLogin();
			} else if (v == items[2]) {

			} else if (v == items[3]) { // 快速登录
				myLogin.guestLogin();
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (myLogin != null)
			myLogin.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void doHandler(Message msg) {
		if (msg.what == ApiDefine.GET_SUCCESS) {
			MyLog.d(TAG, "登录成功");
			ShowUtil.showToast(this, R.string.login_success);
			next(Home.class);
		} else {
			MyLog.d(TAG, "登录失败");
			ShowUtil.showToast(this, R.string.login_fail);
		}
	}
}
