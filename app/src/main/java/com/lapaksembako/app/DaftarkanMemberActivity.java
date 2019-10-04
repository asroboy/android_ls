package com.lapaksembako.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lapaksembako.app.action.Core;
import com.lapaksembako.app.api.ApiClient;
import com.lapaksembako.app.api.ApiInterface;
import com.lapaksembako.app.api_model.GetUserDaftar;
import com.lapaksembako.app.helper.Common;
import com.lapaksembako.app.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DaftarkanMemberActivity extends AppCompatActivity implements View.OnClickListener {

    User userLogin;
    String referralCode;
    Core core;
    Button buttonDaftarkan;
    ApiInterface apiInterface;
    EditText editTextNama, editTextEmail, editTextNomorHandphone, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftarkan_member);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Daftarkan Baru");
        ab.setHomeAsUpIndicator(R.drawable.ic_back_white);

        buttonDaftarkan = findViewById(R.id.buttonDaftarkan);
        editTextNama = findViewById(R.id.editTextNama);
        editTextNomorHandphone = findViewById(R.id.editTextNomorHandphone);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonDaftarkan.setOnClickListener(this);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            userLogin = bundle.getParcelable("user");
            referralCode = bundle.getString("referal_code");
        }

        core = new Core(userLogin);
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
            User user = new User();
            user.setNama(editTextNama.getText().toString());
            user.setPhone(editTextNomorHandphone.getText().toString());
            user.setPassword(editTextPassword.getText().toString());
            user.setEmail(editTextEmail.getText().toString());
            user.setReferrerId(referralCode);

            if (editTextNama.getText().toString().equals("")) {
                showWarning("Harap isi kolom nama");
            } else if (editTextEmail.getText().toString().equals("")) {
                showWarning("Harap isi kolom email");
            } else if (editTextPassword.getText().toString().equals("")) {
                showWarning("Harap isi kolom password");
            } else if (editTextNomorHandphone.getText().toString().equals("")) {
                showWarning("Harap isi kolom nomor handphone");
            } else {
                daftar(user);
            }
        }
    }

    private void daftar(User user) {
        final String encryptedPassword = Common.encryp(user.getPassword());
        Call<GetUserDaftar> ktt = apiInterface.postUserDaftar(user.getNama(), user.getPhone(), user.getEmail(), encryptedPassword, user.getReferrerId());
        Log.d(Common.TAG, "body : " + Common.bodyToString(ktt.request().body()));
        ktt.enqueue(new Callback<GetUserDaftar>() {
            @Override
            public void onResponse(Call<GetUserDaftar> call, Response<GetUserDaftar> response) {

                Log.d(Common.TAG, "Response : " + response.raw());

                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("success")) {
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Log.d(Common.TAG, "Register Gagal : " + response.body().getMessage());
                        showWarning("Pendaftaran gagal dilakukan, silahkan coba lagi. Kesalahan: " + response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<GetUserDaftar> call, Throwable t) {
                Log.d(Common.TAG, "Register Error : " + t.getMessage());
            }
        });
    }

    private void showWarning(String message) {
        Common.showWarningDialog(DaftarkanMemberActivity.this, message, new Common.OnDialogActionSelected() {
            @Override
            public void onPositiveClicked(DialogInterface dialog, int id) {
                dialog.dismiss();
            }

            @Override
            public void onNegativeClicked(DialogInterface dialog, int id) {

            }
        });
    }
}
