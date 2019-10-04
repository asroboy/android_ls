package com.lapaksembako.app;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class BantuanActivity extends AppCompatActivity {

    Spinner spinnerJenisBantuan;
    String [] jenisBantuans = {"-- Jenis Bantuan --"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bantuan);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_back_white);


        ArrayAdapter<String> adapterJenisBantuan = new ArrayAdapter<>(this,
                R.layout.spinner_item, jenisBantuans);
        spinnerJenisBantuan = findViewById(R.id.spinnerJenisBantuan);
        spinnerJenisBantuan.setAdapter(adapterJenisBantuan);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
}
