package com.example.builday365.ViewModel;

import android.util.Log;

import com.example.builday365.Model.DatabaseManager;
import com.example.builday365.Model.Timeline.Memo;

import java.util.Date;

public class MainViewModel {

    private final String TAG = "MainViewModel";

    private DatabaseManager db;


    public MainViewModel(DatabaseManager database)
    {
        this.db = database;
    }

    public void addMemo(Date timeStamp, String memoContent)
    {
        if(db != null)
        {
            db.timeLine.addMemo(timeStamp, memoContent);
        }
    }

    public void getMemoForTimeStamp(Date timeStamp){
        if(db != null)
        {
            Memo m = db.timeLine.getMemoForTimeStamp(timeStamp);
            Log.d(TAG, "memo : " + m.getMemoContent());

        }
    }

}
