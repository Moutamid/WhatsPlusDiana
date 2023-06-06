package com.moutamid.rurovision.sticker.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class ViewSQRPic extends AppCompatImageView {

    public ViewSQRPic(Context context) {
        super(context);
    }

    public ViewSQRPic(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewSQRPic(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}