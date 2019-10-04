package com.lapaksembako.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.lapaksembako.app.action.Core;
import com.lapaksembako.app.adapter.ItemGridviewAdapter;
import com.lapaksembako.app.adapter.FlashSaleGridAdapter;
import com.lapaksembako.app.adapter.KategoriGridAdapter;
import com.lapaksembako.app.adapter.MainSectionPagerAdapter;
import com.lapaksembako.app.adapter.NotifikasiSectionPagerAdapter;
import com.lapaksembako.app.adapter.PromoPagerAdapter;
import com.lapaksembako.app.api.ApiClient;
import com.lapaksembako.app.api.ApiInterface;
import com.lapaksembako.app.api_model.GetCategory;
import com.lapaksembako.app.api_model.GetUserLogin;
import com.lapaksembako.app.fragments.ChartFragment;
import com.lapaksembako.app.helper.Common;
import com.lapaksembako.app.helper.Preferences;
import com.lapaksembako.app.model.Item;
import com.lapaksembako.app.model.Kategori;
import com.lapaksembako.app.view.ExpandableHeighGridView;
import com.lapaksembako.app.view.ImageThumbnailAdapter;
import com.lapaksembako.app.view.TwoWayGridView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private Cursor mImageCursor;
    private ImageThumbnailAdapter mAdapter;
    private TwoWayGridView mImageGrid;
    ApiInterface apiInterface;

    Timer swipeTimer = new Timer();
    int NUM_PAGES = 4;
    int currentPage = 0;
    boolean touched = false;
    Handler handler = new Handler();
    Runnable update;
    ViewPager viewPager;
    ExpandableHeighGridView gridView;
    ArrayList<Item> gridArray = new ArrayList<Item>();

    ArrayList<Kategori> gridKategoriArray = new ArrayList<Kategori>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.menu_indicator_sm);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.openDrawer(Gravity.START);
            }
        });
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        viewPager = findViewById(R.id.view_pager);
//        PromoPagerAdapter promoPagerAdapter = new PromoPagerAdapter(this, getSupportFragmentManager());
//        viewPager.setAdapter(promoPagerAdapter);
        startPagerAutoSwipe();
        for (int i = 0; i < 10; i++) {
            Item item = new Item("Detol Cair " + (200 + i) + " ml", 7900, R.drawable.dettol);
            gridArray.add(item);
        }

        //DATA ITEM
        gridView = findViewById(R.id.gridView);
        gridView.setExpanded(true);
        ItemGridviewAdapter customGridAdapter = new ItemGridviewAdapter(this, R.layout.row_grid, gridArray, null, new Core.OnCallCompleteListener() {
            @Override
            public void onSuccess(Intent data) {
                Log.d(Common.TAG, "success add to chart");
            }

            @Override
            public void onFailed(Intent data) {
                Log.d(Common.TAG, "failed add to chart");
                Common.showWarningDialog(MainActivity.this, "Gagal memproses, silahkan coba lagi.", new Common.OnDialogActionSelected() {
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
        gridView.setAdapter(customGridAdapter);


        //FLASH SALE
        RecyclerView myRecyclerView = findViewById(R.id.cardView);
        myRecyclerView.setHasFixedSize(true);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(getApplicationContext());
        myLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        if (gridArray.size() > 0 & myRecyclerView != null) {
            myRecyclerView.setAdapter(new FlashSaleGridAdapter(gridArray, getApplicationContext()));
        }
        myRecyclerView.setLayoutManager(myLayoutManager);

        //CATEGORY
        getKategories();

        MainSectionPagerAdapter sectionsPagerAdapter = new MainSectionPagerAdapter(this, getSupportFragmentManager());

        ViewPager viewPager = findViewById(R.id.view_pager_main);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs_main);
        tabs.setupWithViewPager(viewPager);


    }

    private ArrayList<Kategori> getKategories() {
        final ArrayList<Kategori> kategoris = new ArrayList<>();
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                Call<GetCategory> kategoriCall = apiInterface.getCategory();

                kategoriCall.enqueue(new Callback<GetCategory>() {
                    @Override
                    public void onResponse(Call<GetCategory> call, Response<GetCategory> response) {
                        for (Kategori kategori : response.body().getKategoris()) {
                            kategoris.add(kategori);
                            if (kategori.getImageResource() == 0) {
                                kategori.setImageResource(R.drawable.ic_kategori_1);
                            }
                            gridKategoriArray.add(kategori);
                        }
                    }

                    @Override
                    public void onFailure(Call<GetCategory> call, Throwable t) {

                    }
                });
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                RecyclerView myRecyclerViewCategory = findViewById(R.id.cardView2);
                myRecyclerViewCategory.setHasFixedSize(true);
                LinearLayoutManager myLayoutManagerKategori = new LinearLayoutManager(getApplicationContext());
                myLayoutManagerKategori.setOrientation(LinearLayoutManager.HORIZONTAL);
                if (gridKategoriArray.size() > 0 & myRecyclerViewCategory != null) {
                    myRecyclerViewCategory.setAdapter(new KategoriGridAdapter(gridKategoriArray, getApplicationContext()));
                }
                myRecyclerViewCategory.setLayoutManager(myLayoutManagerKategori);
            }
        }.execute();


        return kategoris;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notification) {
            Intent intent = new Intent(getApplicationContext(), NotifikasiActivity.class);
            startActivity(intent);
            return true;
        }
//        if (id == R.id.action_barcode) {
//            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
//            startActivity(intent);
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent;

        if (id == R.id.nav_faq) {
            intent = new Intent(getApplicationContext(), FaqActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_bantuan) {
            intent = new Intent(getApplicationContext(), BantuanActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_syarat_dan_ketentuan) {
            intent = new Intent(getApplicationContext(), SyaratDanKetentuanActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_info_aplikasi) {
            intent = new Intent(getApplicationContext(), InfoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_instagram) {
            intent = new Intent(getApplicationContext(), InstagramActivity.class);
            startActivity(intent);
        } else if (id == R.id.test) {
            Preferences p = new Preferences();
            p.init(getApplicationContext());
            p.saveBoolean(Common.IS_LOGIN, false);
            p.saveString(Common.USERNAME, "");
            p.saveString(Common.PASSWORD, "");
            intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void startPagerAutoSwipe() {
        update = new Runnable() {
            public void run() {
                if (!touched) {
                    if (currentPage == NUM_PAGES) {
                        currentPage = 0;
                    }
                    if (currentPage < NUM_PAGES)
                        viewPager.setCurrentItem(currentPage++, true);
                }
            }
        };
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 10, 3000);
    }

}
