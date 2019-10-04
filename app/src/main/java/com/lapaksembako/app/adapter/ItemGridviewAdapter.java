package com.lapaksembako.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lapaksembako.app.ProductDetail2Activity;
import com.lapaksembako.app.ProdukDetailActivity;
import com.lapaksembako.app.R;
import com.lapaksembako.app.action.Core;
import com.lapaksembako.app.helper.Common;
import com.lapaksembako.app.helper.DownloadImageTask;
import com.lapaksembako.app.model.Item;
import com.lapaksembako.app.model.ProductPhoto;
import com.lapaksembako.app.model.User;

import java.util.ArrayList;

/**
 * @author manish.s
 */
public class ItemGridviewAdapter extends ArrayAdapter<Item> {
    Activity context;
    int layoutResourceId;
    User userLogin;
    ArrayList<Item> data = new ArrayList<Item>();
    Core core;
    Core.OnCallCompleteListener onCallCompleteListener;

    public ItemGridviewAdapter(Activity context, int layoutResourceId,
                               ArrayList<Item> data, User userLogin, Core.OnCallCompleteListener onCallCompleteListener) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.userLogin = userLogin;
        this.data = data;
        this.onCallCompleteListener = onCallCompleteListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecordHolder holder = null;
        core = new Core(userLogin);

        if (row == null) {
            LayoutInflater inflater = (context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new RecordHolder();
            holder.itemBox = row.findViewById(R.id.item_box);
            holder.txtTitle = row.findViewById(R.id.item_text);
            holder.textViewHarga = row.findViewById(R.id.textViewHarga);
            holder.imageItem = row.findViewById(R.id.item_image);
            holder.buttonBeli = row.findViewById(R.id.buttonBeli);
            holder.buttonLike = row.findViewById(R.id.buttonLike);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }

        final Item item = data.get(position);
        holder.txtTitle.setText(item.getNama());
        holder.textViewHarga.setText("Rp " + item.getHarga() + "/pcs");

        final ImageView ig = holder.imageItem;
        core.getProductPhotos(item.getId(), new Core.OnCallCompleteListener() {
            @Override
            public void onSuccess(Intent data) {
                ArrayList<ProductPhoto> productPhotos = data.getParcelableArrayListExtra("photos");
                if (productPhotos.size() > 0) {
                    String  fileName = productPhotos.get(0).getPhotoFileName();
                    String imageUrl = Common.BASE_URL_PRODUCT + fileName;
                    Log.d(Common.TAG, imageUrl);
                    Glide.with(context)
                            .load(imageUrl)
                            .into(ig);
                }else{
                    Glide.with(context)
                            .load(context.getResources().getDrawable(R.drawable.no_image))
                            .into(ig);
                }


            }

            @Override
            public void onFailed(Intent data) {

            }
        });

//        if(item.getPhotoFileName() != null && !item.getPhotoFileName().equals("")){
//            new DownloadImageTask(holder.imageItem)
//                    .execute(Common.BASE_URL_PRODUCT + item.getPhotoFileName());
//        }
        holder.itemBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetail2Activity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("item", item);
                bundle.putParcelable("user", userLogin);
                intent.putExtra("bundle", bundle);
                context.startActivityForResult(intent, Common.REQUEST_ITEM_DETAIL);
            }
        });


        final Button buttonLike = holder.buttonLike;
        core.isItemLiked(userLogin.getId(), item.getId(), new Core.OnCallCompleteListener() {
            @Override
            public void onSuccess(Intent data) {
                boolean isLiked = data.getBooleanExtra("islike", false);
                item.setLiked(isLiked);
                changeButtonBackground(buttonLike, item);
            }

            @Override
            public void onFailed(Intent data) {

            }
        });


        holder.buttonBeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (core != null) {
                    core.addToCart(item, 1, false, onCallCompleteListener);
                } else {
                    Log.d(Common.TAG, "Core object is null");
                }
            }
        });

        return row;

    }


    private void changeButtonBackground(final Button buttonLike, final Item item) {
        if (!item.isLiked()) {
            buttonLike.setBackground(context.getResources().getDrawable(R.drawable.ic_like));
            buttonLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buttonLike.setBackground(context.getResources().getDrawable(R.drawable.ic_dislike));
                    if (core != null) {
                        core.like(userLogin.getId(), item.getId(), onCallCompleteListener);
                    } else {
                        Log.d(Common.TAG, "core object is null");
                    }

//                    notifyDataSetChanged();
                }
            });
        } else {
            buttonLike.setBackground(context.getResources().getDrawable(R.drawable.ic_dislike));
            buttonLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buttonLike.setBackground(context.getResources().getDrawable(R.drawable.ic_like));
                    if (core != null) {
                        core.dislike(userLogin.getId(), item.getId(), onCallCompleteListener);
                    } else {
                        Log.d(Common.TAG, "core object is null");
                    }

//                    notifyDataSetChanged();
                }
            });
        }
    }

    static class RecordHolder {
        Item item;
        LinearLayout itemBox;
        TextView txtTitle;
        TextView textViewHarga;
        ImageView imageItem;
        Button buttonBeli;
        Button buttonLike;

    }
}