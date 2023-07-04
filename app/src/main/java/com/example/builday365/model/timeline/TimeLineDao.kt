package com.example.builday365.model.timeline

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TimeLineDao {
    @get:Query("SELECT * FROM TimeLine")
    val all: TimeLine?

    @Insert
    fun insert(timeLine: TimeLine?)

    @Update
    fun update(timeLine: TimeLine?)

    @Delete
    fun delete(timeLine: TimeLine?)
}