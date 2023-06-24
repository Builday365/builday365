package com.example.builday365.model.Timeline;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Entity
public class TimeLine {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private DateTime curTime;
    private List<Memo> curMemos;

    public TimeLine() {
        this.id = System.currentTimeMillis();
        this.curTime = new DateTime(new Date());
        this.curMemos = new ArrayList<>();
    }

    public TimeLine(DateTime curTime, List<Memo> curMemos) {
        this.curTime = curTime;
        this.curMemos = curMemos;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DateTime getCurTime() {
        return curTime;
    }

    public void setCurTime(DateTime curTime) {
        this.curTime = curTime;
    }

    public List<Memo> getCurMemos() {
        return curMemos;
    }

    public void setCurMemos(List<Memo> curMemos) {
        this.curMemos = curMemos;
    }

    @Override
    public String toString() {
        return "TimeLine{" +
                "id=" + id +
                ", curTime=" + curTime +
                ", curMemos=" + curMemos +
                '}';
    }
}
