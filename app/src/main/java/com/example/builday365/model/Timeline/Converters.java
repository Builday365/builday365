package com.example.builday365.model.Timeline;

import androidx.room.ProvidedTypeConverter;
import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

public class Converters {

    @TypeConverter
    public String DateTimeToJason(DateTime date){
        return new Gson().toJson(date);
    }

    @TypeConverter
    public DateTime JasonToDateTime(String DateTimedb){
        return new Gson().fromJson(DateTimedb,DateTime.class);
    }

    @TypeConverter
    public String MemoToJason(List<Memo> memo){
        return new Gson().toJson(memo);
    }

    @TypeConverter
    public List<Memo> JasonToMemo(String Memodb){
        return new Gson().fromJson(Memodb,new TypeToken<List<Memo>>() {}.getType());
    }

}
