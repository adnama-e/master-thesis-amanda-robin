package com.example.amandaeliasson.ecodrivning;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by amandaeliasson on 2018-02-22.
 */

public class ScorePagerAdapter extends FragmentPagerAdapter {
    NamedFragment [] fragments;
    public ScorePagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new NamedFragment[] {
                new Fragment1(), new ProgressFragment(), new AverageFragment()
        };
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //return super.getPageTitle(position);
        return fragments[position].getName();
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
