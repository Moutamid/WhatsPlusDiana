package com.moutamid.rurovision.sticker.repeater;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.moutamid.rurovision.R;
import com.moutamid.rurovision.sticker.views.ViewSQRPic;

import java.io.File;
import java.util.List;

public class RepeaterStickr extends RecyclerView.Adapter<RepeaterStickr.ViewHolder> {


    private final LayoutInflater layoutInflater;
    private final List<String> appMdlBbblTxtsLisy;
    private ItemClickListener listener;

    public RepeaterStickr(Context context, List<String> appMdlBbblTxtsLisy) {
        this.layoutInflater = LayoutInflater.from(context);
        this.appMdlBbblTxtsLisy = appMdlBbblTxtsLisy;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.layout_sticker, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int pos) {
        viewHolder._ivSticker.setImageBitmap(BitmapFactory.decodeFile(new File(this.appMdlBbblTxtsLisy.get(pos)).getAbsolutePath()));
        viewHolder.itemView.setTag(this.appMdlBbblTxtsLisy.get(pos));
    }


    @Override
    public int getItemCount() {
        return appMdlBbblTxtsLisy.size();
    }


    public void setClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(String path, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewSQRPic _ivSticker;
        ProgressBar _progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            _ivSticker = itemView.findViewById(R.id.ivSticker);
            _progressBar = itemView.findViewById(R.id.progressBar);
            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onItemClick(appMdlBbblTxtsLisy.get(getLayoutPosition()), getLayoutPosition());
                }
            });
        }
    }
}