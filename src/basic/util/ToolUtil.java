package basic.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * @author song
 * @version 创建时间：2014-11-27 下午7:49:54
 * 类说明
 */
public class ToolUtil {

    private static Executor executor = Executors.newSingleThreadExecutor();

    public static final void executeInSingleThread(Runnable run) {
        executor.execute(run);
    }

    /**
     * md5加密
     *
     * @param plainText
     * @return
     */
    public static String md5(String plainText) {
        // 返回字符串
        String md5Str = null;
        try {
            // 操作字符串
            StringBuffer buf = new StringBuffer();
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 添加要进行计算摘要的信息,使用 plainText 的 byte 数组更新摘要。
            md.update(plainText.getBytes());

            // 计算出摘要,完成哈希计算。
            byte b[] = md.digest();
            int i;

            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }

                if (i < 16) {
                    buf.append("0");
                }

                // 将整型 十进制 i 转换为16位，用十六进制参数表示的无符号整数值的字符串表示形式。
                buf.append(Integer.toHexString(i));

            }

            // 32位的加密
            md5Str = buf.toString();

            // 16位的加密
            // md5Str = buf.toString().md5Strstring(8,24);

        } catch (Exception e) {
            MyLog.showException(e);
        }
        return md5Str;
    }

    /**
     * 获取IMEI
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        String result = ((TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        if (result == null) {
            return "";
        }
        return result;
    }

    public static String getTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        return df.format(now);
    }

    /**
     * 将指定格式的日期字符串转化成时间戳
     *
     * @param str
     * @param format
     * @return
     */
    public static long parseTimeStr2Timestamp(String str, String format) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(format)) {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
            try {
                synchronized (sdf) {
                    // SimpleDateFormat is not thread safe
                    return sdf.parse(str).getTime();
                }
            } catch (ParseException pe) {
                MyLog.showException(pe);
            }
        }
        return 0l;
    }

    /**
     * 将指定格式的日期字符串解析成日期类
     *
     * @param str
     * @param format
     * @return
     */
    public static Date parseTimeStr2Date(String str, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        try {
            synchronized (sdf) {
                // SimpleDateFormat is not thread safe
                return sdf.parse(str);
            }
        } catch (ParseException pe) {
            MyLog.showException(pe);
        }
        return null;
    }

    /**
     * 将指定格式的日期字符串解析成界面展示的样式
     *
     * @param str
     * @param format
     * @return ("刚刚"、"x分钟前"、"x小时前"、"MM-dd HH:mm"格式)
     */
    public static String parseDate(String str, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        try {
            synchronized (sdf) {
                // SimpleDateFormat is not thread safe
                return getTimeDiff(sdf.parse(str));
            }
        } catch (ParseException pe) {
            MyLog.showException(pe);
        }
        return "";
    }

    /**
     * 将指定格式的时间字符串转换成"yyyy-MM-dd HH:mm:ss"格式的时间字符串
     *
     * @param str
     * @param format
     * @param isSinaData
     * @return "yyyy-MM-dd HH:mm:ss"格式
     */
    public static String formatDateToStr(String str, String format,
                                         boolean isSinaData) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        if (isSinaData) {
            str = Pattern.compile(" \\+\\d{4} ").matcher(str).replaceAll(" ");
            // sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        }
        try {
            synchronized (sdf) {
                // SimpleDateFormat is not thread safe
                Date date = sdf.parse(str);
                SimpleDateFormat sdf_other = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                return sdf_other.format(date);
            }
        } catch (Exception e) {
            MyLog.showException(e);
        }
        return "";
    }

    private static String getTimeDiff(Date date) {
        Calendar cal = Calendar.getInstance();
        long diff = 0;
        Date dnow = cal.getTime();
        String str = "";
        diff = dnow.getTime() - date.getTime();

        // System.out.println("diff---->"+diff);
        if (diff > 24 * 60 * 60 * 1000 || diff < 0) {
            // System.out.println("1天前");
            // str="1天前";
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm",
                    Locale.ENGLISH);
            str = dateFormat.format(date);
        } else if (diff > 1 * 60 * 60 * 1000) {
            Long num = diff / (1 * 60 * 60 * 1000);
            str = String.valueOf(num) + "小时前";
        } else if (diff > 1 * 60 * 1000) {
            Long num = diff / (1 * 60 * 1000);
            str = String.valueOf(num) + "分钟前";
        } else {
            str = "刚刚";
        }
        return str;
    }
    
    public static String formatTimeDiff(String timeStr) {
        if (TextUtils.isEmpty(timeStr)) {
			return timeStr;
		}
        String[] strs = timeStr.split(":");
        if (strs.length == 3) {
        	try {
        		int hour = Integer.parseInt(strs[0]);
    			int min = Integer.parseInt(strs[1]);
    			int sec = Integer.parseInt(strs[2]);
    			
    			if (hour >= 24) {
					return  hour/24 +"天前更新";
				}else if (hour >= 0) {
					return  hour +"小时前更新";
				}else if (min >= 0) {
					return  min +"分钟前更新";
				}else if (hour >= 0) {
					return  sec +"秒前更新";
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
        
        return timeStr;
    }

    /**
     * 显示时间（long转化为String）
     *
     * @param time
     * @return 返回为"yyyy-MM-dd HH:mm:ss"格式的时间显示
     */
    public static String showTimeFromMilis(long time) {
        return showTimeFromMilis(time, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 显示时间（long转化为String）
     *
     * @param time
     * @param formatStr
     *            需要显示的时间格式，比如"yyyy-MM-dd HH:mm:ss"
     * @return 返回指定格式的时间显示
     */
    public static String showTimeFromMilis(long time, String formatStr) {
        String res = "";
        if (time > 0 && !TextUtils.isEmpty(formatStr)) {
            try {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(time);
                SimpleDateFormat sdf = new SimpleDateFormat(formatStr,
                        Locale.ENGLISH);
                res = sdf.format(cal.getTime());
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        return res;
    }

    public static boolean compare_date(String str1, String str2) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date dt1 = df.parse(str1);
            Date dt2 = df.parse(str2);
            if (dt1.getTime() >= dt2.getTime()) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 转换时间（String转化为String）
     *
     * @param time
     *            时间
     * @param formatBefore
     *            转换前的时间格式，比如"yyyy-MM-dd HH:mm:ss"
     * @param formatAfter
     *            转换后的时间格式，比如"MM-dd"
     * @return 返回指定格式的时间显示
     */
    public static String formatTime(String time, String formatBefore,
                                    String formatAfter) {
        if (!TextUtils.isEmpty(time) && !TextUtils.isEmpty(formatBefore)
                && !TextUtils.isEmpty(formatAfter)) {
            try {
                SimpleDateFormat sdf_before = new SimpleDateFormat(
                        formatBefore, Locale.ENGLISH);
                synchronized (sdf_before) {
                    // SimpleDateFormat is not thread safe
                    Date date = sdf_before.parse(time);
                    SimpleDateFormat sdf_after = new SimpleDateFormat(
                            formatAfter, Locale.ENGLISH);
                    return sdf_after.format(date);
                }
            } catch (Exception e) {
                MyLog.showException(e);
            }
        }

        return "";
    }

    /** 判断字符串中是否含有特殊字符 */
    public static boolean containsSpecialWord(String str) {
        boolean result = false;
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）――+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        if (m.find()) {
            result = true;
        }
        return result;
    }

    /**
     * 判断SD卡是否可用（SD卡被占用算作无SD卡）
     *
     * @return true可用,反之false
     */
    public static boolean checkSDcardState() {
        return android.os.Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED) ? true : false;
    }

    /**
     * 关闭键盘
     *
     * @param con
     */
    public static void closeKeyBoard(Activity con) {
        try {
            InputMethodManager imm = (InputMethodManager) con
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(con.getCurrentFocus().getWindowToken(),
                    InputMethodManager.RESULT_UNCHANGED_SHOWN);
            // 得到InputMethodManager的实例
            // if (imm.isActive()) {
            // // 如果开启
            // imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
            // InputMethodManager.HIDE_NOT_ALWAYS);
            // // 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
            // }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * 关闭键盘
     *
     * @param con
     */
    public static void closeKeyBoard(Context con, EditText et) {
        try {
            InputMethodManager imm = (InputMethodManager) con
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(et.getWindowToken(),
                    InputMethodManager.RESULT_UNCHANGED_SHOWN);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * 显示软键盘
     *
     * @param con
     */
    public static void showKeyBoard(Activity con) {
        try {
            InputMethodManager imm = (InputMethodManager) con
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(con.getCurrentFocus(),
                    InputMethodManager.HIDE_IMPLICIT_ONLY);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * 显示软键盘
     *
     * @param con
     */
    public static void showKeyBoard(Activity con, EditText et) {
        try {
            InputMethodManager imm = (InputMethodManager) con
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(et, InputMethodManager.HIDE_IMPLICIT_ONLY);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static void clearCookie(Context context) {
        CookieSyncManager cookieSyncMngr = CookieSyncManager
                .createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public static boolean isAppOnForeground(Activity activity) {
        ActivityManager activityManager = (ActivityManager) activity
                .getApplicationContext().getSystemService(
                        Context.ACTIVITY_SERVICE);
        String packageName = activity.getApplicationContext().getPackageName();

        List<RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    /** 复制文件，检查SD卡存在与否在外层做 */
    public static boolean copyFile(String oldPath, String newPath) {
        File oldFile = new File(oldPath);
        if (!oldFile.exists()) {
            MyLog.e("ToolUtil", "复制文件 旧的文件不存在.");
            return false;
        }

        File newFile = new File(newPath);
        if (newFile.exists()) {
            newFile.delete();
            MyLog.w("ToolUtil", "复制文件 新的文件已存在，删除之.");
        }

        // 开始复制文件
        FileInputStream in = null;
        FileOutputStream out = null;
        FileChannel inC = null;
        FileChannel outC = null;
        try {
            in = new FileInputStream(oldFile);
            out = new FileOutputStream(newFile);
            inC = in.getChannel();
            outC = out.getChannel();
            int length = 1024;
            while (true) {
                if (inC.position() >= inC.size()) {
                    inC.close();
                    outC.close();
                    in.close();
                    out.close();
                    return true;
                }

                if ((inC.size() - inC.position()) < 1024)
                    length = (int) (inC.size() - inC.position());
                else
                    length = 1024;

                inC.transferTo(inC.position(), length, outC);
                inC.position(inC.position() + length);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            MyLog.e("ToolUtil", "复制文件 文件不存在.");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            MyLog.e("ToolUtil", "复制文件 IOException.");
        }

        try {
            inC.close();
            outC.close();
            in.close();
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            MyLog.e("ToolUtil", "复制文件 IOException.");
        }
        return false;
    }

    /**
     * 当ScrollView 有listview时，listview内容显示不全设置
     *
     * @param list
     */
    public static void setListViewHeight(ListView list) {
        if (list == null)
            return;

        ListAdapter listAdapter = list.getAdapter();

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, list);
            listItem.setLayoutParams(new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = (LayoutParams) list.getLayoutParams();
        params.height = totalHeight
                + (list.getDividerHeight() * (listAdapter.getCount() - 1));
        params.width = LayoutParams.MATCH_PARENT;
        list.setLayoutParams(params);

        // LayoutParams params = (LayoutParams)list.getLayoutParams();
        // params.height = totalHeight;
    }

    /**
     * 判断是否是中文字符
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS // 【4E00-9FFF】
                // CJK Unified
                // Ideographs
                // 中日韩统一表意文字
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS // 【F900-FAFF】
                // CJK
                // Compatibility
                // Ideographs
                // 中日韩兼容表意文字
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A // 【3400-4DBF】
                // CJK
                // Unified
                // Ideographs
                // Extension
                // A
                // 中日韩统一表意文字扩充A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION // 【2000-206F】
                // General
                // Punctuation
                // 一般标点符号
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION // 【3000-303F】
                // CJK
                // Symbols
                // and
                // Punctuation
                // 中日韩符号和标点
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) // 【FF00-FFEF】
        // Halfwidth
        // and
        // Fullwidth
        // Forms
        // 半角及全角字符
        {
            return true;
        }
        return false;
    }

    /** 过滤emoji表情 */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) {
                // do nothing，判断到了这里表明，确认有表情字符
                // MyLog.e(TAG, "isEmojiCharacter(codePoint)-->" + codePoint);
                return true;
            }
        }
        return false;
    }

    public static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /**
     * 获取含有汉字的字符串长度
     *
     * @param str
     * @return
     */
    public static int chineseLength(String str) {
        int strLen = 0;
        String chinese = "[\u0391-\uFFE5]";
        // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
        for (int i = 0; i < str.length(); i++) {
            // 获取每一个字符
            String temp = str.substring(i, i + 1);
            // 如果是中文
            if (temp.matches(chinese)) {
                strLen += 2;
            } else {
                strLen += 1;
            }
        }
        return strLen;
    }

    /**
     * 判断是否含有中文
     *
     * @param str
     * @return
     */
    public static boolean isHaveChina(String str) {
        for (int i = 0; i < str.length(); i++) {
            char ss = str.charAt(i);
            boolean s = String.valueOf(ss).matches("[\u4e00-\u9fa5]");
            if (s) {
                return true;
            }
        }

        return false;
    }

    /**
     * 含有空格验证
     *
     * @param str
     * @return
     */
    public static boolean checkBlank(String str) {
        Pattern pattern = Pattern.compile("[\\s]+");
        Matcher matcher = pattern.matcher(str);
        boolean flag = false;
        while (matcher.find()) {
            flag = true;
        }
        return flag;
    }

    /**
     * 账号只能含有数字、字母和下划线
     *
     * @param str
     * @return
     */
    public static boolean checkName(String str) {
        Pattern pattern = Pattern.compile("^[0-9a-zA-Z_]{1,}$");
        Matcher matcher = pattern.matcher(str);
        boolean flag = false;
        while (matcher.find()) {
            flag = true;
        }
        return flag;
    }

	private final static int MB = 1024 * 1024;
	public final static int FREE_SD_SPACE_NEEDED_TO_CACHE = 10;

	/**
	 * 计算sdcard上的剩余空间
	 * 
	 * @return
	 */
	public static int freeSpaceOnSD() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
				.getBlockSize()) / MB;
		return (int) sdFreeMB;
	}

    /**
     * 计算指定目录的剩余空间
     *
     * @return
     */
    public static int freeSpaceOnPath(String path) {
        StatFs stat = new StatFs(path);
        double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
                .getBlockSize()) / MB;
        return (int) sdFreeMB;
    }

	/**
	 * 判断SD卡上是否有足够的剩余空间
	 * 
	 * @return
	 */
	public static boolean isEnoughFreeSpaceOnSD() {
		if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSD()) {
			return false;
		}
		return true;
	}

    /**
     * 判断指定目录是否有足够的剩余空间
     *
     * @return
     */
    public static boolean isEnoughFreeSpaceOnPath(String path) {
        if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnPath(path)) {
            return false;
        }
        return true;
    }


}
