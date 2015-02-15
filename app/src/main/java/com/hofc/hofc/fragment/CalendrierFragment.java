package com.hofc.hofc.fragment;

import com.hofc.hofc.R;
import com.hofc.hofc.adapter.CalendrierAdapter;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.data.download.CalendrierDownloader;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class CalendrierFragment extends CommonFragment  implements FragmentCallback, CustomFragment, SwipeRefreshLayout.OnRefreshListener {

	private ListView calendrierListView;
    private SwipeRefreshLayout swipeCalendrier;

	public static CalendrierFragment newInstance() {
		return new CalendrierFragment();
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendrier, container, false);
        calendrierListView = (ListView) rootView.findViewById(R.id.calendrier_listView);

        swipeCalendrier = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_calendrier);
        swipeCalendrier.setOnRefreshListener(this);
        swipeCalendrier.setColorSchemeColors(Color.BLACK, getResources().getColor(R.color.hofc_blue));

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
        if(swipeCalendrier.isRefreshing())
            swipeCalendrier.setRefreshing(false);
    }

	@Override
	public void refreshDataAndView() {
        this.swipeCalendrier.post(new Runnable() {
            @Override
            public void run() {
                swipeCalendrier.setRefreshing(true);
            }
        });
        super.refreshDataAndView();
		CalendrierDownloader downloader = new CalendrierDownloader(this);
        downloader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}


    @Override
    public void onError() {
        if(swipeCalendrier.isRefreshing())
            swipeCalendrier.setRefreshing(false);

        super.onError();
    }

    @Override
    public void onError(int messageId) {
        if(swipeCalendrier.isRefreshing())
            swipeCalendrier.setRefreshing(false);

        super.onError(messageId);
    }

    @Override
    public void onRefresh() {
        this.refreshDataAndView();
    }
}
