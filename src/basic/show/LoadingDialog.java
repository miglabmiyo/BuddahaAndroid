package basic.show;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;

import com.miglab.buddha.R;

/**
 * @author song
 * @version 创建时间：2015-1-16 下午11:39:16
 * 类说明 加载loading
 */
public class LoadingDialog extends ProgressDialog {
	Context context;
	boolean cancelable = true;
	String message;
	
	public LoadingDialog(Context context) {
		super(context);
		this.context = context;
		init(null);
	}
	
	public LoadingDialog(Context context, String message, boolean paramCancelable) {
		super(context);
		this.context = context;
		this.cancelable = paramCancelable;
		init(message);
	}
	
	private void init(String message){
		setCancelable(cancelable);
		setText(message);
	}
	
	public void setText(String text) {
		if(!TextUtils.isEmpty(text)){
			this.message = text;
		}else
			this.message = context.getString(R.string.loading);

		setMessage(message);
	}
	
	public void start() {
		show();
	}

	public void stop() {
		dismiss();
	}
}