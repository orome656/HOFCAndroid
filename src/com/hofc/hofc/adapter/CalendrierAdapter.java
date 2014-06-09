package com.hofc.hofc.adapter;

import com.hofc.hofc.R;
import com.hofc.hofc.constant.AppConstant;
import com.hofc.hofc.data.CalendrierBDD;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.vo.CalendrierLineVO;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CalendrierAdapter extends BaseAdapter {

	LayoutInflater inflater;
	
	public CalendrierAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		CalendrierBDD.initiate(context);
	}	
	@Override
	public int getCount() {
		if(DataSingleton.getCalendrier() != null) {
			return DataSingleton.getCalendrier().size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return DataSingleton.getCalendrier().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_calendrier, null);
			holder.imageEquipe1 = (ImageView)convertView.findViewById(R.id.calendrier_image_1);
			holder.calendrierEquipe1 = (TextView)convertView.findViewById(R.id.calendrier_equipe_1);
			holder.calendrierScore1 = (TextView)convertView.findViewById(R.id.calendrier_score_1);
			holder.calendrierScore2 = (TextView)convertView.findViewById(R.id.calendrier_score_2);
			holder.calendrierEquipe2 = (TextView)convertView.findViewById(R.id.calendrier_equipe_2);
			holder.imageEquipe2 = (ImageView)convertView.findViewById(R.id.calendrier_image_2);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		CalendrierLineVO line = DataSingleton.getCalendrier().get(position);
		if(AppConstant.hofcName.equalsIgnoreCase(line.getEquipe1())) {
			holder.imageEquipe1.setImageResource(R.drawable.ic_launcher);
			holder.imageEquipe2.setImageResource(android.R.color.transparent);
			holder.calendrierEquipe1.setTextColor(Color.BLUE);
			holder.calendrierEquipe2.setTextColor(Color.BLACK);
			holder.calendrierScore1.setTextColor(Color.BLUE);
			holder.calendrierScore2.setTextColor(Color.BLACK);
		} else if(AppConstant.hofcName.equalsIgnoreCase(line.getEquipe2())) {
			holder.imageEquipe2.setImageResource(R.drawable.ic_launcher);
			holder.imageEquipe1.setImageResource(android.R.color.transparent);
			holder.calendrierEquipe2.setTextColor(Color.BLUE);
			holder.calendrierEquipe1.setTextColor(Color.BLACK);
			holder.calendrierScore2.setTextColor(Color.BLUE);
			holder.calendrierScore1.setTextColor(Color.BLACK);
		}
		
		if(line.getScore1() > line.getScore2()) {
			holder.calendrierScore1.setTypeface(null, Typeface.BOLD);
			holder.calendrierEquipe1.setTypeface(null, Typeface.BOLD);
			holder.calendrierScore2.setTypeface(null, Typeface.NORMAL);
			holder.calendrierEquipe2.setTypeface(null, Typeface.NORMAL);
		} else if (line.getScore1() < line.getScore2()) {
			holder.calendrierScore2.setTypeface(null, Typeface.BOLD);
			holder.calendrierEquipe2.setTypeface(null, Typeface.BOLD);
			holder.calendrierScore1.setTypeface(null, Typeface.NORMAL);
			holder.calendrierEquipe1.setTypeface(null, Typeface.NORMAL);
		} else {
			holder.calendrierScore1.setTypeface(null, Typeface.NORMAL);
			holder.calendrierEquipe1.setTypeface(null, Typeface.NORMAL);
			holder.calendrierScore2.setTypeface(null, Typeface.NORMAL);
			holder.calendrierEquipe2.setTypeface(null, Typeface.NORMAL);
		}
		holder.calendrierEquipe1.setText(line.getEquipe1());
		holder.calendrierScore1.setText(line.getScore1()+"");
		holder.calendrierScore2.setText(line.getScore2()+"");
		holder.calendrierEquipe2.setText(line.getEquipe2());
		
		return convertView;
	}
	
	private class ViewHolder {
		ImageView imageEquipe1;
		TextView calendrierEquipe1;
		TextView calendrierScore1;
		TextView calendrierScore2;
		TextView calendrierEquipe2;
		ImageView imageEquipe2;
	}

}
