package com.hofc.hofc;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.hofc.hofc.adapter.ActusAdapter;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.data.download.ActusDownloader;

public class ActusFragment extends CommonFragment  implements FragmentCallback, CustomFragment {

	private ListView actusListView;
	
	public static ActusFragment newInstance() {
		return new ActusFragment();
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_accueil, container, false);
        actusListView = (ListView) rootView.findViewById(R.id.actus_listview);
        actusListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				String url = ((TextView)v.findViewById(R.id.actus_url)).getText().toString();
                Intent i = new Intent(getActivity(), ActusDetail.class);
                i.putExtra("URL", url);
                startActivity(i);
			}
		});
        if(DataSingleton.isSynchroActuNeeded()) {
        	this.refreshDataAndView();
		} else {
			refreshView();
		}
        return rootView;
    }

	public void refreshView(){
        super.refreshView();
		ActusAdapter adapter = new ActusAdapter(getActivity());
		actusListView.setAdapter(adapter);
	}

	@Override
	public void refreshDataAndView() {
        super.refreshDataAndView();
		ActusDownloader downloader = new ActusDownloader(this);
        downloader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
	
	
}
