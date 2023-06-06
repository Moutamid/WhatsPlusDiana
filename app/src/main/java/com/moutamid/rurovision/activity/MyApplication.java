package com.moutamid.rurovision.activity;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.moutamid.rurovision.R;
import com.moutamid.rurovision.Utils.UserHelper;
import com.moutamid.rurovision.Utils.listener.ListnerOpenAppAds;
import com.moutamid.rurovision.Utils.notifier.EventNotifier;
import com.moutamid.rurovision.Utils.notifier.EventState;
import com.moutamid.rurovision.Utils.notifier.NotifierFactory;
import com.facebook.ads.Ad;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerAdView;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MyApplication extends Application {
    public static final String SHOW_NEVER = "show_never";
    public static final String SHOW_LATER = "show_later";
    public static final String ADMOB_TAG = "Ads:";
    public static AppOpenAd appOpenAd;
    public static Dialog loadAdsDialog;
    public static boolean isShowingAd = false;
    private static MyApplication app;
    private final List<String> mNativeAdsId = new ArrayList<>();
    //InterstitialAd
    public InterstitialAd mInterstitialAd;
    public AdManagerInterstitialAd mAdManagerInterstitialAd;
    //BannerAd
    public boolean isAdNativeAdEnable = false;
    private AdView admobManagerAdView;
    private AdManagerAdView adXManagerAdView;
    //NativeAd
    private List<NativeAd> mNativeAdsGHome = new ArrayList<>();

    public static MyApplication getInstance() {
        return app;
    }

    public static boolean isReviewOn() {
        return !UserHelper.isShowRate();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        List<String> testDeviceIds = Arrays.asList("");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);

        MobileAds.initialize(this, initializationStatus -> {
        });
    }

    public boolean showRatingDialog(Activity act, RateDialog.Builder.RatingDialogFormListener listener, final View.OnClickListener dismissListner) {
        SharedPreferences sharedpreferences = getSharedPreferences(RateDialog.MyPrefs, Context.MODE_PRIVATE);

        if (sharedpreferences.getBoolean(SHOW_NEVER, false)) {
            return false;
        } else {
            if (!act.isDestroyed()) {
                final RateDialog ratingDialog = new RateDialog.Builder(act)
                        .icon(getResources().getDrawable(R.mipmap.ic_launcher)).positiveButtonTextColor(R.color.black)
                        .ratingBarColor(R.color.colorPrimary)
                        .playstoreUrl("https://play.google.com/store/apps/details?id=" + act.getPackageName())
                        .onRatingBarFormSumbit(listener)
                        .build();
                ratingDialog.setOnCancelListener(dialog -> dismissListner.onClick(null));
                ratingDialog.show();
            }
            return true;
        }
    }

    //Native Ads
    public List<NativeAd> getGNativeHome() {
        return mNativeAdsGHome;
    }


    // Load Native Ads
    public void loadAdmobNativeAd(int i, Activity activity) {
        isAdNativeAdEnable = true;

        if (UserHelper.getIsAdEnable() != 1) {
            return;
        }

        if (UserHelper.getShowNative() != 1) {
            isAdNativeAdEnable = false;
            return;
        }

        if (isAdNativeAdEnable && UserHelper.isNetworkConnected(activity)) {
            loadGNativeIntermediate(i);
        }
    }

    public void loadGNativeIntermediate(int adCount) {
        String adUnitId;
        if (adCount == 0) {
            mNativeAdsGHome = new ArrayList<>();
            mNativeAdsId.clear();
            mNativeAdsId.add(UserHelper.getNativeAd());
        }
        AdLoader.Builder builder;

        adUnitId = mNativeAdsId.get(adCount);

        if (adUnitId == null) {
            return;
        }
        if (TextUtils.isEmpty(adUnitId)) {
            return;
        }
        builder = new AdLoader.Builder(this, adUnitId);
        Log.e("Ads :", "NativeAd adUnitId:  " + adUnitId);

        int native_ads_count = 1;
        builder.forNativeAd(nativeAd -> {
            mNativeAdsGHome.add(nativeAd);
            int nextConunt = adCount + 1;
            if (nextConunt < native_ads_count) {
                Log.e("Ads ", "NativeAd nextConunt: " + nextConunt);
                loadGNativeIntermediate(nextConunt);
            }
            if (nextConunt == native_ads_count) {
                Log.e("Ads ", "NativeAd " + nextConunt + ":Last");
                Log.e("NativeAds: ", "last == ");
                EventNotifier notifier = NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_AD_STATUS);
                notifier.eventNotify(EventState.EVENT_AD_LOADED_NATIVE, null);
            }
        });

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(true)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);
        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                Log.e("Ads :", "NativeAd onAdFailedToLoad: " + adError.getMessage());
            }
        }).build();

        if (UserHelper.getAdType().equals(UserHelper.AD_TYPE_ADMOB)) {
            Log.e("Ads: ", "AdMob nativeAd Load");
            adLoader.loadAd(new AdRequest.Builder().build());
        } else if (UserHelper.getAdType().equals(UserHelper.AD_TYPE_ADX)) {
            Log.e("Ads: ", "Adx nativeAd Load");
            adLoader.loadAd(new AdManagerAdRequest.Builder().build());
        }
    }

    public void loadNativeAds(FrameLayout fl_adplaceholder, Activity activity, int gridSize) {
        NativeAdView adView;
        if (UserHelper.getIsAdEnable() == 1) {
            if (MyApplication.getInstance().getGNativeHome() != null && MyApplication.getInstance().getGNativeHome().size() > 0 && MyApplication.getInstance().getGNativeHome().get(0) != null) {
                NativeAd nativeAd = MyApplication.getInstance().getGNativeHome().get(0);
                if (gridSize == 1) {
                    adView = (NativeAdView) LayoutInflater.from(this).inflate(R.layout.ads_native_list, null);
                    populateUnifiedNativeAdListView(nativeAd, adView);
                } else {
                    adView = (NativeAdView) LayoutInflater.from(this).inflate(R.layout.ads_native_list, null);
                    populateUnifiedNativeAdListView(nativeAd, adView);
                }

                fl_adplaceholder.removeAllViews();
                fl_adplaceholder.addView(adView);
                fl_adplaceholder.setVisibility(View.VISIBLE);
            } else {
                fl_adplaceholder.setVisibility(View.GONE);
            }
        }
    }

    public void loadNativeFullAds(FrameLayout fl_adplaceholder, Activity activity, int gridSize) {
        NativeAdView adView;
        if (UserHelper.getIsAdEnable() == 1) {
            if (MyApplication.getInstance().getGNativeHome() != null && MyApplication.getInstance().getGNativeHome().size() > 0 && MyApplication.getInstance().getGNativeHome().get(0) != null) {
                NativeAd nativeAd = MyApplication.getInstance().getGNativeHome().get(0);

                adView = (NativeAdView) LayoutInflater.from(this).inflate(R.layout.ads_native_grid, null);
                populateUnifiedNativeAdGridView(nativeAd, adView);
                fl_adplaceholder.removeAllViews();
                fl_adplaceholder.addView(adView);
                fl_adplaceholder.setVisibility(View.VISIBLE);
            } else {
                fl_adplaceholder.setVisibility(View.GONE);
            }
        }
    }

    private void populateUnifiedNativeAdListView(NativeAd nativeAd, NativeAdView adView) {
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        try {
            ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((TextView) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            Objects.requireNonNull(adView.getPriceView()).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(adView.getPriceView()).setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            Objects.requireNonNull(adView.getStoreView()).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(adView.getStoreView()).setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            Objects.requireNonNull(adView.getStarRatingView()).setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) Objects.requireNonNull(adView.getStarRatingView()))
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            Objects.requireNonNull(adView.getAdvertiserView()).setVisibility(View.INVISIBLE);
        } else {
            ((TextView) Objects.requireNonNull(adView.getAdvertiserView())).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        adView.getStoreView().setVisibility(View.GONE);
        adView.getPriceView().setVisibility(View.GONE);

        adView.setNativeAd(nativeAd);

        VideoController vc = Objects.requireNonNull(nativeAd.getMediaContent()).getVideoController();

        if (vc.hasVideoContent()) {
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    super.onVideoEnd();
                }
            });
        }
    }

    private void populateUnifiedNativeAdGridView(NativeAd nativeAd, NativeAdView adView) {
        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);

        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        try {
            ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((TextView) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            Objects.requireNonNull(adView.getPriceView()).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(adView.getPriceView()).setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            Objects.requireNonNull(adView.getStoreView()).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(adView.getStoreView()).setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            Objects.requireNonNull(adView.getStarRatingView()).setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) Objects.requireNonNull(adView.getStarRatingView()))
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            Objects.requireNonNull(adView.getAdvertiserView()).setVisibility(View.INVISIBLE);
        } else {
            ((TextView) Objects.requireNonNull(adView.getAdvertiserView())).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        adView.getStoreView().setVisibility(View.GONE);
        adView.getPriceView().setVisibility(View.GONE);

        adView.setNativeAd(nativeAd);

        VideoController vc = Objects.requireNonNull(nativeAd.getMediaContent()).getVideoController();

        if (vc.hasVideoContent()) {
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    super.onVideoEnd();
                }
            });
        }
    }

    // Load Banner Ads
    public void loadBanner(RelativeLayout adContainerView, Activity activity) {

        if (!UserHelper.isNetworkConnected(activity)) {
            return;
        }
        if (UserHelper.getIsAdEnable() != 1) {
            return;
        }
        if (UserHelper.getShowBanner() != 1) {
            Log.e(MyApplication.ADMOB_TAG, "Banner Ads ===> Disable");
            return;
        }

        if (UserHelper.getAdType().equalsIgnoreCase(UserHelper.AD_TYPE_ADMOB)) {
            loadAdMobBanner(adContainerView, activity);
        } else if (UserHelper.getAdType().equalsIgnoreCase(UserHelper.AD_TYPE_ADX)) {
            loadAdxBanner(adContainerView, activity);
        }
    }

    private void loadAdMobBanner(RelativeLayout adContainerView, Activity activity) {
        if (UserHelper.getShowBanner() == 1) {
            String adUnitId = "";
            adUnitId = UserHelper.getBannerAd();

            Log.e(ADMOB_TAG, "BannerAd ID ===> " + adUnitId);

            if (TextUtils.isEmpty(adUnitId)) {
                return;
            }

            admobManagerAdView = new AdView(activity);
            admobManagerAdView.setAdUnitId(adUnitId);
            adContainerView.addView(admobManagerAdView);
            AdRequest adRequest = new AdRequest.Builder().build();
            AdSize adSize = getAdSize(activity);
            admobManagerAdView.setAdSize(adSize);
            admobManagerAdView.loadAd(adRequest);
            admobManagerAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    adContainerView.setVisibility(View.VISIBLE);
                    Log.e(ADMOB_TAG, "BannerAd ===> onAdLoaded");
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    Log.e(ADMOB_TAG, "BannerAd ===> onAdFailedToLoad " + loadAdError.getMessage());
                }
            });
        }
    }

    private void loadAdxBanner(RelativeLayout adContainerView, Activity activity) {
        if (UserHelper.getShowBanner() == 1) {
            String adUnitId = "";
            adUnitId = UserHelper.getBannerAd();


            Log.e(ADMOB_TAG, "BannerAd ID ===> " + adUnitId);

            if (TextUtils.isEmpty(adUnitId)) {
                return;
            }

            adXManagerAdView = new AdManagerAdView(activity);
            adXManagerAdView.setAdUnitId(adUnitId);
            adContainerView.addView(adXManagerAdView);
            AdRequest adRequest = new AdRequest.Builder().build();
            AdSize adSize = getAdSize(activity);
            adXManagerAdView.setAdSize(adSize);
            adXManagerAdView.loadAd(adRequest);
            adXManagerAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    adContainerView.setVisibility(View.VISIBLE);
                    Log.e(ADMOB_TAG, "BannerAd ===> onAdLoaded");
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    Log.e(ADMOB_TAG, "BannerAd ===> onAdFailedToLoad " + loadAdError.getMessage());
                }
            });
        }
    }

    private AdSize getAdSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    //Load InterstitialAds
    public void loadInterstitialAd(Activity activity) {

        if (!UserHelper.isNetworkConnected(activity)) {
            return;
        }
        if (UserHelper.getIsAdEnable() != 1) {
            return;
        }
        if (UserHelper.ads_per_session == UserHelper.getAdsPerSession()) {
            return;
        }

        if (UserHelper.getAdType().equalsIgnoreCase(UserHelper.AD_TYPE_ADMOB)) {
            loadAdmobInterstitialAd();
        } else if (UserHelper.getAdType().equalsIgnoreCase(UserHelper.AD_TYPE_ADX)) {
            loadAdxInterstitialAd();
        } else {
            return;
        }


    }

    private void loadAdmobInterstitialAd() {

        if (UserHelper.getShowInterstitial() == 1) {

            String adUnitId = "";
            adUnitId = UserHelper.getInterstitialAd();

            Log.e(ADMOB_TAG, "Admob InterstitialAd ===> " + adUnitId);
            if (TextUtils.isEmpty(adUnitId)) {
                return;
            }
            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(this, adUnitId, adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            mInterstitialAd = interstitialAd;
                            Log.e(ADMOB_TAG, "Admob InterstitialAd ===> onAdLoaded");
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            Log.e(ADMOB_TAG, "Admob InterstitialAd ===> " + loadAdError.getMessage());
                            mInterstitialAd = null;
                        }
                    });
        }
    }

    private void loadAdxInterstitialAd() {
        if (UserHelper.getShowInterstitial() == 1) {

            String adUnitId = "";
            adUnitId = UserHelper.getInterstitialAd();

            Log.e(ADMOB_TAG, "Adx InterstitialAd ===> " + adUnitId);
            if (TextUtils.isEmpty(adUnitId)) {
                return;
            }
            AdManagerAdRequest adManagerAdRequest = new AdManagerAdRequest.Builder().build();
            AdManagerInterstitialAd.load(this, adUnitId, adManagerAdRequest,
                    new AdManagerInterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull AdManagerInterstitialAd interstitialAd) {
                            mAdManagerInterstitialAd = interstitialAd;
                            Log.e(ADMOB_TAG, "Adx InterstitialAd ===>" + "onAdLoaded");
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            Log.e(ADMOB_TAG, "Adx InterstitialAd ===> " + loadAdError.getMessage());
                            mAdManagerInterstitialAd = null;
                        }
                    });
        }
    }

    //Load exit
    public void loadInterstitialAdExit(Activity activity, Intent intent, boolean isFinish) {

        if (!UserHelper.isNetworkConnected(activity)) {
            doNext(activity, intent, isFinish);
            return;
        }
        if (UserHelper.getIsAdEnable() != 1) {
            doNext(activity, intent, isFinish);
            return;
        }
        if (UserHelper.getExitAdEnable() != 1) {
            doNext(activity, intent, isFinish);
            return;
        }

        if (UserHelper.getAdType().equalsIgnoreCase(UserHelper.AD_TYPE_ADMOB)) {
            loadAdmobInterstitialAdExit(activity, intent, isFinish);
        } else if (UserHelper.getAdType().equalsIgnoreCase(UserHelper.AD_TYPE_ADX)) {
            loadAdxInterstitialAdExit(activity, intent, isFinish);
        } else {
            doNext(activity, intent, isFinish);
        }

    }

    private void loadAdmobInterstitialAdExit(Activity activity, Intent intent, boolean isFinish) {

        if (UserHelper.getShowInterstitial() == 1) {

            String adUnitId = "";
            adUnitId = UserHelper.getInterstitialAd();

            Log.e(ADMOB_TAG, "Admob InterstitialAd ===> " + adUnitId);
            if (TextUtils.isEmpty(adUnitId)) {
                return;
            }

            loadAdsDialog = new Dialog(activity);
            loadAdsDialog.setContentView(R.layout.layout_loading);
            loadAdsDialog.setCanceledOnTouchOutside(false);
            loadAdsDialog.setCancelable(false);
            loadAdsDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            loadAdsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loadAdsDialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
            loadAdsDialog.show();

            ((TextView) loadAdsDialog.findViewById(R.id.title)).setText("Loading Ads...");
            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(this, adUnitId, adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            mInterstitialAd = interstitialAd;
                            Log.e(ADMOB_TAG, "InterstitialAd ===> onAdLoaded");
                            if (loadAdsDialog != null && loadAdsDialog.isShowing()) {
                                loadAdsDialog.dismiss();
                            }
                            displayInterstitialAdsExit(activity, intent, isFinish);
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            Log.e(ADMOB_TAG, "InterstitialAd ===> " + loadAdError.getMessage());
                            mInterstitialAd = null;
                            if (loadAdsDialog != null && loadAdsDialog.isShowing()) {
                                loadAdsDialog.dismiss();
                            }
                            doNext(activity, intent, isFinish);
                        }
                    });
        } else {
            doNext(activity, intent, isFinish);
        }
    }

    private void loadAdxInterstitialAdExit(Activity activity, Intent intent, boolean isFinish) {
        if (UserHelper.getShowInterstitial() == 1) {

            String adUnitId = "";
            adUnitId = UserHelper.getInterstitialAd();

            Log.e(ADMOB_TAG, "Adx InterstitialAd ===> " + adUnitId);
            if (TextUtils.isEmpty(adUnitId)) {
                return;
            }

            loadAdsDialog = new Dialog(activity);
            loadAdsDialog.setContentView(R.layout.layout_loading);
            loadAdsDialog.setCanceledOnTouchOutside(false);
            loadAdsDialog.setCancelable(false);
            loadAdsDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            loadAdsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loadAdsDialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
            loadAdsDialog.show();
            ((TextView) loadAdsDialog.findViewById(R.id.title)).setText("Loading Ads...");

            AdManagerAdRequest adManagerAdRequest = new AdManagerAdRequest.Builder().build();
            AdManagerInterstitialAd.load(this, adUnitId, adManagerAdRequest,
                    new AdManagerInterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull AdManagerInterstitialAd interstitialAd) {
                            mAdManagerInterstitialAd = interstitialAd;
                            Log.e(ADMOB_TAG, "Adx InterstitialAd ===>" + "onAdLoaded");
                            if (loadAdsDialog != null && loadAdsDialog.isShowing()) {
                                loadAdsDialog.dismiss();
                            }
                            displayInterstitialAdsExit(activity, intent, isFinish);
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            Log.e(ADMOB_TAG, "Adx InterstitialAd ===> " + loadAdError.getMessage());
                            mAdManagerInterstitialAd = null;
                        }
                    });
        } else {
            doNext(activity, intent, isFinish);
        }
    }

    public void displayInterstitialAds(Activity activity){
        AudienceNetworkAds.initialize(activity);

        com.facebook.ads.InterstitialAd finterstitialAd = new com.facebook.ads.InterstitialAd(activity,
                getResources().getString(R.string.fb_ad_inters));
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.d("MyApplication","Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.d("MyApplication","Interstitial ad dismissed.");

            }

            @Override
            public void onError(Ad ad, com.facebook.ads.AdError adError) {
                Log.d("MyApplication",adError.getErrorMessage());
                Toast.makeText(activity, adError.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d("MyApplication","Interstitial ad is loaded and ready to be displayed!");

                // Show the ad
                if(finterstitialAd == null || !finterstitialAd.isAdLoaded()) {
                    return;
                }
                // Check if ad is already expired or invalidated, and do not show ad if that is the case. You will not get paid to show an invalidated ad.
                if(finterstitialAd.isAdInvalidated()) {
                    return;
                }
                // Show the ad
                finterstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d("MyApplication","Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d("MyApplication","Interstitial ad impression logged!");
               // Toast.makeText(activity, "Interstitial ad impression logged!", Toast.LENGTH_SHORT).show();
            }
        };
        finterstitialAd.loadAd(
                finterstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());

    }


    //Display InterstitialAds
   /* public void displayInterstitialAds(Activity activity, Intent intent, boolean isFinished) {
        if (UserHelper.getAdType().equalsIgnoreCase(UserHelper.AD_TYPE_ADMOB)) {
            displayAdMobInterstitialAd(activity, intent, isFinished);
        } else if (UserHelper.getAdType().equalsIgnoreCase(UserHelper.AD_TYPE_ADX)) {
            displayAdxInterstitialAd(activity, intent, isFinished);
        } else {
            doNext(activity, intent, isFinished);
        }
    }*/

    //1
    private void displayAdMobInterstitialAd(Activity activity, Intent intent, boolean isFinished) {
        int count = UserHelper.getInterstitialAdsCount();
        if (mInterstitialAd != null && count % UserHelper.getInterstitialAdsClick() == 0
                && UserHelper.ads_per_session != UserHelper.getAdsPerSession()) {
            Log.e(ADMOB_TAG, "Admob InterstitialAd ===> " + "Showed");
            mInterstitialAd.show(activity);
            UserHelper.isShowingFullScreenAd = true;
            UserHelper.ads_per_session++;
        } else {
            Log.e(ADMOB_TAG, "Admob InterstitialAd ===> " + "Start Activity but Ad not load.");
            doNext(activity, intent, isFinished);
            UserHelper.isShowingFullScreenAd = false;
        }
        UserHelper.setInterstitialAdsCount(count + 1);

        if (mInterstitialAd != null) {
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when fullscreen content is dismissed.
                    Log.e(ADMOB_TAG, "Admob InterstitialAd ===> The ad was dismissed.");
                    loadInterstitialAd(activity);
                    doNext(activity, intent, isFinished);
                    UserHelper.isShowingFullScreenAd = false;
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    Log.e(ADMOB_TAG, "Admob InterstitialAd ===> The ad failed to show.");
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    mInterstitialAd = null;
                    Log.e(ADMOB_TAG, "Admob InterstitialAd ===> The ad was shown.");
                }
            });
        }
    }

    private void displayAdxInterstitialAd(Activity activity, Intent intent, boolean isFinished) {
        int count = UserHelper.getInterstitialAdsCount();

        int i = UserHelper.getInterstitialAdsClick();
        if (mAdManagerInterstitialAd != null && count % UserHelper.getInterstitialAdsClick() == 0
                && UserHelper.ads_per_session != UserHelper.getAdsPerSession()) {
            Log.e(ADMOB_TAG, "Adx InterstitialAd ===> " + "Showed");
            mAdManagerInterstitialAd.show(activity);
            UserHelper.isShowingFullScreenAd = true;
            UserHelper.ads_per_session++;
        } else {
            Log.e(ADMOB_TAG, "Adx InterstitialAd ===> " + "Start Activity but Ad not load.");
            doNext(activity, intent, isFinished);
            UserHelper.isShowingFullScreenAd = false;
        }
        UserHelper.setInterstitialAdsCount(count + 1);

        if (mAdManagerInterstitialAd != null) {
            mAdManagerInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when fullscreen content is dismissed.
                    Log.e(ADMOB_TAG, "Adx InterstitialAd ===> The ad was dismissed.");
                    loadInterstitialAd(activity);
                    doNext(activity, intent, isFinished);
                    UserHelper.isShowingFullScreenAd = false;
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    Log.e(ADMOB_TAG, "Adx InterstitialAd ===> The ad failed to show.");
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    mAdManagerInterstitialAd = null;
                    Log.e(ADMOB_TAG, "Adx InterstitialAd ===> The ad was shown.");
                }
            });
        }
    }

    //Display exit
    public void displayInterstitialAdsExit(Activity activity, Intent intent, boolean isFinished) {
        if (UserHelper.getExitAdEnable() == 1 && UserHelper.getIsAdEnable() == 1 && UserHelper.getShowInterstitial() == 1) {
            if (UserHelper.getAdType().equalsIgnoreCase(UserHelper.AD_TYPE_ADMOB)) {
                displayAdMobInterstitialAdExit(activity, intent, isFinished);
            } else if (UserHelper.getAdType().equalsIgnoreCase(UserHelper.AD_TYPE_ADX)) {
                displayAdxInterstitialAdExit(activity, intent, isFinished);
            } else {
                doNext(activity, intent, isFinished);
            }
        } else {
            doNext(activity, intent, isFinished);
        }
    }

    private void displayAdMobInterstitialAdExit(Activity activity, Intent intent, boolean isFinished) {
        if (mInterstitialAd != null) {
            Log.e(ADMOB_TAG, "Admob InterstitialAd ===> " + "Showed");
            mInterstitialAd.show(activity);
            UserHelper.isShowingFullScreenAd = true;
        } else {
            Log.e(ADMOB_TAG, "Admob InterstitialAd ===> " + "Start Activity but Ad not load.");
            loadInterstitialAdExit(activity, intent, isFinished);
            UserHelper.isShowingFullScreenAd = false;
        }

        if (mInterstitialAd != null) {
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when fullscreen content is dismissed.
                    Log.e(ADMOB_TAG, "Admob InterstitialAd ===> The ad was dismissed.");
                    doNext(activity, intent, isFinished);
                    UserHelper.isShowingFullScreenAd = false;
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    Log.e(ADMOB_TAG, "Admob InterstitialAd ===> The ad failed to show.");
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    mInterstitialAd = null;
                    Log.e(ADMOB_TAG, "Admob InterstitialAd ===> The ad was shown.");
                }
            });
        }
    }

    private void displayAdxInterstitialAdExit(Activity activity, Intent intent, boolean isFinished) {
        if (mAdManagerInterstitialAd != null) {
            Log.e(ADMOB_TAG, "Adx InterstitialAd ===> " + "Showed");
            mAdManagerInterstitialAd.show(activity);
            UserHelper.isShowingFullScreenAd = true;
        } else {
            Log.e(ADMOB_TAG, "Adx InterstitialAd ===> " + "Start Activity but Ad not load.");
            loadInterstitialAdExit(activity, intent, isFinished);
            UserHelper.isShowingFullScreenAd = false;
        }

        if (mAdManagerInterstitialAd != null) {
            mAdManagerInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when fullscreen content is dismissed.
                    Log.e(ADMOB_TAG, "Adx InterstitialAd ===> The ad was dismissed.");
                    doNext(activity, intent, isFinished);
                    UserHelper.isShowingFullScreenAd = false;
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    Log.e(ADMOB_TAG, "Adx InterstitialAd ===> The ad failed to show.");
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    mAdManagerInterstitialAd = null;
                    Log.e(ADMOB_TAG, "Adx InterstitialAd ===> The ad was shown.");
                }
            });
        }
    }

    //doNext
    private void doNext(Activity activity, Intent intent, boolean isFinished) {
        if (intent != null) {
            activity.startActivity(intent);
        }
        if (isFinished) {
            activity.finish();
        }
    }

    // app open Ads
    public void loadOpenAppAdsOnSplash(ListnerOpenAppAds appCallBackOpenAppAds, Activity context) {

        if (UserHelper.getIsAdEnable() != 1) {
            return;
        }

        if (UserHelper.getShowAppOpen() != 1) {
            return;
        }

        if (!UserHelper.isNetworkConnected(context)) {
            return;
        }

        if (UserHelper.isNetworkConnected(context) && UserHelper.getOpenAdsSplash() == 1) {

            String adUnitId = UserHelper.getAppOpenAd();
            if (adUnitId == null) {
                return;
            }
            if (adUnitId.isEmpty()) {
                return;
            }
            Log.e("Ads: ", "Load Open App class");
            AppOpenAd.load(this, adUnitId, new AdRequest.Builder().build(), AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, new AppOpenAd.AppOpenAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull AppOpenAd ad) {
                    appOpenAd = ad;
                    appCallBackOpenAppAds.onAdLoad(true);
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    appOpenAd = null;
                    appCallBackOpenAppAds.onAdLoad(false);
                }
            });
        }
    }


}
