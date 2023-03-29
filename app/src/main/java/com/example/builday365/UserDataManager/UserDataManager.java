package com.example.builday365.UserDataManager;
import com.example.builday365.UserDataManager.UserData.UserData;

public class UserDataManager {

    private static UserDataManager instance;
    private UserData ud;

    public static synchronized UserDataManager getInstance() {
        if (instance == null) {
            instance = new UserDataManager();
        }
        return instance;
    }

    private UserDataManager() {
        ud = new UserData();
    }

}
