package com.hofc.hofc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hofc.hofc.R;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.vo.ActuVO;

public class ActusAdapter extends BaseAdapter {

	LayoutInflater inflater;
	
	public ActusAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		//CalendrierBDD.initiate(context);
	}	
	@Override
	public int getCount() {
		if(DataSingleton.getActus() != null) {
			return DataSingleton.getActus().size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return DataSingleton.getActus().get(position);
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
			convertView = inflater.inflate(R.layout.actus_view, null);
			holder.imageActu = (ImageView)convertView.findViewById(R.id.actus_view_image);
			holder.titleText = (TextView)convertView.findViewById(R.id.actus_view_title);
			holder.texte = (TextView)convertView.findViewById(R.id.actus_view_texte);
			holder.dateView = (TextView)convertView.findViewById(R.id.actus_view_date);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		ActuVO line = DataSingleton.getActus().get(position);
		//holder.imageActu.setText(line.getEquipe1());
		holder.titleText.setText(line.getTitre());
		holder.texte.setText(line.getTexte());
		holder.dateView.setText(line.getDate().toString());
		
		return convertView;
	}
	
	private class ViewHolder {
		ImageView imageActu;
		TextView titleText;
		TextView texte;
		TextView dateView;
	}

}
