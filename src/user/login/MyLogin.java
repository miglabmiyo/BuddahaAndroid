package user.login;

import java.util.Map;

import user.task.IMEILoginTask;
import user.task.ThirdLoginTask;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import basic.util.MyLog;
import basic.util.ShowUtil;
import basic.util.ToolUtil;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

/**
 * @author song
 * @version 创建时间：2014-12-4 下午5:27:05 类说明
 */
public class MyLogin {
	String TAG = "MyLogin";
	UMSocialService mController;
	Activity ac;
	MyUser myUser;
	Handler h;

	public MyLogin(Activity ac, Handler h) {
		this.ac = ac;
		this.h = h;
		init();
	}

	void init() {
		mController = UMServiceFactory.getUMSocialService("com.umeng.login");
		com.umeng.socialize.utils.Log.LOG = true;
		myUser = MyUser.getInstance();
	}

	public void weiboLogin() {
		mController.getConfig().setSsoHandler(new SinaSsoHandler());

		mController.doOauthVerify(ac, SHARE_MEDIA.SINA, new UMAuthListener() {
			@Override
			public void onError(SocializeException e, SHARE_MEDIA platform) {
				MyLog.e(TAG, "weiboLogin 授权 onError.");
				ShowUtil.showToast(ac, "授权失败.");
			}

			@Override
			public void onComplete(Bundle value, SHARE_MEDIA platform) {
				if (value != null && !TextUtils.isEmpty(value.getString("uid"))) {
					MyLog.v(TAG, "weiboLogin 授权成功 == " + value);
					myUser.id = value.getString("uid");
					myUser.type = 1;
					myUser.token = value.getString("access_token");
					myUser.nickname = value.getString("userName");
					String tmp = value.getString("expires_in");
					myUser.expires = System.currentTimeMillis()
							+ Long.valueOf(tmp);

					mController.getPlatformInfo(ac, SHARE_MEDIA.SINA,
							new UMDataListener() {
								@Override
								public void onStart() {
									MyLog.v(TAG,
											"weiboLogin getPlatformInfo 获取平台数据开始...");
								}

								@Override
								public void onComplete(int status,
										Map<String, Object> info) {
									if (status == 200 && info != null) {
										MyLog.v(TAG,
												"weiboLogin getPlatformInfo info = "
														+ info);
										myUser.gender = (Integer) info
												.get("gender");
										myUser.headUrl = info.get(
												"profile_image_url").toString();
										myUser.location = info.get("location")
												.toString();

										thirdRequest();
									} else {
										MyLog.e(TAG,
												"weiboLogin getPlatformInfo 发生错误："
														+ status);
									}
								}
							});
				} else {
					MyLog.e(TAG, "weiboLogin 授权失败 == " + value);
					ShowUtil.showToast(ac, "授权失败.");
				}
			}

			@Override
			public void onCancel(SHARE_MEDIA platform) {
				MyLog.v(TAG, "weiboLogin 授权 onCancel.");
			}

			@Override
			public void onStart(SHARE_MEDIA platform) {
				MyLog.v(TAG, "weiboLogin 授权 onStart.");
			}
		});

	}

	public void qqLogin() {
		// 参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(ac, "1103552902",
				"DOZGq3GOSGOqv83n");
		qqSsoHandler.addToSocialSDK();

		mController.doOauthVerify(ac, SHARE_MEDIA.QQ, new UMAuthListener() {
			@Override
			public void onStart(SHARE_MEDIA platform) {
				MyLog.v(TAG, "qqLogin 授权 onStart.");
			}

			@Override
			public void onError(SocializeException e, SHARE_MEDIA platform) {
				MyLog.e(TAG, "qqLogin 授权 onError.");
			}

			@Override
			public void onComplete(Bundle value, SHARE_MEDIA platform) {
				MyLog.v(TAG, "qqLogin 授权 onComplete value = " + value);

				myUser.id = value.getString("openid");
				myUser.type = 3;
				myUser.token = value.getString("access_token");
				String tmp = value.getString("expires_in");
				myUser.expires = System.currentTimeMillis() + Long.valueOf(tmp)
						* 1000;

				// 获取相关授权信息
				mController.getPlatformInfo(ac, SHARE_MEDIA.QQ,
						new UMDataListener() {
							@Override
							public void onStart() {
								MyLog.v(TAG, "qqLogin 获取平台数据开始...onStart");
							}

							@Override
							public void onComplete(int status,
									Map<String, Object> info) {
								MyLog.v(TAG,
										"qqLogin 获取平台数据完成...onComplete info = "
												+ info);
								if (status == 200 && info != null) {
									String tmp = info.get("gender").toString();
									if (tmp == "男")
										myUser.gender = 1;
									else
										myUser.gender = 0;

									myUser.nickname = info.get("screen_name")
											.toString();
									myUser.headUrl = info.get(
											"profile_image_url").toString();
									myUser.location = info.get("city")
											.toString();

									thirdRequest();
								} else {
									MyLog.e(TAG,
											"qqLogin getPlatformInfo 发生错误："
													+ status);
								}
							}
						});
			}

			@Override
			public void onCancel(SHARE_MEDIA platform) {
				MyLog.v(TAG, "qqLogin 授权 onCancel.");
			}
		});
	}

	public void wxLogin() {
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(ac, "wx2308555ca50aab0f",
				"22df7cb9923311d97a950345452e6da4");
		wxHandler.addToSocialSDK();

		mController.doOauthVerify(ac, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
			@Override
			public void onStart(SHARE_MEDIA platform) {
				MyLog.v(TAG, "微信登录 授权 onStart.");
			}

			@Override
			public void onError(SocializeException e, SHARE_MEDIA platform) {
				MyLog.e(TAG, "微信登录 授权 onError.");
			}

			@Override
			public void onComplete(Bundle value, SHARE_MEDIA platform) {
				MyLog.v(TAG, "微信登录 授权 onComplete value = " + value);

				// 获取相关授权信息
				mController.getPlatformInfo(ac, SHARE_MEDIA.WEIXIN,
						new UMDataListener() {
							@Override
							public void onStart() {
								MyLog.v(TAG, "微信登录 获取平台数据开始...onStart");
							}

							@Override
							public void onComplete(int status,
									Map<String, Object> info) {
								MyLog.v(TAG,
										"微信登录 获取平台数据完成...onComplete info = "
												+ info);
								if (status == 200 && info != null) {
								} else {
									MyLog.e(TAG, "微信登录 获取平台数据 发生错误："
											+ status);
								}
							}
						});
			}

			@Override
			public void onCancel(SHARE_MEDIA platform) {
				MyLog.v(TAG, "微信登录 授权 onCancel.");
			}
		});
	}

	public void guestLogin() {
		myUser.type = 0;
		String imei = ToolUtil.getIMEI(ac);
		new IMEILoginTask(h, imei).execute();
	}

	public void thirdRequest() {
		new ThirdLoginTask(h).execute();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		MyLog.v(TAG, "onActivityResult requestCode: " + requestCode);
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

}
