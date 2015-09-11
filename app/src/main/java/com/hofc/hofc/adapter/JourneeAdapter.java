package com.hofc.hofc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hofc.hofc.R;
import com.hofc.hofc.constant.AppConstant;
import com.hofc.hofc.constant.ServerConstant;
import com.hofc.hofc.data.LocalDataSingleton;
import com.hofc.hofc.utils.HOFCUtils;
import com.hofc.hofc.vo.MatchVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

public class JourneeAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private final SimpleDateFormat sdf;
	private Context context;
	private final String journeeId;
	private final String equipe;
	private RequestQueue requestQueue;

	public JourneeAdapter(Context context, String journeeId, String equipe, RequestQueue requestQueue) {
        if (context != null) {
            this.inflater = LayoutInflater.from(context);
            this.context = context;
			this.requestQueue = requestQueue;
        }
		this.journeeId = journeeId;
		this.equipe = equipe;
		sdf = new SimpleDateFormat("EEEE dd MMMM yyyy HH:mm");
	}	
	@Override
	public int getCount() {
		if(LocalDataSingleton.getJournee(journeeId, equipe) != null) {
			return LocalDataSingleton.getJournee(journeeId, equipe).size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return LocalDataSingleton.getJournee(journeeId, equipe).get(position);
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
			convertView = inflater.inflate(R.layout.journee_card, parent, false);
			holder.imageEquipe1 = (ImageView)convertView.findViewById(R.id.journee_card_image_1);
			holder.journeeEquipe1 = (TextView)convertView.findViewById(R.id.journee_card_nom_1);
			holder.journeeScore1 = (TextView)convertView.findViewById(R.id.journee_card_score_1);
			holder.journeeScore2 = (TextView)convertView.findViewById(R.id.journee_card_score_2);
			holder.journeeEquipe2 = (TextView)convertView.findViewById(R.id.journee_card_nom_2);
			holder.imageEquipe2 = (ImageView)convertView.findViewById(R.id.journee_card_image_2);
			holder.dateMatch = (TextView)convertView.findViewById(R.id.journee_card_date);
			holder.infoButton = (ImageButton)convertView.findViewById(R.id.journee_info_button);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final MatchVO line = LocalDataSingleton.getJournee(journeeId, equipe).get(position);
		if(line.getEquipe1() != null && line.getEquipe1().contains(AppConstant.hofcName)) {
			holder.imageEquipe1.setImageResource(R.drawable.ic_launcher);
			holder.imageEquipe2.setImageResource(android.R.color.transparent);
            this.applyColor(holder, context.getResources().getColor(R.color.hofc_blue), Color.BLACK);
		} else if(line.getEquipe2() != null && line.getEquipe2().contains(AppConstant.hofcName)) {
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
		holder.journeeEquipe1.setText(line.getEquipe1());
		holder.journeeScore1.setText((line.getScore1() == null)?"":line.getScore1()+"");
		holder.journeeScore2.setText((line.getScore2() == null)?"":line.getScore2()+"");
		holder.journeeEquipe2.setText(line.getEquipe2());
		if(line.getDate() != null)
			holder.dateMatch.setText(sdf.format(line.getDate()));
		holder.infoButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final MaterialDialog progress = new MaterialDialog.Builder(context)
						.progress(true, 0)
						.content(R.string.loading_popup)
						.theme(Theme.LIGHT)
						.show();

				String url = HOFCUtils.buildUrl(ServerConstant.MATCH_CONTEXT, new String[]{line.getIdInfos()});

				JsonObjectRequest jsonRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(final JSONObject response) {
						progress.dismiss();
						MaterialDialog dialog = new MaterialDialog.Builder(context)
								.customView(R.layout.dialog_details_match, true)
								.positiveText(R.string.close_popup_button)
								.theme(Theme.LIGHT)
								.build();
						try {
							((TextView)dialog.findViewById(R.id.dialog_lieu_nom)).setText(response.getString("nom"));
							((TextView)dialog.findViewById(R.id.dialog_lieu_adresse)).setText(response.getString("adresse"));
							((TextView)dialog.findViewById(R.id.dialog_lieu_ville)).setText(response.getString("ville"));
							LinearLayout layout = (LinearLayout)dialog.findViewById(R.id.dialog_layout_arbitre);
							JSONArray arbitres = response.getJSONArray("arbitres");
							if(arbitres.length() == 0) {
								(dialog.findViewById(R.id.dialog_arbitre_titre)).setVisibility(View.GONE);
							} else {
								for(int i = 0; i<arbitres.length(); i++) {
									TextView tv = new TextView(context);
									tv.setText((String) arbitres.get(i));
									LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
									params.setMargins(0,4,0,0);
									tv.setLayoutParams(params);
									tv.setTextColor(context.getResources().getColor(R.color.dialog_content_text));
									tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
									layout.addView(tv);
								}
							}
						} catch (JSONException e) {
							Log.e(JourneeAdapter.class.getName(), "Deserialization error", e);
						}
						dialog.show();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
					}
				});
				requestQueue.add(jsonRequest);

			}
		});
		return convertView;
	}

    private void applyStyle(ViewHolder holder, int style1, int style2) {
        holder.journeeScore1.setTypeface(null, style1);
        holder.journeeEquipe1.setTypeface(null, style1);
        holder.journeeScore2.setTypeface(null, style2);
        holder.journeeEquipe2.setTypeface(null, style2);
    }

    private void applyColor(ViewHolder holder, int color1, int color2) {
        holder.journeeEquipe1.setTextColor(color1);
        holder.journeeEquipe2.setTextColor(color2);
        holder.journeeScore1.setTextColor(color1);
        holder.journeeScore2.setTextColor(color2);
    }

	private class ViewHolder {
		ImageView imageEquipe1;
		TextView journeeEquipe1;
		TextView journeeScore1;
		TextView journeeScore2;
		TextView journeeEquipe2;
		ImageView imageEquipe2;
		TextView dateMatch;
		ImageButton infoButton;
	}

}
