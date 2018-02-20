package com.example.amandaeliasson.ecodrivning;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by amandaeliasson on 2018-01-24.
 */

public class Fragment2 extends Fragment implements View.OnClickListener {
    public Button b;

    public Fragment2() {

        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment2, container, false);
        Button b = (Button) v.findViewById(R.id.button2);
        b.setOnClickListener(this);
        ((Button) v.findViewById(R.id.button3)).setOnClickListener(this);
        ((Button) v.findViewById(R.id.button4)).setOnClickListener(this);

        return v;
    }
    @Override
    public void onClick(View view) {
        Fragment fragment = null;
        Class fragmentClass;
       switch (view.getId()) {
            case R.id.button2:
                fragmentClass = FirstHighScoreButton.class;
                break;
            case R.id.button3:
                fragmentClass = SecondHighScoreButton.class;
                break;
            default:
                fragmentClass = thirdHighScoreButton.class;
    }
        try {
            fragment = (Fragment) fragmentClass.newInstance();

        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();
}


}


