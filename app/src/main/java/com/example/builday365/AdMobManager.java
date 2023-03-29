package com.example.builday365;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class AdMobManager {
    private static final String TAG = "AdMobManager";

    private AdView mAdView;

    public AdMobManager(Context context) {
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.d(TAG, "onInitializationComplete is called");
            }
        });
    }

    public void loadAd(Context context, ViewGroup adContainer) {
        Log.d(TAG, "loadAd is called");

        mAdView = new AdView(context);
        mAdView.setAdUnitId(context.getString(R.string.admob_ad_id));
        mAdView.setAdSize(AdSize.SMART_BANNER);
        adContainer.addView(mAdView);

        AdRequest adRequest = new AdRequest.Builder().build();
        try {
            mAdView.loadAd(adRequest);
        } catch (Exception e) {
            Log.e(TAG, "Failed to load ad: " + e.getMessage());
        }

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                Log.e(TAG, "Ad failed to load. Error code: " + adError.getCode() + ", Error message: " + adError.getMessage());
            }

            @Override
            public void onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        });
    }
}
