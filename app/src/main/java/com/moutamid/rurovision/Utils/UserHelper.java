package com.moutamid.rurovision.Utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

public class UserHelper {
    public static final String MY_PREFERANCE = "my_preferance";
    public static final String REPLY_MSG = "reply_msg";
    public static final String PERMISSION_GRANT = "permission_grant";

    public static final String AD_TYPE_ADMOB = "admob";
    public static final String AD_TYPE_ADX = "adx";
    public static final String IS_AD_ENABLE = "is_ad_enable";
    public static final String AD_TYPE = "ad_type";
    public static final String BANNER_APP = "banner_ad";
    public static final String SHOW_APP_OPEN = "show_app_open";
    public static final String SHOW_APP_OPEN_SPLASH = "show_app_open_splash";
    public static final String SHOW_BANNER = "show_banner";
    public static final String SHOW_INTERSTITIAL = "show_interstitial";
    public static final String SHOW_NATIVE = "show_native";
    public static final String APP_OPEN_AD = "app_open_ad";
    public static final String INTERSTITIAL_AD = "interstitial_ad";
    public static final String NATIVE_ADD_1 = "native_ad_1";
    public static final String USER_IN_SPLASH_INTRO = "USER_IN_SPLASH_INTRO";
    private static final String INTERSTITIAL_ADS_COUNT = "interstitial_ads_count";
    private static final String INTERSTITIAL_ADS_CLICK = "interstitial_ads_click";
    private static final String TRANSFER_LINK = "transfer_link";
    private static final String ADS_PER_SESSION = "ads_per_session";
    private static final String EXIT_AD_ENABLE = "exit_ad_enable";
    public static boolean isShowingFullScreenAd = false;
    public static int ads_per_session = 0;
    public static String splash_screen_wait_count = "splash_screen_wait_count";
    public static String rateApp = "rateApp";
    public static String review_count = "review_count";



    public static boolean isNetworkConnected(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public static String getReplyMsg() {
        return SharedPrefsHelper.getInstance().getString(REPLY_MSG, "");
    }

    public static void setReplyMsg(String key) {
        SharedPrefsHelper.getInstance().setString(REPLY_MSG, key);
    }

    public static String rateAppDone = "rateAppDone";
    public static boolean isRateDone() {
        return SharedPrefsHelper.getInstance().getBoolean(rateAppDone, false);
    }

    public static void setRateDone(boolean rate) {
        SharedPrefsHelper.getInstance().setBoolean(rateAppDone, rate);
    }

    public static boolean isShowRate() {
        return SharedPrefsHelper.getInstance().getBoolean(rateApp, false);
    }

    public static void setShowRate(boolean rate) {
        SharedPrefsHelper.getInstance().setBoolean(rateApp, rate);
    }

    public static int getReviewCount() {
        int type = SharedPrefsHelper.getInstance().getInt(review_count, 5);
        return type;
    }
    public static void setReviewCount(int str) {
        SharedPrefsHelper.getInstance().setInt(review_count, str);
    }
    public static void updateReviewCount() {
        int lastCount = getReviewCount();
        SharedPrefsHelper.getInstance().setInt(review_count, lastCount + 1);
    }

    public static String review_popup_count = "review_popup_count";

    public static int getReviewPopupCount() {
        int type = SharedPrefsHelper.getInstance().getInt(review_popup_count, 1);
        return type;
    }

    public static void setReviewPopupCount(int str) {
        SharedPrefsHelper.getInstance().setInt(review_popup_count, str);
    }

    public static int getInterstitialAdsCount() {
        int mData;
        mData = SharedPrefsHelper.getInstance().getInt(INTERSTITIAL_ADS_COUNT, 0);
        return mData;
    }

    public static void setInterstitialAdsCount(int i) {
        SharedPrefsHelper.getInstance().setInt(INTERSTITIAL_ADS_COUNT, i);
    }

    public static int getIsAdEnable() {
        return SharedPrefsHelper.getInstance().getInt(IS_AD_ENABLE, 0);
    }

    public static void setIsAdEnable(int key) {
        SharedPrefsHelper.getInstance().setInt(IS_AD_ENABLE, key);
    }

    public static String getAdType() {
        return SharedPrefsHelper.getInstance().getString(AD_TYPE, "");
    }

    public static void setAdType(String key) {
        SharedPrefsHelper.getInstance().setString(AD_TYPE, key);
    }


    public static int getInterstitialAdsClick() {
        return SharedPrefsHelper.getInstance().getInt(INTERSTITIAL_ADS_CLICK, 0);
    }

    public static void setInterstitialAdsClick(int i) {
        SharedPrefsHelper.getInstance().setInt(INTERSTITIAL_ADS_CLICK, i);
    }


    //            review

    public static int getAdsPerSession() {
        return SharedPrefsHelper.getInstance().getInt(ADS_PER_SESSION, 0);
    }

    public static void setAdsPerSession(int i) {
        SharedPrefsHelper.getInstance().setInt(ADS_PER_SESSION, i);
    }

    public static String getTransferLink() {
        return SharedPrefsHelper.getInstance().getString(TRANSFER_LINK, "");
    }

    public static void setTransferLink(String i) {
        SharedPrefsHelper.getInstance().setString(TRANSFER_LINK, i);
    }

    public static int getShowAppOpen() {
        int i = SharedPrefsHelper.getInstance().getInt(SHOW_APP_OPEN, 0);
        return i;
    }

    public static void setShowAppOpen(int key) {
        SharedPrefsHelper.getInstance().setInt(SHOW_APP_OPEN, key);
    }

    public static int getOpenAdsSplash() {
        return SharedPrefsHelper.getInstance().getInt(SHOW_APP_OPEN_SPLASH, 0);
    }

    public static void setOpenAdsSplash(int key) {
        SharedPrefsHelper.getInstance().setInt(SHOW_APP_OPEN_SPLASH, key);
    }

    public static int getShowBanner() {
        return SharedPrefsHelper.getInstance().getInt(SHOW_BANNER, 0);
    }

    public static void setShowBanner(int key) {
        SharedPrefsHelper.getInstance().setInt(SHOW_BANNER, key);
    }

    public static int getShowInterstitial() {
        return SharedPrefsHelper.getInstance().getInt(SHOW_INTERSTITIAL, 0);
    }

    public static void setShowInterstitial(int key) {
        SharedPrefsHelper.getInstance().setInt(SHOW_INTERSTITIAL, key);
    }

    public static int getExitAdEnable() {
        return SharedPrefsHelper.getInstance().getInt(EXIT_AD_ENABLE, 0);
    }

    public static void setExitAdEnable(int key) {
        SharedPrefsHelper.getInstance().setInt(EXIT_AD_ENABLE, key);
    }

    public static int getShowNative() {
        return SharedPrefsHelper.getInstance().getInt(SHOW_NATIVE, 0);
    }

    public static void setShowNative(int key) {
        SharedPrefsHelper.getInstance().setInt(SHOW_NATIVE, key);
    }

    public static String getBannerAd() {
        return SharedPrefsHelper.getInstance().getString(BANNER_APP, "");
    }

    public static void setBannerAd(String key) {
        SharedPrefsHelper.getInstance().setString(BANNER_APP, key);
    }

    public static String getAppOpenAd() {
        return SharedPrefsHelper.getInstance().getString(APP_OPEN_AD, "");
    }

    public static void setAppOpenAd(String key) {
        SharedPrefsHelper.getInstance().setString(APP_OPEN_AD, key);
    }

    public static String getInterstitialAd() {
        return SharedPrefsHelper.getInstance().getString(INTERSTITIAL_AD, "");
    }

    public static void setInterstitialAd(String key) {
        SharedPrefsHelper.getInstance().setString(INTERSTITIAL_AD, key);
    }

    public static String getNativeAd() {
        return SharedPrefsHelper.getInstance().getString(NATIVE_ADD_1, "");
    }

    public static void setNativeAd(String key) {
        SharedPrefsHelper.getInstance().setString(NATIVE_ADD_1, key);
    }

    public static boolean getUserInSplashIntro() {
        return SharedPrefsHelper.getInstance().getBoolean(USER_IN_SPLASH_INTRO, false);
    }

    public static void setUserInSplashIntro(boolean rate) {
        SharedPrefsHelper.getInstance().setBoolean(USER_IN_SPLASH_INTRO, rate);
    }

    public static int getSplashScreenWaitCount() {
        int type = SharedPrefsHelper.getInstance().getInt(splash_screen_wait_count, 10);
        return type;
    }

    public static void setSplashScreenWaitCount(int str) {
        SharedPrefsHelper.getInstance().setInt(splash_screen_wait_count, str);
    }


    ///

    public static boolean isPermissionGrant() {
        return SharedPrefsHelper.getInstance().getBoolean(PERMISSION_GRANT, false);
    }
    public static void setPermissionGrant(boolean key) {
        SharedPrefsHelper.getInstance().setBoolean(PERMISSION_GRANT, key);
    }



}
