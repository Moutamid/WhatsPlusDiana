package com.moutamid.rurovision.fragment;

import static android.Manifest.permission.CAMERA;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.moutamid.rurovision.activity.ActSaveMedia;
import com.moutamid.rurovision.activity.MyApplication;
import com.moutamid.rurovision.R;
import com.moutamid.rurovision.activity.SearchProfileActivity;
import com.moutamid.rurovision.activity.TextRepeaterActivity;
import com.moutamid.rurovision.Utils.notifier.EventNotifier;
import com.moutamid.rurovision.Utils.notifier.EventState;
import com.moutamid.rurovision.Utils.notifier.IEventListener;
import com.moutamid.rurovision.Utils.notifier.NotifierFactory;
import com.moutamid.rurovision.activity.WhatsAppWebActivity;
import com.moutamid.rurovision.activity.WhatsappBoosterActivity;
import com.moutamid.rurovision.qr_code_maker.QRcodeMakerActivity;
import com.moutamid.rurovision.qr_code_reader.QRcodeReaderActivity;
import com.moutamid.rurovision.whatsapp_photos.WhatsappPhotosActivity;
import com.moutamid.rurovision.whatsapp_video.WhatsappVideosActivity;


public class HomeFragment extends Fragment implements View.OnClickListener, IEventListener {

    CardView cardWhatsappCleaner;
    CardView cardWhatsappStatus;
    CardView cardWhatsappSaved;
    CardView cardWhatsappWeb;
    CardView cardSearchProfile;
    CardView cardRepeater;
    CardView cardQrGenerator;
    CardView cardQrCodeReader;
    CardView cardWhatsAppBooster;
    ViewPager vpPosters;
    DotsIndicator arc_pi;
    private FrameLayout _fl_adplaceholder, flAdplaceholder2;

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    public static void takePermission(Activity activity, int request_code) {
        ActivityCompat.requestPermissions(activity,
                new String[]{CAMERA}, request_code);
    }

    public static boolean isPermissionGranted(Activity activity) {
        int camera = ContextCompat.checkSelfPermission(activity, CAMERA);
        return camera == PackageManager.PERMISSION_GRANTED;
    }

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        _fl_adplaceholder = view.findViewById(R.id.flAdplaceholder);
        flAdplaceholder2 = view.findViewById(R.id.flAdplaceholder2);

        cardWhatsappCleaner = view.findViewById(R.id.cardWhatsappCleaner);
        cardWhatsappStatus = view.findViewById(R.id.cardWhatsappStatus);
        cardWhatsappSaved = view.findViewById(R.id.cardWhatsappSaved);
        cardWhatsappWeb = view.findViewById(R.id.cardWhatsappWeb);
//        cardWhatsAppGalary = view.findViewById(R.id.cardWhatsAppGalary);
        cardSearchProfile = view.findViewById(R.id.cardSearchProfile);
        cardRepeater = view.findViewById(R.id.cardRepeater);
        cardQrGenerator = view.findViewById(R.id.cardQrGenerator);
        cardQrCodeReader = view.findViewById(R.id.cardQrCodeReader);
        cardWhatsAppBooster = view.findViewById(R.id.cardWhatsAppBooster);
        arc_pi = view.findViewById(R.id.arc_pi);

        vpPosters = view.findViewById(R.id.vpPosters);
        ViewPagerAdapter adapter = new ViewPagerAdapter(requireActivity().getSupportFragmentManager());
        vpPosters.setAdapter(adapter);
        arc_pi.setViewPager(vpPosters);
        vpPosters.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        cardWhatsappCleaner.setOnClickListener(this);
        cardWhatsappStatus.setOnClickListener(this);
        cardWhatsappSaved.setOnClickListener(this);
        cardWhatsappWeb.setOnClickListener(this);
//        cardWhatsAppGalary.setOnClickListener(this);
        cardSearchProfile.setOnClickListener(this);

        cardRepeater.setOnClickListener(this);

        cardQrGenerator.setOnClickListener(this);
        cardQrCodeReader.setOnClickListener(this);
        cardWhatsAppBooster.setOnClickListener(this);

        //registerAdsListener();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //MyApplication.getInstance().loadNativeFullAds(_fl_adplaceholder, requireActivity(), 1);
       // MyApplication.getInstance().loadNativeFullAds(flAdplaceholder2, requireActivity(), 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cardWhatsappCleaner:
                Intent mIntent = new Intent(requireActivity(), WhatsappPhotosActivity.class);
                startActivity(mIntent);
                break;
            case R.id.cardWhatsappStatus:
                Intent intent0 = new Intent(requireActivity(), WhatsappVideosActivity.class);
                startActivity(intent0);
                break;
            case R.id.cardWhatsappSaved:
                Intent intent11 = new Intent(requireActivity(), ActSaveMedia.class);
                startActivity(intent11);
                break;
            case R.id.cardSearchProfile:
                Intent intent4 = new Intent(requireActivity(), SearchProfileActivity.class);
                startActivity(intent4);
             /*   if (UserHelper.isNetworkConnected(requireActivity())) {
                    MyApplication.getInstance().displayInterstitialAds(requireActivity());
                } else {
                    startActivity(intent4);
                }*/
                break;

            case R.id.cardRepeater:
                Intent intentText = new Intent(requireActivity(), TextRepeaterActivity.class);
           /*     if (UserHelper.isNetworkConnected(requireActivity())) {
                    MyApplication.getInstance().displayInterstitialAds(requireActivity());
                } else {
                    startActivity(intentText);
                }*/
                startActivity(intentText);
                break;
            case R.id.cardWhatsAppBooster:
                Intent intentBooster = new Intent(requireActivity(), WhatsappBoosterActivity.class);
                //if (UserHelper.isNetworkConnected(requireActivity())) {
                  //  MyApplication.getInstance().displayInterstitialAds(requireActivity());
                //} else {
                    startActivity(intentBooster);
               // }
                break;

            case R.id.cardWhatsappWeb:
                Intent intent = new Intent(requireActivity(), WhatsAppWebActivity.class);
                //if (UserHelper.isNetworkConnected(requireActivity())) {
                  //  MyApplication.getInstance().displayInterstitialAds(requireActivity());
                //} else {
                    startActivity(intent);
                //}
                break;
            /*case R.id.cardWhatsAppGalary:
                Toast.makeText(requireActivity(), "WhatsApp Galary", Toast.LENGTH_SHORT).show();
                break;*/

            case R.id.cardQrGenerator:
                Intent intent2 = new Intent(requireActivity(), QRcodeMakerActivity.class);
               // if (UserHelper.isNetworkConnected(requireActivity())) {
                 //   MyApplication.getInstance().displayInterstitialAds(requireActivity());
                //} else {
                    startActivity(intent2);
                //}
                break;
            case R.id.cardQrCodeReader:
                Intent intent3 = new Intent(requireActivity(), QRcodeReaderActivity.class);
                if (!isPermissionGranted(requireActivity())) {
                    takePermission(requireActivity(), 1);
                } else {
                  //  if (UserHelper.isNetworkConnected(requireActivity())) {
                    //    MyApplication.getInstance().displayInterstitialAds(requireActivity());
                    //} else {
                        startActivity(intent3);
                    //}
                }
                break;
        }
    }

    private boolean isAppInstalled(String packageName) {
        PackageManager pm = requireActivity().getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {
        FragmentManager fragmentManager;

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            this.fragmentManager = fragmentManager;
        }

        @Override
        public Fragment getItem(int position) {
            FragPosterView fragment = new FragPosterView();
            Bundle bundle = new Bundle();
            bundle.putSerializable("CurrentPosition", position);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return 7;
        }
    }
}