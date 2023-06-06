package com.moutamid.rurovision.reply;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moutamid.rurovision.activity.ConstantMethod;

import com.moutamid.rurovision.activity.MyApplication;
import com.moutamid.rurovision.R;
import com.moutamid.rurovision.Utils.SharedPrefsHelper;
import com.moutamid.rurovision.Utils.notifier.EventNotifier;
import com.moutamid.rurovision.Utils.notifier.EventState;
import com.moutamid.rurovision.Utils.notifier.IEventListener;
import com.moutamid.rurovision.Utils.notifier.NotifierFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ReplyActivity extends AppCompatActivity implements IEventListener {

    RecyclerView replyRecycler;
    AdapterReply adapterReply;
    FloatingActionButton addFloatingBtn;
    ArrayList<String> parentArrayList = new ArrayList<>();
    private FrameLayout _fl_adplaceholder;

    @Override
    public int eventNotify(int eventType, final Object eventObject) {
        int eventState = EventState.EVENT_IGNORED;
        if (eventType == EventState.EVENT_AD_LOADED_NATIVE) {
            eventState = EventState.EVENT_PROCESSED;
            runOnUiThread(() -> new Handler(Looper.myLooper()).postDelayed(() -> MyApplication.getInstance().loadNativeAds(_fl_adplaceholder, ReplyActivity.this, 1), 500));
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
        setContentView(R.layout.activity_reply);

        _fl_adplaceholder = findViewById(R.id.flAdplaceholder);
        registerAdsListener();

        replyRecycler = findViewById(R.id.replyRecycler);
        addFloatingBtn = findViewById(R.id.addFloatingBtn);

        RelativeLayout adViewBanner = findViewById(R.id.adViewBanner);
       // MyApplication.getInstance().loadBanner(adViewBanner, ReplyActivity.this);

        AudienceNetworkAds.initialize(this);

        AdView faceBookBanner = new AdView(this, getString(R.string.fb_ad_banner), AdSize.BANNER_HEIGHT_50);


        adViewBanner.addView(faceBookBanner);

        faceBookBanner.loadAd();
        MyApplication.getInstance().displayInterstitialAds(this);

        replyRecycler.setLayoutManager(new LinearLayoutManager(ReplyActivity.this));

        notifyItems();

        addFloatingBtn.setOnClickListener(v -> {

            ViewDialog alert = new ViewDialog();
            alert.showDialog(ReplyActivity.this);

        });

     //   MyApplication.getInstance().loadNativeAds(_fl_adplaceholder, ReplyActivity.this, 1);
    }
    public void notifyItems() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();

        if (parentArrayList == null) {
            parentArrayList = new ArrayList<>();
        }

        parentArrayList.clear();

        parentArrayList.add("Hello...");
        parentArrayList.add("How are you ?");
        parentArrayList.add("I'm Good, What about you");
        parentArrayList.add("Violence. Violence. Violence. I don't like it!");
        parentArrayList.add("I'm From Surat");

        String strFromDatabase = SharedPrefsHelper.getInstance().getString("LOCAL_DATA", "[]");
        ArrayList<String> stringArrayList = gson.fromJson(strFromDatabase, type);

        for (int i = 0; i < stringArrayList.size(); i++) {
            parentArrayList.add(stringArrayList.get(i));
        }

        adapterReply = new AdapterReply(ReplyActivity.this, parentArrayList, new ReplyListener() {
            @Override
            public void onClickEdit(String s, int layoutrPosition) {
                ViewDialog alert = new ViewDialog();
                alert.showDialog(ReplyActivity.this,s,layoutrPosition);
            }

            @Override
            public void onClickCopy(String s) {
                ConstantMethod.CopyToClipBoard(ReplyActivity.this,s);
            }

            @Override
            public void onClickShare(String s) {
                if (!TextUtils.isEmpty(s)) {
                    Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                    whatsappIntent.setType("text/plain");
                    whatsappIntent.setPackage("com.whatsapp");
                    whatsappIntent.putExtra(Intent.EXTRA_TEXT,s);
                    try {
                        startActivity(whatsappIntent);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(ReplyActivity.this, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ReplyActivity.this, "Empty string.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        replyRecycler.setAdapter(adapterReply);
    }

    public class ViewDialog {

        public void showDialog(Activity activity){
            final Dialog dialog = new Dialog(activity);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.reply_dialog);

            EditText etTextMsg = dialog.findViewById(R.id.etTextReplyMsg);
            Button dialogButton1 = (Button) dialog.findViewById(R.id.btnCancel);
            Button dialogButton2 = (Button) dialog.findViewById(R.id.btnSave);
            dialogButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialogButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<String>>() {
                    }.getType();

                    String strFromDatabase = SharedPrefsHelper.getInstance().getString("LOCAL_DATA", "[]");
                    ArrayList<String> stringArrayList = gson.fromJson(strFromDatabase, type);

                    if (!TextUtils.isEmpty(etTextMsg.getText().toString())) {
                        stringArrayList.add(etTextMsg.getText().toString());
                        String saveData = gson.toJson(stringArrayList);
                        SharedPrefsHelper.getInstance().setString("LOCAL_DATA", saveData);

                        if (adapterReply != null) {
                            notifyItems();
                        }
                    }else {
                        Toast.makeText(activity, "Please enter message..", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });

            dialog.show();

        }

        public void showDialog(ReplyActivity replyActivity, String s, int layoutrPosition) {
            final Dialog dialog = new Dialog(replyActivity);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.reply_dialog);

            EditText etTextMsg = dialog.findViewById(R.id.etTextReplyMsg);
            etTextMsg.setText(s);
            Button dialogButton1 = (Button) dialog.findViewById(R.id.btnCancel);
            Button dialogButton2 = (Button) dialog.findViewById(R.id.btnSave);
            dialogButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialogButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<String>>() {
                    }.getType();

                    String strFromDatabase = SharedPrefsHelper.getInstance().getString("LOCAL_DATA", "[]");
                    ArrayList<String> stringArrayList = gson.fromJson(strFromDatabase, type);


                    if (!TextUtils.isEmpty(etTextMsg.getText().toString())) {
                        stringArrayList.remove(layoutrPosition-5);
                        stringArrayList.add(layoutrPosition-5,etTextMsg.getText().toString());
//                        stringArrayList.add(etTextMsg.getText().toString());
                        String saveData = gson.toJson(stringArrayList);
                        SharedPrefsHelper.getInstance().setString("LOCAL_DATA", saveData);

                        if (adapterReply != null) {
                            notifyItems();
                        }
                    }else {
                        Toast.makeText(replyActivity, "Please enter message..", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });

            dialog.show();
        }
    }

    public void backReply(View view) {
        onBackPressed();
    }
}