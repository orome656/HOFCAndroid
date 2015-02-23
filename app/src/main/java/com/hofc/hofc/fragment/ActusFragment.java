package com.hofc.hofc.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.hofc.hofc.ActusDetail;
import com.hofc.hofc.ActusDiaporama;
import com.hofc.hofc.HOFCApplication;
import com.hofc.hofc.R;
import com.hofc.hofc.adapter.ActusAdapter;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.data.download.ActusDownloader;

public class ActusFragment extends CommonFragment  implements FragmentCallback, CustomFragment, SwipeRefreshLayout.OnRefreshListener {

	private ListView actusListView;
	private SwipeRefreshLayout swipeActus;
    private boolean isLoading;
	public static ActusFragment newInstance() {
		return new ActusFragment();
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if(DataSingleton.getActus() == null)
            DataSingleton.initializeActus(getActivity());
        View rootView = inflater.inflate(R.layout.fragment_accueil, container, false);
        actusListView = (ListView) rootView.findViewById(R.id.actus_listview);
        actusListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				String url = ((TextView)v.findViewById(R.id.actus_url)).getText().toString();
                Intent i;
                if(url.contains("en-images")) {
                    i = new Intent(getActivity(), ActusDiaporama.class);
                } else {
                    i = new Intent(getActivity(), ActusDetail.class);
                }
                i.putExtra("URL", url);
                startActivity(i);
			}
		});
        swipeActus = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_accueil);
        swipeActus.setOnRefreshListener(this);
        swipeActus.setColorSchemeColors(Color.BLACK, getResources().getColor(R.color.hofc_blue));

        if(DataSingleton.isSynchroActuNeeded()) {
        	this.refreshDataAndView();
		} else {
			refreshView();
		}
        return rootView;
    }

	public void refreshView(){
        this.isLoading = true;
        super.refreshView();
        if(actusListView.getAdapter() == null) {
            ActusAdapter adapter = new ActusAdapter(getActivity());
            actusListView.setAdapter(adapter);
        } else {
            ((ActusAdapter)actusListView.getAdapter()).notifyDataSetChanged();
        }
        if(swipeActus.isRefreshing())
            swipeActus.setRefreshing(false);
	}

	@Override
	public void refreshDataAndView() {
        this.isLoading = true;
        this.swipeActus.post(new Runnable() {
            @Override
            public void run() {
                if(isLoading == true)
                    swipeActus.setRefreshing(true);
            }
        });
        super.refreshDataAndView();
        RequestQueue requestQueue = ((HOFCApplication) getActivity().getApplication()).getRequestQueue();
		ActusDownloader.updateActus(requestQueue, this);
	}

    @Override
    public void onError() {
        this.isLoading = false;
        if(swipeActus.isRefreshing())
            swipeActus.setRefreshing(false);

        super.onError();
    }

    @Override
    public void onError(int messageId) {
        this.isLoading = false;
        if(swipeActus.isRefreshing())
            swipeActus.setRefreshing(false);

        super.onError(messageId);
    }

    @Override
    public void onRefresh() {
        this.refreshDataAndView();
    }
}
