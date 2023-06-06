package com.moutamid.rurovision.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.moutamid.rurovision.activity.FontToEmojiActivity;
import com.moutamid.rurovision.activity.InfoWhatsApp;

import com.moutamid.rurovision.activity.MyApplication;
import com.moutamid.rurovision.R;
import com.moutamid.rurovision.Utils.notifier.EventNotifier;
import com.moutamid.rurovision.Utils.notifier.EventState;
import com.moutamid.rurovision.Utils.notifier.IEventListener;
import com.moutamid.rurovision.Utils.notifier.NotifierFactory;
import com.moutamid.rurovision.ascii_face.AsciiFaceTextActivity;
import com.moutamid.rurovision.card_caption.CardCaptionActivity;
import com.moutamid.rurovision.reply.ReplyActivity;
import com.moutamid.rurovision.shayri.ShayriActivity;
import com.moutamid.rurovision.sticker.StickerActivity;

public class TrandingToolsFragment extends Fragment implements View.OnClickListener , IEventListener {

    CardView cardFontEmoji;
    CardView cardSticker;
    CardView cardReply;
    CardView cardCaption;
    CardView cardShayri;
    CardView cardAsciiFaceText;
    CardView cardInfoWhatsapp;
    private FrameLayout _fl_adplaceholder, flAdplaceholder2;

    @Override
    public int eventNotify(int eventType, final Object eventObject) {
        int eventState = EventState.EVENT_IGNORED;
        if (eventType == EventState.EVENT_AD_LOADED_NATIVE) {
            eventState = EventState.EVENT_PROCESSED;
            requireActivity().runOnUiThread(() -> new Handler(Looper.myLooper()).postDelayed(() -> MyApplication.getInstance().loadNativeFullAds(_fl_adplaceholder, requireActivity(), 1), 500));
            requireActivity().runOnUiThread(() -> new Handler(Looper.myLooper()).postDelayed(() -> MyApplication.getInstance().loadNativeFullAds(flAdplaceholder2, requireActivity(), 1), 500));
        }

        return eventState;
    }

    private void registerAdsListener() {
        EventNotifier notifier = NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_AD_STATUS);
        notifier.registerListener(this, 1000);
    }


    public static TrandingToolsFragment newInstance(String param1, String param2) {
        TrandingToolsFragment fragment = new TrandingToolsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tranding_tools, container, false);

        _fl_adplaceholder = view.findViewById(R.id.flAdplaceholder);
        flAdplaceholder2 = view.findViewById(R.id.flAdplaceholder2);

        cardFontEmoji = view.findViewById(R.id.cardFontEmoji);
        cardSticker = view.findViewById(R.id.cardSticker);
        cardReply = view.findViewById(R.id.cardReply);
        cardCaption = view.findViewById(R.id.cardCaption);
        cardShayri = view.findViewById(R.id.cardShayri);
        cardAsciiFaceText = view.findViewById(R.id.cardAsciiFaceText);
        cardInfoWhatsapp = view.findViewById(R.id.cardInfoWhatsapp);

        cardFontEmoji.setOnClickListener(this);
        cardSticker.setOnClickListener(this);
        cardReply.setOnClickListener(this);
        cardCaption.setOnClickListener(this);
        cardShayri.setOnClickListener(this);
        cardAsciiFaceText.setOnClickListener(this);
        cardInfoWhatsapp.setOnClickListener(this);

       // registerAdsListener();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //MyApplication.getInstance().loadNativeFullAds(_fl_adplaceholder, requireActivity(), 1);
        //MyApplication.getInstance().loadNativeFullAds(flAdplaceholder2, requireActivity(), 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cardFontEmoji:
                Intent intent0 = new Intent(requireActivity(), FontToEmojiActivity.class);
               // if (UserHelper.isNetworkConnected(requireActivity())) {
                 //   MyApplication.getInstance().displayInterstitialAds(requireActivity());
               // } else {
                    startActivity(intent0);
                //}
                break;
            case R.id.cardSticker:
                Intent intent2 = new Intent(requireActivity(), StickerActivity.class);
            //    if (UserHelper.isNetworkConnected(requireActivity())) {
              //      MyApplication.getInstance().displayInterstitialAds(requireActivity());
               // } else {
                    startActivity(intent2);
                //}
                break;
            case R.id.cardReply:
                Intent intent3 = new Intent(requireActivity(), ReplyActivity.class);
                //if (UserHelper.isNetworkConnected(requireActivity())) {
                  //  MyApplication.getInstance().displayInterstitialAds(requireActivity());
                //} else {
                    startActivity(intent3);
                //}
                break;
            case R.id.cardCaption:
                Intent intent4 = new Intent(requireActivity(), CardCaptionActivity.class);
           //     if (UserHelper.isNetworkConnected(requireActivity())) {
             //       MyApplication.getInstance().displayInterstitialAds(requireActivity());
               // } else {
                    startActivity(intent4);
                //}
                break;
            case R.id.cardShayri:
                Intent intent5 = new Intent(requireActivity(), ShayriActivity.class);
             //   if (UserHelper.isNetworkConnected(requireActivity())) {
               //     MyApplication.getInstance().displayInterstitialAds(requireActivity());
               // } else {
                    startActivity(intent5);
                //}
                break;
            case R.id.cardAsciiFaceText:
                Intent intent1 = new Intent(requireActivity(), AsciiFaceTextActivity.class);
              //  if (UserHelper.isNetworkConnected(requireActivity())) {
                //    MyApplication.getInstance().displayInterstitialAds(requireActivity());
                //} else {
                    startActivity(intent1);
                //}
//                startActivity(intent1);
                break;
            case R.id.cardInfoWhatsapp:
                Intent intent6 = new Intent(requireActivity(), InfoWhatsApp.class);
                //if (UserHelper.isNetworkConnected(requireActivity())) {
              //      MyApplication.getInstance().displayInterstitialAds(requireActivity());
               // } else {
                    startActivity(intent6);
                //}
                break;
        }
    }
}