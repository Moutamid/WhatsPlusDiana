package com.moutamid.rurovision.ascii_face.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.moutamid.rurovision.R;
import com.moutamid.rurovision.ascii_face.EmojiListener;

public class HappyAdapter extends RecyclerView.Adapter<HappyAdapter.ViewHolder> {
    FragmentActivity requireActivity;
    String[] happyAsciiFace;
    private EmojiListener nEmojiListener;

    public HappyAdapter(FragmentActivity requireActivity, String[] happyAsciiFace, EmojiListener emojiListener ) {
        this.requireActivity = requireActivity;
        this.happyAsciiFace = happyAsciiFace;
        this.nEmojiListener = emojiListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(requireActivity).inflate(R.layout.item_ascii_face, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvAscii.setText(happyAsciiFace[position]);
    }

    @Override
    public int getItemCount() {
        return happyAsciiFace.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAscii;
        ImageView ivWpShare;
        ImageView ivShare;
        ImageView ivCopy;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAscii = itemView.findViewById(R.id.tvAscii);
            ivWpShare = itemView.findViewById(R.id.ivWpShare);
            ivShare = itemView.findViewById(R.id.ivShare);
            ivCopy = itemView.findViewById(R.id.ivCopy);

            ivWpShare.setOnClickListener(v -> {
                if (nEmojiListener != null) {
                    nEmojiListener.onWpShare(tvAscii.getText().toString());
                }
            });
            ivShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (nEmojiListener != null) {
                        nEmojiListener.onShare(tvAscii.getText().toString());
                    }
                }
            });
            ivCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (nEmojiListener != null) {
                        nEmojiListener.onCopy(tvAscii.getText().toString());
                    }
                }
            });
        }
    }
}
