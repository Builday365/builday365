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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private AdMobManager mAdMobManager;

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


    }
}