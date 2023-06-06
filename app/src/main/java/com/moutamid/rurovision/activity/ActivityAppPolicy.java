package com.moutamid.rurovision.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.moutamid.rurovision.R;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;

public class ActivityAppPolicy extends AppCompatActivity {

    ImageView ivBack;
    WebView wvPrivacyPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_policy);

        RelativeLayout adViewBanner = findViewById(R.id.adViewBanner);
//        MyApplication.getInstance().loadBanner(adViewBanner, ActivityAppPolicy.this);
        AudienceNetworkAds.initialize(this);

        AdView faceBookBanner = new AdView(this, getString(R.string.fb_ad_banner), AdSize.BANNER_HEIGHT_50);


        adViewBanner.addView(faceBookBanner);

        faceBookBanner.loadAd();

        ivBack = findViewById(R.id.ivBack);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        wvPrivacyPolicy = findViewById(R.id.wvPrivacyPolicy);
        String url = "https://pages.flycricket.io/whatsapp-tools-5/privacy.html";

        wvPrivacyPolicy.getSettings().setJavaScriptEnabled(true);
        wvPrivacyPolicy.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
//        wvPrivacyPolicy.getSettings().setAppCacheEnabled(true);
        wvPrivacyPolicy.getSettings().setDatabaseEnabled(true);
        wvPrivacyPolicy.getSettings().setDomStorageEnabled(true);
        wvPrivacyPolicy.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        wvPrivacyPolicy.setWebViewClient(new WebViewClient() {

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                String e = error.toString();
                System.out.println("ERROR " + e);
                handler.cancel();
            }
        });
        wvPrivacyPolicy.loadUrl(url);
    }
}