package com.hofc.hofc.view;

import com.hofc.hofc.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActusView extends LinearLayout {
	private LinearLayout linear;
	private ImageView imageView;
	private TextView titleView;
	private TextView texteView;
	private TextView dateView;
	
	public ActusView(Context context) {
		super(context);

		LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.actus_view, this, true);
		this.linear = (LinearLayout) findViewById(R.id.linear_element_actu);
		this.imageView = (ImageView)this.linear.getChildAt(0);
		this.titleView = (TextView)this.linear.getChildAt(1);
		this.texteView = (TextView)this.linear.getChildAt(2);
		this.dateView = (TextView)this.linear.getChildAt(3);
	}

	public ActusView(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.actus_view, this, true);
		
		this.imageView = (ImageView)getChildAt(0);
		this.titleView = (TextView)getChildAt(1);
		this.texteView = (TextView)getChildAt(2);
		this.dateView = (TextView)getChildAt(3);

	}

	public ActusView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	public void setTitle(String title) {
		if(this.titleView != null) {
			this.titleView.setText(title);
		}
	}
	
	public void setTexte(String texte) {
		if(this.texteView != null) {
			this.texteView.setText(texte);
		}
	}

	public void setDate(String date) {
		if(this.dateView != null) {
			this.dateView.setText(date);
		}
	}

}
