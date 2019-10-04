package com.lapaksembako.app.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lapaksembako.app.R;

public class FlashSaleGridViewHolder extends RecyclerView.ViewHolder {

    public TextView titleTextView;
    public TextView textView25;
    public TextView textView30;
    public ImageView coverImageView;
//    public Button button12;
//    public ImageView likeImageView;
//    public ImageView shareImageView;


    public FlashSaleGridViewHolder(View v, final Context context) {
        super(v);
        titleTextView = v.findViewById(R.id.item_text);
        textView25 = v.findViewById(R.id.textViewHarga);
        textView30 = v.findViewById(R.id.textView30);
        textView30.setPaintFlags(textView30.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        coverImageView = v.findViewById(R.id.item_image);
//        button12 = v.findViewById(R.id.button12);
//        button12.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, ProdukDetailActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
//            }
//        });
//        likeImageView = (ImageView) v.findViewById(R.id.likeImageView);
//        shareImageView = (ImageView) v.findViewById(R.id.shareImageView);
//        likeImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int id = (int)likeImageView.getTag();
//                if( id == R.drawable.ic_eye){
//
//                    likeImageView.setTag(R.drawable.ic_eye);
//                    likeImageView.setImageResource(R.drawable.ic_eye);
//
////                    Toast.makeText(getActivity(),titleTextView.getText()+" added to favourites",Toast.LENGTH_SHORT).show();
//
//                }else{
//                    likeImageView.setTag(R.drawable.ic_eye);
//                    likeImageView.setImageResource(R.drawable.ic_eye);
////                    Toast.makeText(getActivity(),titleTextView.getText()+" removed from favourites",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });


    }
}