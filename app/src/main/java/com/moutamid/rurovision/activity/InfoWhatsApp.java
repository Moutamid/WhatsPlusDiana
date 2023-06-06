package com.moutamid.rurovision.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moutamid.rurovision.R;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;


public class InfoWhatsApp extends AppCompatActivity {
TextView tvVersion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_whats_app);

        RelativeLayout adViewBanner = findViewById(R.id.adViewBanner);
        //MyApplication.getInstance().loadBanner(adViewBanner, InfoWhatsApp.this);
        AudienceNetworkAds.initialize(this);

        AdView faceBookBanner = new AdView(this, getString(R.string.fb_ad_banner), AdSize.BANNER_HEIGHT_50);


        adViewBanner.addView(faceBookBanner);

        faceBookBanner.loadAd();
        MyApplication.getInstance().displayInterstitialAds(this);
        tvVersion = findViewById(R.id.tvVersion);
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo("com.whatsapp", 0);
            String version = pInfo.versionName;
            tvVersion.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void backWhatsappInfo(View view) {
        onBackPressed();
    }
}