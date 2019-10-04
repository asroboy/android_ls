package com.lapaksembako.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lapaksembako.app.api.ApiClient;
import com.lapaksembako.app.api.ApiInterface;
import com.lapaksembako.app.api_model.GetUserDaftar;
import com.lapaksembako.app.helper.Common;
import com.lapaksembako.app.model.User;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DaftarActivity extends AppCompatActivity {

    TextView textViewMasuk, textViewDaftar2;
    ApiInterface apiInterface;
    EditText editTextNama, editTextNomorHandphone, editTextPassword, editTextKodeReferral;

    Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
        getSupportActionBar().hide();

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        editTextNama = findViewById(R.id.editTextNama);
        editTextNomorHandphone = findViewById(R.id.editTextNomorHandphone);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextKodeReferral = findViewById(R.id.editTextKodeReferral);
        button2 = findViewById(R.id.button2);

        textViewMasuk = findViewById(R.id.textViewMasuk);
        textViewDaftar2 = findViewById(R.id.textViewResetPassword);
        textViewMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
        button2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        editTextPassword.setTransformationMethod(null);
                        return true;
                    case MotionEvent.ACTION_UP:
                        editTextPassword.setTransformationMethod(new PasswordTransformationMethod());
                        return true;
                }
                return false;
            }
        });

        textViewDaftar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User();
                user.setNama(editTextNama.getText().toString());
                user.setPhone(editTextNomorHandphone.getText().toString());
                user.setPassword(editTextPassword.getText().toString());
                user.setReferrerId(editTextKodeReferral.getText().toString());
                if (editTextNama.getText().toString().equals("")) {
                    showWarning("Harap isi nama Anda");
                } else if (editTextPassword.getText().toString().equals("")) {
                    showWarning("Harap isi kolom password");
                } else if (editTextNomorHandphone.getText().toString().equals("")) {
                    showWarning("Harap isi nomor handphone");
                } else {
                    daftar(user);
                }

            }
        });

    }

    private void showWarning(String message) {
        Common.showWarningDialog(DaftarActivity.this, message, new Common.OnDialogActionSelected() {
            @Override
            public void onPositiveClicked(DialogInterface dialog, int id) {
                dialog.dismiss();
            }

            @Override
            public void onNegativeClicked(DialogInterface dialog, int id) {

            }
        });
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
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
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


}
