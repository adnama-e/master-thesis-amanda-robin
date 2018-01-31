package com.example.amandaeliasson.ecodrivning;

import android.app.ActionBar;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity /*implements NavigationView.OnNavigationItemSelectedListener*/ {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    View layout_interact;


    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout_interact = (View) findViewById(R.id.drawLayout);

        //Toolbar to replace the Actionbar
        setUpToolbar();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawLayout);
        navigationView = (NavigationView) findViewById(R.id.nView);


        setUpNavigationDrawerContent(navigationView);

        drawerToggle = setUpDrawerToggle();

        drawerLayout.addDrawerListener(drawerToggle);


    }

    public void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
            case R.id.nav1:
                fragmentClass = Fragment1.class;
                break;
            case R.id.nav2:
                fragmentClass = Fragment2.class;
                break;
            default:
                fragmentClass = Fragment3.class;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();

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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_0) {
            layout_interact.setBackgroundColor(getResources().getColor(R.color.green4));

        } else if (keyCode == KeyEvent.KEYCODE_1) {
            layout_interact.setBackgroundColor(getResources().getColor(R.color.red));
        }else{
            layout_interact.setBackgroundColor(Color.WHITE);
        }
        return super.onKeyDown(keyCode, event);
    }
}

//    private void updateGUI() {
//        EditText text = (EditText) findViewById(R.id.numberText);
//        if (text.equals("1")) {
//            layout_interact.setBackgroundColor(Color.GREEN);
//        } else if (text.equals("0")) {
//            layout_interact.setBackgroundColor(Color.RED);
//        } else {
//            layout_interact.setBackgroundColor(Color.WHITE);
//        }
//    }




    /*  *//*  ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                drawer.closeDrawers();
                return true;
            }
        });*//*


   *//* public void setUpNavigationDrawer(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        setUpActionBarDrawerToogle();

        if(navigationView!=null){
            setUpDrawerAsContent(navigationView);
        }
    }*//*
    *//*public void setUpDrawerAsContent(NavigationView view){
        view.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    item.setChecked(true);
                    drawerLayout.closeDrawers();
                    return true;
                }
        });

    }
    public void setUpActionBarDrawerToogle(){
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }*//*

    @Override
   *//* public void onBackPressed() {
        if (isNavigationDrawerOpen()) {
            closeNavigationDrawer();
        } else {
            super.onBackPressed();
        }
    }
    public boolean isNavigationDrawerOpen(){
        if(drawerLayout !=null && drawerLayout.isDrawerOpen(GravityCompat.START)){
            return true;
        }else{
            return false;
        }
    }
    public void closeNavigationDrawer(){
        if(drawerLayout!=null){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }*//*

    @Override
   *//* public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fm = getFragmentManager();

        if (id == R.id.nav1) {
            setTitle("Fragement1");
            Fragment1 f1 = new Fragment1();
            fm.beginTransaction().replace(R.id.main_content, f1).commit();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/
