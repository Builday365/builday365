package com.example.builday365.model.test

import androidx.room.Entity
import java.text.SimpleDateFormat
import java.util.Date

@Entity
class DateTime(date: Date?) {
    var year: Int
    var month: Int
    var day: Int
    var hour: Int
    var minute: Int
    var second: Int

    init {
        val yearFormat = SimpleDateFormat("yyyy")
        val monthFormat = SimpleDateFormat("MM")
        val dayFormat = SimpleDateFormat("dd")
        val hourFormat = SimpleDateFormat("HH")
        val minuteFormat = SimpleDateFormat("mm")
        val secondFormat = SimpleDateFormat("ss")
        year = yearFormat.format(date).toInt()
        month = monthFormat.format(date).toInt()
        day = dayFormat.format(date).toInt()
        hour = hourFormat.format(date).toInt()
        minute = minuteFormat.format(date).toInt()
        second = minuteFormat.format(date).toInt()
    }
}