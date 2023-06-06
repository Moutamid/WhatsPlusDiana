package com.moutamid.rurovision.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;


import com.moutamid.rurovision.R;
import com.moutamid.rurovision.Utils.notifier.EventNotifier;
import com.moutamid.rurovision.Utils.notifier.EventState;
import com.moutamid.rurovision.Utils.notifier.IEventListener;
import com.moutamid.rurovision.Utils.notifier.NotifierFactory;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;

import java.util.ArrayList;

public class TextRepeaterActivity extends AppCompatActivity implements IEventListener {

    EditText etTextMsg;
    EditText etTextRepeateNumber;
    EditText etTextRepeates;
    Button btnRepeat;
    Button btnCopy;
    Button btnClear;
    Button btnShare;
    SwitchCompat switchNewLine;
    String text = "";
    String limitNo;
    int count = 0;
    private FrameLayout _fl_adplaceholder;

    @Override
    public int eventNotify(int eventType, final Object eventObject) {
        int eventState = EventState.EVENT_IGNORED;
        if (eventType == EventState.EVENT_AD_LOADED_NATIVE) {
            eventState = EventState.EVENT_PROCESSED;
            runOnUiThread(() -> new Handler(Looper.myLooper()).postDelayed(() -> MyApplication.getInstance().loadNativeAds(_fl_adplaceholder, TextRepeaterActivity.this, 1), 500));
        }

        return eventState;
    }

    private void registerAdsListener() {
        EventNotifier notifier = NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_AD_STATUS);
        notifier.registerListener(this, 1000);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_repeater);

        RelativeLayout adViewBanner = findViewById(R.id.adViewBanner);
       // MyApplication.getInstance().loadBanner(adViewBanner, TextRepeaterActivity.this);
        AudienceNetworkAds.initialize(this);

        AdView faceBookBanner = new AdView(this, getString(R.string.fb_ad_banner), AdSize.BANNER_HEIGHT_50);


        adViewBanner.addView(faceBookBanner);

        faceBookBanner.loadAd();

        _fl_adplaceholder = findViewById(R.id.flAdplaceholder);
        //registerAdsListener();


        etTextMsg = findViewById(R.id.etTextMsg);
        etTextRepeateNumber = findViewById(R.id.etTextRepeateNumber);
        etTextRepeates = findViewById(R.id.etTextRepeates);
        btnRepeat = findViewById(R.id.btnRepeat);
        btnCopy = findViewById(R.id.btnCopy);
        btnClear = findViewById(R.id.btnClear);
        btnShare = findViewById(R.id.btnShare);
        switchNewLine = findViewById(R.id.switchNewLine);

        limitNo = etTextRepeateNumber.getText().toString();
        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(TextRepeaterActivity.this);
                if (!TextUtils.isEmpty(etTextMsg.getText().toString()) && !TextUtils.isEmpty(etTextRepeateNumber.getText().toString())) {
//                    int i1 = countChar(etTextRepeateNumber.getText().toString());
                    etTextRepeates.getText().clear();
                    String input = etTextMsg.getText().toString();
                    int number = Integer.parseInt(etTextRepeateNumber.getText().toString());
                    ArrayList<String> data = new ArrayList<>();
                    if (switchNewLine.isChecked()) {
                        for (int i = 0; i < number; i++) {
                            data.add(input);
                            data.add("\n");
                        }
                    } else {
                        for (int i = 0; i < number; i++) {
                            data.add(input);
                        }
                    }
                    StringBuilder builder = new StringBuilder();

                    for (int i = 0; i < data.size(); i++) {

                        int finalI = i;
//                        new Handler(getMainLooper()).post(() -> {
                            builder.append(data.get(finalI));
//                        });


                    }
                    etTextRepeates.append(builder);
                } else {
                    Toast.makeText(TextRepeaterActivity.this, getString(R.string.txt_please_enter_your_message_and_counter), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCopy.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(etTextRepeates.getText().toString())) {
                ConstantMethod.CopyToClipBoard(TextRepeaterActivity.this, etTextRepeates.getText().toString());
//                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//                clipboard.setText(etTextRepeates.getText().toString());
//                Toast.makeText(this, "Copy Succesfully..", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(TextRepeaterActivity.this, getString(R.string.txt_please_enter_your_message), Toast.LENGTH_SHORT).show();
            }
        });
        btnClear.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(etTextRepeates.getText().toString())) {
                etTextRepeates.getText().clear();
            } else {
                Toast.makeText(TextRepeaterActivity.this, getString(R.string.txt_please_enter_your_message), Toast.LENGTH_SHORT).show();
            }
        });
        btnShare.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(etTextRepeates.getText().toString())) {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, etTextRepeates.getText().toString());
                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(TextRepeaterActivity.this, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(TextRepeaterActivity.this, getString(R.string.txt_please_enter_your_message), Toast.LENGTH_SHORT).show();
            }
        });
        MyApplication.getInstance().displayInterstitialAds(this);
     //   MyApplication.getInstance().loadNativeAds(_fl_adplaceholder, TextRepeaterActivity.this, 1);
    }

//    public int countChar(String str) {
//        count = 0;
//        for (int i = 0; i < str.length(); i++) {
//            count++;
//        }
//
//        return count;
//    }

    public void backclickTextRepeater(View view) {
        onBackPressed();
    }
}