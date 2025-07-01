package com.kernelapps.emicalc.FIles;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.kernelapps.emicalc.Constants;
import com.kernelapps.emicalc.R;
import com.kernelapps.emicalc.StartActivity;
import com.kernelapps.emicalc.ads.AdsConstant;
import com.kernelapps.emicalc.ads.AdsManager;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;

public class splash extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(512, 512);
        getWindow().setStatusBarColor(0);
        setContentView(R.layout.splash_activity);
        AdsManager.INSTANCE.loadInterstitial(this);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), 0);
        this.sharedPreferences = sharedPreferences;
        if (!sharedPreferences.getString(Constants.CURRENCY, "null").equals("null")) {
            Constants.CURRENCY_STORED = this.sharedPreferences.getString(Constants.CURRENCY, null);
        }
        AppOpenAd.load(this, AdsConstant.admobAppOpenId, new AdRequest.Builder().build(), new AppOpenAd.AppOpenAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent2 = new Intent(splash.this, StartActivity.class);
                        splash.this.startActivity(intent2);
                        splash.this.finish();
                    }
                }, 1500L);
            }

            @Override
            public void onAdLoaded(@NonNull AppOpenAd appOpenAd) {

                appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        Intent intent2 = new Intent(splash.this, StartActivity.class);
                        splash.this.startActivity(intent2);
                        splash.this.finish();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        Intent intent2 = new Intent(splash.this, StartActivity.class);
                        splash.this.startActivity(intent2);
                        splash.this.finish();
                    }
                });
                appOpenAd.show(splash.this);
            }
        });


    }
}
