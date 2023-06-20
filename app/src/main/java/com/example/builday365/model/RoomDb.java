package com.example.builday365.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.builday365.model.Timeline.Converters;
import com.example.builday365.model.Timeline.TimeLine;
import com.example.builday365.model.Timeline.TimeLineDao;

@Database(entities = {TimeLine.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class RoomDb extends RoomDatabase {
    public abstract TimeLineDao timeLineDao();

}
