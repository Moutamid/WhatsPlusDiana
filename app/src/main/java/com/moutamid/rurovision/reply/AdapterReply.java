package com.moutamid.rurovision.reply;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.moutamid.rurovision.R;

import java.util.ArrayList;

public class AdapterReply extends RecyclerView.Adapter<AdapterReply.ViewHolder> {

    private final Activity activity;
    private ReplyListener replyListener;
    private ArrayList<String> parentArrayList = new ArrayList<>();

    public AdapterReply(Activity activity, ArrayList<String> parentArrayList, ReplyListener replyListener) {
        this.activity = activity;
        this.parentArrayList = parentArrayList;
        this.replyListener = replyListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_reply_msg,
                parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position > 4) {
            holder.ivEdit.setVisibility(View.VISIBLE);
        } else {
            holder.ivEdit.setVisibility(View.GONE);
        }
        holder.tvAscii.setText(parentArrayList.get(position));


    }

    @Override
    public int getItemCount() {
        return parentArrayList.size();
    }

    public void notifyDataSet(ArrayList<String> parentArrayList) {
        this.parentArrayList.clear();
        this.parentArrayList.addAll(parentArrayList);
        notifyDataSetChanged();
    }

    public interface onClickReplys {
        public void onClickEdit(String s, int layoutrPosition);

        public void onClickCopy(String s);

        public void onClickShare(String s);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvAscii;
        ImageView ivWpShare;
        ImageView ivEdit;
        ImageView ivCopy;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAscii = itemView.findViewById(R.id.tvAscii);
            ivWpShare = itemView.findViewById(R.id.ivWpShare);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivCopy = itemView.findViewById(R.id.ivCopy);

            ivEdit.setOnClickListener(v -> {
                if (replyListener != null) {
                    replyListener.onClickEdit(tvAscii.getText().toString(), getLayoutPosition());
                }
            });
            ivCopy.setOnClickListener(v -> {
                if (replyListener != null) {
                    replyListener.onClickCopy(tvAscii.getText().toString());
                }
            });
            ivWpShare.setOnClickListener(v -> {
                 if (replyListener != null) {
                    replyListener.onClickShare(tvAscii.getText().toString());
                }
            });
        }
    }
}
