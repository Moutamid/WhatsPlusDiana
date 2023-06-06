package com.moutamid.rurovision.qr_code_maker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.moutamid.rurovision.activity.MyApplication;
import com.moutamid.rurovision.R;
import com.moutamid.rurovision.Utils.notifier.EventNotifier;
import com.moutamid.rurovision.Utils.notifier.EventState;
import com.moutamid.rurovision.Utils.notifier.IEventListener;
import com.moutamid.rurovision.Utils.notifier.NotifierFactory;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class QRcodeMakerActivity extends AppCompatActivity implements View.OnClickListener , IEventListener {

    private static final String TAG = "QRGenerator";
    public EditText et_value;
    protected ImageView iv_back;
    private TextView iv_generate;
    private ImageView iv_qrcode;
    private TextView iv_refresh;
    private Bitmap qrImg;
    private File qrPath;
    public String qrType;
    protected String qr_value;
    private Spinner qrtype_spinner;
    private String[] spinner_item = {"Text", "E-mail", "Phone", "Sms", "Url_Key"};
    private FrameLayout _fl_adplaceholder;

    @Override
    public int eventNotify(int eventType, final Object eventObject) {
        int eventState = EventState.EVENT_IGNORED;
        if (eventType == EventState.EVENT_AD_LOADED_NATIVE) {
            eventState = EventState.EVENT_PROCESSED;
            runOnUiThread(() -> new Handler(Looper.myLooper()).postDelayed(() -> MyApplication.getInstance().loadNativeAds(_fl_adplaceholder, QRcodeMakerActivity.this, 1), 500));
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
        setContentView(R.layout.activity_qrcode_maker);


        RelativeLayout adViewBanner = findViewById(R.id.adViewBanner);
    //    MyApplication.getInstance().loadBanner(adViewBanner, QRcodeMakerActivity.this);

        AudienceNetworkAds.initialize(this);

        AdView faceBookBanner = new AdView(this, getString(R.string.fb_ad_banner), AdSize.BANNER_HEIGHT_50);


        adViewBanner.addView(faceBookBanner);

        faceBookBanner.loadAd();


        _fl_adplaceholder = findViewById(R.id.flAdplaceholder);
  //      registerAdsListener();

        init();
        MyApplication.getInstance().displayInterstitialAds(this);
//        MyApplication.getInstance().loadNativeAds(_fl_adplaceholder, QRcodeMakerActivity.this, 1);
    }
    private void init() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        qrtype_spinner = (Spinner) findViewById(R.id.qrtype_spinner);
        et_value = (EditText) findViewById(R.id.et_value);
        iv_qrcode = (ImageView) findViewById(R.id.iv_qrcode);
        iv_generate = (TextView) findViewById(R.id.iv_generate);
        iv_refresh = (TextView) findViewById(R.id.iv_refresh);
        qrType = Contents.Type.TEXT;
        initSpinner();
    }

    private void initSpinner() {
        qrtype_spinner.setAdapter(new CustomSpinnerAdapter(spinner_item));
        qrtype_spinner.setPopupBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.app_light_color)));

        qrtype_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                if (i == 0) {
                    qrType = Contents.Type.TEXT;
                    et_value.setHint("Enter your text");
                    et_value.setInputType(InputType.TYPE_CLASS_TEXT);
                } else if (i == 1) {
                    qrType = Contents.Type.EMAIL;
                    et_value.setHint("Enter your e-mail");
                    et_value.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                } else if (i == 2) {
                    qrType = Contents.Type.PHONE;
                    et_value.setHint("Enter your phone");
                    et_value.setInputType(InputType.TYPE_CLASS_PHONE);
                } else if (i == 3) {
                    qrType = Contents.Type.SMS;
                    et_value.setHint("Enter your sms");
                    et_value.setInputType(InputType.TYPE_CLASS_TEXT);
                } else if (i == 4) {
                    qrType = Contents.URL_KEY;
                    et_value.setHint("Enter your url_key");
                    et_value.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    qrType = Contents.Type.TEXT;
                    et_value.setHint("Enter your text");
                    et_value.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }
        });
    }

    public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
        private String[] array;

        @Override
        public long getItemId(int i) {
            return (long) i;
        }

        public CustomSpinnerAdapter(String[] strArr) {
            array = strArr;
        }

        @Override
        public int getCount() {
            return array.length;
        }

        @Override
        public Object getItem(int i) {
            return array[i];
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Typeface createFromAsset = Typeface.createFromAsset(getAssets(), "inter_medium.ttf");
            TextView textView = new TextView(QRcodeMakerActivity.this);
            textView.setText(array[i]);
            textView.setTextSize(14.0f);
            textView.setTypeface(createFromAsset);
//            textView.setBackground(getResources().getDrawable(R.drawable.round_shape));
//            textView.setBackgroundTintList(new ColorStateList(new int[][]{new int[0]}, new int[]{getResources().getColor(R.color.light_txt_color)}));
            textView.setTextColor(getResources().getColor(R.color.black));
            textView.setGravity(17);
            textView.setPadding(43, 15, 43, 15);
            return textView;
        }

        @Override
        public View getDropDownView(int i, View view, ViewGroup viewGroup) {
            Typeface createFromAsset = Typeface.createFromAsset(getAssets(), "inter_medium.ttf");
            TextView textView = new TextView(QRcodeMakerActivity.this);
            textView.setText(array[i]);
            textView.setTextSize(16.0f);
            textView.setTypeface(createFromAsset);
            textView.setTextColor(getResources().getColor(R.color.black));
            textView.setGravity(16);
            textView.setPadding(60, 35, 0x00A0, 35);
            return textView;
        }
    }

    @Override
    public void onClick(View view) {
        Uri uri;
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                return;
            case R.id.iv_generate:
                qr_value = et_value.getText().toString();
                hideSoftKeyboard(view);
                if (qr_value.length() > 0) {
                    Display defaultDisplay = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                    Point point = new Point();
                    defaultDisplay.getSize(point);
                    int x = point.x;
                    int y = point.y;
                    if (x >= y) {
                        x = y;
                    }
                    try {

                        Intent intent = new Intent("com.google.zxing.client.android.ENCODE");
                        intent.putExtra(Intents.Encode.FORMAT, BarcodeFormat.QR_CODE.toString());
                        intent.putExtra(Intents.Encode.TYPE, qrType);
                        intent.putExtra(Intents.Encode.DATA, qr_value);
                        QRCodeEncoder qRCodeEncoder = new QRCodeEncoder(this, intent, (x * 3) / 4, false);
                        Log.e(TAG, "onClick: " + qrType);
                        qrImg = qRCodeEncoder.encodeAsBitmap();
                        iv_qrcode.setVisibility(View.VISIBLE);
                        iv_qrcode.setImageBitmap(qrImg);
                        iv_generate.setVisibility(View.GONE);
                        iv_refresh.setVisibility(View.VISIBLE);
                        if(qrImg!=null)saveBitmap();
                        return;
                    } catch (WriterException e) {
                        e.printStackTrace();
                        return;
                    }
                } else {
                    et_value.setError("Required");
                    return;
                }
            case R.id.iv_qrcode:
                try {
                    File file = new File(qrPath.getPath());
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("image/*");
                    if (Build.VERSION.SDK_INT >= 23) {
                        uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
                    } else {
                        uri = Uri.fromFile(file);
                    }
                    intent.putExtra("android.intent.extra.STREAM", uri);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(intent, "Share image using"));
                    return;
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return;
                }
            case R.id.iv_refresh:
                et_value.setText("");
                iv_qrcode.setVisibility(View.GONE);
                iv_refresh.setVisibility(View.GONE);
                iv_generate.setVisibility(View.VISIBLE);

                return;
            default:
                return;
        }
    }

    private File makeDir() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + getResources().getString(R.string.app_name) + "/QRCode/");
        if (!file.exists() && !file.mkdirs()) {
            return null;
        }
        File file2 = new File(file.getPath() + File.separator + "temp.jpg");
        qrPath = file2;
        return file2;
    }

    public void saveBitmap() {
        File makeDir = makeDir();
        if (makeDir != null) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(makeDir);
                qrImg.compress(Bitmap.CompressFormat.PNG, 80, fileOutputStream);
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void hideSoftKeyboard(View view) {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}