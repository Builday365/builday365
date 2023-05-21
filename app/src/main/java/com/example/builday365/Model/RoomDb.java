package com.example.builday365.Model;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.builday365.Model.Timeline.Converters;
import com.example.builday365.Model.Timeline.TimeLine;
import com.example.builday365.Model.Timeline.TimeLineDao;

@Database(entities = {TimeLine.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class RoomDb extends RoomDatabase {
    public abstract TimeLineDao timeLineDao();

}
