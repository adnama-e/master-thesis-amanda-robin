package com.example.amandaeliasson.ecodrivning;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class DriveMode extends Fragment implements View.OnClickListener {
    private DataProvider dp;

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
        Button b = v.findViewById(R.id.buttonF);
        b.setOnClickListener(this);
        v.findViewById(R.id.buttonS).setOnClickListener(this);
        v.findViewById(R.id.buttonT).setOnClickListener(this);
        v.findViewById(R.id.buttonSC).setOnClickListener(this);
        v.findViewById(R.id.buttonfe).setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        Fragment f;
        switch (view.getId()) {
            case R.id.buttonF:
                f = new DriveModeWarning();
                break;
            case R.id.buttonS:
                f = new DriveModeSmile();
                break;
            case R.id.buttonT:
                f = new DriveModeThird();
                break;
            case R.id.buttonfe:
                f = new DriveModeFlower();
                break;
            default:
                f = new DriveModeFourth();
        }

        try {
            Bundle args = new Bundle();
            args.putSerializable(MainActivity.ARGS_DATA_PROVIDER, dp);
            f.setArguments(args);
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content2, f).commit();
    }

}
