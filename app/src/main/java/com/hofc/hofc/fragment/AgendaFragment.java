package com.hofc.hofc.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.hofc.hofc.HOFCApplication;
import com.hofc.hofc.R;
import com.hofc.hofc.adapter.AgendaAdapter;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.data.download.AgendaDownloader;

public class AgendaFragment extends CommonFragment  implements FragmentCallback, CustomFragment, SwipeRefreshLayout.OnRefreshListener {

	private ListView agendaListView;
    private SwipeRefreshLayout swipeagenda;

	public static AgendaFragment newInstance() {
		return new AgendaFragment();
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_agenda, container, false);
        agendaListView = (ListView) rootView.findViewById(R.id.agenda_listView);

        swipeagenda = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_agenda);
        swipeagenda.setOnRefreshListener(this);
        swipeagenda.setColorSchemeColors(Color.BLACK, getResources().getColor(R.color.hofc_blue));

        this.refreshDataAndView();
        return rootView;
    }

    public void refreshView() {
        super.refreshView();
        if(agendaListView.getAdapter() == null) {
            AgendaAdapter adapter = new AgendaAdapter(getActivity());
            agendaListView.setAdapter(adapter);
        } else {
            ((AgendaAdapter)agendaListView.getAdapter()).notifyDataSetChanged();
        }
        if(swipeagenda.isRefreshing())
            swipeagenda.setRefreshing(false);
    }

	@Override
	public void refreshDataAndView() {
        this.swipeagenda.post(new Runnable() {
            @Override
            public void run() {
                swipeagenda.setRefreshing(true);
            }
        });
        super.refreshDataAndView();
        RequestQueue requestQueue = ((HOFCApplication) getActivity().getApplication()).getRequestQueue();
        AgendaDownloader.update(requestQueue, this);
	}


    @Override
    public void onError() {
        if(swipeagenda.isRefreshing())
            swipeagenda.setRefreshing(false);

        super.onError();
    }

    @Override
    public void onError(int messageId) {
        if(swipeagenda.isRefreshing())
            swipeagenda.setRefreshing(false);

        super.onError(messageId);
    }

    @Override
    public void onRefresh() {
        this.refreshDataAndView();
    }
}
