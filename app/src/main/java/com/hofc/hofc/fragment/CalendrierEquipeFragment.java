package com.hofc.hofc.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hofc.hofc.R;
import com.hofc.hofc.constant.AppConstant;
import com.hofc.hofc.utils.HOFCUtils;

public class CalendrierEquipeFragment extends Fragment {

	public static CalendrierEquipeFragment newInstance() {
		return new CalendrierEquipeFragment();
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_equipe_calendrier, container, false);

        SectionsPagerAdapter customPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.calendrier_pager);
        viewPager.setAdapter(customPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(0);

        return rootView;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return CalendrierFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return AppConstant.TEAM_NUMBER;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Equipe " + (position + 1);
        }
    }

}
