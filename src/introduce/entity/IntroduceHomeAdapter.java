package introduce.entity;

import java.util.List;
import com.miglab.buddha.R;
import book.entity.BookInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author 赵龙权
 * @version 创建时间：2015-1-9 下午4:44:03
 * 类说明    介绍首页列表
 */
public class IntroduceHomeAdapter extends BaseAdapter{

	private Context mContext;
	private List<BookInfo> list;
	
	public IntroduceHomeAdapter(Context context, List<BookInfo> list) {
		// TODO Auto-generated constructor stub
		super();
		this.mContext = context;
		this.list = list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.fm_book_list_item, null);
			holder = new ViewHolder();
			convertView.setTag(holder);
			holder.tv_chapter = (TextView) convertView.findViewById(R.id.tv_chapter);
		}
		
		try {
			BookInfo bookInfo = list.get(position);
			if(bookInfo != null){
				holder.tv_chapter.setText("第" + bookInfo.index + "章：" + bookInfo.name);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return convertView;
	}
	
	class ViewHolder {
		TextView tv_chapter; //章节
	}
	
	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}

}
