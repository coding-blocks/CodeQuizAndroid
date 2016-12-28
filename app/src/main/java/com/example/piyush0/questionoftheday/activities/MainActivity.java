package com.example.piyush0.questionoftheday.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.fragments.ArchiveFragment;
import com.example.piyush0.questionoftheday.fragments.ChallengeFragment;
import com.example.piyush0.questionoftheday.fragments.MyChallengesFragment;
import com.example.piyush0.questionoftheday.fragments.MyProfileFragment;
import com.example.piyush0.questionoftheday.utils.FontsOverride;
import com.example.piyush0.questionoftheday.utils.Refresh;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String SHARED_PREF_NAME = "TodaySolved";

    private SharedPreferences sharedPreferences;
    private FragmentManager fragmentManager;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*This is done in every activity to change the font of the toolbar*/
        FontsOverride.applyFontForToolbarTitle(this, FontsOverride.FONT_PROXIMA_NOVA, getWindow());


        initToolbar();
        initDrawerAndNavigationView();
        initSharedPrefs();
        initFragmentManager();

        Refresh.refresh(sharedPreferences, fragmentManager, this);
    }

    private void initSharedPrefs() {
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    private void initDrawerAndNavigationView() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initFragmentManager() {
        fragmentManager = getSupportFragmentManager();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_today) {

            Refresh.refresh(sharedPreferences, fragmentManager, this);

        } else if (id == R.id.nav_challenge) {

            fragmentManager.beginTransaction()
                    .replace(R.id.content_main,
                            ChallengeFragment.newInstance()).commit();

        } else if (id == R.id.nav_archive) {

            fragmentManager.beginTransaction()
                    .replace(R.id.content_main,
                            ArchiveFragment.newInstance()).commit();

        } else if (id == R.id.nav_profile) {

            fragmentManager.beginTransaction()
                    .replace(R.id.content_main,
                            MyProfileFragment.newInstance()).commit();
        } else if (id == R.id.nav_my_challenges) {

            fragmentManager.beginTransaction()
                    .replace(R.id.content_main,
                            MyChallengesFragment.newInstance()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
