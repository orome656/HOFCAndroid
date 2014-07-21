package com.hofc.hofc;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hofc.hofc.adapter.ActusAdapter;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.data.download.ActusDownloader;

public class ActusFragment extends Fragment  implements FragmentCallback, CustomFragment {

	private ListView actusListView;
	
	public static ActusFragment newInstance() {
		ActusFragment fragment = new ActusFragment();
		return fragment;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_accueil, container, false);
        actusListView = (ListView) rootView.findViewById(R.id.actus_listview);
        actusListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				String url = ((TextView)v.findViewById(R.id.actus_url)).getText().toString();
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				v.getContext().startActivity(i);
			}
		});
		//CalendrierBDD.initiate(getActivity());
        if(DataSingleton.isSynchroActuNeeded()) {
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
		ActusAdapter adapter = new ActusAdapter(getActivity());
		actusListView.setAdapter(adapter);
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
		ActusDownloader downloader = new ActusDownloader(this);
    	downloader.execute();
	}
	
	
}
