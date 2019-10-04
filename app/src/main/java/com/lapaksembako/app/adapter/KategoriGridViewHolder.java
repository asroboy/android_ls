package com.lapaksembako.app.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lapaksembako.app.R;

public class KategoriGridViewHolder extends RecyclerView.ViewHolder {

    public ImageView coverImageView;

    public KategoriGridViewHolder(View v, final Context context) {
        super(v);
        coverImageView = v.findViewById(R.id.item_image);

    }
}