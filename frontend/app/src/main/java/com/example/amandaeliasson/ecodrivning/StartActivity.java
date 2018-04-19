package com.example.amandaeliasson.ecodrivning;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.CardView;

import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class StartActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);
        RelativeLayout mContainerView = findViewById(R.id.startBackground);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.carmoving22);
        Bitmap blurredBitmap = BlurBuilder.blur(this, originalBitmap);
        mContainerView.setBackground(new BitmapDrawable(getResources(), blurredBitmap));
        CardView drive = findViewById(R.id.card);
        drive.setOnClickListener(this);
        CardView score = findViewById(R.id.cardView);
        score.setOnClickListener( this);
    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(this, MainActivity.class);
        switch (view.getId()) {
            case R.id.card:
                i.putExtra(MainActivity.ARGS_INITIAL_FRAGMENT, DriveMode.TAG);
                break;
            default:
                i.putExtra(MainActivity.ARGS_INITIAL_FRAGMENT, ScoreFragment.TAG);
        }
        startActivity(i);
    }
}
