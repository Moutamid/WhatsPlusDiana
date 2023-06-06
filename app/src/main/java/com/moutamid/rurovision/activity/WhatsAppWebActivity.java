package com.moutamid.rurovision.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.moutamid.rurovision.R;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;


public class WhatsAppWebActivity extends AppCompatActivity {

    WebView wvWhatsappWeb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app_web);

        RelativeLayout adViewBanner = findViewById(R.id.adViewBanner);
        //MyApplication.getInstance().loadBanner(adViewBanner, WhatsAppWebActivity.this);

        AudienceNetworkAds.initialize(this);

        AdView faceBookBanner = new AdView(this, getString(R.string.fb_ad_banner), AdSize.BANNER_HEIGHT_50);


        adViewBanner.addView(faceBookBanner);

        faceBookBanner.loadAd();


        wvWhatsappWeb = findViewById(R.id.wvWhatsappWeb);

//        wvWhatsappWeb.removeAllViews();
//        wvWhatsappWeb .loadUrl("https://web.whatsapp.com/");// https://tekdude.blogspot.in/");
//        wvWhatsappWeb.getSettings().setBuiltInZoomControls(true);
//        wvWhatsappWeb.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        setContentView(wvWhatsappWeb );



        MyApplication.getInstance().displayInterstitialAds(this);
        wvWhatsappWeb.loadUrl("https://web.whatsapp.com/");
        wvWhatsappWeb.setWebViewClient(new WebViewClient());
        wvWhatsappWeb.getSettings().setJavaScriptEnabled(true);
        wvWhatsappWeb.getSettings().setUseWideViewPort(true);
        wvWhatsappWeb.setWebChromeClient(new WebChromeClient());
        wvWhatsappWeb.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Win64; x64; rv:46.0) Gecko/20100101 Firefox/68.0");
        wvWhatsappWeb.getSettings().setGeolocationEnabled(true);
        wvWhatsappWeb.getSettings().setDomStorageEnabled(true);
        wvWhatsappWeb.getSettings().setDatabaseEnabled(true);
        wvWhatsappWeb.getSettings().setSupportMultipleWindows(true);
//        wvWhatsappWeb.getSettings().setAppCacheEnabled(true);
        wvWhatsappWeb.getSettings().setNeedInitialFocus(true);
        wvWhatsappWeb.getSettings().setLoadWithOverviewMode(true);
        wvWhatsappWeb.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//wvWhatsappWeb.getSettings().setBlockNetworkLoads(true);
        wvWhatsappWeb.getSettings().setBlockNetworkImage(true);
        wvWhatsappWeb.getSettings().setBuiltInZoomControls(true);
        wvWhatsappWeb.setInitialScale(100);
    }

    public void backclickWhatsAppWeb(View view) {
        onBackPressed();
    }
}