package com.hofc.hofc.adapter;

import com.hofc.hofc.R;
import com.hofc.hofc.constant.AppConstant;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.vo.ClassementLineVO;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ClassementAdapter extends BaseAdapter {

	LayoutInflater inflater;
    Context context;
	
	public ClassementAdapter(Context context) {
        if(context != null) {
            inflater = LayoutInflater.from(context);
            this.context = context;
        }
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
			convertView = inflater.inflate(R.layout.item_classement, null);

            holder.classementPosition = (TextView) convertView.findViewById(R.id.classement_position);
            holder.classementNom = (TextView) convertView.findViewById(R.id.classement_nom);
            holder.classementPoints = (TextView) convertView.findViewById(R.id.classement_points);
            holder.classementJoue = (TextView) convertView.findViewById(R.id.classement_joue);
            holder.classementDiff = (TextView) convertView.findViewById(R.id.classement_diff);
            holder.classementVictoire = (TextView) convertView.findViewById(R.id.classement_victoire);
            holder.classementNul = (TextView) convertView.findViewById(R.id.classement_nul);
            holder.classementDefaite = (TextView) convertView.findViewById(R.id.classement_defaite);
            holder.classementBp = (TextView) convertView.findViewById(R.id.classement_bp);
            holder.classementBc = (TextView) convertView.findViewById(R.id.classement_bc);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		ClassementLineVO line = DataSingleton.getClassement().get(position);
		
		if(AppConstant.hofcName.equalsIgnoreCase(line.getNom())) {
			this.setLineColor(holder, context.getResources().getColor(R.color.hofc_blue));
		} else {
			this.setLineColor(holder, Color.BLACK);
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
		this.setBoldStyle(holder);


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
        holder.classementVictoire.setTextColor(color);
		holder.classementNul.setTextColor(color);
        holder.classementDefaite.setTextColor(color);
        holder.classementBp.setTextColor(color);
        holder.classementBc.setTextColor(color);
        holder.classementDiff.setTextColor(color);
		
	}

    private void setBoldStyle(ViewHolder holder) {
        holder.classementPoints.setTypeface(null, Typeface.BOLD);
        holder.classementJoue.setTypeface(null, Typeface.BOLD);
        holder.classementVictoire.setTypeface(null, Typeface.BOLD);
        holder.classementNul.setTypeface(null, Typeface.BOLD);
        holder.classementDefaite.setTypeface(null, Typeface.BOLD);
        holder.classementBp.setTypeface(null, Typeface.BOLD);
        holder.classementBc.setTypeface(null, Typeface.BOLD);
        holder.classementDiff.setTypeface(null, Typeface.BOLD);
    }

}
