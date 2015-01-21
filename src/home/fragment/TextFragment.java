package home.fragment;

import android.text.TextUtils;
import android.widget.TextView;
import basic.show.BaseFragment;
import book.task.BookMallTask;

import com.miglab.buddha.R;

public class TextFragment extends BaseFragment  {
	String text;
	int color;
	
	public TextFragment(String text, int color) {
		super();
		this.text = text;
		this.color = color;
	}

	@Override
	protected void setLayout() {
		rootResource = R.layout.fm_text;
	}

	@Override
	protected void initView() {
		new BookMallTask(h).execute();

		if(!TextUtils.isEmpty(text)){
			TextView tv = (TextView)vRoot.findViewById(R.id.tv);
			tv.setText(text);
			if(color > 0){
				tv.setBackgroundColor(color);
			}
		}
	}

}
