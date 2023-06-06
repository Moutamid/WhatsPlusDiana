package com.moutamid.rurovision.sticker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;


import com.moutamid.rurovision.activity.MyApplication;
import com.moutamid.rurovision.R;
import com.moutamid.rurovision.Utils.UserHelper;
import com.moutamid.rurovision.sticker.model.StickrMainOpt;
import com.moutamid.rurovision.sticker.repeater.RepeaterStickrMainOpt;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;

import java.util.ArrayList;

public class StickerActivity extends AppCompatActivity {

    RecyclerView rvStickerOptions;


    public final String NATIVE_AD = "NativeAd";

    public final String STICKER_1 = "Baby";
    public final String STICKER_2 = "Birthday";
    public final String STICKER_3 = "Emoj";
    public final String STICKER_4 = "Food";
    public final String STICKER_5 = "Halloween";
    public final String STICKER_6 = "Love";
    public final String STICKER_7 = "Music";
    public final String STICKER_8 = "Sale";
    //public final String STICKER_9 = "Social";
    public final String STICKER_10 = "Transport";
    public final String STICKER_11 = "Travel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker);

        RelativeLayout adViewBanner = findViewById(R.id.adViewBanner);

        AudienceNetworkAds.initialize(this);

        AdView faceBookBanner = new AdView(this, getString(R.string.fb_ad_banner), AdSize.BANNER_HEIGHT_50);


        adViewBanner.addView(faceBookBanner);

        faceBookBanner.loadAd();

     //   MyApplication.getInstance().loadBanner(adViewBanner, StickerActivity.this);
        MyApplication.getInstance().displayInterstitialAds(this);
        rvStickerOptions = findViewById(R.id.rvStickerOptions);

        rvStickerOptions.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        stickerMainAdapter();
    }

    private void stickerMainAdapter() {

        ArrayList<StickrMainOpt> stickerParentList = createStickerParentList();
        RepeaterStickrMainOpt stickerCategoryListAdapter = new RepeaterStickrMainOpt(StickerActivity.this, stickerParentList, new CallBack_PIP() {
            @Override
            public void onItemClick(View v, int pos) {

                Intent intent = new Intent(StickerActivity.this,StickerListActivity.class);
                intent.putExtra("curPos",pos);
                intent.putExtra("curName",stickerParentList.get(pos).getParentText());
                if (UserHelper.isNetworkConnected(StickerActivity.this)) {
                    MyApplication.getInstance().displayInterstitialAds(StickerActivity.this);
                } else {
                    startActivity(intent);
                }
//                startActivity(intent);
            }
        });

        rvStickerOptions.setAdapter(stickerCategoryListAdapter);
    }



    private ArrayList<StickrMainOpt> createStickerParentList() {
        String[] stickerListTitle = new String[0];
        Integer[] stickerListIcon = new Integer[0];

        if (UserHelper.isNetworkConnected(StickerActivity.this)){
            stickerListTitle = new String[]{STICKER_1, STICKER_2, STICKER_3,NATIVE_AD, STICKER_4,
                    STICKER_5, STICKER_6,NATIVE_AD, STICKER_7, STICKER_8, NATIVE_AD,STICKER_10, STICKER_11};
            stickerListIcon = new Integer[]{R.drawable.ic_sticker_1, R.drawable.ic_sticker_2,
                    R.drawable.ic_sticker_3,R.drawable.circle ,R.drawable.ic_sticker_4,
                    R.drawable.ic_sticker_5, R.drawable.ic_sticker_6,R.drawable.circle,
                    R.drawable.ic_sticker_7, R.drawable.ic_sticker_8,R.drawable.circle, R.drawable.ic_sticker_10,
                    R.drawable.ic_sticker_11};
        }else {
            stickerListTitle = new String[]{STICKER_1, STICKER_2, STICKER_3, STICKER_4, STICKER_5,
                    STICKER_6, STICKER_7, STICKER_8, STICKER_10, STICKER_11};
            stickerListIcon = new Integer[]{R.drawable.ic_sticker_1, R.drawable.ic_sticker_2,
                    R.drawable.ic_sticker_3, R.drawable.ic_sticker_4, R.drawable.ic_sticker_5,
                    R.drawable.ic_sticker_6, R.drawable.ic_sticker_7, R.drawable.ic_sticker_8, R.drawable.ic_sticker_10,
                    R.drawable.ic_sticker_11};
        }

        ArrayList<StickrMainOpt> stickerParentList = new ArrayList<>();

        for (int k = 0; k < stickerListIcon.length; k++) {
            StickrMainOpt stickerParentMode3 = new StickrMainOpt();
            stickerParentMode3.setParentIcon(getResources().getDrawable(stickerListIcon[k]));
            stickerParentMode3.setParentText(stickerListTitle[k]);
            stickerParentList.add(stickerParentMode3);
        }
        return stickerParentList;
    }


    public void backclickSticker(View view) {
        onBackPressed();
    }
}