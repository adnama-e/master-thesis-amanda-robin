package com.example.amandaeliasson.ecodrivning;

import android.app.DatePickerDialog;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.design.widget.NavigationView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity implements Observer /*implements NavigationView.OnNavigationItemSelectedListener*/ {

    public static String ARGS_DATA_PROVIDER = "ARGS_DATA_PROVIDER";
    public static String ARGS_STATE = "ARGS_STATE";


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    public static PinpointManager pinpointManager;
    private Toolbar toolbar;
    View layout_interact;
    private DataHandler dataHandler;
    private DataProvider dp;
    private State state;
    private TextView startDate;

    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        FragmentTransaction fragment;
        layout_interact = (View) findViewById(R.id.drawLayout);
        //Toolbar to replace the Actionbar
        setUpToolbar();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawLayout);
        navigationView = (NavigationView) findViewById(R.id.nView);
        setUpNavigationDrawerContent(navigationView);
        drawerToggle = setUpDrawerToggle();

        drawerLayout.addDrawerListener(drawerToggle);

        dp = new DataProviderMockup();
        state= new State();
        state.addObserver(this);

        startDate = findViewById(R.id.start_date);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStartDatePicker();
            }
        });

//        dataHandler = new DataHandler(getAssets());

       /* AWSMobileClient.getInstance().initialize(this).execute();
        PinpointConfiguration pinpointConfig = new PinpointConfiguration(
                getApplicationContext(),
                AWSMobileClient.getInstance().getCredentialsProvider(),
                AWSMobileClient.getInstance().getConfiguration());

        pinpointManager = new PinpointManager(pinpointConfig);

        // Start a session with Pinpoint
        pinpointManager.getSessionClient().startSession();

        // Stop the session and submit the default app started event
        pinpointManager.getSessionClient().stopSession();
        pinpointManager.getAnalyticsClient().submitEvents();*/

    }
    public void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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

    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.drive:
                fragmentClass = DriveMode.class;
                break;
            case R.id.score:
                fragmentClass = ScoreFragment.class;
                startDate.setVisibility(View.VISIBLE);
                break;

            case R.id.goal:
                fragmentClass = GoalFragment.class;
                break;
            case R.id.Friends:
                fragmentClass = FriendFragment.class;
                break;
            default:
                fragmentClass = SettingsFragment.class;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();

            Bundle args = new Bundle();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       /* int id = item.getItemId();
        switch (id) {
            case R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;*/
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


    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void update(Observable observable, Object o) {
        DateFormat dateFormat = SimpleDateFormat.getDateInstance();
        startDate.setText(dateFormat.format(state.getStartDate().getTime()));
    }
}
