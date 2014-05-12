package com.hofc.hofc.adapter;

import com.hofc.hofc.R;
import com.hofc.hofc.constant.AppConstant;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.vo.ClassementLineVO;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ClassementAdapter extends BaseAdapter {

	LayoutInflater inflater;
	
	public ClassementAdapter(Context context) {
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return DataSingleton.getClassement().size();
	}

	@Override
	public Object getItem(int position) {
		return DataSingleton.getClassement().get(position);
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
			convertView = inflater.inflate(R.layout.item_classement, null);
			holder.classementPosition = (TextView) convertView.findViewById(R.id.classement_position);
			holder.classementNom = (TextView) convertView.findViewById(R.id.classement_nom);
			holder.classementPoints = (TextView) convertView.findViewById(R.id.classement_points);
			holder.classementJoue = (TextView) convertView.findViewById(R.id.classement_joue);
			holder.classementDiff = (TextView) convertView.findViewById(R.id.classement_diff);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		ClassementLineVO line = DataSingleton.getClassement().get(position);
		
		if(AppConstant.hofcName.equalsIgnoreCase(line.getNom())) {
			this.setLineColor(holder, Color.BLUE);
		} else {
			this.setLineColor(holder, Color.BLACK);
		}
		
		holder.classementPosition.setText(position + 1 +"");
		holder.classementNom.setText(line.getNom());
		holder.classementPoints.setText(line.getPoints() +"");
		holder.classementJoue.setText(line.getJoue() +"");
		holder.classementDiff.setText(line.getDiff() +"");
		
		return convertView;
	}
	
	private class ViewHolder {
		TextView classementPosition;
		TextView classementNom;
		TextView classementPoints;
		TextView classementJoue;
		TextView classementDiff;
	}
	/**
	 * 
	 * @param color
	 */
	private void setLineColor(ViewHolder holder, int color) {
		holder.classementPosition.setTextColor(color);
		holder.classementNom.setTextColor(color);
		holder.classementPoints.setTextColor(color);
		holder.classementJoue.setTextColor(color);
		holder.classementDiff.setTextColor(color);
		
	}

}
