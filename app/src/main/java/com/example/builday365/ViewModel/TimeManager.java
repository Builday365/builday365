package com.example.builday365.ViewModel;

import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.builday365.Model.Timeline.DateTime;

import java.util.Date;

public class TimeManager{

    private static final String TAG = "MainService";
    private static TimeManager instance;

    DateTime cur_time;

    public TimeManager() {
        timeBarUpdater();
    }

    public static synchronized TimeManager getInstance() {
        if (instance == null) {
            instance = new TimeManager();
        }
        return instance;
    }

    private void timeBarUpdater()
    {
        Handler handler;
        Runnable runnable;

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                currentTimePrinter();
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    void currentTimePrinter()
    {
        cur_time = new DateTime(new Date(System.currentTimeMillis()));
        Log.d(TAG,  cur_time.year + "-" + cur_time.month + "-" +cur_time.day + " " + cur_time.hour + ":" + cur_time.minute + ":" + cur_time.second);
    }

}


