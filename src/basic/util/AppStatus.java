package basic.util;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;


/**
 * @author song
 * @version 创建时间：2014-11-24 下午4:38:40
 * 类说明
 */
public class AppStatus extends Application {
	private static AppStatus instance;
	public static Activity frontActivity;
	private static List<Activity> activityList = new ArrayList<Activity>();

	public static AppStatus getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance = this;
		
		// 设定内存利用率的百分比
//		VMRuntime.getRuntime().setTargetHeapUtilization(0.75f);

		// 在使用 百度地图SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(this);
	}

	public synchronized static void register(Activity activity) {
		activityList.add(activity);
	}

	public synchronized static void resume(Activity activity) {
		frontActivity = activity;
	}

	/** Activity被销毁时，从Activities中移除 */
	public synchronized static void unregister(Activity activity) {
		if (activityList != null && activityList.size() != 0) {
			activityList.remove(activity);

			if (!activity.isFinishing()) {
				activity.finish();
			}
		} else {
			// MyLog.i("UserStatus","No Activity in pool! unregister");
		}
	}

	public static Activity getActivityByName(String name) {

		for (int i = activityList.size() - 1; i >= 0; i--) {
			Activity ac = activityList.get(i);
			if (ac.isFinishing())
				continue;
			if (ac.getClass().getSimpleName().equalsIgnoreCase(name)) {
				return ac;
			}
		}
		return null;
	}

	public static Activity getActivityByContainsName(String name) {

		for (int i = activityList.size() - 1; i >= 0; i--) {
			Activity ac = activityList.get(i);
			if (ac.isFinishing())
				continue;
			if (ac.getClass().getSimpleName().contains(name)) {
				return ac;
			}
		}
		return null;
	}

	/**
	 * 退出程序
	 * 
	 * @param con
	 */
	public synchronized static void exitApp(Context con) {
		// 结束activity队列中的所有activity
		if (activityList != null) {
			for (Activity ac : activityList) {
				if (!ac.isFinishing()) {
					ac.finish();
				}
			}
		}

		// 清除通知栏
		((NotificationManager) con
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE))
				.cancelAll();
		// System.exit(0);
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public static void showCurrentOpenActivity() {
		for (int i = activityList.size() - 1; i >= 0; i--) {
			Activity ac = activityList.get(i);
			if (ac.isFinishing())
				continue;
			MyLog.i("AppStatus", "showCurrentOpenActivity ac = "
					+ ac.getClass().getName());
		}
	}

}