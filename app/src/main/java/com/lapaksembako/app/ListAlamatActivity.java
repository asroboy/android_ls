package com.lapaksembako.app;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.lapaksembako.app.action.Core;
import com.lapaksembako.app.adapter.AlamatListAdapter;
import com.lapaksembako.app.helper.Common;
import com.lapaksembako.app.model.Address;
import com.lapaksembako.app.model.User;

import java.util.ArrayList;

public class ListAlamatActivity extends AppCompatActivity implements View.OnClickListener {

    ListView listAlamat;
    ArrayList<Address> addresses = new ArrayList<>();
    User user;
    Core core;
    AlamatListAdapter adapter;
    Button buttonTambahAlamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_alamat);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_back_white);
        ab.setTitle("Daftar Alamat");

        listAlamat = findViewById(R.id.listAlamat);
        buttonTambahAlamat = findViewById(R.id.buttonTambahAlamat);
        buttonTambahAlamat.setOnClickListener(this);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            user = bundle.getParcelable("user");
        }

        loadAddress();

    }


    private void loadAddress() {
        if (user != null) {
            core = new Core(user);
            core.getAddress(new Core.OnCallCompleteListener() {
                @Override
                public void onSuccess(Intent data) {
                    addresses = data.getParcelableArrayListExtra("alamats");
                    adapter = new AlamatListAdapter(addresses, getApplicationContext());
                    listAlamat.setAdapter(adapter);
                }

                @Override
                public void onFailed(Intent data) {

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
        if (id == R.id.buttonTambahAlamat) {
            tambahAlamat();
        }
    }

    private void tambahAlamat() {
        Intent intent = new Intent(ListAlamatActivity.this, AlamatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);
        intent.putExtras(bundle);
        startActivityForResult(intent, Common.REQUEST_ADD_ADDRESS);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Common.REQUEST_ADD_ADDRESS){
            if(resultCode == RESULT_OK){
                addresses.clear();
                loadAddress();
                adapter.notifyDataSetChanged();
            }
        }
    }
}
