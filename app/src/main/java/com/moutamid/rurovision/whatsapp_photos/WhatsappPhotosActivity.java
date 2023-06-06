package com.moutamid.rurovision.whatsapp_photos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moutamid.rurovision.R;
import com.moutamid.rurovision.fragment.FragImage;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;

public class WhatsappPhotosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatsapp_photos);

        findViewById(R.id.mToolBarThumb).setOnClickListener(v -> onBackPressed());
        ((TextView) findViewById(R.id.mToolBarText)).setText("What's Photos");

        RelativeLayout adViewBanner = findViewById(R.id.adViewBanner);
        //MyApplication.getInstance().loadBanner(adViewBanner, CardCaptionActivity.this);
        AudienceNetworkAds.initialize(this);

        AdView faceBookBanner = new AdView(this, getString(R.string.fb_ad_banner), AdSize.BANNER_HEIGHT_50);


        adViewBanner.addView(faceBookBanner);

        faceBookBanner.loadAd();

        FragImage newFragment = new FragImage();

        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentContainer, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}