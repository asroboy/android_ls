package com.lapaksembako.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lapaksembako.app.R;
import com.lapaksembako.app.action.Core;
import com.lapaksembako.app.api.ApiClient;
import com.lapaksembako.app.api.ApiInterface;
import com.lapaksembako.app.api_model.PostCart;
import com.lapaksembako.app.api_model.PostResult;
import com.lapaksembako.app.fragments.dummy.DummyContent.DummyItem;
import com.lapaksembako.app.helper.Common;
import com.lapaksembako.app.helper.OnListFragmentInteractionListener;
import com.lapaksembako.app.model.Cart;
import com.lapaksembako.app.model.Item;
import com.lapaksembako.app.model.ProductPhoto;
import com.lapaksembako.app.model.User;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemChartRecyclerViewAdapter extends RecyclerView.Adapter<MyItemChartRecyclerViewAdapter.ViewHolder> {

    private final List<Item> mValues;
    private final Cart mCart;
    private final OnListFragmentInteractionListener mListener;
    Button mButtonTotal;
    int mTotal = 0;
    OnDataChangedListener mDataChangedListener;
    Context mContext;
    User mUser;
    Core core;

    public MyItemChartRecyclerViewAdapter(User user, Context context, List<Item> items, OnListFragmentInteractionListener listener, Cart cart, Button buttonTotal, int total,
                                          OnDataChangedListener dataChangedListener) {
        mContext = context;
        mUser = user;
        mValues = items;
        mListener = listener;
        mCart = cart;
        mButtonTotal = buttonTotal;
        mTotal = total;
        mDataChangedListener = dataChangedListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_cart, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        core = new Core(mUser);
        holder.mItem = mValues.get(position);

        core.getProductPhotos(holder.mItem.getId(), new Core.OnCallCompleteListener() {
            @Override
            public void onSuccess(Intent data) {
                ArrayList<ProductPhoto> productPhotos = data.getParcelableArrayListExtra("photos");
                String fileName = "no_image.jpg";
                if (productPhotos.size() > 0) {
                    fileName = productPhotos.get(0).getPhotoFileName();
                }

                String imageUrl = Common.BASE_URL_PRODUCT + fileName;
                Log.d(Common.TAG, imageUrl);
                Glide.with(mContext)
                        .load(imageUrl)
                        .into(holder.imageViewUserRefer);
            }

            @Override
            public void onFailed(Intent data) {

            }
        });

        holder.txtName.setText(holder.mItem.getNama());
        holder.txtPrice.setText("Rp " + holder.mItem.getHarga());
        holder.numberPicker.setValue(mValues.get(position).getQuantity());
        holder.numberPicker.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int value, ActionEnum action) {
                int h = holder.mItem.getQuantity() * holder.mItem.getHarga();
                int hr = holder.mItem.getHarga();
                Log.d(Common.TAG, "qty " + holder.mItem.getQuantity());
                Log.d(Common.TAG, "value " + value + " harga " + hr + " total hrg item " + h);
                mTotal = mTotal - h;
                mTotal = mTotal + (value * hr);
                Log.d(Common.TAG, "total now " + mTotal);
                mButtonTotal.setText("Total Rp " + mTotal);
                holder.mItem.setQuantity(value);
                updateQty(value, mCart.getId(), holder.mItem.getId());
                mDataChangedListener.onChanged(holder.mItem, position, mTotal);
            }
        });

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int h = holder.mItem.getQuantity() * holder.mItem.getHarga();
                mTotal = mTotal - h;
                mButtonTotal.setText("Total Rp " + mTotal);
                deleteItemCart(mCart.getId(), holder.mItem.getId());
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    public void updateQty(int qty, int idCart, int productId) {
        Call<PostResult> postUpdateQty = ApiClient.getClient().create(ApiInterface.class).updateQty(idCart, productId, qty);
        postUpdateQty.enqueue(new Callback<PostResult>() {
            @Override
            public void onResponse(Call<PostResult> call, Response<PostResult> response) {
                Log.d(Common.TAG, response.body().getMessage());
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<PostResult> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());

            }
        });
    }

    private void deleteItemCart(int idCart, int idItem) {
        Call<PostCart> getCartCall = ApiClient.getClient().create(ApiInterface.class).deleteItemFromCart(idCart, idItem);
        getCartCall.enqueue(new Callback<PostCart>() {
            @Override
            public void onResponse(Call<PostCart> call, Response<PostCart> response) {
                if (response.isSuccessful()) {
                    mListener.onListItemDelete(OnListFragmentInteractionListener.SUCCESS);
                }
            }

            @Override
            public void onFailure(Call<PostCart> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
                notifyDataSetChanged();
            }
        });

    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public Item mItem;
        public TextView txtName;
        public TextView txtPrice;
        public NumberPicker numberPicker;
        public Button buttonDelete;
        public ImageView imageViewUserRefer;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            txtName = view.findViewById(R.id.textViewNominal);
            txtPrice = view.findViewById(R.id.textView51);
            numberPicker = view.findViewById(R.id.number_picker);
            buttonDelete = view.findViewById(R.id.buttonDelete);
            imageViewUserRefer = view.findViewById(R.id.imageViewUserRefer);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + txtName.getText() + "'";
        }
    }

    public static interface OnDataChangedListener {
        void onChanged(Item item, int position, int totalBelanja);
    }
}
