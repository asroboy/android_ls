package com.lapaksembako.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.lapaksembako.app.action.Core;
import com.lapaksembako.app.adapter.ItemGridviewAdapter;
import com.lapaksembako.app.adapter.KategoriGridviewAdapter;
import com.lapaksembako.app.api.ApiClient;
import com.lapaksembako.app.api.ApiInterface;
import com.lapaksembako.app.api_model.GetCategory;
import com.lapaksembako.app.api_model.GetItems;
import com.lapaksembako.app.fragments.ChartFragment;
import com.lapaksembako.app.helper.Common;
import com.lapaksembako.app.model.Item;
import com.lapaksembako.app.model.Kategori;
import com.lapaksembako.app.model.User;
import com.lapaksembako.app.view.ExpandableHeighGridView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemsInCategoryActivity extends AppCompatActivity {

    ExpandableHeighGridView gridViewWishList;
    ApiInterface apiInterface;
    ArrayList<Item> gridItemsArray = new ArrayList<Item>();
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_in_category);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_back_white);


        gridViewWishList = findViewById(R.id.gridViewItemList);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            int categoryId = bundle.getInt("categoryId");
            String categoryName = bundle.getString("categoryName");
            user = bundle.getParcelable("user");
            getItems(categoryId);
            ab.setTitle(categoryName);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<Item> getItems(int categoryId) {
        final ArrayList<Item> items = new ArrayList<>();
        Call<GetItems> itemsCall = apiInterface.getItemsInCategory(categoryId);
        itemsCall.enqueue(new Callback<GetItems>() {
            @Override
            public void onResponse(Call<GetItems> call, Response<GetItems> response) {
                if (response.isSuccessful()) {
                    Log.d(Common.TAG, response.body().getStatus() + " " + response.body().getItems().size());
                    if (response.body().getItems() != null) {
                        for (Item item : response.body().getItems()) {
                            if (item.getImageResId() == 0) {
                                item.setImageResId(R.drawable.no_image);
                            }
                            Log.i(Common.TAG, "Default image resource : " + R.drawable.no_image);
                            Log.i(Common.TAG, "Image resource : " + item.getImageResId());
                            items.add(item);
                            gridItemsArray.add(item);
                        }
                        ItemGridviewAdapter customGridAdapter = new ItemGridviewAdapter(ItemsInCategoryActivity.this, R.layout.row_grid, gridItemsArray, user, new Core.OnCallCompleteListener() {
                            @Override
                            public void onSuccess(Intent data) {
                                Log.d(Common.TAG, "success add to chart");

                            }

                            @Override
                            public void onFailed(Intent data) {
                                Log.d(Common.TAG, "failed add to chart");
                                Common.showWarningDialog(ItemsInCategoryActivity.this, "Gagal memproses, silahkan coba lagi.", new Common.OnDialogActionSelected() {
                                    @Override
                                    public void onPositiveClicked(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onNegativeClicked(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });
                        gridViewWishList.setAdapter(customGridAdapter);
                    }
                } else {
                    Log.e(Common.TAG, "Request gagal " + response.message());
                }

            }

            @Override
            public void onFailure(Call<GetItems> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
            }
        });
        return items;
    }

}
