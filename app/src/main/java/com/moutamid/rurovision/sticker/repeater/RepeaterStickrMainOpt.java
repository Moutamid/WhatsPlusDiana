package com.moutamid.rurovision.sticker.repeater;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.rurovision.activity.MyApplication;
import com.moutamid.rurovision.R;
import com.moutamid.rurovision.sticker.CallBack_PIP;
import com.moutamid.rurovision.sticker.model.StickrMainOpt;

import java.util.ArrayList;

public class RepeaterStickrMainOpt extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final String NATIVE_AD = "NativeAd";
    private final ArrayList<StickrMainOpt> appMdlStikrMainOptArrayList;
    private final CallBack_PIP callBackPip;
    Activity mActivity;

    public RepeaterStickrMainOpt(Activity activity, ArrayList<StickrMainOpt> appMdlStikrMainOptArrayList, CallBack_PIP callBackPip) {
        super();
        this.mActivity = activity;
        this.appMdlStikrMainOptArrayList = appMdlStikrMainOptArrayList;
        this.callBackPip = callBackPip;
    }

    @Override
    public int getItemViewType(int position) {
        StickrMainOpt stickrMainOpt = appMdlStikrMainOptArrayList.get(position);
        if (stickrMainOpt.getParentText().equalsIgnoreCase(NATIVE_AD)) {
            return 2;
        } else {
            return 1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == 2) {
            return new AdsHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_native_ads, viewGroup, false));
        } else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_sticker_main_opt, viewGroup, false);
            return new ViewHolder(v);
        }

    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) viewHolder;
            holder._tvTitle.setText(appMdlStikrMainOptArrayList.get(i).getParentText());
            holder._ivPhotoThumb.setImageDrawable(appMdlStikrMainOptArrayList.get(i).getParentIcon());
        } else if (viewHolder instanceof AdsHolder) {
            AdsHolder holder1 = (AdsHolder) viewHolder;
            MyApplication.getInstance().loadNativeFullAds(holder1.fl_adplaceholder, mActivity,500);
        }
    }


    @Override
    public int getItemCount() {
        return appMdlStikrMainOptArrayList.size();
    }

    public static class AdsHolder extends RecyclerView.ViewHolder {

        FrameLayout fl_adplaceholder;


        AdsHolder(View itemView) {
            super(itemView);

            fl_adplaceholder = itemView.findViewById(R.id.fl_adplaceholder);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatImageView _ivPhotoThumb;
        public TextView _tvTitle;


        public ViewHolder(final View itemView) {
            super(itemView);
            _ivPhotoThumb = itemView.findViewById(R.id.ivPhotoThumb);
            _tvTitle = itemView.findViewById(R.id.tvTitle);

            _tvTitle.setVisibility(View.VISIBLE);
            itemView.setOnClickListener(v -> callBackPip.onItemClick(itemView, getLayoutPosition()));
        }
    }
}

