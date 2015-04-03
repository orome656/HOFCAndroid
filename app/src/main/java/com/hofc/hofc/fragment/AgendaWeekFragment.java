package com.hofc.hofc.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.hofc.hofc.HOFCApplication;
import com.hofc.hofc.R;
import com.hofc.hofc.adapter.AgendaAdapter;
import com.hofc.hofc.data.download.AgendaDownloader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by maladota on 03/04/2015.
 */
public class AgendaWeekFragment extends CommonFragment  implements FragmentCallback, CustomFragment {
    private String date;
    private boolean isLoading = false;
    private ListView agendaListView;
    private String callArgument;

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private static SimpleDateFormat fffFormat = new SimpleDateFormat("yyyy-MM-dd");
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
        callArgument = getArguments().getString("date");

        this.refreshDataAndView();

        return rootView;
    }

    public void refreshView(){
        this.isLoading = true;
        super.refreshView();
        if(agendaListView.getAdapter() == null) {
            AgendaAdapter adapter = new AgendaAdapter(getActivity());
            agendaListView.setAdapter(adapter);
        } else {
            ((AgendaAdapter)agendaListView.getAdapter()).notifyDataSetChanged();
        }
        /*
        if(swipeActus.isRefreshing())
            swipeActus.setRefreshing(false);
            */
    }

    @Override
    public void refreshDataAndView() {
        this.isLoading = true;
        /*
        this.swipeActus.post(new Runnable() {
            @Override
            public void run() {
                if(isLoading)
                    swipeActus.setRefreshing(true);
            }
        });
        */
        super.refreshDataAndView();
        RequestQueue requestQueue = ((HOFCApplication) getActivity().getApplication()).getRequestQueue();
        AgendaDownloader.update(requestQueue, this, callArgument);
    }

    @Override
    public void onError() {
        this.isLoading = false;
        /*
        if(swipeActus.isRefreshing())
            swipeActus.setRefreshing(false);
*/
        super.onError();
    }

    @Override
    public void onError(int messageId) {
        this.isLoading = false;
        /*
        if(swipeActus.isRefreshing())
            swipeActus.setRefreshing(false);*/

        super.onError(messageId);
    }
    /*
    @Override
    public void onRefresh() {
        this.refreshDataAndView();
    }
    */
}
