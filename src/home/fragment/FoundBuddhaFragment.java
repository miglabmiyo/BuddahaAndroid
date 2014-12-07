package home.fragment;

import java.util.ArrayList;

import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import basic.show.BaseFragment;
import basic.util.MyLog;

import com.miglab.buddha.R;

/**
 * @author song
 * @version 创建时间：2014-11-29 下午8:05:02 类说明 发现佛缘
 */
public class FoundBuddhaFragment extends BaseFragment {
	View vRoot;
	int screenW;// 当前屏幕宽度
	FoundBuddhaTitle title;
	FoundBuddhaPager page;

	public FoundBuddhaFragment(int screenW) {
		this.screenW = screenW;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		vRoot = inflater.inflate(R.layout.fm_foundbuddha, container, false);
		initView();
		return vRoot;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	void initView() {
		title = new FoundBuddhaTitle();
		page = new FoundBuddhaPager();
	}

	class FoundBuddhaTitle {
		TextView tv_recommend, tv_introduce, tv_bookmall, tv_map, tv_activity;
		ImageView iv_cursor;
		int bmpW;// 横线图片宽度
		int offset;// 图片移动的偏移量
		int one;// 两个相邻页面的偏移量

		public FoundBuddhaTitle() {
			init();
		}

		void init() {
			setView();
			setListener();
		}

		void setView() {
			tv_recommend = (TextView) vRoot.findViewById(R.id.tv_recommend);
			tv_introduce = (TextView) vRoot.findViewById(R.id.tv_introduce);
			tv_bookmall = (TextView) vRoot.findViewById(R.id.tv_bookmall);
			tv_map = (TextView) vRoot.findViewById(R.id.tv_map);
			tv_activity = (TextView) vRoot.findViewById(R.id.tv_activity);

			initCursor();
		}

		void setListener() {
			View[] views = { tv_recommend, tv_introduce, tv_bookmall, tv_map,
					tv_activity };

			for (int i = 0; i < views.length; i++) {
				View view = views[i];
				view.setOnClickListener(new txListener(i));
			}
		}

		/*
		 * 初始化图片的位移像素
		 */
		void initCursor() {
			if (iv_cursor != null)
				return;

			iv_cursor = (ImageView) vRoot.findViewById(R.id.iv_cursor);
			bmpW = 8;// iv_cursor.getMeasuredWidth();
			offset = (screenW / 5 - bmpW) / 2;
			MyLog.i("FoundBuhhdaTitle", "initCursor bmpW: " + bmpW
					+ ", screenW: " + screenW + ", offset: " + offset);

			// imgageview设置平移，使下划线平移到初始位置（平移一个offset）
			Matrix matrix = new Matrix();
			matrix.postTranslate(offset, 0);
			iv_cursor.setImageMatrix(matrix);

			one = offset * 2 + bmpW;
		}

		void changeCursor(int index) {
			if (page == null)
				return;
			Animation animation = new TranslateAnimation(page.currIndex * one,
					index * one, 0, 0);// 平移动画
			page.currIndex = index;
			animation.setFillAfter(true);// 动画终止时停留在最后一帧，不然会回到没有执行前的状态
			animation.setDuration(200);// 动画持续时间0.2秒
			iv_cursor.startAnimation(animation);// 是用ImageView来显示动画的
		}

		class txListener implements View.OnClickListener {
			private int index = 0;

			public txListener(int i) {
				index = i;
			}

			@Override
			public void onClick(View v) {
				if (page != null)
					page.setCurrentItem(index);
			}
		}
	}

	class FoundBuddhaPager {
		ViewPager mPager;
		ArrayList<BaseFragment> fragmentList;
		int currIndex;// 当前页卡编号

		public FoundBuddhaPager(){
			initViewPager();
		}
		
		void initViewPager() {
			mPager = (ViewPager) vRoot.findViewById(R.id.viewpager);
			fragmentList = new ArrayList<BaseFragment>();
			fragmentList.add(new RecommendFragment());
			fragmentList.add(new TextFragment("介绍", 0xFFFFAD86));
			fragmentList.add(new TextFragment("书库", 0xFFAAAAFF));
			fragmentList.add(new TextFragment("地图", 0xFFFFD0FF));
			fragmentList.add(new TextFragment("活动", 0xFFFFD2D2));

			// 给ViewPager设置适配器
			mPager.setAdapter(new MyFragmentPagerAdapter(
					FoundBuddhaFragment.this.getActivity()
							.getSupportFragmentManager(), fragmentList));
			mPager.setCurrentItem(0);// 设置当前显示标签页为第一页
			mPager.setOnPageChangeListener(new MyOnPageChangeListener());// 页面变化时的监听器
		}

		void setCurrentItem(int index) {
			mPager.setCurrentItem(index);
		}

		class MyFragmentPagerAdapter extends FragmentPagerAdapter {
			ArrayList<BaseFragment> list;

			public MyFragmentPagerAdapter(FragmentManager fm,
					ArrayList<BaseFragment> list) {
				super(fm);
				this.list = list;
			}

			@Override
			public int getCount() {
				return list.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				return list.get(arg0);
			}

		}

		class MyOnPageChangeListener implements OnPageChangeListener {

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				if (title != null)
					title.changeCursor(arg0);
			}
		}
	}
}
