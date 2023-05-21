package com.example.builday365.Model;
import com.example.builday365.Model.Timeline.TimeLine;

public class DatabaseManager {

    private static DatabaseManager instance;

    public TimeLine timeLine;
    private DatabaseManager() {
        timeLine = new TimeLine();
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }


}
