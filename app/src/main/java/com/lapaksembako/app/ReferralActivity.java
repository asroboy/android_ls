package com.lapaksembako.app;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.lapaksembako.app.action.Core;
import com.lapaksembako.app.adapter.ReferrerListAdapter;
import com.lapaksembako.app.model.User;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ReferralActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textViewJumlahRefer, textViewReferralCode;
    ListView listViewReferral;
    Core core;
    User userLogin;
    Button buttonDaftarkan;
    String referralCode;

    private final int REQUEST_ADD_USER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Referral");
        ab.setHomeAsUpIndicator(R.drawable.ic_back_white);

        textViewJumlahRefer = findViewById(R.id.textViewJumlahRefer);
        textViewReferralCode = findViewById(R.id.textViewReferralCode);
        listViewReferral = findViewById(R.id.listViewReferral);
        buttonDaftarkan = findViewById(R.id.buttonDaftarkan);
        buttonDaftarkan.setOnClickListener(this);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            userLogin = bundle.getParcelable("user");
        }

        core = new Core(userLogin);
        loadData();
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
        if (id == R.id.buttonDaftarkan) {
            Intent intent = new Intent(ReferralActivity.this, DaftarkanMemberActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("referal_code", referralCode);
            bundle.putParcelable("user", userLogin);
            intent.putExtra("bundle", bundle);
            startActivityForResult(intent, REQUEST_ADD_USER);
        }
    }

    private void loadData() {
        core.getReferral(userLogin.getId(), new Core.OnCallCompleteListener() {
            @Override
            public void onSuccess(Intent data) {
                referralCode = data.getStringExtra("referral_code");
                textViewReferralCode.setText(referralCode);

                ArrayList<User> users = data.getParcelableArrayListExtra("referrer");
                textViewJumlahRefer.setText(String.valueOf(users.size()));

                ArrayList<String> usersName = new ArrayList<>();
                for (User u : users) {
                    usersName.add(u.getNama());
                }
                ReferrerListAdapter itemsAdapter = new ReferrerListAdapter(getApplicationContext(), R.layout.row_item_refer, users);
                listViewReferral.setAdapter(itemsAdapter);
                itemsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(Intent data) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            loadData();
    }
}
