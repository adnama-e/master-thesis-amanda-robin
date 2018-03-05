package com.example.amandaeliasson.ecodrivning;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by amandaeliasson on 2018-01-24.
 */

public class ScoreFragment extends Fragment {
    public ScoreFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_score, container, false);
        ViewPager p = (ViewPager) v.findViewById(R.id.pager);
        p.setAdapter(new ScorePagerAdapter(getFragmentManager()));
        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(p);
        return v;
    }
}

