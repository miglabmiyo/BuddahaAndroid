package basic.show;

import user.login.MyUser;
import user.task.IMEILoginTask;
import user.task.ThirdLoginTask;
import android.os.Message;
import android.text.TextUtils;
import basic.net.ApiDefine;

import com.miglab.buddha.R;

/**
 * @author song
 * @version 创建时间：2014-11-24 下午4:45:37 类说明 起始页
 */
public class Start extends BaseActivity {

	/** 初始化 */
	@Override
	protected void init() {
		setContentView(R.layout.ac_start);

		// MobclickAgent.updateOnlineConfig(this);
		// MobclickAgent.onError(this);

		autoLogin();
	}

	/** 静默登录 */
	private void autoLogin() {
		MyUser user = MyUser.getInstance();
		user.readRecord(this);
		if (user.isLogin()) {
			new ThirdLoginTask(h).execute();
		} else {
			if (!TextUtils.isEmpty(user.imei)) {
				new IMEILoginTask(h, user.imei).execute();
			} else {
				h.sendMessageDelayed(h.obtainMessage(5), 1000);
			}
		}
	}

	/** handler处理 */
	@Override
	protected void doHandler(Message msg) {
		if (msg.what == ApiDefine.GET_SUCCESS) {
			next(Home.class);
		} else {
			next(Login.class);
		}
	}

}