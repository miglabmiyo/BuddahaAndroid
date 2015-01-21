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
	String HOME_INFO = "/find/1/broad.fcgi";
	String NEAR_BUILD = "/build/1/nearbuild.fcgi";
	String RECOMMEND_BUILD = "/find/1/building.fcgi";
	String SEARCH_BUILD = "/build/1/searchtype.fcgi";
	String BUILD_CONTENT = "/build/1/summary.fcgi";
	String BOOKMALL_HOME = "/find/1/book.fcgi";
	String BOOKMALL_TYPE = "/book/1/searchtype.fcgi";
	String BOOK_DETAIL = "/book/1/booksummary.fcgi";
	String CHAPTER_LIST = "/book/1/chapterlist.fcgi";
	String MY_BOOK_LIST = "/book/1/booklist.fcgi";
	String ADD_TO_SHELF = "/book/1/wantgetbook.fcgi";
	String WANT_GET_BOOK = "/book/1/wantgetbook.fcgi";
	
	String ACTIVE_LIST = "/activities/1/activities.fcgi";
	String ACTIVE_CONTENT = "/activities/1/summary.fcgi";
	String INTRODUCE_HISATHR = "/intro/1/hisathr.fcgi";
	String INTRODUCE_HOME = "/find/1/intro.fcgi";
	String INTRODUCE_ART = "/intro/1/art.fcgi";
	String INTRODUCE_BOOK_TYPE = "/intro/1/introsearchtype.fcgi";
	
	int GET_SUCCESS = 100001;
	int FRESH_IMAGE = 100002;
	
	int ERROR_TIMEOUT = 200000;
	int ERROR_UNKNOWN = 200001;
	int ERROR_PARAMS = 200002;
	
	int GET_INTRODUCE_HISTORY_SUCCESS = 300001;
	int GET_INTRODUCE_THOUGHT_SUCCESS = 300002;
	
	int GET_SUCCESS_OBJ = 400001;
	int GET_SUCCESS_LIST = 400002;
	
	String ERRORMSG_UNKNOWN = "未知错误原因";
}
