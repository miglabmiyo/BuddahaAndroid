package book.entity;

import java.util.ArrayList;

/**
 * @author song
 * @version 创建时间：2015-1-16 下午11:15:37
 * 类说明
 */
public class BookReadInfo {
	public int id;
	public String booktoken;
	public int total; //总共章节数
	public ArrayList<ChapterInfo> list;

}
