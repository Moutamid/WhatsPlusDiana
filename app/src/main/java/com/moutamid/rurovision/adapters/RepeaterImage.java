package com.moutamid.rurovision.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moutamid.rurovision.R;
import com.moutamid.rurovision.Utils.Common;
import com.moutamid.rurovision.databinding.RowStatusBinding;
import com.moutamid.rurovision.model.StatusModel;

import java.util.List;

public class RepeaterImage extends RecyclerView.Adapter<ItemViewHolder> {

    private final List<StatusModel> imagesList;
    private Context context;
    private final RelativeLayout container;

    public RepeaterImage(List<StatusModel> imagesList, RelativeLayout container) {
        this.imagesList = imagesList;
        this.container = container;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        @NonNull RowStatusBinding itemBinding = RowStatusBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemViewHolder(itemBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {

        final StatusModel status = imagesList.get(position);
        if (status.isVideo()) {
            holder.ivVideo.setVisibility(View.VISIBLE);
        } else {
            holder.ivVideo.setVisibility(View.GONE);
        }
        if (status.isApi30()) {
//            holder.save.setVisibility(View.GONE);
            Glide.with(context).load(status.getDocumentFile().getUri()).into(holder.imageView);
        } else {
            holder.save.setVisibility(View.VISIBLE);
            Glide.with(context).load(status.getFile()).into(holder.imageView);
        }

        holder.save.setOnClickListener(v -> Common.copyFile(status, context, container));

        holder.share.setOnClickListener(v -> {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);

            shareIntent.setType("image/jpg");

            if (status.isApi30()) {
                shareIntent.putExtra(Intent.EXTRA_STREAM, status.getDocumentFile().getUri());
            } else {
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + status.getFile().getAbsolutePath()));
            }

            context.startActivity(Intent.createChooser(shareIntent, "Share image"));

        });

        holder.imageView.setOnClickListener(v -> {

            final AlertDialog.Builder alertD = new AlertDialog.Builder(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.row_image_full_screen, null);
            alertD.setView(view);

            ImageView imageView = view.findViewById(R.id.img);
            if (status.isApi30()) {
                Glide.with(context).load(status.getDocumentFile().getUri()).into(imageView);
            } else {
                Glide.with(context).load(status.getFile()).into(imageView);
            }

            AlertDialog alert = alertD.create();
            alert.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;
            alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alert.show();

        });

    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

}
