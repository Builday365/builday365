package com.example.builday365.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.room.Room.databaseBuilder
import com.example.builday365.model.RoomDb

class MainViewModel(application: Application) : ViewModel() {
    private val TAG = "MainViewModel"
    private val Mainapplication: Application
    var backgroundThread = Thread {
        Log.d(TAG, "backgroundThread runs")
        val db = databaseBuilder(
            Mainapplication.applicationContext,
            RoomDb::class.java,
            "timeline-db"
        ).build()
    }

    init {
        Log.d(TAG, "MainViewModel constructor")
        val db = databaseBuilder(
            application.applicationContext,
            RoomDb::class.java,
            "timeline-db"
        ).build()
        backgroundThread.start()
        Mainapplication = application
    }
}