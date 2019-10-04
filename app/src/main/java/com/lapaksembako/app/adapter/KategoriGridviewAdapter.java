package com.lapaksembako.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lapaksembako.app.ProdukDetailActivity;
import com.lapaksembako.app.R;
import com.lapaksembako.app.helper.Common;
import com.lapaksembako.app.helper.DownloadImageTask;
import com.lapaksembako.app.model.Item;
import com.lapaksembako.app.model.Kategori;

import java.util.ArrayList;

/**
 * @author manish.s
 */
public class KategoriGridviewAdapter extends ArrayAdapter<Kategori> {

    Context context;
    int layoutResourceId;
    ArrayList<Kategori> data = new ArrayList<Kategori>();

    public KategoriGridviewAdapter(Context context, int layoutResourceId,
                                   ArrayList<Kategori> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecordHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RecordHolder();
            holder.txtTitle = row.findViewById(R.id.nama_kategori);
            holder.imageItem = row.findViewById(R.id.icon_kategori);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }

        Kategori item = data.get(position);
        holder.txtTitle.setText(item.getNama());
        Log.d(Common.TAG, "Icon : " + item.getIcon());
        if (item.getIcon().equals("")) {
//            holder.imageItem.setImageDrawable(context.getResources().getDrawable(R.drawable.no_image));
            Glide.with(getContext())
                    .load(context.getResources().getDrawable(R.drawable.no_image))
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.imageItem);
        } else {
            Glide.with(getContext())
                    .load(Common.BASE_URL_KATEGORY + item.getIcon())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.imageItem);
//            new DownloadImageTask(holder.imageItem)
//                    .execute(Common.BASE_URL_KATEGORY + item.getIcon());
        }


        return row;

    }

    static class RecordHolder {
        TextView txtTitle;
        ImageView imageItem;

    }
}