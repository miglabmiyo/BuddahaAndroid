package com.miglab.buddha.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import basic.show.Start;
import basic.util.MyLog;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	private final static String TAG = "WXEntryActivity";
	private IWXAPI api;
	private static final String APP_ID = "wx2308555ca50aab0f";

	// private WeiXinShare weiXinShare;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.my_work_layout);
		MyLog.i(TAG, "onCreate ... ");
		api = WXAPIFactory.createWXAPI(this, APP_ID, false);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		MyLog.i(TAG, "onNewIntent ... ");
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
		MyLog.v(TAG, "onReq ... ");
		MyLog.i(TAG, "onReq-->" + req);
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			MyLog.i(TAG, "COMMAND_GETMESSAGE_FROM_WX");
			goToGetMsg();
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			MyLog.i(TAG, "COMMAND_SHOWMESSAGE_FROM_WX");
			finish();
			break;
		default:
			break;
		}
	}

	private void goToGetMsg() {
		Intent intent = new Intent(this, Start.class);
		intent.putExtras(getIntent());
		startActivity(intent);
		finish();
	}

	public void onResp(BaseResp resp) {
		int result = 0;
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			// result = Constant.SHARE_SUCCESS;
			// MobclickAgent.onEvent(this, "share","weixin_success");
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			// result = Constant.SHARE_CANCEL;
			// MobclickAgent.onEvent(this, "share","weixin_fail");
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			// result = Constant.SHARE_FAIL;
			// MobclickAgent.onEvent(this, "share","weixin_fail");
			break;
		default:
			// result = Constant.SHARE_FAIL;
			// MobclickAgent.onEvent(this, "share","weixin_fail");
			break;
		}
		MyLog.v(TAG, "onResp ... " + result);
		finish();
	}

}