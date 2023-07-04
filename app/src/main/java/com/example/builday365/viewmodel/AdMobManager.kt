package com.example.builday365.viewmodel

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import com.example.builday365.R
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds

class AdMobManager(context: Context?) {
    private var mAdView: AdView? = null

    init {
        MobileAds.initialize(context!!) { Log.d(TAG, "onInitializationComplete is called") }
    }

    fun loadAd(context: Context, adContainer: ViewGroup) {
        Log.d(TAG, "loadAd is called")
        mAdView = AdView(context)
        mAdView!!.adUnitId = context.getString(R.string.admob_ad_id)
        mAdView!!.setAdSize(AdSize.SMART_BANNER)
        adContainer.addView(mAdView)
        val adRequest = AdRequest.Builder().build()
        try {
            mAdView!!.loadAd(adRequest)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load ad: " + e.message)
        }
        mAdView!!.adListener = object : AdListener() {
            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.e(
                    TAG,
                    "Ad failed to load. Error code: " + adError.code + ", Error message: " + adError.message
                )
            }

            override fun onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        }
    }

    companion object {
        private const val TAG = "AdMobManager"
    }
}