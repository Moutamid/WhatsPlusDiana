package com.moutamid.rurovision.shayri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.moutamid.rurovision.activity.ConstantMethod;
import com.moutamid.rurovision.activity.MyApplication;
import com.moutamid.rurovision.R;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class ShayriActivity extends AppCompatActivity {

    ShayriAdapter ShayriAdapter;
    ViewPager viewPager;
    ArrayList<String> formList;
    ArrayList<String> copyFormList;
    Button btnCopy;
    Button btnShare;
    TextView btnBack;
    TextView btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shayri);

        RelativeLayout adViewBanner = findViewById(R.id.adViewBanner);
    //    MyApplication.getInstance().loadBanner(adViewBanner, ShayriActivity.this);

        AudienceNetworkAds.initialize(this);

        AdView faceBookBanner = new AdView(this, getString(R.string.fb_ad_banner), AdSize.BANNER_HEIGHT_50);


        adViewBanner.addView(faceBookBanner);

        faceBookBanner.loadAd();


        MyApplication.getInstance().displayInterstitialAds(this);
        viewPager = findViewById(R.id.viewPager);
        btnCopy = findViewById(R.id.btnCopy);
        btnShare = findViewById(R.id.btnShare);
        btnBack = findViewById(R.id.btnBack);
        btnNext = findViewById(R.id.btnNext);

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("shayris");
            int i1 = m_jArry.length();
            HashMap<String, String> m_li;
            formList = new ArrayList<>();
            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                String formula_value = jo_inside.getString("shayriText");

                formList.add(formula_value);

            }
            copyFormList = formList;
            ShayriAdapter = new ShayriAdapter(getSupportFragmentManager(), formList);
            viewPager.setAdapter(ShayriAdapter);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            viewPager.setOffscreenPageLimit(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnCopy.setOnClickListener(v -> ConstantMethod.CopyToClipBoard(ShayriActivity.this, copyFormList.get(viewPager.getCurrentItem())));
        btnShare.setOnClickListener(v -> {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, copyFormList.get(viewPager.getCurrentItem()));
                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ShayriActivity.this, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
            }
        });
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = ShayriActivity.this.getAssets().open("shayri.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void backclickShayri(View view) {
        onBackPressed();
    }
}