package com.hofc.hofc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hofc.hofc.R;
import com.hofc.hofc.constant.AppConstant;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.vo.CalendrierLineVO;

import java.text.SimpleDateFormat;

public class CalendrierAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private final SimpleDateFormat sdf;
	private Context context;
	
	public CalendrierAdapter(Context context) {
        if (context != null) {
            inflater = LayoutInflater.from(context);
            this.context = context;
        }
		sdf = new SimpleDateFormat("EEEE dd MMMM yyyy");
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
			convertView = inflater.inflate(R.layout.calendrier_card, parent, false);
			holder.imageEquipe1 = (ImageView)convertView.findViewById(R.id.calendrier_card_image_1);
			holder.calendrierEquipe1 = (TextView)convertView.findViewById(R.id.calendrier_card_nom_1);
			holder.calendrierScore1 = (TextView)convertView.findViewById(R.id.calendrier_card_score_1);
			holder.calendrierScore2 = (TextView)convertView.findViewById(R.id.calendrier_card_score_2);
			holder.calendrierEquipe2 = (TextView)convertView.findViewById(R.id.calendrier_card_nom_2);
			holder.imageEquipe2 = (ImageView)convertView.findViewById(R.id.calendrier_card_image_2);
			holder.dateMatch = (TextView)convertView.findViewById(R.id.calendrier_card_date);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		CalendrierLineVO line = DataSingleton.getCalendrier().get(position);
		if(AppConstant.hofcName.equalsIgnoreCase(line.getEquipe1())) {
			holder.imageEquipe1.setImageResource(R.drawable.ic_launcher);
			holder.imageEquipe2.setImageResource(android.R.color.transparent);
            this.applyColor(holder, context.getResources().getColor(R.color.hofc_blue), Color.BLACK);
		} else if(AppConstant.hofcName.equalsIgnoreCase(line.getEquipe2())) {
			holder.imageEquipe2.setImageResource(R.drawable.ic_launcher);
			holder.imageEquipe1.setImageResource(android.R.color.transparent);
            this.applyColor(holder, Color.BLACK, context.getResources().getColor(R.color.hofc_blue));
		}

		if(line.getScore1() != null && line.getScore1() > line.getScore2()) {
            this.applyStyle(holder, Typeface.BOLD, Typeface.NORMAL);
		} else if (line.getScore1() != null && line.getScore1() < line.getScore2()) {
            this.applyStyle(holder, Typeface.NORMAL, Typeface.BOLD);
		} else {
            this.applyStyle(holder,Typeface.NORMAL, Typeface.NORMAL);
		}
		holder.calendrierEquipe1.setText(line.getEquipe1());
		holder.calendrierScore1.setText((line.getScore1() == null)?"":line.getScore1()+"");
		holder.calendrierScore2.setText((line.getScore2() == null)?"":line.getScore2()+"");
		holder.calendrierEquipe2.setText(line.getEquipe2());
		if(line.getDate() != null)
			holder.dateMatch.setText(sdf.format(line.getDate()));

		return convertView;
	}

    private void applyStyle(ViewHolder holder, int style1, int style2) {
        holder.calendrierScore1.setTypeface(null, style1);
        holder.calendrierEquipe1.setTypeface(null, style1);
        holder.calendrierScore2.setTypeface(null, style2);
        holder.calendrierEquipe2.setTypeface(null, style2);
    }

    private void applyColor(ViewHolder holder, int color1, int color2) {
        holder.calendrierEquipe1.setTextColor(color1);
        holder.calendrierEquipe2.setTextColor(color2);
        holder.calendrierScore1.setTextColor(color1);
        holder.calendrierScore2.setTextColor(color2);
    }

	private class ViewHolder {
		ImageView imageEquipe1;
		TextView calendrierEquipe1;
		TextView calendrierScore1;
		TextView calendrierScore2;
		TextView calendrierEquipe2;
		ImageView imageEquipe2;
		TextView dateMatch;
	}

}
