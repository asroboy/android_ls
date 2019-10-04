package com.lapaksembako.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lapaksembako.app.R;
import com.lapaksembako.app.model.Item;

import java.util.ArrayList;

public class FlashSaleGridAdapter extends RecyclerView.Adapter<FlashSaleGridViewHolder> {
    private ArrayList<Item> list;
    private Context context;
    public FlashSaleGridAdapter(ArrayList<Item> Data, Context context) {
        list = Data;
        this.context = context;
    }

    @Override
    public FlashSaleGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_item, parent, false);
        FlashSaleGridViewHolder holder = new FlashSaleGridViewHolder(view, context);
        return holder;
    }

    @Override
    public void onBindViewHolder(final FlashSaleGridViewHolder holder, int position) {

        holder.titleTextView.setText(list.get(position).getNama());
        holder.textView25.setText("Rp. " + list.get(position).getHarga() + " / pcs");
        holder.coverImageView.setImageResource(list.get(position).getImageResId());
        holder.coverImageView.setTag(list.get(position).getImageResId());
//        holder.likeImageView.setTag(R.drawable.ic_like);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}