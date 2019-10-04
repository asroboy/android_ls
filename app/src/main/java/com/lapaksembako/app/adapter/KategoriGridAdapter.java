package com.lapaksembako.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lapaksembako.app.R;
import com.lapaksembako.app.model.Kategori;

import java.util.ArrayList;

public class KategoriGridAdapter extends RecyclerView.Adapter<KategoriGridViewHolder> {
    private ArrayList<Kategori> list;
    private Context context;


    public KategoriGridAdapter(ArrayList<Kategori> Data, Context context) {
        list = Data;
        this.context = context;
    }

    @Override
    public KategoriGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_item_kategori, parent, false);
        KategoriGridViewHolder holder = new KategoriGridViewHolder(view, context);
        return holder;
    }

    @Override
    public void onBindViewHolder(final KategoriGridViewHolder holder, int position) {
        holder.coverImageView.setImageResource(list.get(position).getImageResource());
        holder.coverImageView.setTag(list.get(position).getImageResource());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}