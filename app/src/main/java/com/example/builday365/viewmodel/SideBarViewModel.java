package com.example.builday365.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.builday365.model.Timeline.Memo;
import com.example.builday365.model.Timeline.TimeLine;

import java.sql.Time;
import java.util.Date;

public class SideBarViewModel extends ViewModel {

    private MutableLiveData<String> memo = new MutableLiveData<String>();
    private MutableLiveData<TimeLine> mtimeline = new MutableLiveData<TimeLine>();


    public SideBarViewModel() {
    }

    public LiveData<TimeLine> getTimeline() {
        return mtimeline;
    }
    // Method to add a memo
    public void addMemo(String memoContent, Date stime, Date etime) {

        TimeLine currentTimeLine = mtimeline.getValue();

        if(currentTimeLine == null)
        {
            currentTimeLine = new TimeLine();
        }

        Memo newMemo = new Memo(stime, etime, memoContent);
        currentTimeLine.getCurMemos().add(newMemo);
        mtimeline.setValue(currentTimeLine);
    }
}
