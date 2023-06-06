package com.moutamid.rurovision.activity;

import static com.moutamid.rurovision.activity.RateDialog.MyPrefs;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.moutamid.rurovision.Utils.UserHelper;

public class BaseActivity extends AppCompatActivity {
    public static BroadcastReceiver mNetworkReceiver;
    public static Dialog dialog;

    /*private static void registerNetworkBroadcast(Context context) {
        context.registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public static void noInternetDialog(boolean value, Context context) {
        dialog.setContentView(R.layout.dialog_no_net);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;

        ImageView ivimageView = dialog.findViewById(R.id.imageView2);
        TextView ivRetry = dialog.findViewById(R.id.dialogRetryBtn);


        ivimageView.setImageResource(R.drawable.ic_no_internet);

        ivRetry.setOnClickListener(view -> {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                registerNetworkBroadcast(context);
            }
        });


        if (value) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Log.i("ShowManager", "Dismiss");
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    ((Activity) context).finish();
                    Intent intent = ((Activity) context).getIntent();
                    ((Activity) context).overridePendingTransition(0, 0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    ((Activity) context).finish();
                    ((Activity) context).overridePendingTransition(0, 0);
                    context.startActivity(intent);
                }
            }, 10);
        } else {
            try {
                if (dialog != null && !dialog.isShowing()) {
                    dialog.show();
                    Log.i("ShowManager", "Show");
                }

            } catch (WindowManager.BadTokenException e) {
                //use a log message
                Log.i("WindowManager", e.getMessage());
            }
        }
    }*/

    public void hideSystemBars() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        WindowInsetsControllerCompat windowInsetsController = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (windowInsetsController == null) {
            return;
        }
        // Configure the behavior of the hidden system bars
        windowInsetsController.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
    }

    public void showRate() {
        SharedPreferences sharedpreferences = getSharedPreferences(MyPrefs, Context.MODE_PRIVATE);
        if (!sharedpreferences.getBoolean(RateDialog.SHOW_LATER, false)) {
            if (MyApplication.isReviewOn()) {
                if (!UserHelper.isRateDone()) {
                    if (MyApplication.getInstance().showRatingDialog(BaseActivity.this, feedback -> ShowSnackbar(), view -> {
                    })) ;
                }
            }
        }
    }

    private void ShowSnackbar() {
        finish();
    }

    public void setStatusMode(Activity activity) {
//        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        View view = getWindow().getDecorView();
//        switch (nightModeFlags) {
//            case Configuration.UI_MODE_NIGHT_YES:
//
//            case Configuration.UI_MODE_NIGHT_UNDEFINED:
//                if (AppCompatDelegate.MODE_NIGHT_NO == setting.getTheme()) {
//                    view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//                } else if (AppCompatDelegate.MODE_NIGHT_YES == setting.getTheme()) {
//                    view.setSystemUiVisibility(view.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//                }
//                view.setSystemUiVisibility(view.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//                break;
//
//            case Configuration.UI_MODE_NIGHT_NO:
                view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//                break;
//        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialog = new Dialog(BaseActivity.this);
//        mNetworkReceiver = new NetworkStateReceiver();
//        registerNetworkBroadcast(BaseActivity.this);
    }

    /*private void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }*/

    /*@Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }*/
}
