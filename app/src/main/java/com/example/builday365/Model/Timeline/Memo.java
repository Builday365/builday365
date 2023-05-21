package com.example.builday365.Model.Timeline;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

public class Memo {
    private Date timeStamp;
    private String memoContent;

    public Memo(Date timeStamp, String memoContent) {
        this.timeStamp = timeStamp;
        this.memoContent = memoContent;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMemoContent() {
        return memoContent;
    }

    public void setMemoContent(String memoContent) {
        this.memoContent = memoContent;
    }

    @Override
    public String toString() {
        return "Memo{" +
                "timeStamp=" + timeStamp +
                ", memoContent='" + memoContent + '\'' +
                '}';
    }
}