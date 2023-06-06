package com.moutamid.rurovision.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.moutamid.rurovision.R;
import com.moutamid.rurovision.Utils.Utility;

public class ActivityRelocation extends AppCompatActivity {

    LinearLayout lin_playstore;
    ImageView mIvplaystoreicon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relocation);

        lin_playstore = findViewById(R.id.lin_playstore);
        mIvplaystoreicon = findViewById(R.id.ivplaystoreicon);
        init();
        lin_playstore.setOnClickListener(v -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri
                        .parse("https://play.google.com/store/apps/details?id="
                                + getIntent().getStringExtra("link"))));
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage());
            }
        });
    }

    private void init() {
        Bitmap bitmapLocal = Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.ic_playstore, 256, 256);
        mIvplaystoreicon.setImageBitmap(bitmapLocal);

    }
}