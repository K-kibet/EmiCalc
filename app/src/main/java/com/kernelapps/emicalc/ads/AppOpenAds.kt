package com.kernelapps.emicalc.ads

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback

class AppOpenAds(val myApplication: Application): LifecycleObserver, ActivityLifecycleCallbacks {

    private val LOG_TAG = "AppOpenManager"
    private var appOpenAd: AppOpenAd? = null
    private var loadCallback: AppOpenAdLoadCallback? = null
    private var currentActivity: Activity? = null
    private var isShowingAd = false

    fun AppOpenShow() {
        if (!isShowingAd && isAdAvailable()) {
            Log.d(LOG_TAG, "Will show ad.")
            val fullScreenContentCallback: FullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        // Set the reference to null so isAdAvailable() returns false.
                        appOpenAd = null
                        isShowingAd = false
                        AppOpenGetAds()
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {}
                    override fun onAdShowedFullScreenContent() {
                        isShowingAd = true
                    }
                }
            appOpenAd!!.fullScreenContentCallback = fullScreenContentCallback
            appOpenAd!!.show(currentActivity!!)
        } else {
            Log.d(LOG_TAG, "Can not show ad.")
            AppOpenGetAds()
        }
    }
    init {

        this.myApplication!!.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    fun AppOpenGetAds() {
        if (isAdAvailable()) {
            return
        }
        loadCallback = object : AppOpenAdLoadCallback() {
            override fun onAdLoaded(ad: AppOpenAd) {
                appOpenAd = ad
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {}
        }
        val request = getAdRequest()
        currentActivity?.let {
            AppOpenAd.load(
                it, AdsConstant.admobAppOpenId,
                request, loadCallback!!
            )
        }
    }

    private fun getAdRequest(): AdRequest {
        return AdRequest.Builder().build()
    }

    fun isAdAvailable(): Boolean {
        return appOpenAd != null
    }

    override
    fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override
    fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }
    override
    fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }
    override
    fun onActivityStopped(activity: Activity) {}
    override
    fun onActivityPaused(activity: Activity) {}
    override
    fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}
    override
    fun onActivityDestroyed(activity: Activity) {
        currentActivity = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        AppOpenShow()
        Log.d(LOG_TAG, "onStart")
    }

}