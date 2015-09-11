package com.hofc.hofc.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.hofc.hofc.HOFCApplication;
import com.hofc.hofc.R;
import com.hofc.hofc.adapter.ClassementAdapter;
import com.hofc.hofc.constant.ServerConstant;
import com.hofc.hofc.data.ClassementBDD;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.data.HashMapDataSingleton;
import com.hofc.hofc.data.download.DataDownloader;
import com.hofc.hofc.vo.ClassementLineVO;

public class ClassementFragment extends CommonFragment implements FragmentCallback, CustomFragment, SwipeRefreshLayout.OnRefreshListener {

	private ListView classementListView;
	private SwipeRefreshLayout swipeClassement;
    private int equipeNumber;

	public static ClassementFragment newInstance(int equipeNumber) {
        ClassementFragment classementFragment = new ClassementFragment();
        Bundle args = new Bundle();
        args.putInt("equipeNumber", equipeNumber);

        classementFragment.setArguments(args);

        return classementFragment;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_classement, container, false);
        classementListView = (ListView) rootView.findViewById(R.id.classement_listView);

        equipeNumber = getArguments().getInt("equipeNumber");

        View header = inflater.inflate(R.layout.item_classement, null);
        this.populateHeader(header);
        classementListView.addHeaderView(header);

        swipeClassement = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_classement);
        swipeClassement.setOnRefreshListener(this);
        swipeClassement.setColorSchemeColors(Color.BLACK, getResources().getColor(R.color.hofc_blue));

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(HashMapDataSingleton.getInstance(ClassementLineVO.class, ClassementBDD.class).isSynchroNeeded()
                || HashMapDataSingleton.getInstance(ClassementLineVO.class, ClassementBDD.class).get("equipe"+equipeNumber) == null) {
            this.refreshDataAndView();
        } else {
            this.refreshView();
        }
    }

    void refreshView(){
        super.refreshView();
        if(classementListView.getAdapter() == null) {
            ClassementAdapter adapter = new ClassementAdapter(getActivity(), "equipe"+equipeNumber);
            classementListView.setAdapter(adapter);
        } else {
            ((ClassementAdapter)((HeaderViewListAdapter)classementListView.getAdapter()).getWrappedAdapter()).notifyDataSetChanged();
        }
        if(swipeClassement.isRefreshing())
            swipeClassement.setRefreshing(false);
	}

	@Override
	public void refreshDataAndView() {
        this.swipeClassement.post(new Runnable() {
            @Override
            public void run() {
                swipeClassement.setRefreshing(true);
            }
        });
        super.refreshDataAndView();
        RequestQueue requestQueue = ((HOFCApplication) getActivity().getApplication()).getRequestQueue();
        DataDownloader.downloadWithKey(requestQueue, ServerConstant.CLASSEMENT_CONTEXT[equipeNumber - 1], null, this, ClassementLineVO.class, ClassementBDD.class, "equipe"+equipeNumber);
	}


    @Override
    public void onError() {
        if(swipeClassement.isRefreshing())
            swipeClassement.setRefreshing(false);

        super.onError();
    }

    @Override
    public void onError(int messageId) {
        if(swipeClassement.isRefreshing())
            swipeClassement.setRefreshing(false);

        super.onError(messageId);
    }
    @Override
    public void onRefresh() {
        this.refreshDataAndView();
    }

    private void populateHeader(View header) {
        TextView classementPosition = (TextView) header.findViewById(R.id.classement_position);
        classementPosition.setText(" ");
        classementPosition.setTextColor(getResources().getColor(android.R.color.black));
        TextView classementNom = (TextView) header.findViewById(R.id.classement_nom);
        classementNom.setText("Equipe");
        classementNom.setTextColor(getResources().getColor(android.R.color.black));
        TextView classementPoints = (TextView) header.findViewById(R.id.classement_points);
        classementPoints.setText(getResources().getString(R.string.classement_header_points));
        classementPoints.setTextColor(getResources().getColor(android.R.color.black));
        TextView classementJoue = (TextView) header.findViewById(R.id.classement_joue);
        classementJoue.setText(getResources().getString(R.string.classement_header_joue));
        classementJoue.setTextColor(getResources().getColor(android.R.color.black));
        TextView classementDiff = (TextView) header.findViewById(R.id.classement_diff);
        classementDiff.setText(getResources().getString(R.string.classement_header_diff));
        classementDiff.setTextColor(getResources().getColor(android.R.color.black));
        TextView classementVictoire = (TextView) header.findViewById(R.id.classement_victoire);
        classementVictoire.setText(getResources().getString(R.string.classement_header_victoire));
        classementVictoire.setTextColor(getResources().getColor(android.R.color.black));
        TextView classementNul = (TextView) header.findViewById(R.id.classement_nul);
        classementNul.setText(getResources().getString(R.string.classement_header_nul));
        classementNul.setTextColor(getResources().getColor(android.R.color.black));
        TextView classementDefaite = (TextView) header.findViewById(R.id.classement_defaite);
        classementDefaite.setText(getResources().getString(R.string.classement_header_defaite));
        classementDefaite.setTextColor(getResources().getColor(android.R.color.black));
        TextView classementBp = (TextView) header.findViewById(R.id.classement_bp);
        classementBp.setText(getResources().getString(R.string.classement_header_bp));
        classementBp.setTextColor(getResources().getColor(android.R.color.black));
        TextView classementBc = (TextView) header.findViewById(R.id.classement_bc);
        classementBc.setText(getResources().getString(R.string.classement_header_bc));
        classementBc.setTextColor(getResources().getColor(android.R.color.black));
    }
}
