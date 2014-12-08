package basic.net;
/**
 * @author song
 * @version 创建时间：2014-11-27 下午7:39:04
 * 类说明
 */
public interface ApiDefine {

	String DOMAIN = "http://112.124.49.59/cgi-bin/buddha";
	
	String QUICK_LOGIN = "/user/1/quicklogin.fcgi";
	String OTHER_LOGIN = "/user/1/thirdlogin.fcgi";
	String BAIDU_PUSH = "/user/1/bdbindpush.fcgi";
	String HOME_INFO = "/find/1/findrecom.fcgi";
	

	int LOGIN_SUCCESS = 100001;
	int GET_SUCCESS = 100002;
	
	
	int ERROR_TIMEOUT = 200000;
	int ERROR_UNKNOWN = 200001;
	
	String ERRORMSG_UNKNOWN = "未知错误原因";
}
