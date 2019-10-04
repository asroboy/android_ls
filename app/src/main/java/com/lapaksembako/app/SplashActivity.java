package com.lapaksembako.app;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lapaksembako.app.api.ApiClient;
import com.lapaksembako.app.api.ApiInterface;
import com.lapaksembako.app.api_model.GetUserLogin;
import com.lapaksembako.app.helper.Common;
import com.lapaksembako.app.helper.Preferences;
import com.lapaksembako.app.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {


    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    public boolean checkPermissionREAD_EXTERNAL_STORAGE(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (AppCompatActivity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context, Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (AppCompatActivity) context,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }


    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{permission},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        boolean isPremissionGranted = checkPermissionREAD_EXTERNAL_STORAGE(SplashActivity.this);
        if (isPremissionGranted) {
            next();
        }


    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    next();
                } else {
                    Common.showWarningDialog(SplashActivity.this, "Harap beri beri izin aplikasi untuk mengakses beberapa fitur", new Common.OnDialogActionSelected() {

                        @Override
                        public void onPositiveClicked(DialogInterface dialog, int id) {
                            checkPermissionREAD_EXTERNAL_STORAGE(SplashActivity.this);
                        }

                        @Override
                        public void onNegativeClicked(DialogInterface dialog, int id) {

                        }
                    });
                }
        }
    }


    private void next() {
        Preferences preferences = new Preferences();
        preferences.init(getApplicationContext());
        if (preferences.getBoolean(Common.IS_WELLCOME, true)) {
            startActivity(new Intent(SplashActivity.this, WellcomeActivity.class));
            finish();
        } else {
            if (preferences.getBoolean(Common.IS_AGREE_TOS, false)) {
                if (preferences.getBoolean(Common.IS_LOGIN, false)) {
                    login(preferences.getString(Common.USERNAME, ""), preferences.getString(Common.PASSWORD, ""));
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }

            } else {
                startActivity(new Intent(SplashActivity.this, TermOfUseActivity.class));
                finish();
            }

        }
    }

    private void login(final String phone, final String password) {
        final String encryptedPassword = Common.encryp(password);
        Log.d(Common.TAG, phone + " | " + encryptedPassword);
        Call<GetUserLogin> ktt = ApiClient.getClient().create(ApiInterface.class).postUserLogin(phone, encryptedPassword);
        ktt.enqueue(new Callback<GetUserLogin>() {
            @Override
            public void onResponse(Call<GetUserLogin> call, Response<GetUserLogin> response) {
                Log.d(Common.TAG, "RE-LOGIN SPLASH " + response.body());
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("success")) {
                        final User user = response.body().getDataUser();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Preferences preferences = new Preferences();
                                preferences.init(getApplicationContext());
                                preferences.saveString(Common.USERNAME, phone);
                                preferences.saveString(Common.PASSWORD, password);

                                Intent intent = new Intent(getApplicationContext(), MainBottomNavigationActivity.class);
                                intent.putExtra("user", user);
                                startActivity(intent);
                                finish();
                            }
                        });

                    } else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Preferences preferences = new Preferences();
                                preferences.init(getApplicationContext());
                                preferences.saveBoolean(Common.IS_LOGIN, false);
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });

                    }
                }
            }

            @Override
            public void onFailure(Call<GetUserLogin> call, Throwable t) {

            }
        });
    }

}
