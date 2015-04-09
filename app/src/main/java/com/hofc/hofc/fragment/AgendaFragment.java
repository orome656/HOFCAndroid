package com.hofc.hofc.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hofc.hofc.R;
import com.hofc.hofc.constant.AppConstant;
import com.hofc.hofc.utils.HOFCUtils;

public class AgendaFragment extends Fragment {

	public static AgendaFragment newInstance() {
		return new AgendaFragment();
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_agenda, container, false);

        SectionsPagerAdapter customPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.agenda_pager);
        viewPager.setAdapter(customPagerAdapter);

        viewPager.setCurrentItem(AppConstant.AGENDA_WEEK_NUMBER);

        return rootView;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return AgendaWeekFragment.newInstance(getPageTitle(position).toString());
        }

        @Override
        public int getCount() {
            return AppConstant.AGENDA_WEEK_NUMBER * 2 + 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            int weekDiff = position - AppConstant.AGENDA_WEEK_NUMBER;
            return HOFCUtils.getTabTitle(weekDiff);
        }
    }

}
