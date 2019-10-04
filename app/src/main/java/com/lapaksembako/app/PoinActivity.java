package com.lapaksembako.app;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.lapaksembako.app.action.Core;
import com.lapaksembako.app.adapter.PoinHistoriListAdapter;
import com.lapaksembako.app.adapter.TransaksiHistoriListAdapter;
import com.lapaksembako.app.model.Balance;
import com.lapaksembako.app.model.Poin;
import com.lapaksembako.app.model.User;

import java.util.ArrayList;

public class PoinActivity extends AppCompatActivity {

    TextView textViewTotalPoinBalance;
    ListView listViewHistoryPoin;
    User userLogin;
    int poin = 0;
    Core core;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poin);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Poin");
        ab.setHomeAsUpIndicator(R.drawable.ic_back_white);
        textViewTotalPoinBalance = findViewById(R.id.textViewTotalPoinBalance);
        listViewHistoryPoin = findViewById(R.id.listViewHistoryPoin);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            userLogin = bundle.getParcelable("user");
            poin = bundle.getInt("poin");
        }

        textViewTotalPoinBalance.setText(String.valueOf(poin));
        core = new Core(userLogin);
        core.getSejarahPoin(userLogin.getId(), new Core.OnCallCompleteListener() {
            @Override
            public void onSuccess(Intent data) {
                int totalBalance = data.getIntExtra("total_balance", 0);

                ArrayList<Poin> points = data.getParcelableArrayListExtra("poins");

                PoinHistoriListAdapter itemsAdapter = new PoinHistoriListAdapter(getApplicationContext(), R.layout.row_item_transaksi_hist, points);
                listViewHistoryPoin.setAdapter(itemsAdapter);
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
}
