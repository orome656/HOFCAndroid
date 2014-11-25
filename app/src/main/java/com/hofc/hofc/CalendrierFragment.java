package com.hofc.hofc;

import com.hofc.hofc.adapter.CalendrierAdapter;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.data.download.CalendrierDownloader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class CalendrierFragment extends CommonFragment  implements FragmentCallback, CustomFragment {

	private ListView calendrierListView;
	
	public static CalendrierFragment newInstance() {
		return new CalendrierFragment();
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendrier, container, false);
        calendrierListView = (ListView) rootView.findViewById(R.id.calendrier_listView);
        if(DataSingleton.isSynchroCalendrierNeeded()) {
        	this.refreshDataAndView();
		} else {
			refreshView();
		}
        return rootView;
    }

    public void refreshView() {
        super.refreshView();
        CalendrierAdapter adapter = new CalendrierAdapter(getActivity());
        calendrierListView.setAdapter(adapter);
    }

	@Override
	public void refreshDataAndView() {
        super.refreshDataAndView();
		CalendrierDownloader downloader = new CalendrierDownloader(this);
        downloader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
	
	
}
