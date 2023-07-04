package com.example.builday365.view

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.builday365.R
import com.example.builday365.view.MapsFragment.MapsFragmentListener
import com.example.builday365.view.UiFragment.UiFragmentListener
import com.example.builday365.viewmodel.AdMobManager
import com.example.builday365.viewmodel.MainViewModel

class MainActivity : AppCompatActivity(), MapsFragmentListener, UiFragmentListener {
    private var mAdMobManager: AdMobManager? = null
    private var mapsFragment: MapsFragment? = null
    private var uiFragment: UiFragment? = null
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // AdMob Manager
        mAdMobManager = AdMobManager(this)
        mAdMobManager!!.loadAd(this, findViewById(R.id.adView))
        val mv = MainViewModel(application)
        mapsFragment = MapsFragment()
        uiFragment = UiFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.map, mapsFragment!!)
            .replace(R.id.ui, uiFragment!!)
            .commit()
    }

    override fun onInputASent(input: CharSequence) {
        if (input === "true") {
            uiFragment!!.map_cur_location_click_listener()
        }
    }

    override fun onInputBSent(input: CharSequence?) {}

    companion object {
        private const val TAG = "MainActivity"
    }
}