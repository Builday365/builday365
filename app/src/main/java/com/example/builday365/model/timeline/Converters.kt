package com.example.builday365.model.timeline

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun DateTimeToJason(date: DateTime?): String {
        return Gson().toJson(date)
    }

    @TypeConverter
    fun JasonToDateTime(DateTimedb: String?): DateTime {
        return Gson().fromJson(DateTimedb, DateTime::class.java)
    }

    @TypeConverter
    fun MemoToJason(memo: List<Memo?>?): String {
        return Gson().toJson(memo)
    }

    @TypeConverter
    fun JasonToMemo(Memodb: String?): List<Memo> {
        return Gson().fromJson(Memodb, object : TypeToken<List<Memo?>?>() {}.type)
    }
}