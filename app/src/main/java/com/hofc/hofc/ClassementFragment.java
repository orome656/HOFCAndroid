package com.hofc.hofc;

import com.hofc.hofc.adapter.ClassementAdapter;
import com.hofc.hofc.data.ClassementBDD;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.data.download.ClassementDownloader;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class ClassementFragment extends Fragment implements FragmentCallback, CustomFragment {

	private ListView classementListView;
	
	public static ClassementFragment newInstance() {
		return new ClassementFragment();
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_classement, container, false);
        classementListView = (ListView) rootView.findViewById(R.id.classement_listView);
        ClassementBDD.initiate(getActivity());
		if(DataSingleton.isSynchroClassementNeeded()) {
			this.refreshDataAndView();
		} else {
			this.refreshView();
		}
        return rootView;
    }
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	

	public void refreshView(){
		ClassementAdapter adapter = new ClassementAdapter(getActivity());
		classementListView.setAdapter(adapter);
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
		ClassementDownloader downloader = new ClassementDownloader(this);
		downloader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        //downloader.execute();
	}
	
}
