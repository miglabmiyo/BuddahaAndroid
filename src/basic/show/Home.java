package basic.show;

import home.fragment.FoundBuddhaFragment;
import home.fragment.TextFragment;
import user.login.MyUser;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import basic.util.AppStatus;
import basic.util.MyLog;
import basic.util.ShowUtil;

import com.miglab.buddha.R;

/**
 * @author song
 * @version 创建时间：2014-11-24 下午5:05:02 类说明 首页
 */
public class Home extends BaseFragmentActivity {
	View content;
	FragmentManager fragmentManager;
	FragmentTransaction fragmentTransaction;

	/** 初始化 */
	@Override
	protected void init() {
		setView();
	}

	void setView() {
		this.setContentView(R.layout.ac_home);
		new HomeTitle(this);
		content = this.findViewById(R.id.home_content);
	}

	void changeContent(int selection) {
		MyLog.i(TAG, "changeContent selection: " + selection);
		switch (selection) {
		case 1:
			openFragment(new FoundBuddhaFragment());
			break;
		case 2:
			if (MyUser.getInstance().isLogin()) {
				openFragment(new TextFragment("我的佛缘", 0xFF8E8E8E));
			} else {
				startActivity(new Intent(this, Login.class));
			}
			break;
		case 3:
			openFragment(new TextFragment("个人中心", 0xFF84C1FF));
			break;
		case 4:
			openFragment(new TextFragment("搜索", 0xFF80FFFF));
			break;
		case 5:
			openFragment(new TextFragment("消息", 0xFFFFFF6F));
			break;
		}
	}

	/** 点击菜单的选项，弹出相对应的页面 */
	void openFragment(BaseFragment frag) {
		if (frag == null)
			return;

		if (fragmentManager == null)
			fragmentManager = Home.this.getSupportFragmentManager();

		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.home_content, frag);
		fragmentTransaction.commitAllowingStateLoss();
	}

	class HomeTitle {
		TextView tv_findfo, tv_myfo, tv_myinfo;
		ImageView iv_msg, iv_search;
		Activity ac;
		int selColor = 0xff3a4e47, transparent = 0x00000000;

		public HomeTitle(Activity ac) {
			this.ac = ac;
			init();
		}

		void init() {
			findView();
			setListener();

			doClick(tv_findfo);
		}

		void findView() {
			tv_findfo = (TextView) ac.findViewById(R.id.tv_findfo);
			tv_myfo = (TextView) ac.findViewById(R.id.tv_myfo);
			tv_myinfo = (TextView) ac.findViewById(R.id.tv_myinfo);
			iv_msg = (ImageView) ac.findViewById(R.id.iv_msg);
			iv_search = (ImageView) ac.findViewById(R.id.iv_search);
		}

		void setListener() {
			View[] views = { tv_findfo, tv_myfo, tv_myinfo, iv_msg, iv_search };
			OnClickListener l = new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					doClick(arg0);
				}
			};

			for (View v : views) {
				v.setOnClickListener(l);
			}
		}

		void doClick(View v) {
			int selection = 0;

			if (v == tv_findfo) { // 发现佛缘
				tv_findfo.setBackgroundColor(selColor);
				selection = 1;
			} else {
				tv_findfo.setBackgroundColor(transparent);
			}

			if (v == tv_myfo) { // 我的佛缘
				tv_myfo.setBackgroundColor(selColor);
				selection = 2;
			} else {
				tv_myfo.setBackgroundColor(transparent);
			}

			if (v == tv_myinfo) { // 个人中心
				tv_myinfo.setBackgroundColor(selColor);
				selection = 3;
			} else {
				tv_myinfo.setBackgroundColor(transparent);
			}

			if (v == iv_search) { // 搜索
				iv_search.setSelected(true);
				selection = 4;
			} else {
				iv_search.setSelected(false);
			}

			if (v == iv_msg) { // 消息
				iv_msg.setSelected(true);
				selection = 5;
			} else {
				iv_msg.setSelected(false);
			}

			changeContent(selection);
		}

	}

	private long exitTime = 0;

	@Override
	protected void back() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			ShowUtil.showToast(this, R.string.exit_tips);
			exitTime = System.currentTimeMillis();
		} else if ((System.currentTimeMillis() - exitTime) > 0) {
			// ToolUtil.closeKeyBoard(this);
			AppStatus.exitApp(this);
		}
	}

}
