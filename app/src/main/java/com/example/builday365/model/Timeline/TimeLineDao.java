package com.example.builday365.model.Timeline;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TimeLineDao {

    @Query("SELECT * FROM TimeLine")

    TimeLine getAll();

    @Insert
    void insert(TimeLine timeLine);

    @Update
    void update(TimeLine timeLine);

    @Delete
    void delete(TimeLine timeLine);

}
