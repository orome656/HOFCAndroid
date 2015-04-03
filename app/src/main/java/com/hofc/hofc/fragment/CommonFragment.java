package com.hofc.hofc.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.hofc.hofc.R;

/**
 * Created by maladota on 08/11/2014.
 * This Class is a common implementation for fragment, implementing common methods
 */
public class CommonFragment extends Fragment implements CustomFragment, FragmentCallback {
    @Override
    public void refreshDataAndView() {}

    public void refreshView(){}

    @Override
    public void onTaskDone() {
        this.refreshView();
    }

    @Override
    public void onError() {
        if(getActivity() != null)
            Toast.makeText(getActivity(), R.string.connexion_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(int messageId) {
        if(getActivity() != null)
            Toast.makeText(getActivity(), messageId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}
