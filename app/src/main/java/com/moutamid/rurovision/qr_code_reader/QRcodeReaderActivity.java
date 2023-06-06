package com.moutamid.rurovision.qr_code_reader;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.google.zxing.Result;
import com.moutamid.rurovision.activity.MyApplication;
import com.moutamid.rurovision.R;


import java.util.ArrayList;
import java.util.Iterator;

public class QRcodeReaderActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    public static final String EXTRA_QUERY = "query";
    public static final String TEXT_ENTRY = "text";
    private static final String TAG = "QRReaderActivity";

    public String barcode_result;
    protected int camera_id = -1;
    private ArrayList<Integer> selected_indices;
    public ViewGroup viewGroup;

    public ZXingScannerView zXingScannerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_reader);

        RelativeLayout adViewBanner = findViewById(R.id.adViewBanner);
      //  MyApplication.getInstance().loadBanner(adViewBanner, QRcodeReaderActivity.this);

        AudienceNetworkAds.initialize(this);

        AdView faceBookBanner = new AdView(this, getString(R.string.fb_ad_banner), AdSize.BANNER_HEIGHT_50);


        adViewBanner.addView(faceBookBanner);

        faceBookBanner.loadAd();
        MyApplication.getInstance().displayInterstitialAds(this);
        init();
    }
    private void init() {
        viewGroup = (ViewGroup) findViewById(R.id.fl_camera);
        zXingScannerView = new ZXingScannerView(this);
        viewGroup.addView(zXingScannerView);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void setupBarcodeFormats() {
        ArrayList arrayList = new ArrayList();

        if (selected_indices == null || selected_indices.isEmpty()) {
            selected_indices = new ArrayList<>();
            for (int i = 0; i < ZXingScannerView.ALL_FORMATS.size(); i++) {
                selected_indices.add(Integer.valueOf(i));
            }
        }
        Iterator<Integer> it = selected_indices.iterator();
        while (it.hasNext()) {
            arrayList.add(ZXingScannerView.ALL_FORMATS.get(it.next().intValue()));
        }

        if (zXingScannerView != null) {
            zXingScannerView.setFormats(arrayList);
        }
    }

    @Override
    public void handleResult(Result result) {
        barcode_result = result.getText();
        Log.e(TAG, result.getText());
        Log.e(TAG, result.getBarcodeFormat().toString());
        new ToneGenerator(5, 100).startTone(24);
        final Dialog dialog = new Dialog(this, R.style.ThemeWithRoundShape);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_qr_result);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setLayout(-1, -2);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        TextView tv_search = dialog.findViewById(R.id.tv_search);
        TextView tv_result = dialog.findViewById(R.id.tv_result);

        if (barcode_result.startsWith("tel")) {
            tv_search.setText("Call");
        }
        tv_result.setText(barcode_result);

        ((TextView) dialog.findViewById(R.id.tv_share)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("text/*");
                intent.putExtra("android.intent.extra.SUBJECT", "");
                intent.putExtra("android.intent.extra.TEXT", barcode_result);
                startActivity(Intent.createChooser(intent, "Share text using"));
                dialog.dismiss();
            }
        });
        ((TextView) dialog.findViewById(R.id.tv_search)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (barcode_result.startsWith("tel")) {
                    intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(barcode_result));
                } else {
                    intent = new Intent("android.intent.action.WEB_SEARCH");
                    intent.setClassName("com.google.android.googlequicksearchbox", "com.google.android.googlequicksearchbox.SearchActivity");
                    intent.putExtra(EXTRA_QUERY, barcode_result);
                }
                startActivity(intent);
                dialog.dismiss();
            }
        });
        ((ImageView) dialog.findViewById(R.id.iv_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText(TEXT_ENTRY, barcode_result));
                Toast.makeText(QRcodeReaderActivity.this, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
                if (zXingScannerView == null) {

                    zXingScannerView = new ZXingScannerView(QRcodeReaderActivity.this);
                    viewGroup.addView(zXingScannerView);
                }
                zXingScannerView.setResultHandler(QRcodeReaderActivity.this);
                zXingScannerView.startCamera(camera_id);
                setupBarcodeFormats();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onResume() {
        if (zXingScannerView == null) {
            zXingScannerView = new ZXingScannerView(this);
            viewGroup.addView(zXingScannerView);
        }
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera(camera_id);
        setupBarcodeFormats();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        zXingScannerView.stopCamera();
        super.onDestroy();
    }
}