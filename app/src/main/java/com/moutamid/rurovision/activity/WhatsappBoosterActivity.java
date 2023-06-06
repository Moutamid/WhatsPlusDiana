package com.moutamid.rurovision.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.moutamid.rurovision.R;
import com.moutamid.rurovision.Utils.notifier.EventNotifier;
import com.moutamid.rurovision.Utils.notifier.EventState;
import com.moutamid.rurovision.Utils.notifier.IEventListener;
import com.moutamid.rurovision.Utils.notifier.NotifierFactory;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;


public class WhatsappBoosterActivity extends AppCompatActivity implements IEventListener {

    RelativeLayout llScanIntro;
    ImageView ivCircle;
    ImageView ivBack;
    LottieAnimationView booster_lottie;
    LottieAnimationView booster_success_lottie;
    private FrameLayout _fl_adplaceholder;

    @Override
    public int eventNotify(int eventType, final Object eventObject) {
        int eventState = EventState.EVENT_IGNORED;
        if (eventType == EventState.EVENT_AD_LOADED_NATIVE) {
            eventState = EventState.EVENT_PROCESSED;
            runOnUiThread(() -> new Handler(Looper.myLooper()).postDelayed(() -> MyApplication.getInstance().loadNativeAds(_fl_adplaceholder, WhatsappBoosterActivity.this, 1), 500));
        }

        return eventState;
    }

    private void registerAdsListener() {
        EventNotifier notifier = NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_AD_STATUS);
        notifier.registerListener(this, 1000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatsapp_booster);

        RelativeLayout adViewBanner = findViewById(R.id.adViewBanner);
      //  MyApplication.getInstance().loadBanner(adViewBanner, WhatsappBoosterActivity.this);

        AudienceNetworkAds.initialize(this);

        AdView faceBookBanner = new AdView(this, getString(R.string.fb_ad_banner), AdSize.BANNER_HEIGHT_50);


        adViewBanner.addView(faceBookBanner);

        faceBookBanner.loadAd();

        _fl_adplaceholder = findViewById(R.id.flAdplaceholder);
        //registerAdsListener();

        llScanIntro = findViewById(R.id.llScanIntro);
        ivCircle = findViewById(R.id.ivCircle);
        booster_lottie = findViewById(R.id.booster_lottie);
        booster_success_lottie = findViewById(R.id.booster_success_lottie);
        ivBack = findViewById(R.id.ivBack);
        ivCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBoostAnim();
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        MyApplication.getInstance().displayInterstitialAds(this);
     //   MyApplication.getInstance().loadNativeAds(_fl_adplaceholder, WhatsappBoosterActivity.this, 1);
    }

    private void showBoostAnim() {
        llScanIntro.setVisibility(View.GONE);
        booster_lottie.setVisibility(View.VISIBLE);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                booster_lottie.setVisibility(View.GONE);
                booster_success_lottie.setVisibility(View.VISIBLE);
            }
        }, 5000);
    }


}