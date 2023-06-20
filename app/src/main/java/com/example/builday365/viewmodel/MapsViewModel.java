package com.example.builday365.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

import android.util.Log;

public class MapsViewModel extends ViewModel {
    private static final String TAG = "MapsViewModel";

    private MutableLiveData<Boolean> mFocused = new MutableLiveData<Boolean>();
    private MutableLiveData<LatLng> mLastLocation = new MutableLiveData<>();
    private MutableLiveData<LatLng> mHomeLocation = new MutableLiveData<>();

    public void setFocused(Boolean isFocused) {
        mFocused.setValue(isFocused);
    }

    public void setLastLocation(LatLng lastLocation) {
        this.mLastLocation.setValue(lastLocation);
    }

    public void setHomeLocation(LatLng homeLocation) {
        this.mHomeLocation.setValue(homeLocation);
    }

    public LiveData<Boolean> getFocused() {
        return mFocused;
    }

    public LiveData<LatLng> getLastLocation() {
        return mLastLocation;
    }

    public LiveData<LatLng> getHomeLocation() {
        return mHomeLocation;
    }

    public void onFabClick() {
        mFocused.setValue(true);
    }
}
