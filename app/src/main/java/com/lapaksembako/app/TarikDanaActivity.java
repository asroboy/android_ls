package com.lapaksembako.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.lapaksembako.app.action.Core;
import com.lapaksembako.app.api.ApiClient;
import com.lapaksembako.app.api.ApiInterface;
import com.lapaksembako.app.api_model.GetAkunBank;
import com.lapaksembako.app.helper.Common;
import com.lapaksembako.app.model.AkunBank;
import com.lapaksembako.app.model.Poin;
import com.lapaksembako.app.model.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TarikDanaActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    RadioButton ambilDiToko;
    RadioButton tranfer;
    User userLogin;
    int balance = 0;
    EditText editTextNominal;
    Spinner spinnerPilihBank;
    TextView textViewAtasNama;
    TextView textViewNomorRekening, textViewTambahAkun, textViewSaldo;
    TextView textView70, textView71, textView73;
    Button buttonTarik;
    boolean isAmbilDiToko = true;
    ApiInterface apiInterface;
    ArrayList<AkunBank> akunBanks;
    AkunBank selectedAkun;
    Core core;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarik_dana);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Tarik Dana");
        ab.setHomeAsUpIndicator(R.drawable.ic_back_white);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        core = new Core(userLogin);

        textViewSaldo = findViewById(R.id.textViewSaldo);
        ambilDiToko = findViewById(R.id.radioButtonAmbilDiToko);
        tranfer = findViewById(R.id.radioButtonTransfer);
        editTextNominal = findViewById(R.id.editTextNominal);
        spinnerPilihBank = findViewById(R.id.spinnerPilihBank);
        textViewAtasNama = findViewById(R.id.textViewAtasNama);
        textViewNomorRekening = findViewById(R.id.textViewNomorRekening);
        textViewTambahAkun = findViewById(R.id.textViewTambahAkun);
        textView70 = findViewById(R.id.textView70);
        textView71 = findViewById(R.id.textView71);
        textView73 = findViewById(R.id.textView73);
        buttonTarik = findViewById(R.id.buttonTarik);

        buttonTarik.setOnClickListener(this);
        textViewTambahAkun.setOnClickListener(this);
        ambilDiToko.setOnCheckedChangeListener(this);
        tranfer.setOnCheckedChangeListener(this);

        switchTransferAmbil(false);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            userLogin = bundle.getParcelable("user");
            balance = bundle.getInt("balance");
        }

        textViewSaldo.setText("Anda memiliki saldo sebesar Rp " + balance);
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

    private void switchTransferAmbil(boolean isAmbilDiToko) {
        this.isAmbilDiToko = isAmbilDiToko;
        if (!isAmbilDiToko) {
            spinnerPilihBank.setVisibility(View.GONE);
            textViewAtasNama.setVisibility(View.GONE);
            textViewNomorRekening.setVisibility(View.GONE);
            textView70.setVisibility(View.GONE);
            textView71.setVisibility(View.GONE);
            textView73.setVisibility(View.GONE);
            textViewTambahAkun.setVisibility(View.GONE);
        } else {
            spinnerPilihBank.setVisibility(View.VISIBLE);
            textViewAtasNama.setVisibility(View.VISIBLE);
            textViewNomorRekening.setVisibility(View.VISIBLE);
            textView70.setVisibility(View.VISIBLE);
            textView71.setVisibility(View.VISIBLE);
            textView73.setVisibility(View.VISIBLE);
            textViewTambahAkun.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id = compoundButton.getId();
        if (id == R.id.radioButtonAmbilDiToko) {
            switchTransferAmbil(true);
        }
        if (id == R.id.radioButtonTransfer) {
            switchTransferAmbil(false);
            getAkunBank();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonTarik) {
            String typeTarik = isAmbilDiToko ? "Ke Toko" : "Transfer";
            int nominal = Integer.parseInt(editTextNominal.getText().toString());
            if(nominal > 0){
                if(nominal > balance){
                    showWarning("Anda melebihi batas saldo", 0);
                }else {
                    core.withdraw(userLogin.getId(), typeTarik, selectedAkun.getBankName(), selectedAkun.getBankAccount(), selectedAkun.getAccountNumber(), nominal, "Pending", new Core.OnCallCompleteListener() {
                        @Override
                        public void onSuccess(Intent data) {
                            showWarning("Permohonan penarikan dana berhasil, silahkan tunggu sampai proses seelsai dilakukan oleh Admin Lapak Sembako", 1);
                        }

                        @Override
                        public void onFailed(Intent data) {
                            showWarning("Terjadi kesalahan saat menghubungkan dengan server, silahkan coba lagi setelah beberapa saat", 0);
                        }
                    });
                }

            }else{
                showWarning("Nominal tidak boleh kurang dari 0", 0);
            }

        }

        if (id == R.id.textViewTambahAkun) {
            tambahAkunBank();
        }
    }

    private void showWarning(String message, final int actionCode) {
        Common.showWarningDialog(TarikDanaActivity.this, message, new Common.OnDialogActionSelected() {
            @Override
            public void onPositiveClicked(DialogInterface dialog, int id) {
                dialog.dismiss();
                if (actionCode == 1)
                    finish();
            }

            @Override
            public void onNegativeClicked(DialogInterface dialog, int id) {

            }
        });
    }

    private void tambahAkunBank() {
        Intent intent = new Intent(TarikDanaActivity.this, TambahAkunBankActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", userLogin);
        intent.putExtras(bundle);
        startActivityForResult(intent, Common.REQUEST_ADD_BANK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Common.REQUEST_ADD_BANK) {
            if (resultCode == RESULT_OK) {
                getAkunBank();
            }
        }
    }

    private void getAkunBank() {
        akunBanks = new ArrayList<>();
        Call<GetAkunBank> flashSaleCall = apiInterface.getAkunBank(userLogin.getId());
        flashSaleCall.enqueue(new Callback<GetAkunBank>() {
            @Override
            public void onResponse(Call<GetAkunBank> call, Response<GetAkunBank> response) {
                if (response.isSuccessful()) {
                    Log.d(Common.TAG, response.body().getStatus() + " " + response.body().getAkunBanks().size());
                    if (response.body().getAkunBanks() != null) {
                        ArrayList<String> banks = new ArrayList<>();
                        for (AkunBank akun : response.body().getAkunBanks()) {
                            akunBanks.add(akun);
                            banks.add(akun.getBankName());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                                R.layout.spinner_item, banks);
                        spinnerPilihBank.setAdapter(adapter);
                        spinnerPilihBank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if(akunBanks.size() > 0){
                                    selectedAkun = akunBanks.get(i);
                                    textViewAtasNama.setText(selectedAkun.getBankAccount());
                                    textViewNomorRekening.setText(selectedAkun.getAccountNumber());
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                } else {
                    Log.e(Common.TAG, "Request gagal " + response.message());
                }

            }

            @Override
            public void onFailure(Call<GetAkunBank> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
            }
        });
    }
}
