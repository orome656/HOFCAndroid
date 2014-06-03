package com.hofc.hofc;

import com.hofc.hofc.adapter.CalendrierAdapter;
import com.hofc.hofc.data.CalendrierBDD;
import com.hofc.hofc.data.DataSingleton;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class CalendrierFragment extends Fragment {

	CalendrierBDD bdd;
	
	public static CalendrierFragment newInstance() {
		CalendrierFragment fragment = new CalendrierFragment();
		return fragment;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendrier, container, false);
		if(bdd == null)
			bdd = new CalendrierBDD(getActivity());
		
        if(DataSingleton.isSynchroCalendrierNeeded()) {
			DataSingleton.launchSynchroCalendrier(new FragmentCallback() {
				@Override
				public void onTaskDone() {refreshView();}
				@Override
				public void onError() {Toast.makeText(getActivity(), "Merci de vérifier votre connexion",  Toast.LENGTH_SHORT).show();}
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
		ListView lv = (ListView) getActivity().findViewById(R.id.calendrier_listView);
		CalendrierAdapter adapter = new CalendrierAdapter(getActivity());
		lv.setAdapter(adapter);
	}
	
	
}
