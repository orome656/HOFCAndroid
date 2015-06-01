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
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.utils.HOFCUtils;

public class JourneeFragment extends Fragment {

	public static JourneeFragment newInstance() {
		return new JourneeFragment();
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_journee, container, false);

        SectionsPagerAdapter customPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.journee_pager);
        viewPager.setAdapter(customPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(1);

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
