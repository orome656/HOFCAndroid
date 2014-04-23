package com.hofc.hofc;

import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.vo.ClassementLineVO;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ClassementFragment extends Fragment {

	public static ClassementFragment newInstance() {
		ClassementFragment fragment = new ClassementFragment();
		return fragment;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_classement, container, false);
		if(DataSingleton.isSynchroClassementNeeded()) {
			DataSingleton.launchSynchroClassement(new FragmentCallback() {
				
				@Override
				public void onTaskDone() {
					refreshView();
				}

				@Override
				public void onError() {
					Toast.makeText(getActivity(), "Merci de vérifier votre connexion",  Toast.LENGTH_SHORT).show();
					
				}
			});
		}
        return rootView;
    }
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}
	

	public void refreshView(){
		if(DataSingleton.getClassement() != null) {
			TableLayout table = (TableLayout) getActivity().findViewById(R.id.classement_layout);
			int i = 1;
			for(ClassementLineVO line : DataSingleton.getClassement()) {
				TableRow row = new TableRow(getActivity());
				
				TextView place = new TextView(getActivity());
				place.setText(i + "");
				
				TextView nom = new TextView(getActivity());
				nom.setText(line.getNom());
				
				TextView points = new TextView(getActivity());
				points.setText(line.getPoints() + "");
				// TODO
				
				row.addView(place);
				row.addView(nom);
				row.addView(points);
				
				table.addView(row);
				i++;
			}
		}
	}
	
}
