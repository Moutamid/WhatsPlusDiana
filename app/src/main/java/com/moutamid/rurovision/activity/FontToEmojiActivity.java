package com.moutamid.rurovision.activity;

import static com.moutamid.rurovision.activity.TextRepeaterActivity.hideSoftKeyboard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.moutamid.rurovision.R;
import com.moutamid.rurovision.Utils.notifier.EventNotifier;
import com.moutamid.rurovision.Utils.notifier.EventState;
import com.moutamid.rurovision.Utils.notifier.IEventListener;
import com.moutamid.rurovision.Utils.notifier.NotifierFactory;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;

import java.io.IOException;
import java.io.InputStream;

public class FontToEmojiActivity extends AppCompatActivity implements IEventListener {

    EditText etText;
    EditText etTextEmoji;
    EditText etTextwithEmoji;
    Button btnTransform;
    Button btnCopy;
    Button btnClear;
    Button btnShare;
    private FrameLayout _fl_adplaceholder;

    @Override
    public int eventNotify(int eventType, final Object eventObject) {
        int eventState = EventState.EVENT_IGNORED;
        if (eventType == EventState.EVENT_AD_LOADED_NATIVE) {
            eventState = EventState.EVENT_PROCESSED;
            runOnUiThread(() -> new Handler(Looper.myLooper()).postDelayed(() -> MyApplication.getInstance().loadNativeAds(_fl_adplaceholder, FontToEmojiActivity.this, 1), 500));
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
        setContentView(R.layout.activity_font_to_emoji);

        _fl_adplaceholder = findViewById(R.id.flAdplaceholder);
       // registerAdsListener();

        RelativeLayout adViewBanner = findViewById(R.id.adViewBanner);
        //MyApplication.getInstance().loadBanner(adViewBanner, FontToEmojiActivity.this);
        AudienceNetworkAds.initialize(this);

        AdView faceBookBanner = new AdView(this, getString(R.string.fb_ad_banner), AdSize.BANNER_HEIGHT_50);


        adViewBanner.addView(faceBookBanner);

        faceBookBanner.loadAd();

        etText = findViewById(R.id.etText);
        etTextEmoji = findViewById(R.id.etTextEmoji);
        etTextwithEmoji = findViewById(R.id.etTextwithEmoji);
        btnTransform = findViewById(R.id.btnTransform);
        btnCopy = findViewById(R.id.btnCopy);
        btnClear = findViewById(R.id.btnClear);
        btnShare = findViewById(R.id.btnShare);

        btnTransform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(FontToEmojiActivity.this);
                if (!TextUtils.isEmpty(etText.getText().toString()) && !TextUtils.isEmpty(etTextEmoji.getText().toString())) {
                    char[] charArray = etText.getText().toString().toCharArray();
                    etTextwithEmoji.setText(".\n");
                    for (char c2 : charArray) {
                        if (c2 == '?') {
                            try {
                                InputStream open = getBaseContext().getAssets().open("ques.txt");
                                byte[] bArr = new byte[open.available()];
                                open.read(bArr);
                                open.close();
                                etTextwithEmoji.append(new String(bArr).replaceAll("[*]", etTextEmoji.getText().toString()) + "\n\n");
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        } else if (c2 == ((char) (c2 & '_')) || Character.isDigit(c2)) {
                            try {
                                InputStream open2 = getBaseContext().getAssets().open(c2 + ".txt");
                                byte[] bArr2 = new byte[open2.available()];
                                open2.read(bArr2);
                                open2.close();
                                etTextwithEmoji.append(new String(bArr2).replaceAll("[*]", etTextEmoji.getText().toString()) + "\n\n");
                            } catch (IOException e3) {
                                e3.printStackTrace();
                            }
                        } else {
                            try {
                                InputStream open3 = getBaseContext().getAssets().open("low" + c2 + ".txt");
                                byte[] bArr3 = new byte[open3.available()];
                                open3.read(bArr3);
                                open3.close();
                                etTextwithEmoji.append(new String(bArr3).replaceAll("[*]", etTextEmoji.getText().toString()) + "\n\n");
                            } catch (IOException e4) {
                                e4.printStackTrace();
                            }
                        }
                    }
                }
            }
        });

        btnCopy.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(etTextwithEmoji.getText().toString())) {
                ConstantMethod.CopyToClipBoard(FontToEmojiActivity.this, etTextwithEmoji.getText().toString());
//                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//                clipboard.setText(etTextwithEmoji.getText().toString());
//                Toast.makeText(this, "Copy Succesfully..", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(FontToEmojiActivity.this, "Empty string.", Toast.LENGTH_SHORT).show();
            }
        });
        btnClear.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(etTextwithEmoji.getText().toString())) {
                etTextwithEmoji.getText().clear();
            }else{
                Toast.makeText(FontToEmojiActivity.this, "Empty string.", Toast.LENGTH_SHORT).show();
            }
        });
        btnShare.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(etTextwithEmoji.getText().toString())) {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, etTextwithEmoji.getText().toString());
                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(FontToEmojiActivity.this, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(FontToEmojiActivity.this, "Empty string.", Toast.LENGTH_SHORT).show();
            }
        });
        MyApplication.getInstance().displayInterstitialAds(this);
        //MyApplication.getInstance().loadNativeAds(_fl_adplaceholder, FontToEmojiActivity.this, 1);
    }

    public void backFontToEmoji(View view) {
        onBackPressed();
    }
}