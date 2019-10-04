package com.lapaksembako.app;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.lapaksembako.app.action.Core;
import com.lapaksembako.app.adapter.ReferrerListAdapter;
import com.lapaksembako.app.adapter.TransaksiHistoriListAdapter;
import com.lapaksembako.app.model.Balance;
import com.lapaksembako.app.model.User;

import java.util.ArrayList;

public class KomisiActivity extends AppCompatActivity implements View.OnClickListener {

    Core core;
    User userLogin;
    int balance = 0;
    TextView textViewTotalBalance, textViewJumlahTransaksi;
    ListView listViewHistoryBalance;
    Button buttonTarikDana;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_komisi);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Komisi");
        ab.setHomeAsUpIndicator(R.drawable.ic_back_white);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            userLogin = bundle.getParcelable("user");
            balance = bundle.getInt("balance");
        }

        textViewTotalBalance = findViewById(R.id.textViewTotalBalance);
        textViewJumlahTransaksi = findViewById(R.id.textViewJumlahTransaksi);
        listViewHistoryBalance = findViewById(R.id.listViewHistoryBalance);
        buttonTarikDana = findViewById(R.id.buttonTarikDana);
        textViewTotalBalance.setText("Rp " + balance);
        buttonTarikDana.setOnClickListener(this);

        core = new Core(userLogin);
        core.getSejarahTransaksi(userLogin.getId(), new Core.OnCallCompleteListener() {
            @Override
            public void onSuccess(Intent data) {
                ArrayList<Balance> users = data.getParcelableArrayListExtra("balances");
                int jumlahTransaksi = users.size();
                textViewJumlahTransaksi.setText(String.valueOf(jumlahTransaksi));

                TransaksiHistoriListAdapter itemsAdapter = new TransaksiHistoriListAdapter(getApplicationContext(), R.layout.row_item_transaksi_hist, users);
                listViewHistoryBalance.setAdapter(itemsAdapter);
            }

            @Override
            public void onFailed(Intent data) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonTarikDana) {
            Intent intent = new Intent(KomisiActivity.this, TarikDanaActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", userLogin);
            bundle.putInt("balance", balance);
            intent.putExtra("bundle", bundle);
            startActivity(intent);
        }
    }
}
