package com.example.builday365.model.Timeline;

import androidx.room.Entity;

import java.text.SimpleDateFormat;
import java.util.Date;
@Entity
public class DateTime {
    public int year;
    public int month;
    public int day;
    public int hour;
    public int minute;
    public int second;

    public DateTime(Date date) {
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
        SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");
        SimpleDateFormat secondFormat = new SimpleDateFormat("ss");

        year = Integer.parseInt(yearFormat.format(date));
        month = Integer.parseInt(monthFormat.format(date));
        day = Integer.parseInt(dayFormat.format(date));
        hour = Integer.parseInt(hourFormat.format(date));
        minute = Integer.parseInt(minuteFormat.format(date));
        second = Integer.parseInt(minuteFormat.format(date));
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }
}
