package home.fragment;

import home.view.SecondViewPager;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import basic.io.ImageUtil;
import basic.show.BaseFragment;
import basic.util.AppStatus;
import basic.util.MyLog;
import basic.util.ToolUtil;

import com.miglab.buddha.R;

/**
 * @author song
 * @version 创建时间：2014-11-29 下午10:06:15 类说明 推荐
 */
public class RecommendFragment extends BaseFragment {
	View vRoot;
	Activity ac;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		vRoot = inflater.inflate(R.layout.fm_recommend, container, false);
		ac = this.getActivity();
		init();
		return vRoot;
	}

	void init() {
		new RecommendBanner();
		new RecommendNews();
	}

	class RecommendBanner {
		SecondViewPager vp;
		AdViewPagerAdapter vpAdapter;
		ArrayList<AdvertBannerInfo> mlist = new ArrayList<AdvertBannerInfo>();
		GridView dotsGridView;
		DotsAdapter dotsAdapter;
		final static int FRESH_BANNER_IMAGE = 55500; // 下载横幅图片后更新控件

		public RecommendBanner() {
			setView();
			freshViewPager();
		}

		void setView() {
			vp = (SecondViewPager) vRoot.findViewById(R.id.viewpager);
			dotsGridView = (GridView) vRoot.findViewById(R.id.main_dots);
		}

		synchronized void freshViewPager() {
			MyLog.v(TAG, "freshViewPager.");
			getList();

			AdViewPagerAdapterClickMethod method = new AdViewPagerAdapterClickMethod() {

				@Override
				public void click(ImageView img, int position) {
					if (mlist == null || mlist.isEmpty())
						return;

					final AdvertBannerInfo info = mlist.get(position);
					if (info == null)
						return;

					img.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							clickBanner(info);
						}

					});

				}

			};

			vpAdapter = new AdViewPagerAdapter(ac, mlist, method);
			vp.setAdapter(vpAdapter);

			vp.setCurrentItem(0);

			if (mlist.size() > 1) {
				dotsAdapter = new DotsAdapter(ac, mlist.size());
				dotsGridView.setNumColumns(mlist.size());
				dotsGridView.setAdapter(dotsAdapter);
				dotsGridView.bringToFront();
			}

			List<String> images = new ArrayList<String>();
			for (AdvertBannerInfo info : mlist) {
				if (!TextUtils.isEmpty(info.imgUrl)) {
					images.add(info.imgUrl);
				}
			}

			ImageUtil.showBannerImage(ac, handler, images, FRESH_BANNER_IMAGE,
					false, 1);
		}

		void getList() {
			SharedPreferences sharedPrefs = AppStatus.frontActivity
					.getSharedPreferences("home_content", Context.MODE_PRIVATE);

			if (sharedPrefs != null) {
				String str = sharedPrefs.getString("advert", null);
				if (!TextUtils.isEmpty(str)) {
					try {
						JSONArray array = new JSONArray(str);
						mlist.clear();
						for (int i = 0; i < array.length(); i++) {
							JSONObject obj = array.optJSONObject(i);
							if (obj != null) {
								AdvertBannerInfo info = new AdvertBannerInfo(
										obj);
								mlist.add(info);
							}
						}
					} catch (JSONException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}

				}
			}
		}

		void clickBanner(AdvertBannerInfo info) {

		}

		Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO 自动生成的方法存根
				super.handleMessage(msg);

				try {
					int nCommand = msg.what;
					switch (nCommand) {
					case FRESH_BANNER_IMAGE:
						freshBannerImages();
						break;
					default:
						break;
					}
				} catch (Exception e) {
					MyLog.showException(e);
				}
			}

		};

		/** 刷新横幅背景图片 */
		void freshBannerImages() {
			if (vpAdapter == null || mlist.size() <= 0)
				return;

			vpAdapter.notifyDataSetChanged();
		}

		class AdvertBannerInfo {
			String title;
			int weight;
			String imgUrl;
			int imgResource;

			public AdvertBannerInfo(JSONObject json) {
				if (json != null) {
					title = json.optString("title");
					weight = json.optInt("weight");
					imgUrl = json.optString("pic");
				}
			}

			public AdvertBannerInfo(int resource) {
				imgResource = resource;
			}
		}

		class AdViewPagerAdapter extends PagerAdapter {
			private List<View> mViews;
			private Context mContext;
			private AdViewPagerAdapterClickMethod method;
			private ArrayList<AdvertBannerInfo> bannerList;
			private boolean isDefault = false;
			String path;
			private Bitmap bitmap = null;

			public AdViewPagerAdapter(Context con,
					ArrayList<AdvertBannerInfo> list,
					AdViewPagerAdapterClickMethod method) {
				super();
				mContext = con;
				bannerList = list;
				this.method = method;

				if (bannerList == null || bannerList.isEmpty()) // 采取默认
					initBanner();
				else {
					LayoutInflater lf = LayoutInflater.from(mContext);
					mViews = new ArrayList<View>();// 将要分页显示的View装入数组中
					for (int i = 0; lf != null && i < bannerList.size(); i++) {
						View view = lf.inflate(R.layout.view_image, null);
						mViews.add(view);
					}
				}

				path = ImageUtil.initImagePath(mContext, ImageUtil.BANNER_PATH);

			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return bannerList.size();
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				// TODO Auto-generated method stub
				return arg0 == arg1;
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				// TODO Auto-generated method stub
				container.removeView(mViews.get(position));// 删除页卡
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				// TODO Auto-generated method stub
				container.addView(mViews.get(position), 0);// 添加页卡
				ImageView img = (ImageView) mViews.get(position).findViewById(
						R.id.image);
				setImage(img, position);
				method.click(img, position);
				return mViews.get(position);
			}

			private void initBanner() {
				isDefault = true;

				LayoutInflater lf = LayoutInflater.from(mContext);

				mViews = new ArrayList<View>();// 将要分页显示的View装入数组中

				View view1 = lf.inflate(R.layout.view_image, null);
				mViews.add(view1);

				AdvertBannerInfo item1 = new AdvertBannerInfo(R.drawable.banner);
				bannerList.add(item1);

			}

			private void setImage(ImageView img, int position) {
				try {
					/* 显示图片 */
					if (isDefault) {
						int pic = bannerList.get(position).imgResource;
						if (pic > 0)
							img.setImageResource(pic);
					} else {
						boolean isSetImage = false;
						String pic = bannerList.get(position).imgUrl;
						if (pic != null && pic.length() > 0) {
							String imageName = ToolUtil.md5(pic);
							if (imageName != null && imageName.length() > 0
									&& new File(path + imageName).exists()) {
								try {

									bitmap = getBitmap(path + imageName);
									if (bitmap != null) {
										img.setImageBitmap(bitmap);
										isSetImage = true;
									}
								} catch (OutOfMemoryError e) {
									// TODO: handle exception
									// ShowUtil.showErrorOOMToast(mContext,
									// R.string.tost_oomerror);
									if (bitmap != null) {
										bitmap.recycle();
										bitmap = null;
									}
								}
							}
						}

						if (!isSetImage)
							img.setImageResource(R.drawable.banner);
					}
				} catch (Exception e) {
					MyLog.e("AdViewPagerAdapter",
							"getView error--"
									+ position
									+ ((bannerList == null) ? "list == null"
											: ((bannerList.get(position) == null) ? "list.get(position) == null"
													: bannerList.get(position)
															.toString())));
				}
			}

			@Override
			public void notifyDataSetChanged() {
				// TODO Auto-generated method stub
				super.notifyDataSetChanged();

				if (mViews.isEmpty())
					return;

				for (int i = 0; i < mViews.size(); i++) {
					ImageView img = (ImageView) mViews.get(i).findViewById(
							R.id.image);
					setImage(img, i);
				}
			}

			private Bitmap getBitmap(String path) {
				if (TextUtils.isEmpty(path)) {
					return null;
				}
				File f = new File(path);
				if (!f.exists()) {
					return null;
				}
				Bitmap b = null;
				try {
					BitmapFactory.Options opts = new BitmapFactory.Options();
					opts.inJustDecodeBounds = true;
					BitmapFactory.decodeStream(new FileInputStream(path), null,
							opts);
					opts.inSampleSize = 3;

					// System.out.println("图片取样值："+opts.inSampleSize);
					opts.inJustDecodeBounds = false;
					b = BitmapFactory.decodeStream(new FileInputStream(path),
							null, opts);
					//
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				} catch (OutOfMemoryError error) {
					if (b != null) {
						b.recycle();
					}
					error.printStackTrace();

				}
				return b;
			}

		}

		class DotsAdapter extends BaseAdapter {

			private int count = 0;
			private Context mContext;
			private int select = 0;
			private int normalDotResource = R.drawable.img_dot;
			private int selectDotResource = R.drawable.img_dot_c;

			public DotsAdapter(Context mContext, int count) {
				super();
				this.count = count;
				MyLog.i("count===" + count);
				this.mContext = mContext;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return count;
			}

			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return arg0;
			}

			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return arg0;
			}

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				arg1 = View.inflate(mContext, R.layout.item_dot, null);
				ImageView iv = (ImageView) arg1.findViewById(R.id.dot);
				if (arg0 == select)
					iv.setImageResource(selectDotResource);
				else
					iv.setImageResource(normalDotResource);
				return arg1;
			}

			public void setSelect(int select) {
				this.select = select;
			}

			public void setDotResource(int selectDotResource,
					int normalDotResource) {
				this.selectDotResource = selectDotResource;
				this.normalDotResource = normalDotResource;
			}

			public void setSelectDotResource(int selectDotResource) {
				this.selectDotResource = selectDotResource;
			}

			public void setNormalDotResource(int normalDotResource) {
				this.normalDotResource = normalDotResource;
			}
		}

	}

	interface AdViewPagerAdapterClickMethod {
		void click(ImageView img, int position);
	}

	class RecommendNews {
		View ly_news;

		public RecommendNews() {
			ly_news = vRoot.findViewById(R.id.ly_news);
			initData();
			setListener();
		}

		void initData() {
			SharedPreferences sharedPrefs = AppStatus.frontActivity
					.getSharedPreferences("home_content", Context.MODE_PRIVATE);

			if (sharedPrefs != null) {
				String news = sharedPrefs.getString("news", null);
				if (!TextUtils.isEmpty(news)) {
					try {
						JSONObject json = new JSONObject(news);
						String title = json.optString("title");
						if (!TextUtils.isEmpty(title)) {
							TextView tv_news_title = (TextView) ly_news
									.findViewById(R.id.tv_news_title);
							tv_news_title.setText(title);
						}

						String summary = json.optString("summary");
						if (!TextUtils.isEmpty(summary)) {
							TextView tv_news_content = (TextView) ly_news
									.findViewById(R.id.tv_news_content);
							tv_news_content.setText(summary);
						}
					} catch (JSONException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}

				}

				String str = sharedPrefs.getString("advert", null);
				MyLog.v(TAG, "advert == " + str);
			}

		}

		void setListener() {
			ly_news.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				}

			});
		}

		void getImage() {

		}
	}

	class RecommendBooks {
		ImageView[] iv_books = new ImageView[3];

		public RecommendBooks() {
			initData();
		}

		void initData() {
			SharedPreferences sharedPrefs = AppStatus.frontActivity
					.getSharedPreferences("home_content", Context.MODE_PRIVATE);

			if (sharedPrefs != null) {
				String str = sharedPrefs.getString("books", null);
				if (!TextUtils.isEmpty(str)) {
					MyLog.v(TAG, "books = " + str);
					// try {
					// JSONArray array = new JSONArray(str);
					// mlist.clear();
					// for (int i = 0; i < array.length(); i++) {
					// JSONObject obj = array.optJSONObject(i);
					// if (obj != null) {
					// AdvertBannerInfo info = new AdvertBannerInfo(
					// obj);
					// mlist.add(info);
					// }
					// }
					// } catch (JSONException e) {
					// // TODO 自动生成的 catch 块
					// e.printStackTrace();
					// }

				}
			}

		}

	}

	class RecommendActivitys{
		
	}
}
