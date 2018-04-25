package com.example.amandaeliasson.ecodrivning;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

/**
 * Created by amandaeliasson on 2018-03-12.
 */

public class DriveMode extends Fragment implements View.OnClickListener {
    public static final String TAG = "com.example.ecodriving.amandaeliasson.DriveMode";
    public Button b;
    private DataProvider dp;
    //public Button dataButton;

    public DriveMode() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       Bundle args = getArguments();
       dp = (DataProvider) args.getSerializable(MainActivity.ARGS_DATA_PROVIDER);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.drivemode, container, false);
        Button b = (Button) v.findViewById(R.id.buttonF);
        b.setOnClickListener(this);
        ((Button) v.findViewById(R.id.buttonS)).setOnClickListener(this);
        ((Button) v.findViewById(R.id.buttonT)).setOnClickListener(this);
        ((Button) v.findViewById(R.id.buttonSC)).setOnClickListener(this);
        ((Button) v.findViewById(R.id.buttonfe)).setOnClickListener(this);
        //dataButton  = v.findViewById(R.id.data_button);
        return v;
    }


   public void onClick(View view) {
        Fragment f = null;
        Class fragmentClass;
       switch (view.getId()) {
           case R.id.buttonF:
                fragmentClass = DriveModeWarning.class;
                break;
            case R.id.buttonS:
                fragmentClass = DriveModeSmile.class;
                break;
            case R.id.buttonT:
                fragmentClass = DriveModeThird.class;
                break;
            case R.id.buttonfe:
               fragmentClass = DriveModeFlower.class;
                break;
            default:
                fragmentClass = DriveModeFourth.class;
        }
        try {
            f = (Fragment) fragmentClass.newInstance();
            Bundle args = new Bundle();
           args.putSerializable(MainActivity.ARGS_DATA_PROVIDER, dp);
            f.setArguments(args);

        } catch (Exception e) {
           e.printStackTrace();
        }
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content2, f).commit();
    }
    public void onResume() {
        super.onResume();
       ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
   public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
   }

}
