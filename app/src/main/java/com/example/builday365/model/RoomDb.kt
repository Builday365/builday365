package com.example.builday365.model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.builday365.model.timeline.Converters
import com.example.builday365.model.timeline.TimeLine
import com.example.builday365.model.timeline.TimeLineDao

@Database(entities = [TimeLine::class], version = 1)
@TypeConverters(*[Converters::class])
abstract class RoomDb : RoomDatabase() {
    abstract fun timeLineDao(): TimeLineDao?
}