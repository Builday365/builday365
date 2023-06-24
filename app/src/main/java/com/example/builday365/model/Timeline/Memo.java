package com.example.builday365.model.Timeline;

import java.util.Date;

public class Memo {
    private Date startTime;
    private Date endTime;
    private String memoContent;

    public Memo(Date startTime, Date endTime, String memoContent) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.memoContent = memoContent;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public String getMemoContent() {
        return memoContent;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setMemoContent(String memoContent) {
        this.memoContent = memoContent;
    }

    @Override
    public String toString() {
        return "Memo{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", memoContent='" + memoContent + '\'' +
                '}';
    }
}