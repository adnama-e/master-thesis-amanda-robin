package com.example.amandaeliasson.ecodrivning;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by amandaeliasson on 2018-02-22.
 */

public class ScorePagerAdapter extends FragmentPagerAdapter {
    NamedFragment [] fragments;
    public ScorePagerAdapter(FragmentManager fm, State state) {
        super(fm);
        Bundle args= new Bundle();
        args.putSerializable(MainActivity.ARGS_STATE, state);
        NamedFragment fragment1 = new Fragment1();
        fragment1.setArguments(args);
        NamedFragment progressFragment = new ProgressFragment();
        progressFragment.setArguments(args);
        NamedFragment averageFragment = new AverageFragment();
        averageFragment.setArguments(args);
        fragments = new NamedFragment[] {
                fragment1, progressFragment, averageFragment
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
