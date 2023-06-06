package com.moutamid.rurovision.activity;

import static com.moutamid.rurovision.activity.MainActivity.PERMISSIONS;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.moutamid.rurovision.Utils.Constants;
import com.moutamid.rurovision.R;
import com.moutamid.rurovision.Utils.Common;
import com.moutamid.rurovision.Utils.UserHelper;
import com.moutamid.rurovision.Utils.notifier.EventNotifier;
import com.moutamid.rurovision.Utils.notifier.EventState;
import com.moutamid.rurovision.Utils.notifier.IEventListener;
import com.moutamid.rurovision.Utils.notifier.NotifierFactory;

import java.io.File;

public class StartActivity extends BaseActivity implements IEventListener {

    RelativeLayout btnStart;
    LottieAnimationView lottieShare;
    LottieAnimationView lottieRateUS;
    LottieAnimationView lottiePolicy;
    private FrameLayout _fl_adplaceholder;

    @Override
    public int eventNotify(int eventType, final Object eventObject) {
        int eventState = EventState.EVENT_IGNORED;
        if (eventType == EventState.EVENT_AD_LOADED_NATIVE) {
            eventState = EventState.EVENT_PROCESSED;
            runOnUiThread(() -> new Handler(Looper.myLooper()).postDelayed(() -> MyApplication.getInstance().loadNativeAds(_fl_adplaceholder, StartActivity.this, 1), 500));
        }

        return eventState;
    }

    private void registerAdsListener() {
        EventNotifier notifier = NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_AD_STATUS);
        notifier.registerListener(this, 1000);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        _fl_adplaceholder = findViewById(R.id.flAdplaceholder);
//        registerAdsListener();

        UserHelper.setUserInSplashIntro(false);
      /*  if (appOpenAd != null && UserHelper.isNetworkConnected(this) && UserHelper.getOpenAdsSplash() == 1) {
            FullScreenContentCallback callback = new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    UserHelper.isShowingFullScreenAd = false;
                    isShowingAd = false;
                    appOpenAd = null;
                    Log.e(MyApplication.ADMOB_TAG, "Open AD ===> The ad was dismissed.");
                    if (UserHelper.getShowAppOpen() == 1 && UserHelper.getIsAdEnable() == 1) {
                        OpenAppAds appLifecycleObserver = new OpenAppAds(MyApplication.getInstance());
                        ProcessLifecycleOwner.get().getLifecycle().addObserver(appLifecycleObserver);
                    }
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    UserHelper.isShowingFullScreenAd = true;
                    isShowingAd = true;
                    Log.e(MyApplication.ADMOB_TAG, "Open AD ===> The ad was shown.");
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    Log.e(MyApplication.ADMOB_TAG, "Open AD ===> The ad failed to show.");
                    isShowingAd = true;
                    if (UserHelper.getShowAppOpen() == 1 && UserHelper.getIsAdEnable() == 1) {
                        OpenAppAds appLifecycleObserver = new OpenAppAds(MyApplication.getInstance());
                        ProcessLifecycleOwner.get().getLifecycle().addObserver(appLifecycleObserver);
                    }
                }

            };
            appOpenAd.setFullScreenContentCallback(callback);
            appOpenAd.show(this);
            isShowingAd = true;
        } else {
            if (UserHelper.getShowAppOpen() == 1 && UserHelper.getIsAdEnable() == 1) {
                OpenAppAds appLifecycleObserver = new OpenAppAds(MyApplication.getInstance());
                ProcessLifecycleOwner.get().getLifecycle().addObserver(appLifecycleObserver);
            }
        }*/

        hideSystemBars();
        btnStart = findViewById(R.id.btnStart);
        Constants.checkApp(StartActivity.this);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserHelper.isPermissionGrant()) {
                    toMain();
                } else {
                    permissonDialog();
                }

            }
        });

        lottieShare = findViewById(R.id.lottieShare);
        lottieRateUS = findViewById(R.id.lottieRateUS);
        lottiePolicy = findViewById(R.id.lottiePolicy);

        lottieShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareApp(StartActivity.this);
            }
        });
        lottieRateUS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RateApp(StartActivity.this);
            }
        });
        lottiePolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, ActivityAppPolicy.class);
                if (UserHelper.isNetworkConnected(StartActivity.this)) {
                    MyApplication.getInstance().displayInterstitialAds(StartActivity.this);
                } else {
                    startActivity(intent);
                }
            }
        });
        MyApplication.getInstance().displayInterstitialAds(StartActivity.this);
   //     MyApplication.getInstance().loadNativeAds(_fl_adplaceholder, StartActivity.this, 1);
    }

    private void toMain() {
        Intent mIntent = new Intent(StartActivity.this, MainActivity.class);
        startActivity(mIntent);
        finish();
    }

    public void ShareApp(Context context) {
        final String appLink = "\nhttps://play.google.com/store/apps/details?id=" + context.getPackageName();
        Intent sendInt = new Intent(Intent.ACTION_SEND);
        sendInt.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
        sendInt.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_app_message) + appLink);
        sendInt.setType("text/plain");
        context.startActivity(Intent.createChooser(sendInt, "Share"));
    }

    public void RateApp(Context context) {
        final String appName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appName)));
        } catch (ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
        }
    }

    private void permissonDialog() {
        if (arePermissionDenied()) {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(1);
            dialog.setContentView(R.layout.dialog_permission);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);

            TextView tvAllow = dialog.findViewById(R.id.tvAllow);
            TextView tvCancel = dialog.findViewById(R.id.tvCancel);


            tvAllow.setOnClickListener(view -> {
                toMain();
            });
            tvCancel.setOnClickListener(view -> {
                if (dialog.isShowing() && !isFinishing()) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        } else {
            toMain();
        }

    }

    private boolean arePermissionDenied() {
        Common.APP_DIR = Environment.getExternalStorageDirectory().getPath() + File.separator + "StatusDownloader";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return getContentResolver().getPersistedUriPermissions().size() <= 0;
        }

        for (String permissions : PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), permissions) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }

        return false;
    }
}