package com.hofc.hofc.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
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
import com.hofc.hofc.utils.HOFCUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by maladota on 03/04/2015.
 * Fragment d'affichage des matchs d'une semaine
 */
public class AgendaWeekFragment extends CommonFragment  implements FragmentCallback, CustomFragment, SwipeRefreshLayout.OnRefreshListener {
    private boolean isLoading = false;
    private ListView agendaListView;
    private String callArgument;
    private SwipeRefreshLayout swipeAgenda;
    private String semaine;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
    private static final SimpleDateFormat fffFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
    public AgendaWeekFragment() {

    }

    public static AgendaWeekFragment newInstance(String date) {
        try {
            AgendaWeekFragment agendaWeekFragment = new AgendaWeekFragment();
            Date dateObject = sdf.parse(date);
            Bundle args = new Bundle();
            args.putString("date", fffFormat.format(dateObject));

            agendaWeekFragment.setArguments(args);

            return agendaWeekFragment;
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_agenda_week,
                container, false);
        agendaListView = (ListView)rootView.findViewById(R.id.agenda_listView);
        semaine = getArguments().getString("date");
        try {
            Date dateObject = fffFormat.parse(getArguments().getString("date"));
            if(!HOFCUtils.isDateInCurrentWeek(dateObject)) {
                callArgument = getArguments().getString("date");
            } else {
                callArgument = null;
            }
        } catch (ParseException e) {
            callArgument = getArguments().getString("date");
            Log.e(AgendaWeekFragment.class.getName(), "Erreur lors de la deserialisation de la date", e);
        }

        swipeAgenda = (SwipeRefreshLayout)rootView.findViewById(R.id.agenda_swipe);
        swipeAgenda.setOnRefreshListener(this);
        swipeAgenda.setColorSchemeColors(Color.BLACK, getResources().getColor(R.color.hofc_blue));
        if(getUserVisibleHint()) {
            if (DataSingleton.getAgenda(semaine) == null) {
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
            if (DataSingleton.getAgenda(semaine) == null) {
                this.refreshDataAndView();
            } else {
                this.refreshView();
            }
        }
    }

    public void refreshView(){
        this.isLoading = true;
        super.refreshView();
        if(agendaListView.getAdapter() == null) {
            AgendaAdapter adapter = new AgendaAdapter(getActivity(), semaine);
            agendaListView.setAdapter(adapter);
        } else {
            ((AgendaAdapter)agendaListView.getAdapter()).notifyDataSetChanged();
        }

        if(swipeAgenda.isRefreshing())
            swipeAgenda.setRefreshing(false);

    }

    @Override
    public void refreshDataAndView() {
        this.isLoading = true;

        this.swipeAgenda.post(new Runnable() {
            @Override
            public void run() {
                if (isLoading)
                    swipeAgenda.setRefreshing(true);
            }
        });

        super.refreshDataAndView();
        RequestQueue requestQueue = ((HOFCApplication) getActivity().getApplication()).getRequestQueue();
        AgendaDownloader.update(requestQueue, this, callArgument);
    }

    @Override
    public void onError() {
        this.isLoading = false;

        if(swipeAgenda.isRefreshing())
            swipeAgenda.setRefreshing(false);

        super.onError();
    }

    @Override
    public void onError(int messageId) {
        this.isLoading = false;

        if(swipeAgenda.isRefreshing())
            swipeAgenda.setRefreshing(false);

        super.onError(messageId);
    }

    @Override
    public void onRefresh() {
        this.refreshDataAndView();
    }

}