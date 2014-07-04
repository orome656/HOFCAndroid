package com.hofc.hofc;

import com.hofc.hofc.data.CalendrierBDD;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.data.download.ActusDownloader;
import com.hofc.hofc.data.download.CalendrierDownloader;
import com.hofc.hofc.vo.ActuVO;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ActusFragment extends Fragment  implements FragmentCallback {

	private LinearLayout actusLinear;
	
	public static ActusFragment newInstance() {
		ActusFragment fragment = new ActusFragment();
		return fragment;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_accueil, container, false);
        actusLinear = (LinearLayout) rootView.findViewById(R.id.actus_linear);
		//CalendrierBDD.initiate(getActivity());
        if(DataSingleton.isSynchroCalendrierNeeded()) {
        	ActusDownloader downloader = new ActusDownloader(this);
        	downloader.execute();
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
		for (ActuVO actu : DataSingleton.getActus()) {
			TextView t = new TextView(this.getActivity());
			t.setText(actu.getTitre());
			actusLinear.addView(t);
		}
	}

	@Override
	public void onTaskDone() {
		refreshView();
		
	}

	@Override
	public void onError() {
		Toast.makeText(getActivity(), "Merci de vérifier votre connexion",  Toast.LENGTH_SHORT).show();
	}
	
	
}
