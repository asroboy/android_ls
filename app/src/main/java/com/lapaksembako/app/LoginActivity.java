package com.lapaksembako.app;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.login.Login;
import com.lapaksembako.app.api.ApiClient;
import com.lapaksembako.app.api.ApiInterface;
import com.lapaksembako.app.api_model.GetUserLogin;
import com.lapaksembako.app.helper.Common;
import com.lapaksembako.app.helper.Preferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextView daftar, login;
    ApiInterface apiInterface;
    EditText editTextNomorHandphone, editTextPassword;
    Button buttonLupaPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        daftar = findViewById(R.id.textViewDaftar);
        login = findViewById(R.id.textViewLogin);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        editTextNomorHandphone = findViewById(R.id.editTextNomorHandphone);
        editTextPassword = findViewById(R.id.editTextPassword);

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DaftarActivity.class));
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editTextNomorHandphone.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if(username.equals("") || password.equals("")){
                    showWarning("Username dan password harap diisi");
                }else{
                    login(username, password);
                }

            }
        });

        buttonLupaPassword = findViewById(R.id.buttonLupaPassword);
        buttonLupaPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, LupaPasswordActivity.class));
                finish();
            }
        });
    }


    private void setIsLogin() {
        Preferences preferences = new Preferences();
        preferences.init(getApplicationContext());
        preferences.saveBoolean(Common.IS_LOGIN, true);
    }

    private void showWarning(String message) {
        Common.showWarningDialog(LoginActivity.this, message, new Common.OnDialogActionSelected() {
            @Override
            public void onPositiveClicked(DialogInterface dialog, int id) {
                dialog.dismiss();
            }

            @Override
            public void onNegativeClicked(DialogInterface dialog, int id) {

            }
        });
    }

    private void login(final String phone, final String password) {
        final ProgressDialog pd = Common.initProgresDialog(LoginActivity.this, "Memproses", "Mohon tunggu ... ");
        pd.show();
        final String encryptedPassword = Common.encryp(password);
        Log.d(Common.TAG, phone + " | " + encryptedPassword);
        Call<GetUserLogin> ktt = apiInterface.postUserLogin(phone, encryptedPassword);
        ktt.enqueue(new Callback<GetUserLogin>() {
            @Override
            public void onResponse(Call<GetUserLogin> call, Response<GetUserLogin> response) {
                Log.d(Common.TAG, response.body().getStatus());
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("success")) {
                        Preferences preferences = new Preferences();
                        preferences.init(getApplicationContext());
                        preferences.saveString(Common.USERNAME, phone);
                        preferences.saveString(Common.PASSWORD, password);

                        setIsLogin();
                        Intent intent = new Intent(getApplicationContext(), MainBottomNavigationActivity.class);
                        intent.putExtra("user", response.body().getDataUser());
                        startActivity(intent);
                        pd.dismiss();
                        finish();
                    } else {
                        pd.dismiss();
                        showWarning("Tidak bisa login, periksa kembali nomor ponsel dan password Anda. Pastikan nomor ponsel terdaftar.");
                    }
                }
            }

            @Override
            public void onFailure(Call<GetUserLogin> call, Throwable t) {
                pd.dismiss();
            }
        });
    }
}
