package com.moutamid.rurovision.Utils;

import static androidx.lifecycle.Lifecycle.Event.ON_START;
import static com.moutamid.rurovision.activity.MyApplication.isShowingAd;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.moutamid.rurovision.activity.MyApplication;

public class OpenAppAds implements LifecycleObserver, Application.ActivityLifecycleCallbacks {

    private final MyApplication myApplication;
    private AppOpenAd appOpenAd = null;
    private AppOpenAd.AppOpenAdLoadCallback loadCallback;
    private Activity currentActivity;

    public OpenAppAds(MyApplication myApplication) {
        this.myApplication = myApplication;
        this.myApplication.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(ON_START)
    public void onStart() {
        showAdIfAvailable();
        Logger.AppLog(MyApplication.ADMOB_TAG, "Open AD ==> " + " onStart");
    }

    public void showAdIfAvailable() {

        if (UserHelper.getIsAdEnable() != 1) {
            return;
        }

        if (UserHelper.getUserInSplashIntro()) {
            return;
        }

        if (!isShowingAd && !UserHelper.isShowingFullScreenAd && isAdAvailable() && UserHelper.getShowAppOpen() == 1) {
            Logger.AppLog(MyApplication.ADMOB_TAG, "Open AD ==> " + " Will show Ad");
            FullScreenContentCallback fullScreenContentCallback =
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Set the reference to null so isAdAvailable() returns false.
                            appOpenAd = null;
                            isShowingAd = false;
                            fetchAd();
                            Logger.AppLog(MyApplication.ADMOB_TAG, "Open AD ===> The ad was dismissed.");
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            isShowingAd = true;
                            Logger.AppLog(MyApplication.ADMOB_TAG, "Open AD ===> The ad failed to show.");
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            isShowingAd = true;
                            Logger.AppLog(MyApplication.ADMOB_TAG, "Open AD ===> The ad was shown.");
                        }
                    };

            appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
            appOpenAd.show(currentActivity);
            isShowingAd = true;

        } else {
            if (UserHelper.getShowAppOpen() == 1) {
                Logger.AppLog(MyApplication.ADMOB_TAG, "Open App ID ==> " + " Enable");
                fetchAd();
            } else {
                Logger.AppLog(MyApplication.ADMOB_TAG, "Open App ID ==> " + " Disabled");
            }
        }
    }


    public void fetchAd() {


        if (UserHelper.getIsAdEnable() == 0) {
            return;
        }

        if (UserHelper.getShowAppOpen() == 0) {
            return;
        }

        if (isAdAvailable()) {
            return;
        }
        if (UserHelper.getUserInSplashIntro()) {
            return;
        }
        loadCallback =
                new AppOpenAd.AppOpenAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull AppOpenAd ad) {
                        appOpenAd = ad;
                        Logger.AppLog(MyApplication.ADMOB_TAG, "Open App ID Load == > onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Logger.AppLog(MyApplication.ADMOB_TAG, "Open App ID Fail Load == > " + loadAdError.getMessage());
                    }
                };
        AdRequest request = getAdRequest();
        String adUnitId = "";
        adUnitId = UserHelper.getAppOpenAd();

        if (TextUtils.isEmpty(adUnitId)) {
            return;
        }
        Logger.AppLog(MyApplication.ADMOB_TAG, "Open App ID ==> " + adUnitId);
        AppOpenAd.load(myApplication, adUnitId, request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
    }

    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }


    public boolean isAdAvailable() {
        return appOpenAd != null;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        currentActivity = null;
    }
}