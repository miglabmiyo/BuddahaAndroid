package basic.task;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import basic.io.ImageUtil;
import basic.util.MyLog;
import basic.util.ToolUtil;

/**
 * @author song
 * @version 创建时间：2012-11-28 下午2:06:26 类说明 批量下载图片并通知adapter更新
 */
public class ImagesDownloadTask extends AsyncTask<Void, Void, Object> {
	private final static String TAG = "ImagesDownloadTask";
	private Handler handler;
	private int count = 0;
	private List<String> images;
	private String path;
	private int msgWhat;
	private boolean sleep;
	private int every = 3;

	public ImagesDownloadTask(Handler handler, List<String> list, String path,
			int msgWhat, boolean needSleep) {
		this.handler = handler;
		images = list;
		this.path = path;
		this.msgWhat = msgWhat;
		sleep = needSleep;
	}

	public ImagesDownloadTask(Handler handler, List<String> list, String path,
			int msgWhat, boolean needSleep, int space) {
		this.handler = handler;
		images = list;
		this.path = path;
		this.msgWhat = msgWhat;
		sleep = needSleep;
		if (space > 0)
			every = space;
	}

	@Override
	protected Object doInBackground(Void... params) {
		// TODO Auto-generated method stub
		if (images == null || images.size() <= 0)
			return null;

		try {
			Bitmap bitmap = null;

			// MyLog.v(TAG,
			// "开始下载图片:"+MyLog.showStringArr(images));

			for (String url : images) { // 只遍历一次
				try {
					if (url == null || url.length() <= 0)
						continue;

					String imageName = ToolUtil.md5(url);
					if (new File(path + imageName).exists()) {
						// bitmap = ImageUtil.getBitmap(path + imageName);
						// if (bitmap != null) {
						// // MyLog.i(TAG, "图片存在SD卡上：" + url);
						// bitmap.recycle();
						// bitmap = null;
						continue;
						// }
					}

					MyLog.i(TAG, "从网络获取图片：" + url);

					// bitmap = ImageLoader.loadImageFromInternet(url);

					// boolean res = loadImageToFile(url, path + imageName);

					byte[] data = downloadInputStream(url);
					if (data != null) {
						bitmap = BitmapFactory.decodeByteArray(data, 0,
								data.length);
						// BitmapFactory.Options opts = new
						// BitmapFactory.Options();
						// opts.inJustDecodeBounds = true;
						// BitmapFactory.decodeByteArray(data, 0, data.length,
						// opts);
						//
						// opts.inSampleSize = ImageUtil.computeSampleSize(opts,
						// -1,
						// 100 * 100);
						// opts.inJustDecodeBounds = false;
						//
						// bitmap = BitmapFactory.decodeByteArray(data, 0,
						// data.length,
						// opts);
					}

					// if(res){
					// bitmap = ImageUtil.getBitmap(path + imageName);
					// if(bitmap != null){
					// // //MyLog.i(TAG, "图片存在SD卡上："+url);
					// bitmap.recycle();
					// bitmap = null;
					// // continue;
					// }
					// }

					if (data != null && bitmap != null) {
						ImageUtil.saveBitmapOther(bitmap, path + imageName);
						// ImageUtil.savePicToLocal(bitmap, path + imageName);
						bitmap.recycle();
						// MyLog.i(TAG, "图片保存到SD卡上，备用：" + url);
					} else {
						// MyLog.e(TAG, "图片下载失败：" + url);
					}

					if (count != 0 && count % every == 0)
						handler.sendEmptyMessage(msgWhat);

				} catch (Exception e) {
					e.printStackTrace();

				} catch (OutOfMemoryError error) {
					error.printStackTrace();

				} finally {
					count++;
				}
			}

			handler.sendEmptyMessage(msgWhat);

		} catch (Exception e) {
			// TODO: handle exception
			// MyLog.e(TAG, "图片list有问题.");
		}
		return null;
	}

	private boolean loadImageToFile(String url, String fileName)
			throws IOException {
		// MyLog.v(TAG, "url:"+url+", fileName:"+fileName);

		if (TextUtils.isEmpty(fileName))
			return false;

		// InputStream ins = null;
		// try {
		// ins = downloadInputStream(url);
		// } catch (Exception e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// if(ins == null)
		// return false;
		//
		boolean res = false;
		//
		// File file = new File(fileName);
		// if(!file.exists()){
		// file.createNewFile();
		// }
		// BufferedInputStream in = new BufferedInputStream(ins);
		// BufferedOutputStream out = new BufferedOutputStream(new
		// FileOutputStream(file));
		// try {
		//
		// int len = -1;
		// byte[] b = new byte[1024];
		// while((len = in.read(b)) != -1) {
		// out.write(b, 0, len);
		// }
		//
		// res = true;
		// }catch (Exception e) {
		// // TODO: handle exception
		// e.printStackTrace();
		// }finally {
		// if(in != null) {
		// try {
		// in.close();
		// }catch (Exception e) {
		// // TODO: handle exception
		// e.printStackTrace();
		// }
		// }
		// if(out != null) {
		// try {
		// out.close();
		// }catch (Exception e) {
		// // TODO: handle exception
		// e.printStackTrace();
		// }
		// }
		// }

		return res;
	}

	private byte[] downloadInputStream(String url) throws Exception {
		HttpClient client = AndroidHttpClient.newInstance("Android");
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 3000);
		HttpConnectionParams.setSocketBufferSize(params, 3000);
		HttpGet httpGet = null;
		InputStream inputStream = null;
		byte[] data = null;
		try {
			httpGet = new HttpGet(url);
			HttpResponse response = client.execute(httpGet);
			int stateCode = response.getStatusLine().getStatusCode();
			if (stateCode != HttpStatus.SC_OK) {
				// MyLog.d(TAG, "func [loadImage] stateCode=" + stateCode);
				return null;
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				try {
					inputStream = entity.getContent();
					data = readStream(inputStream, sleep);
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (ClientProtocolException e) {
			httpGet.abort();
			e.printStackTrace();
		} catch (IOException e) {
			httpGet.abort();
			e.printStackTrace();
		} finally {
			((AndroidHttpClient) client).close();
		}
		return data;

	}

	/*
	 * 得到图片字节流 数组大小
	 */
	public static byte[] readStream(InputStream inStream, boolean sleep)
			throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
			if (sleep)
				Thread.sleep(100);
		}
		outStream.close();
		inStream.close();
		return outStream.toByteArray();
	}

}
