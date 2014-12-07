package home.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import basic.show.BaseFragment;

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View vRoot = inflater.inflate(R.layout.fm_text, container, false);
		if(!TextUtils.isEmpty(text)){
			TextView tv = (TextView)vRoot.findViewById(R.id.tv);
			tv.setText(text);
			if(color > 0){
				tv.setBackgroundColor(color);
			}
		}
		
		
		return vRoot;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

}
