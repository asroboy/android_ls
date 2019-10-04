package com.lapaksembako.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.lapaksembako.app.fragments.ChartFragment;
import com.lapaksembako.app.fragments.IntagramPostFragment;
import com.lapaksembako.app.fragments.MainFragment;
import com.lapaksembako.app.fragments.ProfileFragment;
import com.lapaksembako.app.fragments.SejarahBelanjaFragment;
import com.lapaksembako.app.fragments.WishListFragment;
import com.lapaksembako.app.helper.Common;
import com.lapaksembako.app.helper.OnFragmentInteractionListener;
import com.lapaksembako.app.helper.OnListFragmentInteractionListener;
import com.lapaksembako.app.helper.Preferences;
import com.lapaksembako.app.model.Item;
import com.lapaksembako.app.model.User;

public class MainBottomNavigationActivity extends AppCompatActivity implements OnFragmentInteractionListener, OnListFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportActionBar().setTitle("Home");
                    replaceFragment(MainFragment.newInstance(userLogin));
                    return true;
                case R.id.love:
                    getSupportActionBar().setTitle("Favorit");
                    replaceFragment(WishListFragment.newInstance("", userLogin));
                    return true;
                case R.id.n:
                    return true;
                case R.id.navigation_dashboard:
                    getSupportActionBar().setTitle("Sejarah Belanja");
                    replaceFragment(SejarahBelanjaFragment.newInstance("", userLogin));
                    return true;
                case R.id.nav_profile:
                    getSupportActionBar().setTitle("Profil");
                    replaceFragment(ProfileFragment.newInstance(userLogin));
                    return true;
            }
            return false;
        }
    };


    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.fab) {
                getSupportActionBar().setTitle("Keranjang Belanja");
                replaceFragment(ChartFragment.newInstance(userLogin));
            }
        }
    };

    User userLogin;
    FloatingActionButton fab;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bottom_navigation);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.menu_indicator_sm);

        getSupportActionBar().setTitle("Home");

        Bundle b = getIntent().getExtras();
        if (b != null) {
            userLogin = b.getParcelable("user");
        }

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


        BottomNavigationViewEx navView = findViewById(R.id.bnve);
        fab = findViewById(R.id.fab);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fab.setOnClickListener(clickListener);
        fragmentManager = getSupportFragmentManager();
        addFragment(MainFragment.newInstance(userLogin));
    }


    public void addFragment(Fragment fragment) {
        fragmentManager.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
    }

    public void replaceFragment(Fragment fragment) {
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

    @Override
    public void onListFragmentInteraction(Item item) {

    }

    @Override
    public void onListItemDelete(int code) {
        if (code == SUCCESS) {
            getSupportActionBar().setTitle("Keranjang Belanja");
            replaceFragment(ChartFragment.newInstance(userLogin));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Common.logD("ON ACT RESULT");
        if (requestCode == Common.REQUEST_UPDATE_PROFILE) {
            Common.logD("REQUEST UPDATE PROFILE");
            if (data != null) {
                User user = data.getParcelableExtra("user_update");
                Common.logD("Activity result : " + userLogin.getNama());
                userLogin.setNama(user.getNama());
                userLogin.setPhone(user.getPhone());
                userLogin.setEmail(user.getEmail());

                getSupportActionBar().setTitle("Profil");
                replaceFragment(ProfileFragment.newInstance(userLogin));
            } else {
                Common.logD("Data null");
            }
        }
        if (requestCode == Common.REQUEST_ADD_BANK) {
            Common.logD("REQUEST ADD BANK");
            getSupportActionBar().setTitle("Profil");
            replaceFragment(ProfileFragment.newInstance(userLogin));
        }

        if (requestCode == Common.REQUEST_ADD_ADDRESS) {
            Common.logD("REQUEST ADD ALAMAT");
            getSupportActionBar().setTitle("Profil");
            replaceFragment(ProfileFragment.newInstance(userLogin));
        }

        if (requestCode == Common.REQUEST_ITEM_DETAIL) {
            if (resultCode == RESULT_OK) {
                Common.logD("REQUEST ITEM CART");
                getSupportActionBar().setTitle("Keranjang Belanja");
                replaceFragment(ChartFragment.newInstance(userLogin));
            }

        }
        if (requestCode == Common.REQUEST_PESANAN) {
            if (resultCode == RESULT_OK) {
                Common.logD("REQUEST PESANAN");
                getSupportActionBar().setTitle("Sejarah Belanja");
                replaceFragment(SejarahBelanjaFragment.newInstance("", userLogin));
            }

        }
    }
}
