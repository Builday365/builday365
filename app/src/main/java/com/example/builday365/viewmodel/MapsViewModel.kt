package com.example.builday365.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class MapsViewModel : ViewModel() {
    private val mFocused = MutableLiveData<Boolean>()
    private val mLastLocation = MutableLiveData<LatLng>()
    private val mHomeLocation = MutableLiveData<LatLng>()
    fun setFocused(isFocused: Boolean) {
        mFocused.value = isFocused
    }

    fun setLastLocation(lastLocation: LatLng) {
        mLastLocation.value = lastLocation
    }

    fun setHomeLocation(homeLocation: LatLng) {
        mHomeLocation.value = homeLocation
    }

    val focused: LiveData<Boolean>
        get() = mFocused
    val lastLocation: LiveData<LatLng>
        get() = mLastLocation
    val homeLocation: LiveData<LatLng>
        get() = mHomeLocation

    fun onFabClick() {
        mFocused.value = true
    }

    companion object {
        private const val TAG = "MapsViewModel"
    }
}