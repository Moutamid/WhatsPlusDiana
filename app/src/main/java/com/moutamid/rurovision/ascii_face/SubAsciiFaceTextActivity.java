package com.moutamid.rurovision.ascii_face;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.moutamid.rurovision.activity.ConstantMethod;

import com.moutamid.rurovision.activity.MyApplication;
import com.moutamid.rurovision.R;
import com.moutamid.rurovision.ascii_face.adapter.HappyAdapter;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;

import java.util.ArrayList;

public class SubAsciiFaceTextActivity extends AppCompatActivity {
    RecyclerView firdtRecycler;
    HappyAdapter adapter;
    ArrayList<String> happyAsciiFace;
    TextView TVTitle;
    String[] AsciiFace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_ascii_face_text);

        RelativeLayout adViewBanner = findViewById(R.id.adViewBanner);
   //     MyApplication.getInstance().loadBanner(adViewBanner, SubAsciiFaceTextActivity.this);
        AudienceNetworkAds.initialize(this);

        AdView faceBookBanner = new AdView(this, getString(R.string.fb_ad_banner), AdSize.BANNER_HEIGHT_50);


        adViewBanner.addView(faceBookBanner);

        faceBookBanner.loadAd();

        MyApplication.getInstance().displayInterstitialAds(this);

        AsciiFace = getIntent().getStringArrayExtra("AsciiFace");

        TVTitle = findViewById(R.id.TVTitle);
        firdtRecycler = findViewById(R.id.firdtRecycler);
        happyAsciiFace = getEmojis(SubAsciiFaceTextActivity.this);

        firdtRecycler.setLayoutManager(new LinearLayoutManager(SubAsciiFaceTextActivity.this, RecyclerView.VERTICAL, false));

        adapter = new HappyAdapter(SubAsciiFaceTextActivity.this, AsciiFace, new EmojiListener() {
            @Override
            public void onWpShare(String emojiUnicode) {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, emojiUnicode);
                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(SubAsciiFaceTextActivity.this, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(SubAsciiFaceTextActivity.this, "direct wp share", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onShare(String emojiUnicode) {
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
                intent.putExtra(android.content.Intent.EXTRA_TEXT, emojiUnicode);
                startActivity(Intent.createChooser(intent, getString(R.string.share_using)));
                Toast.makeText(SubAsciiFaceTextActivity.this, "share", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCopy(String emojiUnicode) {
                ConstantMethod.CopyToClipBoard(SubAsciiFaceTextActivity.this,emojiUnicode);
            }
        });
        firdtRecycler.setAdapter(adapter);
    }

    public static ArrayList<String> getEmojis(Context context) {
        ArrayList<String> nConvertedEmojiList = new ArrayList<>();
        String[] nEmojiList = context.getResources().getStringArray(R.array.emoji);
        for (String emojiUnicode : nEmojiList) {
            nConvertedEmojiList.add(convertEmoji(emojiUnicode));
        }
        return nConvertedEmojiList;
    }
    private static String convertEmoji(String emoji) {
        String nReturnedEmoji;
        try {
            int convertEmojiToInt = Integer.parseInt(emoji.substring(2), 16);
            nReturnedEmoji = new String(Character.toChars(convertEmojiToInt));
        } catch (NumberFormatException e) {
            nReturnedEmoji = "";
        }
        return nReturnedEmoji;
    }

    public void backSubAscii(View view) {
        onBackPressed();
    }
}