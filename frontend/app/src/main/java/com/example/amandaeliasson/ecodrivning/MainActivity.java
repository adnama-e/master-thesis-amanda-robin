package com.example.amandaeliasson.ecodrivning;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public class MainActivity extends AppCompatActivity implements Observer {
    public static String ARGS_DATA_PROVIDER = "ARGS_DATA_PROVIDER";
    public static String ARGS_STATE = "ARGS_STATE";

    public static String ARGS_INITIAL_FRAGMENT = "ARGS_INITIAL_FRAGMENT";

    public static String ARGS_DB_MAPPER = "ARGS_DB_MAPPER";
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    private Toolbar toolbar;
    private DataProvider dp;
    private State state;
    private TextView startDate;
    private TextView endDate;
    static DynamoDBMapper dynamoDBMapper;
    View layout_interact;


    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        layout_interact = (View) findViewById(R.id.drawLayout);

        setUpToolbar();
        drawerLayout = findViewById(R.id.drawLayout);
        navigationView = findViewById(R.id.nView);
        setUpNavigationDrawerContent(navigationView);
        drawerToggle = setUpDrawerToggle();

        drawerLayout.addDrawerListener(drawerToggle);



        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                Log.d("MainActivity", "AWSMobileClient is instantiated and you are connected to AWS!");
            }
        }).execute();


        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());
        dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                .build();
        //dp = new AWSDataProvider();
        dp = new DataProviderMockup();
        state = new State();
        state.addObserver(this);

        startDate = findViewById(R.id.start_date);
        endDate = findViewById(R.id.end_date);

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEndDatePicker();
            }
        });
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStartDatePicker();
            }
        });
        DateFormat dateFormat = SimpleDateFormat.getDateInstance();
        startDate.setText("From: "+dateFormat.format(state.getStartDate().getTime()));
        endDate.setText("To: "+dateFormat.format(state.getEndDate().getTime()));

        Intent intent = getIntent();
        String initClassTag = intent.getStringExtra(ARGS_INITIAL_FRAGMENT);
        if(initClassTag != null){
            Fragment fragment = null;
            switch (initClassTag){
                case DriveMode.TAG:
                    fragment = new DriveMode();
                    break;
                case ScoreFragment.TAG:
                    fragment = new ScoreFragment();
                    endDate.setVisibility(View.VISIBLE);
                    startDate.setVisibility(View.VISIBLE);
                    break;

            }
            if(fragment!=null){
                Bundle args = new Bundle();
                args.putSerializable(MainActivity.ARGS_DATA_PROVIDER, dp);
                args.putSerializable(MainActivity.ARGS_STATE, state);
                fragment.setArguments(args);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flcontent, fragment).commit();
            }
        }



    }
    public void setUpToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public ActionBarDrawerToggle setUpDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    }

    private void setUpNavigationDrawerContent(NavigationView navigation) {
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
    }
    public void openMenuItem(int id){
        Fragment fragment = null;
        Class fragmentClass;
        switch (id) {
            case R.id.drive:
                fragmentClass = DriveMode.class;
                endDate.setVisibility(View.INVISIBLE);
                startDate.setVisibility(View.INVISIBLE);
                break;
            case R.id.score:
                fragmentClass = ScoreFragment.class;
                endDate.setVisibility(View.VISIBLE);
                startDate.setVisibility(View.VISIBLE);
                break;

            case R.id.goal:
                fragmentClass = GoalFragment.class;
                endDate.setVisibility(View.INVISIBLE);
                startDate.setVisibility(View.INVISIBLE);
                break;
            case R.id.Friends:
                fragmentClass = FriendFragment.class;
                endDate.setVisibility(View.INVISIBLE);
                startDate.setVisibility(View.INVISIBLE);
                break;
            default:
                fragmentClass = SettingsFragment.class;
                endDate.setVisibility(View.INVISIBLE);
                startDate.setVisibility(View.INVISIBLE);
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();

            Bundle args = new Bundle();

            args.putSerializable(MainActivity.ARGS_DATA_PROVIDER, dp);

            args.putSerializable(MainActivity.ARGS_STATE, state);
            fragment.setArguments(args);
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flcontent, fragment).commit();

    }

    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.drive:
                fragmentClass = DriveMode.class;
                endDate.setVisibility(View.INVISIBLE);
                startDate.setVisibility(View.INVISIBLE);
                break;
            case R.id.score:
                fragmentClass = ScoreFragment.class;
                endDate.setVisibility(View.VISIBLE);
                startDate.setVisibility(View.VISIBLE);
                break;

            case R.id.goal:
                fragmentClass = GoalFragment.class;
                endDate.setVisibility(View.INVISIBLE);
                startDate.setVisibility(View.INVISIBLE);
                break;
            case R.id.Friends:
                fragmentClass = FriendFragment.class;
                endDate.setVisibility(View.INVISIBLE);
                startDate.setVisibility(View.INVISIBLE);
                break;
            default:
                fragmentClass = SettingsFragment.class;
                endDate.setVisibility(View.INVISIBLE);
                startDate.setVisibility(View.INVISIBLE);
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();

            Bundle args = new Bundle();

            args.putSerializable(MainActivity.ARGS_DATA_PROVIDER, dp);

            args.putSerializable(MainActivity.ARGS_STATE, state);
            fragment.setArguments(args);
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flcontent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        drawerLayout.closeDrawers();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    public void openStartDatePicker(){
        Calendar currentDate = state.getStartDate();
        DatePickerDialog pickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                state.setStartDate(year, month, day);
            }
        },currentDate.get(Calendar.YEAR),currentDate.get(Calendar.MONTH),currentDate.get(Calendar.DAY_OF_MONTH));
        pickerDialog.show();
    }

    public void openEndDatePicker(){
        Calendar currentDate = state.getEndDate();
        DatePickerDialog pickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                state.setEndDate(year, month, day);
            }
        },currentDate.get(Calendar.YEAR),currentDate.get(Calendar.MONTH),currentDate.get(Calendar.DAY_OF_MONTH));
        pickerDialog.show();
    }

    @Override
    public void update(Observable observable, Object o) {
        DateFormat dateFormat = SimpleDateFormat.getDateInstance();
        startDate.setText("From: " + dateFormat.format(state.getStartDate().getTime()));
        endDate.setText("To: " + dateFormat.format(state.getEndDate().getTime()));
    }
}
