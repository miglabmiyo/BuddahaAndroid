package basic.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import android.os.Environment;
import basic.util.BasicDefine;

/**
 * @author song
 * @version 创建时间：2014-11-27 下午8:49:02
 * 类说明
 */
public class FileCache extends FileWriter{

	private final String DIR = "/.bullha";
	String directory = null;

	public FileCache() {
		directory = getDirectory();
	}

	/**
	 * 初始化目录
	 * 
	 * @return
	 */
	private String getDirectory() {

		String cacheDirectory = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			cacheDirectory = Environment.getExternalStorageDirectory()
					.getPath() + DIR;
		} else {
			cacheDirectory = BasicDefine.PACKAGE_CACHE_DIR;
		}

		File cacheDir = new File(cacheDirectory);
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}

		return cacheDirectory;
	}

	/**
	 * 获取文件大小
	 * 
	 * @param path
	 * @return
	 */
	public long getFileSize(String path) {

		long filesize = 0;

		File file = new File(path);
		if (null != file && file.exists() && file.isFile()) {
			filesize = file.length();
		}
		// //MyLog.d("path: " + path + ", filesize: " + filesize);

		return filesize;
	}

	/**
	 * 根据路径删除指定的目录或文件，无论存在与否
	 * 
	 * @param sPath
	 *            要删除的目录或文件
	 * @return 删除成功返回 true，否则返回 false。
	 */
	public boolean deleteFolder(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 判断目录或文件是否存在
		if (!file.exists()) { // 不存在返回 false
			return flag;
		} else {
			// 判断是否为文件
			if (file.isFile()) { // 为文件时调用删除文件方法
				return deleteFile(sPath);
			} else { // 为目录时调用删除目录方法
				return deleteDirectory(sPath);
			}
		}
	}

	/**
	 * 根据文件路径删除文件
	 * 
	 * @param path
	 * @return
	 */
	public boolean deleteFile(String path) {
		if(path == null)
			return false;
		
		File file = new File(path);
		if (null != file && file.exists() && file.isFile()) {
			return file.delete();
		}
		return true;
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param sPath
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public boolean deleteDirectory(String sPath) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 更新文件的修改时间
	 * 
	 * @param dir
	 * @param filename
	 */
	public void updateFileModifyTime(String dir, String filename) {
		if (null == dir || null == filename) {
			return;
		}

		File file = new File(dir, filename);
		long now = System.currentTimeMillis();
		file.setLastModified(now);
	}

	/**
	 * 如果指定目录dir下，文件个数大于filecount，则删除指定比例percent的文件 filecount大于0，则直接删除指定比例文件
	 * percent的值为0到1
	 * 
	 * @param dir
	 * @param filecount
	 * @param percent
	 */
	public ArrayList<String> deleteSomeFiles(String dir, int filecount,
			float percent, String fileext) {

		ArrayList<String> deleteFileNames = new ArrayList<String>();

		if (null == dir || filecount == 0 || percent == 0) {
			return deleteFileNames;
		}

		File fileDir = new File(dir);
		File[] files = fileDir.listFiles();
		if (files != null && files.length > filecount) {
			int deleteFileCount = (int) (files.length * percent);
			Arrays.sort(files, new FileLastModifySort());

			for (int i = 0; i < deleteFileCount; i++) {
				File pfile = files[i];
				if (pfile.getName().endsWith(fileext)) {
					pfile.delete();
					deleteFileNames.add(pfile.getName());
				}
			}
		}

		return deleteFileNames;
	}

	class FileLastModifySort implements Comparator<File> {

		public int compare(File file1, File file2) {
			// TODO Auto-generated method stub
			if (file1.lastModified() > file2.lastModified()) {
				return 1;
			} else if (file1.lastModified() == file2.lastModified()) {
				return 0;
			} else {
				return -1;
			}
		}

	}
}
