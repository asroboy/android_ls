package com.lapaksembako.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lapaksembako.app.action.Core;
import com.lapaksembako.app.helper.Common;
import com.lapaksembako.app.model.Item;
import com.lapaksembako.app.model.Kategori;
import com.lapaksembako.app.model.ProductPhoto;
import com.lapaksembako.app.model.User;

import java.util.ArrayList;

public class ProductDetail2Activity extends AppCompatActivity implements View.OnClickListener {

    Item item;
    Core core;
    TextView textViewProductName, textViewPrice, textViewStatusKetersediaan,
            textViewDeskripsi, textViewShortDesc, textViewKategori;
    Button buttonPesan;
    User user;
    ImageView imageViewHeader;
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_back_white);

        Bundle b = getIntent().getBundleExtra("bundle");
        if (b != null) {
            item = b.getParcelable("item");
            user = b.getParcelable("user");
        }

        core = new Core(user);

        fab = findViewById(R.id.fab);
        core.isItemLiked(user.getId(), item.getId(), new Core.OnCallCompleteListener() {
            @Override
            public void onSuccess(Intent data) {
                boolean isLiked = data.getBooleanExtra("islike", false);
                Log.d(Common.TAG, "Complete call is liked " + isLiked);
                item.setLiked(isLiked);
                changeButtonBackground(fab);
            }

            @Override
            public void onFailed(Intent data) {

            }
        });


        textViewProductName = findViewById(R.id.textViewProductName);
        textViewPrice = findViewById(R.id.textViewPrice);
        textViewStatusKetersediaan = findViewById(R.id.textViewStatus);
        textViewShortDesc = findViewById(R.id.textViewShortDesc);
        textViewShortDesc.setVisibility(View.GONE);
        textViewDeskripsi = findViewById(R.id.textViewDeskripsi);
        buttonPesan = findViewById(R.id.buttonPesan);
        imageViewHeader = findViewById(R.id.header);
        textViewKategori = findViewById(R.id.textViewKategori);


        core.getProductPhotos(item.getId(), new Core.OnCallCompleteListener() {
            @Override
            public void onSuccess(Intent data) {
                ArrayList<ProductPhoto> productPhotos = data.getParcelableArrayListExtra("photos");
                String fileName = "no_image.jpg";
                if (productPhotos.size() > 0) {
                    fileName = productPhotos.get(0).getPhotoFileName();
                }

                String imageUrl = Common.BASE_URL_PRODUCT + fileName;
                Log.d(Common.TAG, imageUrl);
                Glide.with(ProductDetail2Activity.this)
                        .load(imageUrl)
                        .into(imageViewHeader);
            }

            @Override
            public void onFailed(Intent data) {

            }
        });

        core.getCategoryById(item.getCategoryId(), new Core.OnCallCompleteListener() {
            @Override
            public void onSuccess(Intent data) {
                Kategori kategori = data.getBundleExtra("bundle").getParcelable("category");
                textViewKategori.setText("Kategori " + kategori.getNama());
            }

            @Override
            public void onFailed(Intent data) {

            }
        });

        if (item != null) {
            textViewProductName.setText(item.getNama());
            textViewPrice.setText("Rp " + item.getHarga() + "/pcs");
            String ketersediaan = "Habis";
            if (item.getStock() > 0) {
                ketersediaan = "Tersedia " + item.getStock() + "pcs";
            }
            textViewStatusKetersediaan.setText(ketersediaan);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (item.getDescription() != null)
                    textViewDeskripsi.setText(Html.fromHtml(item.getDescription(), Html.FROM_HTML_MODE_COMPACT));
                if (item.getShortDescription() != null) {
                    textViewShortDesc.setVisibility(View.VISIBLE);
                    textViewShortDesc.setText(Html.fromHtml(item.getShortDescription(), Html.FROM_HTML_MODE_COMPACT));
                }
            } else {
                if (item.getDescription() != null)
                    textViewDeskripsi.setText(Html.fromHtml(item.getDescription()));
                if (item.getShortDescription() != null) {
                    textViewShortDesc.setVisibility(View.VISIBLE);
                    textViewShortDesc.setText(Html.fromHtml(item.getShortDescription()));
                }
            }

            buttonPesan.setOnClickListener(this);
        }

        if (user != null) {
            Log.d(Common.TAG, "User tidak null");
            core = new Core(user);
        } else {
            Log.d(Common.TAG, "User null");
        }

    }


    Core.OnCallCompleteListener onCallCompleteListener = new Core.OnCallCompleteListener() {
        @Override
        public void onSuccess(Intent data) {
            changeButtonBackground(fab);
        }

        @Override
        public void onFailed(Intent data) {

        }
    };

    private void changeButtonBackground(final FloatingActionButton buttonLike) {
        if (!item.isLiked()) {
            buttonLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
            buttonLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item.setLiked(true);
                    buttonLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_dislike));
                    core.like(user.getId(), item.getId(), onCallCompleteListener);
                }
            });
        } else {
            buttonLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_dislike));
            buttonLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item.setLiked(false);
                    buttonLike.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
                    core.dislike(user.getId(), item.getId(), onCallCompleteListener);
                }
            });
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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonPesan) {
            if (core != null) {
                Log.d(Common.TAG, "Core tidak null");
                core.addToCart(item, 1, false, new Core.OnCallCompleteListener() {
                    @Override
                    public void onSuccess(Intent data) {
                        Log.d(Common.TAG, "success add to chart");
                        setResult(RESULT_OK);
                        finish();

                    }

                    @Override
                    public void onFailed(Intent data) {
                        Log.d(Common.TAG, "failed add to chart");
                        Common.showWarningDialog(ProductDetail2Activity.this, "Gagal memproses, silahkan coba lagi.", new Common.OnDialogActionSelected() {
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
            } else {
                Log.d(Common.TAG, "Core null");
            }

        }
    }
}
