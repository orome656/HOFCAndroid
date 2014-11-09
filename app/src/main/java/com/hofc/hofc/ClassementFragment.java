package com.hofc.hofc;

import com.hofc.hofc.adapter.ClassementAdapter;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.data.download.ClassementDownloader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.concurrent.Callable;

public class ClassementFragment extends CommonFragment implements FragmentCallback, CustomFragment {

	private ListView classementListView;
	
	public static ClassementFragment newInstance() {
		return new ClassementFragment();
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_classement, container, false);
        classementListView = (ListView) rootView.findViewById(R.id.classement_listView);
		if(DataSingleton.isSynchroClassementNeeded()) {
			this.refreshDataAndView();
		} else {
			this.refreshView();
		}
        return rootView;
    }

	public void refreshView(){
		ClassementAdapter adapter = new ClassementAdapter(getActivity());
		classementListView.setAdapter(adapter);
	}

	@Override
	public void refreshDataAndView() {
		ClassementDownloader downloader = new ClassementDownloader(this);
		downloader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
	
}
