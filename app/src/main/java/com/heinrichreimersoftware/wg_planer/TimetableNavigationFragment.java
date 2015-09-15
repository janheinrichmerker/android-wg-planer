package com.heinrichreimersoftware.wg_planer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.heinrichreimersoftware.wg_planer.notifications.TimetableNotification;
import com.heinrichreimersoftware.wg_planer.utils.Utils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TimetableNavigationFragment extends Fragment {

    private static final String STATE_PAGER_POSITION = "pager_position_timetable";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.pager)
    ViewPager viewPager;
    @Bind(R.id.pager_tab_strip)
    PagerSlidingTabStrip tabs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timetable_navigation, container, false);
        ButterKnife.bind(this, rootView);

        ActionBarActivity activity = (ActionBarActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        getActivity().setTitle(R.string.title_fragment_timetable);

        TimetableNotification.cancel(getActivity());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        sectionsPagerAdapter.notifyDataSetChanged();

        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setPageMargin(Utils.dpToPx(8, getActivity()));
        viewPager.setPageMarginDrawable(R.drawable.pager_margin);

        Calendar calendar = new GregorianCalendar();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        int position = 0;
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                position = 0;
                break;
            case Calendar.TUESDAY:
                position = 1;
                break;
            case Calendar.WEDNESDAY:
                position = 2;
                break;
            case Calendar.THURSDAY:
                position = 3;
                break;
            case Calendar.FRIDAY:
                position = 4;
                break;
            case Calendar.SATURDAY:
                position = 0;
                break;
            case Calendar.SUNDAY:
                position = 0;
                break;
        }

        if (savedInstanceState != null && savedInstanceState.getInt(STATE_PAGER_POSITION, -1) >= 0 && savedInstanceState.getInt(STATE_PAGER_POSITION, -1) < sectionsPagerAdapter.getCount()) {
            position = savedInstanceState.getInt(STATE_PAGER_POSITION, -1);
        }
        viewPager.setCurrentItem(position, true);

        tabs.setViewPager(viewPager);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_PAGER_POSITION, viewPager.getCurrentItem());
        super.onSaveInstanceState(outState);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new TimetableFragment();
            Bundle args = new Bundle();
            args.putInt(TimetableFragment.ARG_SECTION_NUMBER, position);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.label_monday).toUpperCase(l);
                case 1:
                    return getString(R.string.label_tuesday).toUpperCase(l);
                case 2:
                    return getString(R.string.label_wednesday).toUpperCase(l);
                case 3:
                    return getString(R.string.label_thursday).toUpperCase(l);
                case 4:
                    return getString(R.string.label_friday).toUpperCase(l);
            }
            return null;
        }
    }
}