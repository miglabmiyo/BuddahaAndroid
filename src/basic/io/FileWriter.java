package basic.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.text.TextUtils;
import basic.util.MyLog;

/**
 * @author song
 * @version 创建时间：2014-11-27 下午8:39:38 类说明 简易文件读写
 */
public class FileWriter {
	private String TAG = "FileWriter";
	private int PASS = 125;

	/** 读文件 */
	public String readFile(String path) {
		MyLog.i(TAG, "readFile enter：path--" + path);
		String res = null;
		FileInputStream fis = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			fis = new FileInputStream(path);
			int len;
			while ((len = fis.read()) != -1) {
				len = len ^ PASS;
				baos.write(len);
			}
			res = baos.toString();
		} catch (FileNotFoundException e) {
			MyLog.showException(e);
		} catch (IOException e) {
			MyLog.showException(e);
		} finally {
			try {
				if (fis != null)
					fis.close();

				if (baos != null)
					baos.close();
			} catch (IOException e1) {
				MyLog.showException(e1);
			}
		}
		MyLog.i(TAG, "readFile end.");
		return res;
	}

	/** 写文件 */
	public boolean writeFile(String path, String content) {
		if (TextUtils.isEmpty(path) || TextUtils.isEmpty(content)) {
			MyLog.e(TAG, "writeFile 参数为空---path：" + path);
			return false;
		}
		MyLog.i(TAG, "writeFile enter：path--" + path);
		boolean success = false;
		FileOutputStream fos = null;
		InputStream is = null;
		try {
			fos = new FileOutputStream(path);
			is = new ByteArrayInputStream(content.getBytes());
			int len;
			while ((len = is.read()) != -1) {
				len = len ^ PASS;
				fos.write(len);
			}
			success = true;
		} catch (FileNotFoundException e) {
			MyLog.e(TAG, "writeFile FileNotFoundException path:" + path);
			if (MyLog.isShowlog())
				e.printStackTrace();

		} catch (IOException e) {
			MyLog.e(TAG, "writeFile IOException.");
			if (MyLog.isShowlog())
				e.printStackTrace();
		} finally {
			try {
				if (fos != null)
					fos.close();

				if (is != null)
					is.close();
			} catch (IOException e1) {
				MyLog.e(TAG, "writeFile close FileOutputStream error.");
				if (MyLog.isShowlog())
					e1.printStackTrace();
			}
		}
		MyLog.i(TAG, "writeFile end：success--" + success);
		return success;
	}
}
