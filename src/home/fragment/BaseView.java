package home.fragment;

import basic.util.MyLog;

/**
 * @author song
 * @version 创建时间：2014-12-27 下午6:18:31 类说明
 */
public class BaseView {
	protected void onDataUpdated(Object obj) {
		MyLog.v("onDataUpdated obj: " + obj);
	}
}
