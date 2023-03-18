package com.example.builday365;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
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
        });    }

    public void loadAd(Context context, ViewGroup adContainer) {



        Log.d(TAG, "loadAd is called");

        mAdView = new AdView(context);
        mAdView.setAdUnitId(context.getString(R.string.admob_ad_id));
        mAdView.setAdSize(AdSize.BANNER);
        adContainer.addView(mAdView);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
