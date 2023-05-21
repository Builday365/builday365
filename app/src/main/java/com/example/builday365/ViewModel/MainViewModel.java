package com.example.builday365.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import com.example.builday365.Model.RoomDb;

public class MainViewModel extends ViewModel {

    private final String TAG = "MainViewModel";
    private final Application Mainapplication;


    public MainViewModel(Application application)
    {
        Log.d(TAG, "MainViewModel constructor");

        
        RoomDb db = Room.databaseBuilder(application.getApplicationContext(), RoomDb.class, "timeline-db").build();
        backgroundThread.start();
        Mainapplication = application;
    }
    Thread backgroundThread = new Thread(new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "backgroundThread runs");

            RoomDb db = Room.databaseBuilder(Mainapplication.getApplicationContext(), RoomDb.class, "timeline-db").build();
        }
    });

}
