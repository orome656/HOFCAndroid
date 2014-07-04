package com.hofc.hofc.view;

import com.hofc.hofc.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActusView extends LinearLayout {

	public ActusView(Context context) {
		super(context);
	}

	public ActusView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		ImageView imageView = (ImageView)getChildAt(0);
		TextView titleView = (TextView)getChildAt(1);
		TextView texteView = (TextView)getChildAt(2);
		TextView dateView = (TextView)getChildAt(3);

		LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.actus_view, this, true);
	}

	public ActusView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

}
