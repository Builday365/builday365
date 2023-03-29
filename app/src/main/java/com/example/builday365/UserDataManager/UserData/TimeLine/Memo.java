package com.example.builday365.UserDataManager.UserData.TimeLine;

import java.util.Date;

public class Memo {


    private Date timeStamp;
    private String memoContent;

    public Memo(Date timeStamp, String memoContent) {
        this.timeStamp = timeStamp;
        this.memoContent = memoContent;
    }

    public void setMemo(Date timeStamp, String memoContent)
    {
        this.timeStamp = timeStamp;
        this.memoContent = memoContent;
    }

    public Date getTimeStamp() {
        return this.timeStamp;
    }

    public String getMemoContent() {
        return this.memoContent;
    }

}