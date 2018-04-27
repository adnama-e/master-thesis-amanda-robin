package com.example.amandaeliasson.ecodrivning;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

/**
 * Created by amandaeliasson on 2018-01-24.
 */

public class ScoreFragment extends Fragment {
    public static final String TAG = "com.example.amandaeliasson.ecodriving.ScoreFragment";
    private State state;
    private FragmentPagerAdapter adapter;

    public ScoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        state = (State) getArguments().getSerializable(MainActivity.ARGS_STATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_score, container, false);
        ViewPager p = (ViewPager) v.findViewById(R.id.pager);
        if (adapter == null) adapter = new ScorePagerAdapter(getChildFragmentManager(), state);
        p.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(p);
        return v;
    }

}

