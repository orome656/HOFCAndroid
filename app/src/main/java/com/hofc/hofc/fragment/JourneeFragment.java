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
import com.hofc.hofc.data.LocalDataSingleton;

public class JourneeFragment extends Fragment {

	public static JourneeFragment newInstance() {
		return new JourneeFragment();
	}
	private String equipe;
    private int matchNumber;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_journee, container, false);
        equipe = getArguments().getString("equipe");
        if(LocalDataSingleton.getParams() == null) {
            matchNumber = 0;
        } else if("equipe1".equalsIgnoreCase(equipe)) {
            matchNumber = LocalDataSingleton.getParams().getSeasonMatchCount();
        } else if ("equipe2".equalsIgnoreCase(equipe)) {
            matchNumber = LocalDataSingleton.getParams().getSeasonMatchCountEquipe2();
        } else if ("equipe3".equalsIgnoreCase(equipe)) {
            matchNumber = LocalDataSingleton.getParams().getSeasonMatchCountEquipe3();
        }
        if(matchNumber == 0) {
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
            return JourneeWeekFragment.newInstance(getPageTitle(position).toString(), equipe);
        }

        @Override
        public int getCount() {
            return matchNumber;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position + 1 +"";
        }
    }

}
