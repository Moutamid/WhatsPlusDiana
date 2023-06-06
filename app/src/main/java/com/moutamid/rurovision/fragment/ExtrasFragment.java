package com.moutamid.rurovision.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.moutamid.rurovision.activity.AboutUsActivity;
import com.moutamid.rurovision.activity.MyApplication;
import com.moutamid.rurovision.R;
import com.moutamid.rurovision.Utils.notifier.EventNotifier;
import com.moutamid.rurovision.Utils.notifier.EventState;
import com.moutamid.rurovision.Utils.notifier.IEventListener;
import com.moutamid.rurovision.Utils.notifier.NotifierFactory;


public class ExtrasFragment extends Fragment implements View.OnClickListener, IEventListener {

    TextView tvAboutUs;
    TextView tvContactUs;
    TextView ShareNow;
    TextView tvRateUs;
    private FrameLayout _fl_adplaceholder;

    public static ExtrasFragment newInstance(String param1, String param2) {
        ExtrasFragment fragment = new ExtrasFragment();
        return fragment;
    }

    @Override
    public int eventNotify(int eventType, final Object eventObject) {
        int eventState = EventState.EVENT_IGNORED;
        if (eventType == EventState.EVENT_AD_LOADED_NATIVE) {
            eventState = EventState.EVENT_PROCESSED;
            requireActivity().runOnUiThread(() -> new Handler(Looper.myLooper()).postDelayed(() -> MyApplication.getInstance().loadNativeFullAds(_fl_adplaceholder, requireActivity(), 1), 500));
        }

        return eventState;
    }

    private void registerAdsListener() {
        EventNotifier notifier = NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_AD_STATUS);
        notifier.registerListener(this, 1000);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_extras, container, false);

        _fl_adplaceholder = view.findViewById(R.id.flAdplaceholder);

        tvAboutUs = view.findViewById(R.id.tvAboutUs);
        tvAboutUs.setOnClickListener(this);
        tvContactUs = view.findViewById(R.id.tvContactUs);
        tvContactUs.setOnClickListener(this);
        ShareNow = view.findViewById(R.id.ShareNow);
        ShareNow.setOnClickListener(this);
        tvRateUs = view.findViewById(R.id.tvRateUs);
        tvRateUs.setOnClickListener(this);

        registerAdsListener();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MyApplication.getInstance().loadNativeFullAds(_fl_adplaceholder, requireActivity(), 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvAboutUs:
                startActivity(new Intent(requireActivity(), AboutUsActivity.class));
                break;
            case R.id.tvContactUs:
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"bigteach26@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, requireActivity().getString(R.string.app_name));
                intent.putExtra(Intent.EXTRA_TEXT, requireActivity().getPackageName() + "\nYour message here...");
                startActivity(intent);

                break;
            case R.id.ShareNow:
                ShareApp(requireActivity());
                break;
            case R.id.tvRateUs:
                RateApp(requireActivity());
                break;
        }
    }

    public void ShareApp(Context context) {
        final String appLink = "\nhttps://play.google.com/store/apps/details?id=" + context.getPackageName();
        Intent sendInt = new Intent(Intent.ACTION_SEND);
        sendInt.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
        sendInt.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_app_message) + appLink);
        sendInt.setType("text/plain");
        context.startActivity(Intent.createChooser(sendInt, "Share"));
    }

    public void RateApp(Context context) {
        final String appName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appName)));
        } catch (ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
        }
    }
}