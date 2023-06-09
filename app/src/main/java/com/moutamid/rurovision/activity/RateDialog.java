package com.moutamid.rurovision.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialog;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.moutamid.rurovision.R;
import com.moutamid.rurovision.Utils.UserHelper;


public class RateDialog extends AppCompatDialog implements RatingBar.OnRatingBarChangeListener, View.OnClickListener {

    public static final String SHOW_NEVER = "show_never";
    public static final String SHOW_LATER = "show_later";
    private static final String SESSION_COUNT = "session_count";
    public static String MyPrefs = "RatingDialog";
    private SharedPreferences sharedpreferences;

    private final Context context;
    private final Builder builder;
    private TextView tvTitle, tvNegative, tvPositive, tvFeedback, tvSubmit, tvCancel;
    private RatingBar ratingBar;
    private ImageView ivIcon;
    private EditText etFeedback;
    private LinearLayout ratingButtons, feedbackButtons;
    private final int session;
    private boolean thresholdPassed = true;

    public RateDialog(Context context, Builder builder) {
        super(context);
        this.context = context;
        this.builder = builder;

        this.session = builder.session;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.dialog_rating);

        tvTitle = findViewById(R.id.dialog_rating_title);
        tvNegative = findViewById(R.id.dialog_rating_button_negative);
        tvPositive = findViewById(R.id.dialog_rating_button_positive);
        tvFeedback = findViewById(R.id.dialog_rating_feedback_title);
        tvSubmit = findViewById(R.id.dialog_rating_button_feedback_submit);
        tvCancel = findViewById(R.id.dialog_rating_button_feedback_cancel);
        ratingBar = findViewById(R.id.dialog_rating_rating_bar);
        ivIcon = findViewById(R.id.dialog_rating_icon);
        etFeedback = findViewById(R.id.dialog_rating_feedback);
        ratingButtons = findViewById(R.id.dialog_rating_buttons);
        feedbackButtons = findViewById(R.id.dialog_rating_feedback_buttons);

        init();
    }

    private void init() {

        tvTitle.setText(builder.title);
        tvPositive.setText(builder.positiveText);
        tvNegative.setText(builder.negativeText);

        tvFeedback.setText(builder.formTitle);
        tvSubmit.setText(builder.submitText);
        tvCancel.setText(builder.cancelText);
        etFeedback.setHint(builder.feedbackFormHint);

        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        int color = typedValue.data;

        tvTitle.setTextColor(builder.titleTextColor != 0 ? ContextCompat.getColor(context, builder.titleTextColor) : ContextCompat.getColor(context, R.color.black));
        tvPositive.setTextColor(builder.positiveTextColor != 0 ? ContextCompat.getColor(context, builder.positiveTextColor) : color);
        tvNegative.setTextColor(builder.negativeTextColor != 0 ? ContextCompat.getColor(context, builder.negativeTextColor) : ContextCompat.getColor(context, R.color.cpb_grey));

        tvFeedback.setTextColor(builder.titleTextColor != 0 ? ContextCompat.getColor(context, builder.titleTextColor) : ContextCompat.getColor(context, R.color.black));
        tvSubmit.setTextColor(builder.positiveTextColor != 0 ? ContextCompat.getColor(context, builder.positiveTextColor) : color);
        tvCancel.setTextColor(builder.negativeTextColor != 0 ? ContextCompat.getColor(context, builder.negativeTextColor) : ContextCompat.getColor(context, R.color.green));

        if (builder.feedBackTextColor != 0) {
            etFeedback.setTextColor(ContextCompat.getColor(context, builder.feedBackTextColor));
        }

        if (builder.positiveBackgroundColor != 0) {
            tvPositive.setBackgroundResource(builder.positiveBackgroundColor);
            tvSubmit.setBackgroundResource(builder.positiveBackgroundColor);

        }
        if (builder.negativeBackgroundColor != 0) {
            tvNegative.setBackgroundResource(builder.negativeBackgroundColor);
            tvCancel.setBackgroundResource(builder.negativeBackgroundColor);
        }

        if (builder.ratingBarColor != 0) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
                stars.getDrawable(2).setColorFilter(ContextCompat.getColor(context, builder.ratingBarColor), PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(1).setColorFilter(ContextCompat.getColor(context, builder.ratingBarColor), PorterDuff.Mode.SRC_ATOP);
                    int ratingBarBackgroundColor = builder.ratingBarBackgroundColor != 0 ? builder.ratingBarBackgroundColor : R.color.green_50;
                stars.getDrawable(0).setColorFilter(ContextCompat.getColor(context, ratingBarBackgroundColor), PorterDuff.Mode.SRC_ATOP);
            } else {
                Drawable stars = ratingBar.getProgressDrawable();
                DrawableCompat.setTint(stars, ContextCompat.getColor(context, builder.ratingBarColor));
            }
        }

        Drawable d = context.getPackageManager().getApplicationIcon(context.getApplicationInfo());
        ivIcon.setImageDrawable(builder.drawable != null ? builder.drawable : d);

        ratingBar.setOnRatingBarChangeListener(this);
        tvPositive.setOnClickListener(this);
        tvNegative.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
        tvCancel.setOnClickListener(this);

        if (session == 1) {
            tvNegative.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.dialog_rating_button_negative) {

            cancel();
            showNever();

        } else if (view.getId() == R.id.dialog_rating_button_positive) {
            sharedpreferences = context.getSharedPreferences(MyPrefs, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(SHOW_LATER, true);
            editor.commit();

            cancel();

        } else if (view.getId() == R.id.dialog_rating_button_feedback_submit) {

            String feedback = etFeedback.getText().toString().trim();
            if (TextUtils.isEmpty(feedback)) {

                Animation shake = AnimationUtils.loadAnimation(context, R.anim.a_shake);
                etFeedback.startAnimation(shake);
                return;
            }

            if (builder.ratingDialogFormListener != null) {
                builder.ratingDialogFormListener.onFormSubmitted(feedback);
            }

            dismiss();
            showNever();

        } else if (view.getId() == R.id.dialog_rating_button_feedback_cancel) {

            cancel();

        }

    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {


        if (ratingBar.getRating() >= 4.1) {
            thresholdPassed = true;

            if (builder.ratingThresholdClearedListener == null) {
                setRatingThresholdClearedListener();
            }
            builder.ratingThresholdClearedListener.onThresholdCleared(this, ratingBar.getRating(), thresholdPassed);

        } else {
            thresholdPassed = false;

            if (builder.ratingThresholdFailedListener == null) {
                setRatingThresholdFailedListener();
            }
            builder.ratingThresholdFailedListener.onThresholdFailed(this, ratingBar.getRating(), thresholdPassed);
        }

        if (builder.ratingDialogListener != null) {
            builder.ratingDialogListener.onRatingSelected(ratingBar.getRating(), thresholdPassed);
        }
//        showNever();
    }

    private void setRatingThresholdClearedListener() {
        builder.ratingThresholdClearedListener = new Builder.RatingThresholdClearedListener() {
            @Override
            public void onThresholdCleared(RateDialog ratingDialog, float rating, boolean thresholdCleared) {
                openPlaystore(context);
                new Handler(Looper.myLooper()).postDelayed(() -> Toast.makeText(context, "Scroll down to rate us", Toast.LENGTH_SHORT).show(), 2000);
                dismiss();
            }
        };
    }

    private void setRatingThresholdFailedListener() {
        builder.ratingThresholdFailedListener = new Builder.RatingThresholdFailedListener() {
            @Override
            public void onThresholdFailed(RateDialog ratingDialog, float rating, boolean thresholdCleared) {
                openForm();
            }
        };
    }

    private void openForm() {
        tvFeedback.setVisibility(View.VISIBLE);
        etFeedback.setVisibility(View.VISIBLE);
        feedbackButtons.setVisibility(View.VISIBLE);
        ratingButtons.setVisibility(View.GONE);
        ivIcon.setVisibility(View.GONE);
        tvTitle.setVisibility(View.GONE);
        ratingBar.setVisibility(View.GONE);
    }

    private void openPlaystore(Context context) {
        UserHelper.setRateDone(true);
        showNever();
        final Uri marketUri = Uri.parse(builder.playstoreUrl);
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, marketUri));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "Couldn't find PlayStore on this device", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void show() {

        super.show();
    }

    private void showNever() {
        sharedpreferences = context.getSharedPreferences(MyPrefs, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(SHOW_NEVER, true);
        editor.commit();
    }

    public static class Builder {

        private final Context context;
        private String title, positiveText, negativeText, playstoreUrl;
        private String formTitle, submitText, cancelText, feedbackFormHint;
        private int positiveTextColor, negativeTextColor, titleTextColor, ratingBarColor, ratingBarBackgroundColor, feedBackTextColor;
        private int positiveBackgroundColor, negativeBackgroundColor;
        private RatingThresholdClearedListener ratingThresholdClearedListener;
        private RatingThresholdFailedListener ratingThresholdFailedListener;
        private RatingDialogFormListener ratingDialogFormListener;
        private RatingDialogListener ratingDialogListener;
        private Drawable drawable;

        private int session = 1;


        public Builder(Context context) {
            this.context = context;
            // Set default PlayStore URL
            this.playstoreUrl = "market://details?id=" + context.getPackageName();
            initText();
        }

        private void initText() {
            title = context.getString(R.string.rating_dialog_experience);
            positiveText = context.getString(R.string.rating_dialog_maybe_later);
            negativeText = context.getString(R.string.rating_dialog_never);
            formTitle = context.getString(R.string.rating_dialog_feedback_title);
            submitText = context.getString(R.string.rating_dialog_submit);
            cancelText = context.getString(R.string.rating_dialog_cancel);
            feedbackFormHint = context.getString(R.string.rating_dialog_suggestions);
        }

        public Builder session(int session) {
            this.session = session;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder icon(Drawable drawable) {
            this.drawable = drawable;
            return this;
        }


        public Builder positiveButtonTextColor(int positiveTextColor) {
            this.positiveTextColor = positiveTextColor;
            return this;
        }

        public Builder onRatingBarFormSumbit(RatingDialogFormListener ratingDialogFormListener) {
            this.ratingDialogFormListener = ratingDialogFormListener;
            return this;
        }

        public Builder ratingBarColor(int ratingBarColor) {
            this.ratingBarColor = ratingBarColor;
            return this;
        }


        public Builder playstoreUrl(String playstoreUrl) {
            this.playstoreUrl = playstoreUrl;
            return this;
        }

        public RateDialog build() {
            RateDialog ratingDialog = new RateDialog(context, this);
            return ratingDialog;
        }

        public interface RatingThresholdClearedListener {
            void onThresholdCleared(RateDialog ratingDialog, float rating, boolean thresholdCleared);
        }

        public interface RatingThresholdFailedListener {
            void onThresholdFailed(RateDialog ratingDialog, float rating, boolean thresholdCleared);
        }

        public interface RatingDialogFormListener {
            void onFormSubmitted(String feedback);
        }

        public interface RatingDialogListener {
            void onRatingSelected(float rating, boolean thresholdCleared);
        }
    }
}
