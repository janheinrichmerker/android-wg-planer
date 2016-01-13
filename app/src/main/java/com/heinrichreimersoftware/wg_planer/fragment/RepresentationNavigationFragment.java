package com.heinrichreimersoftware.wg_planer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.heinrichreimersoftware.materialdrawer.DrawerActivity;
import com.heinrichreimersoftware.wg_planer.R;
import com.heinrichreimersoftware.wg_planer.notifications.RepresentationsNotification;
import com.heinrichreimersoftware.wg_planer.utils.CalendarUtils;
import com.heinrichreimersoftware.wg_planer.utils.Utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RepresentationNavigationFragment extends Fragment {

    private static final String STATE_PAGER_POSITION = "pager_position_representations";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.pager)
    ViewPager viewPager;
    @Bind(R.id.pager_tab_strip)
    PagerSlidingTabStrip tabs;

    private int dayOfWeek = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_representation_navigation, container, false);
        ButterKnife.bind(this, rootView);

        DrawerActivity activity = (DrawerActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        RepresentationsNotification.cancel(getActivity());

        Calendar calendar = new GregorianCalendar();
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        sectionsPagerAdapter.notifyDataSetChanged();

        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setPageMargin(Utils.dpToPx(8, getActivity()));
        viewPager.setPageMarginDrawable(R.drawable.pager_margin);

        int position = 0;
        if (savedInstanceState != null && savedInstanceState.getInt(STATE_PAGER_POSITION, -1) >= 0 && savedInstanceState.getInt(STATE_PAGER_POSITION, -1) < sectionsPagerAdapter.getCount()) {
            position = savedInstanceState.getInt(STATE_PAGER_POSITION, -1);
        }
        viewPager.setCurrentItem(position, true);

        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
            getActivity().setTitle(R.string.title_fragment_representation_monday);

            tabs.setVisibility(View.GONE);
        } else {
            getActivity().setTitle(R.string.title_fragment_representation);

            tabs.setViewPager(viewPager);
            tabs.setVisibility(View.VISIBLE);
        }

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
            RepresentationFragment fragment = new RepresentationFragment();

            Bundle args = new Bundle();

            switch (position) {
                case 0:
                    switch (dayOfWeek) {
                        case Calendar.FRIDAY:
                            args.putLong(RepresentationFragment.ARG_DATE, CalendarUtils.today().getTimeInMillis());
                            break;
                        case Calendar.SATURDAY:
                        case Calendar.SUNDAY:
                            args.putLong(RepresentationFragment.ARG_DATE, CalendarUtils.nextMonday().getTimeInMillis());
                            break;
                        default:
                            args.putLong(RepresentationFragment.ARG_DATE, CalendarUtils.today().getTimeInMillis());
                            break;
                    }
                    break;
                case 1:
                    switch (dayOfWeek) {
                        case Calendar.FRIDAY:
                            args.putLong(RepresentationFragment.ARG_DATE, CalendarUtils.nextMonday().getTimeInMillis());
                            break;
                        case Calendar.SATURDAY:
                        case Calendar.SUNDAY:
                            return new Fragment();
                        default:
                            args.putLong(RepresentationFragment.ARG_DATE, CalendarUtils.tomorrow().getTimeInMillis());
                            break;
                    }
                    break;
            }

            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) ? 1 : 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.title_section_representation_today);
                case 1:
                    if (dayOfWeek == Calendar.FRIDAY) {
                        return getString(R.string.title_section_representation_monday);
                    } else {
                        return getString(R.string.title_section_representation_tomorrow);
                    }
            }
            return null;
        }
    }
}