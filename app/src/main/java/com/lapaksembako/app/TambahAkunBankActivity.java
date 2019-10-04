package com.lapaksembako.app;

import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lapaksembako.app.api.ApiClient;
import com.lapaksembako.app.api.ApiInterface;
import com.lapaksembako.app.api_model.PostResult;
import com.lapaksembako.app.helper.Common;
import com.lapaksembako.app.model.AkunBank;
import com.lapaksembako.app.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahAkunBankActivity extends AppCompatActivity implements View.OnClickListener {


    User user;
    ApiInterface apiInterface;

    Button buttonSimpanAkun;
    EditText editTextNomorAkun, editTextNamaAkun, editTextNamaBank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_akun_bank);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            user = bundle.getParcelable("user");
        }
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Akun Bank");
        ab.setHomeAsUpIndicator(R.drawable.ic_back_white);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);


        editTextNomorAkun = findViewById(R.id.editTextNomorAkun);
        editTextNamaAkun = findViewById(R.id.editTextNamaAkun);
        editTextNamaBank = findViewById(R.id.editTextNamaBank);
        buttonSimpanAkun = findViewById(R.id.buttonSimpanAkun);
        buttonSimpanAkun.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonSimpanAkun) {
            AkunBank akunBank = new AkunBank();
            akunBank.setBankName(editTextNamaBank.getText().toString());
            akunBank.setBankAccount(editTextNamaAkun.getText().toString());
            akunBank.setAccountNumber(editTextNomorAkun.getText().toString());
            akunBank.setIdMember(String.valueOf(user.getId()));
            simpanAkun(akunBank);
        }
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


    private void simpanAkun(AkunBank akunBank) {
        Call<PostResult> updateProfileCall = apiInterface.postSimpanAkunBank(akunBank.getBankName(),
                akunBank.getBankName(), akunBank.getAccountNumber(), user.getId());
        updateProfileCall.enqueue(new Callback<PostResult>() {
            @Override
            public void onResponse(Call<PostResult> call, Response<PostResult> response) {
                Log.d(Common.TAG, response.body().getStatus());
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("success")) {
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Log.d(Common.TAG, "Update Gagal : " + response.body().getMessage());
                        showWarning("Update gagal dilakukan, silahkan coba lagi. Kesalahan: " +
                                response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<PostResult> call, Throwable t) {
                Log.d(Common.TAG, "Register Error : " + t.getMessage());
            }
        });
    }

    private void showWarning(String message) {
        Common.showWarningDialog(TambahAkunBankActivity.this, message, new Common.OnDialogActionSelected() {
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
