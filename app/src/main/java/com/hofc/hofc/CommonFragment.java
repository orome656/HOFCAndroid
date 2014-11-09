package com.hofc.hofc;

import android.app.Activity;
import android.app.Fragment;
import android.widget.Toast;

import java.util.concurrent.Callable;

/**
 * Created by maladota on 08/11/2014.
 */
public class CommonFragment extends Fragment implements CustomFragment, FragmentCallback {
    private Accueil mainActivity;
    @Override
    public void refreshDataAndView() {

    }

    public void refreshView(){}

    @Override
    public void onTaskDone() {
        mainActivity.endRefresh();
        this.refreshView();
    }

    @Override
    public void onError() {
        if(getActivity() != null)
            Toast.makeText(getActivity(), R.string.connexion_error, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mainActivity = (Accueil)activity;
    }
}
