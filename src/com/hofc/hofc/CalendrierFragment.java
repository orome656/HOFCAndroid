package com.hofc.hofc;

import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.vo.CalendrierLineVO;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class CalendrierFragment extends Fragment {

	public static CalendrierFragment newInstance() {
		CalendrierFragment fragment = new CalendrierFragment();
		
		return fragment;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendrier, container, false);
        if(DataSingleton.isSynchroCalendrierNeeded()) {
			DataSingleton.launchSynchroCalendrier(new FragmentCallback() {
				
				@Override
				public void onTaskDone() {
					refreshView();
				}
			});
		} else {
			this.refreshView();
		}
        return rootView;
    }
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}
	
	public void refreshView(){
		if(DataSingleton.getCalendrier() != null) {
			TableLayout table = (TableLayout) getActivity().findViewById(R.id.calendrier_layout);
			for(CalendrierLineVO line : DataSingleton.getCalendrier()) {
				TableRow row = new TableRow(getActivity());
				
				TextView equipe1 = new TextView(getActivity());
				equipe1.setText(line.getEquipe1());
				
				TextView score = new TextView(getActivity());
				score.setText(line.getScore1() + "-" + line.getScore2());
				
				TextView equipe2 = new TextView(getActivity());
				equipe2.setText(line.getEquipe2());
				
				row.addView(equipe1);
				row.addView(score);
				row.addView(equipe2);
				
				table.addView(row);
			}
		}
	}
	
	
}
