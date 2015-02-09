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
import android.widget.TextView;

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

        View header = inflater.inflate(R.layout.item_classement, null);

        TextView classementPosition = (TextView) header.findViewById(R.id.classement_position);
        classementPosition.setText(" ");
        TextView classementNom = (TextView) header.findViewById(R.id.classement_nom);
        classementNom.setText("Equipe");
        TextView classementPoints = (TextView) header.findViewById(R.id.classement_points);
        classementPoints.setText("Pts");
        TextView classementJoue = (TextView) header.findViewById(R.id.classement_joue);
        classementJoue.setText("J");
        TextView classementDiff = (TextView) header.findViewById(R.id.classement_diff);
        classementDiff.setText("+/-");
        TextView classementVictoire = (TextView) header.findViewById(R.id.classement_victoire);
        classementVictoire.setText("V");
        TextView classementNul = (TextView) header.findViewById(R.id.classement_nul);
        classementNul.setText("N");
        TextView classementDefaite = (TextView) header.findViewById(R.id.classement_defaite);
        classementDefaite.setText("D");
        TextView classementBp = (TextView) header.findViewById(R.id.classement_bp);
        classementBp.setText("BP");
        TextView classementBc = (TextView) header.findViewById(R.id.classement_bc);
        classementBc.setText("BC");
        classementListView.addHeaderView(header);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(DataSingleton.isSynchroClassementNeeded()) {
            this.refreshDataAndView();
        } else {
            this.refreshView();
        }
    }

    public void refreshView(){
        super.refreshView();
		ClassementAdapter adapter = new ClassementAdapter(getActivity());
		classementListView.setAdapter(adapter);
	}

	@Override
	public void refreshDataAndView() {
        super.refreshDataAndView();
		ClassementDownloader downloader = new ClassementDownloader(this);
		downloader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
	
}
