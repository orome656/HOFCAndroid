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
import com.hofc.hofc.adapter.JourneeAdapter;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.data.download.JourneeDownloader;

/**
 * Created by maladota on 03/04/2015.
 * Fragment d'affichage des matchs d'une semaine
 */
public class JourneeWeekFragment extends CommonFragment  implements FragmentCallback, CustomFragment, SwipeRefreshLayout.OnRefreshListener {
    private boolean isLoading = false;
    private ListView journeeListView;
    private SwipeRefreshLayout swipeJournee;
    private String journee;

    public JourneeWeekFragment() {

    }

    public static JourneeWeekFragment newInstance(String journeeId) {
        JourneeWeekFragment journeeWeekFragment = new JourneeWeekFragment();
        Bundle args = new Bundle();
        args.putString("journee", journeeId);

        journeeWeekFragment.setArguments(args);

        return journeeWeekFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_journee_week,
                container, false);
        journeeListView = (ListView)rootView.findViewById(R.id.journee_listView);
        journee = getArguments().getString("journee");

        swipeJournee = (SwipeRefreshLayout)rootView.findViewById(R.id.journee_swipe);
        swipeJournee.setOnRefreshListener(this);
        swipeJournee.setColorSchemeColors(Color.BLACK, getResources().getColor(R.color.hofc_blue));
        if(getUserVisibleHint()) {
            if (DataSingleton.getJournee(journee) == null) {
                this.refreshDataAndView();
            } else {
                this.refreshView();
            }
        }

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getView() != null) {
            if (DataSingleton.getJournee(journee) == null) {
                this.refreshDataAndView();
            } else {
                this.refreshView();
            }
        }
    }

    void refreshView(){
        this.isLoading = true;
        super.refreshView();
        if(journeeListView.getAdapter() == null) {
            JourneeAdapter adapter = new JourneeAdapter(getActivity(), journee, ((HOFCApplication) getActivity().getApplication()).getRequestQueue());
            journeeListView.setAdapter(adapter);
        } else {
            ((JourneeAdapter) journeeListView.getAdapter()).notifyDataSetChanged();
        }

        if(swipeJournee.isRefreshing())
            swipeJournee.setRefreshing(false);

    }

    @Override
    public void refreshDataAndView() {
        this.isLoading = true;

        this.swipeJournee.post(new Runnable() {
            @Override
            public void run() {
                if (isLoading)
                    swipeJournee.setRefreshing(true);
            }
        });

        super.refreshDataAndView();
        RequestQueue requestQueue = ((HOFCApplication) getActivity().getApplication()).getRequestQueue();
        JourneeDownloader.update(requestQueue, this, journee);
    }

    @Override
    public void onError() {
        this.isLoading = false;

        if(swipeJournee.isRefreshing())
            swipeJournee.setRefreshing(false);

        if(this.getUserVisibleHint())
            super.onError();
    }

    @Override
    public void onError(int messageId) {
        this.isLoading = false;

        if(swipeJournee.isRefreshing())
            swipeJournee.setRefreshing(false);
        if(this.getUserVisibleHint())
            super.onError(messageId);
    }

    @Override
    public void onRefresh() {
        this.refreshDataAndView();
    }

}
