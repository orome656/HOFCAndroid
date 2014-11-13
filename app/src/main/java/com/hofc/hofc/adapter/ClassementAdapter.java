package com.hofc.hofc.adapter;

import com.hofc.hofc.R;
import com.hofc.hofc.constant.AppConstant;
import com.hofc.hofc.data.ClassementBDD;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.vo.ClassementLineVO;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ClassementAdapter extends BaseAdapter {

	LayoutInflater inflater;
	
	public ClassementAdapter(Context context) {
        if(context != null)
		    inflater = LayoutInflater.from(context);
		//ClassementBDD.initiate(context);
	}
	
	@Override
	public int getCount() {
		if(DataSingleton.getClassement() != null) {
			return DataSingleton.getClassement().size();
		} else {
			return 0;
		}
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
			convertView = inflater.inflate(R.layout.classement_element, null);
			holder.classementPosition = (TextView) convertView.findViewById(R.id.classement_view_rank);
			holder.classementNom = (TextView) convertView.findViewById(R.id.classement_view_name);
            holder.classementPoints = (TextView) convertView.findViewById(R.id.classement_view_points_value);
            holder.classementJoue = (TextView) convertView.findViewById(R.id.classement_view_joue_value);
            holder.classementDiff = (TextView) convertView.findViewById(R.id.classement_view_diff_value);
            holder.classementVictoire = (TextView) convertView.findViewById(R.id.classement_view_victoire_value);
            holder.classementNul = (TextView) convertView.findViewById(R.id.classement_view_nul_value);
            holder.classementDefaite = (TextView) convertView.findViewById(R.id.classement_view_defaite_value);
            holder.classementBp = (TextView) convertView.findViewById(R.id.classement_view_bp_value);
            holder.classementBc = (TextView) convertView.findViewById(R.id.classement_view_bc_value);
			holder.imageEquipe = (ImageView) convertView.findViewById(R.id.classement_view_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		ClassementLineVO line = DataSingleton.getClassement().get(position);
		
		if(AppConstant.hofcName.equalsIgnoreCase(line.getNom())) {
            holder.imageEquipe.setImageResource(R.drawable.ic_launcher);
			//this.setLineColor(holder, Color.BLUE);
		} else {
            holder.imageEquipe.setImageBitmap(null);
			//this.setLineColor(holder, Color.BLACK);
		}
		
		holder.classementPosition.setText(position + 1 +"");
		holder.classementNom.setText(line.getNom());
		holder.classementPoints.setText(line.getPoints() +"");
		holder.classementJoue.setText(line.getJoue() +"");
		holder.classementDiff.setText(line.getDiff() +"");
        holder.classementVictoire.setText(line.getGagne() +"");
        holder.classementNul.setText(line.getNul() +"");
        holder.classementDefaite.setText(line.getPerdu() +"");
        holder.classementBp.setText(line.getBp() +"");
        holder.classementBc.setText(line.getBc() +"");
		
		return convertView;
	}
	
	private class ViewHolder {
		TextView classementPosition;
		TextView classementNom;
		TextView classementPoints;
		TextView classementJoue;
		TextView classementDiff;
        TextView classementVictoire;
        TextView classementNul;
        TextView classementDefaite;
        TextView classementBp;
        TextView classementBc;
        ImageView imageEquipe;
	}
	/**
	 * Permet d'application une couleur sur la ligne du classement
	 * @param color Couleur a insérer sur la ligne
	 */
	private void setLineColor(ViewHolder holder, int color) {
		holder.classementPosition.setTextColor(color);
		holder.classementNom.setTextColor(color);
		holder.classementPoints.setTextColor(color);
		holder.classementJoue.setTextColor(color);
		holder.classementDiff.setTextColor(color);
		
	}

}
