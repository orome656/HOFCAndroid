package com.hofc.hofc.adapter;

import com.hofc.hofc.R;
import com.hofc.hofc.data.DataSingleton;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CalendrierAdapter extends BaseAdapter {

	LayoutInflater inflater;
	
	public CalendrierAdapter(Context context) {
		inflater = LayoutInflater.from(context);
	}	
	@Override
	public int getCount() {
		return DataSingleton.getCalendrier().size();
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
			
			holder.calendrierEquipe1 = (TextView)convertView.findViewById(R.id.calendrier_equipe_1);
			holder.calendrierScore1 = (TextView)convertView.findViewById(R.id.calendrier_score_1);
			holder.calendrierScore2 = (TextView)convertView.findViewById(R.id.calendrier_score_2);
			holder.calendrierEquipe2 = (TextView)convertView.findViewById(R.id.calendrier_equipe_2);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.calendrierEquipe1.setText(DataSingleton.getCalendrier().get(position).getEquipe1());
		holder.calendrierScore1.setText(DataSingleton.getCalendrier().get(position).getScore1()+"");
		holder.calendrierScore2.setText(DataSingleton.getCalendrier().get(position).getScore2()+"");
		holder.calendrierEquipe2.setText(DataSingleton.getCalendrier().get(position).getEquipe2());
		
		return convertView;
	}
	
	private class ViewHolder {
		TextView calendrierEquipe1;
		TextView calendrierScore1;
		TextView calendrierScore2;
		TextView calendrierEquipe2;
	}

}
