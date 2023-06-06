package com.moutamid.rurovision.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.moutamid.rurovision.R;
import com.moutamid.rurovision.Utils.Utility;

public class ActivityThankYou extends BaseActivity {

    ImageView ivThankyou;
    ImageView ivThanksTxt;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        hideSystemBars();

        ivThankyou = findViewById(R.id.ivThankyou);
        ivThanksTxt = findViewById(R.id.ivThanksTxt);

        Bitmap bitmapLocal1 = Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.ic_logo, 500, 500);
        ivThankyou.setImageBitmap(bitmapLocal1);

        Bitmap bitmapLocal2 = Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.bye, 500, 500);
        ivThanksTxt.setImageBitmap(bitmapLocal2);

        new Handler(Looper.getMainLooper()).postDelayed(this::finish, 1500);
    }
}