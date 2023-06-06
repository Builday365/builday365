package com.example.builday365.View;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.builday365.Model.RoomDb;
import com.example.builday365.ViewModel.*;
import com.example.builday365.R;

public class MainActivity extends AppCompatActivity
        implements MapsFragment.MapsFragmentListener, UiFragment.UiFragmentListener {
    private static final String TAG = "MainActivity";
    private AdMobManager mAdMobManager;

    private MapsFragment mapsFragment;
    private UiFragment uiFragment;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // AdMob Manager
        mAdMobManager = new AdMobManager(this);
        mAdMobManager.loadAd(this, findViewById(R.id.adView));

        MainViewModel mv = new MainViewModel(getApplication());

        mapsFragment = new MapsFragment();
        uiFragment = new UiFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.map, mapsFragment)
                .replace(R.id.ui, uiFragment)
                .commit();
    }

    @Override
    public void onInputASent(CharSequence input) {
        if (input == "true") {
            uiFragment.map_cur_location_click_listener();
        }
    }

    @Override
    public void onInputBSent(CharSequence input) {}
}