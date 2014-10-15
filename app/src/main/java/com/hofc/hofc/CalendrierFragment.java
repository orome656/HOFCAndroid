package com.hofc.hofc;

import com.hofc.hofc.adapter.CalendrierAdapter;
import com.hofc.hofc.data.CalendrierBDD;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.data.download.CalendrierDownloader;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class CalendrierFragment extends Fragment  implements FragmentCallback, CustomFragment {

	private ListView calendrierListView;
	
	public static CalendrierFragment newInstance() {
		return new CalendrierFragment();
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendrier, container, false);
        calendrierListView = (ListView) rootView.findViewById(R.id.calendrier_listView);
		CalendrierBDD.initiate(getActivity());
        if(DataSingleton.isSynchroCalendrierNeeded()) {
        	this.refreshDataAndView();
		} else {
			refreshView();
		}
        return rootView;
    }
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	public void refreshView(){
		CalendrierAdapter adapter = new CalendrierAdapter(getActivity());
		calendrierListView.setAdapter(adapter);
	}

	@Override
	public void onTaskDone() {
		refreshView();
		
	}

	@Override
	public void onError() {
		Toast.makeText(getActivity(), "Merci de vérifier votre connexion",  Toast.LENGTH_SHORT).show();
	}

	@Override
	public void refreshDataAndView() {
		CalendrierDownloader downloader = new CalendrierDownloader(this);
    	downloader.execute();
	}
	
	
}
