package com.hofc.hofc.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.widget.Toast;

import com.hofc.hofc.Accueil;
import com.hofc.hofc.R;

/**
 * Created by maladota on 08/11/2014.
 * This Class is a common implementation for fragment, implementing common methods
 */
public class CommonFragment extends Fragment implements CustomFragment, FragmentCallback {
    private Accueil mainActivity;
    @Override
    public void refreshDataAndView() {
        mainActivity.startRefresh();

    }

    public void refreshView(){}

    @Override
    public void onTaskDone() {
        mainActivity.endRefresh();
        this.refreshView();
    }

    @Override
    public void onError() {
        mainActivity.endRefresh();
        if(getActivity() != null)
            Toast.makeText(getActivity(), R.string.connexion_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String message) {
        mainActivity.endRefresh();
        if(getActivity() != null)
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(int messageId) {
        mainActivity.endRefresh();
        if(getActivity() != null)
            Toast.makeText(getActivity(), messageId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mainActivity = (Accueil)activity;
    }
}
