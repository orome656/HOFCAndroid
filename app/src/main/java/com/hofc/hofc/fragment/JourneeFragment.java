package com.hofc.hofc.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.hofc.hofc.R;
import com.hofc.hofc.data.DataSingleton;

public class JourneeFragment extends Fragment {

	public static JourneeFragment newInstance() {
		return new JourneeFragment();
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_journee, container, false);
        if(DataSingleton.getParams() == null || DataSingleton.getParams().getSeasonMatchCount() == 0) {
            MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                    .content(R.string.param_error)
                    .positiveText(R.string.close_popup_button)
                    .theme(Theme.LIGHT)
                    .build();
            dialog.show();
        } else {
            SectionsPagerAdapter customPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

            ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.journee_pager);
            viewPager.setAdapter(customPagerAdapter);
            viewPager.setOffscreenPageLimit(3);
            viewPager.setCurrentItem(1);
        }
        return rootView;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return JourneeWeekFragment.newInstance(getPageTitle(position).toString());
        }

        @Override
        public int getCount() {
            return DataSingleton.getParams().getSeasonMatchCount();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position + 1 +"";
        }
    }

}
