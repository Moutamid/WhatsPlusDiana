package com.moutamid.rurovision.activity;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.moutamid.rurovision.model.ADsDataModel;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moutamid.rurovision.R;
import com.moutamid.rurovision.Utils.Logger;
import com.moutamid.rurovision.Utils.UserHelper;
import com.moutamid.rurovision.Utils.Utility;

public class SplashActivity extends BaseActivity {
    private static final int RC_APP_UPDATE = 11;
    ImageView ivAppIcon,ivAppIcon2;
    private boolean isAppOpenAdLoad = false;
    private AppUpdateManager mAppUpdateManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        hideSystemBars();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        ivAppIcon = findViewById(R.id.ivAppIcon);
        ivAppIcon2 = findViewById(R.id.ivAppIcon2);

        Bitmap bitmapLocal1 = Utility.decodeSampledBitmapFromResource(getResources(),
                R.drawable.ic_logo, 500, 500);
        ivAppIcon.setImageBitmap(bitmapLocal1);
        ivAppIcon2.setImageBitmap(bitmapLocal1);


        UserHelper.setUserInSplashIntro(true);
        UserHelper.ads_per_session = 0;
        UserHelper.setReviewCount(0);

        /*if (UserHelper.isNetworkConnected(SplashActivity.this)) {
            doNext("{\n" +
                    "\"Id\":11,\n" +
                    "\"banner_id\":\"ca-app-pub-3940256099942544/6300978111\",\n" +
                    "\"interstitial_id\":\"ca-app-pub-3940256099942544/1033173712\",\n" +
                    "\"native_id\":\"ca-app-pub-3940256099942544/2247696110\",\n" +
                    "\"open_app_id\":\"ca-app-pub-3940256099942544/3419835294\",\n" +
                    "\"is_show_ads\":1,\n" +
                    "\"show_banner\":1,\n" +
                    "\"show_interstitial\":1,\n" +
                    "\"show_interstitial_exit\":1,\n" +
                    "\"interstitial_count\":3,\n" +
                    "\"show_native\":1,\n" +
                    "\"show_open_app\":1,\n" +
                    "\"show_open_app_splash\":1,\n" +
                    "\"ads_per_seassion\":10,\n" +
                    "\"relocation\":\"\",\n" +
                    "\"adtype\":\"admob\",\n" +
                    "\"splash_time\":10\n" +
                    "}");
        } else {
            toHome();
        }*/
        toHome();
        inAppUpdate();


    }
    InstallStateUpdatedListener mInstallUpdated = new InstallStateUpdatedListener() {
        @Override
        public void onStateUpdate(InstallState state) {
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                snackbarUpdateApp();
            } else if (state.installStatus() == InstallStatus.INSTALLED) {
                if (mAppUpdateManager != null) {
                    mAppUpdateManager.unregisterListener(mInstallUpdated);
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        mAppUpdateManager.unregisterListener(mInstallUpdated);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserHelper.setUserInSplashIntro(true);
        try {
            mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(result -> {
                if (result.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    try {
                        mAppUpdateManager.startUpdateFlowForResult(result, IMMEDIATE, SplashActivity.this, RC_APP_UPDATE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            });
            mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    snackbarUpdateApp();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void snackbarUpdateApp() {
        /*Snackbar snackbar = Snackbar.make(findViewById(R.id.rlMain), "New app is ready!", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Install", v -> {
            if (mAppUpdateManager != null) {
                mAppUpdateManager.completeUpdate();
            }
        });

        snackbar.setActionTextColor(getResources().getColor(R.color.white));
        snackbar.show();*/
    }

    private void inAppUpdate() {

        mAppUpdateManager = AppUpdateManagerFactory.create(this);

        mAppUpdateManager.registerListener(mInstallUpdated);

        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)) {

                try {
                    mAppUpdateManager.startUpdateFlowForResult(appUpdateInfo, IMMEDIATE, SplashActivity.this, RC_APP_UPDATE);

                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }

            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                snackbarUpdateApp();
            }
        });
    }


    public void doNext(String response) {
        ADsDataModel appData = parseAppUserListModel(response);
        setAppData(appData);

        String link = UserHelper.getTransferLink();
        if (!TextUtils.isEmpty(link) && !link.equalsIgnoreCase("null")) {
            startActivity(new Intent(this, ActivityRelocation.class).putExtra("link", link));
            finish();
            return;
        }


        if (UserHelper.getIsAdEnable() == 1 && UserHelper.isNetworkConnected(SplashActivity.this)) {
            if (UserHelper.getShowInterstitial() == 1) {
                MyApplication.getInstance().loadInterstitialAd(SplashActivity.this);
            }
            if (UserHelper.getShowNative() == 1) {
                MyApplication.getInstance().loadAdmobNativeAd(0, SplashActivity.this);
            }
            if (UserHelper.getShowAppOpen() == 1) {

                MyApplication.getInstance().loadOpenAppAdsOnSplash(isLoaded -> {
                    if (isLoaded && !isFinishing() && !isDestroyed()) {
                        Log.e("Ads Next", "From OpenApp Load");
                        isAppOpenAdLoad = true;
                        toHome();
                    }
                }, SplashActivity.this);


                new Handler(Looper.myLooper()).postDelayed(() -> {
                    if (!isAppOpenAdLoad) {
                        Log.e("Ads Next ", "From OpenApp Time Out");
                        toHome();
                    }
                }, UserHelper.getSplashScreenWaitCount() * 1000L);

            } else {
                toHome();
                Log.e(MyApplication.ADMOB_TAG, "OpenApps Ads ===> Disable");
            }

        } else {
            toHome();
        }

    }

    public ADsDataModel parseAppUserListModel(String jsonObject) {
        try {
            Gson gson = new Gson();
            TypeToken<ADsDataModel> token = new TypeToken<ADsDataModel>() {
            };
            return gson.fromJson(jsonObject, token.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setAppData(ADsDataModel appData) {

        try {
            UserHelper.setBannerAd(appData.getBannerid());
            UserHelper.setShowBanner(appData.getBanner());

            UserHelper.setInterstitialAd(appData.getInterstitialid());
            UserHelper.setShowInterstitial(appData.getInterstitial());
            UserHelper.setInterstitialAdsClick(appData.getAds_per_click());

            UserHelper.setNativeAd(String.valueOf(appData.getNativeid()));
            UserHelper.setShowNative(appData.getNative());

            UserHelper.setAppOpenAd(String.valueOf(appData.getOpenadid()));
            UserHelper.setShowAppOpen(appData.getOpenad());
            UserHelper.setOpenAdsSplash(appData.getIs_splash_on());


            UserHelper.setAdType(String.valueOf(appData.getAdtype()));
            UserHelper.setTransferLink(appData.getTransfer_link());
            UserHelper.setIsAdEnable(appData.getIsAdEnable());
            UserHelper.setExitAdEnable(appData.getExit_ad_enable());
            UserHelper.setAdsPerSession(appData.getAds_per_session());
            UserHelper.setSplashScreenWaitCount(appData.getSplash_time());

        } catch (Exception e) {
            Logger.AppLog("Ads:: Exception", e.getMessage());
            Toast.makeText(this, e.getMessage() + "", Toast.LENGTH_SHORT).show();
        }
    }

    private void toHome() {
        runOnUiThread(() -> new Handler(Looper.myLooper()).postDelayed(() ->{
            startActivity(new Intent(SplashActivity.this, StartActivity.class));
            finish();
        }, 2000));
    }




}
