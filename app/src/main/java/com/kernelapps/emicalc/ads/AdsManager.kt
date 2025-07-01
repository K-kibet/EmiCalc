package com.kernelapps.emicalc.ads

import android.app.Activity
import android.util.DisplayMetrics
import android.view.Display
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.kernelapps.emicalc.R
import com.kernelapps.emicalc.ads.AdsConstant.displayCounter
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView


object AdsManager {

   private var counter=0
    private var mInterstitialAd: InterstitialAd? = null
   fun loadBanner(activity: Activity, linearLayout: LinearLayout){
       val adView = AdView(activity)
       adView.setAdSize(
           getAdSize(
               activity,
               linearLayout
           )
       )
       adView.adUnitId = AdsConstant.admobBannerId
       linearLayout.visibility= View.VISIBLE
       linearLayout.addView(adView)

       val adRequest = AdRequest.Builder().build()
       adView.loadAd(adRequest)
   }

    fun  loadInterstitial(activity: Activity){
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(activity, AdsConstant.admobInterstitialId, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null

            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        })
    }

    fun showInterstitial(activity: Activity, adsCallback: AdsCallBack){
        counter++
        if(counter >= displayCounter){
            if(mInterstitialAd !=null) {
                mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        adsCallback.onClosed()
                        loadInterstitial(activity)
                        counter = 0

                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        adsCallback.onClosed()
                        loadInterstitial(activity)
                    }
                }
                mInterstitialAd!!.show(activity)
            }else{
                adsCallback.onClosed()
                loadInterstitial(activity)
            }
        }else{
            adsCallback.onClosed()
        }
    }

    private fun getAdSize(activity: Activity,adContainerView:LinearLayout): AdSize {
        // Determine the screen width (less decorations) to use for the ad width.
        val display: Display = activity.windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val density = outMetrics.density
        var adWidthPixels: Float = adContainerView.width.toFloat()

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0f) {
            adWidthPixels = outMetrics.widthPixels.toFloat()
        }
        val adWidth = (adWidthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth)
    }

    private lateinit var nativeAdHistory:NativeAd
    fun loadNative(activity: Activity,frameLayout: FrameLayout){
            val builder =
                AdLoader.Builder(activity, AdsConstant.admobNativeId)

            builder.forNativeAd { nativeAd: NativeAd ->

                nativeAdHistory = nativeAd
                val adView = activity.layoutInflater
                    .inflate(
                        R.layout.ad_unified_small,
                        frameLayout,
                        false
                    ) as NativeAdView
                populateCustomNativeAdView(
                    nativeAd,
                    adView
                )
                frameLayout.removeAllViews()
                frameLayout.addView(adView)
            }

            val videoOptions = VideoOptions.Builder().setStartMuted(true).build()

            val adOptions = NativeAdOptions.Builder().setVideoOptions(videoOptions).build()

            builder.withNativeAdOptions(adOptions)

            val adLoader = builder
                .withAdListener(
                    object : AdListener() {
                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
//
                        }
                    })
                .build()

            adLoader.loadAd(AdRequest.Builder().build())
    }


    private fun populateCustomNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        adView.priceView = adView.findViewById(R.id.ad_price)
        adView.starRatingView = adView.findViewById(R.id.ad_stars)
        adView.storeView = adView.findViewById(R.id.ad_store)
        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)
        (adView.headlineView as TextView?)!!.text = nativeAd.headline
        if (nativeAd.body == null) {
            adView.bodyView!!.visibility = View.INVISIBLE
        } else {
            adView.bodyView!!.visibility = View.VISIBLE
            (adView.bodyView as TextView?)!!.text = nativeAd.body
        }
        if (nativeAd.callToAction == null) {
            adView.callToActionView!!.visibility = View.INVISIBLE
        } else {
            adView.callToActionView!!.visibility = View.VISIBLE
            (adView.callToActionView as AppCompatButton?)!!.text = nativeAd.callToAction
        }
        if (nativeAd.icon == null) {
            adView.iconView!!.visibility = View.GONE
        } else {
            (adView.iconView as ImageView?)!!.setImageDrawable(
                nativeAd.icon!!.drawable
            )
            adView.iconView!!.visibility = View.VISIBLE
        }
        if (nativeAd.starRating == null) {
            adView.starRatingView!!.visibility = View.INVISIBLE
        } else {
            (adView.starRatingView as RatingBar?)?.rating = nativeAd.starRating!!.toFloat()
            adView.starRatingView!!.visibility = View.VISIBLE
        }
        if (nativeAd.advertiser == null) {
            adView.advertiserView!!.visibility = View.INVISIBLE
        } else {
            (adView.advertiserView as TextView?)!!.text = nativeAd.advertiser
            adView.advertiserView!!.visibility = View.VISIBLE
        }
        adView.setNativeAd(nativeAd)
    }

    interface AdsCallBack{
        fun onClosed()
    }
}