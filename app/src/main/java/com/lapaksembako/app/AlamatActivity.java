package com.lapaksembako.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.lapaksembako.app.api.ApiClient;
import com.lapaksembako.app.api.ApiInterface;
import com.lapaksembako.app.api_model.GetAddress;
import com.lapaksembako.app.api_model.GetCity;
import com.lapaksembako.app.api_model.GetProvince;
import com.lapaksembako.app.api_model.GetUserDaftar;
import com.lapaksembako.app.api_model.PostResult;
import com.lapaksembako.app.helper.Common;
import com.lapaksembako.app.model.Address;
import com.lapaksembako.app.model.City;
import com.lapaksembako.app.model.Province;
import com.lapaksembako.app.model.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlamatActivity extends AppCompatActivity implements View.OnClickListener {

    Spinner spinnerProvinsi, spinnerKota;
    EditText editTextAlamat;
    CheckBox checkBoxDefault;
    Button buttonSimpanAlamat;
    ApiInterface apiInterface;
    ArrayList<Province> provinces;
    ArrayList<City> cities;
    Province selectedProvince;
    City selectedCity;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alamat);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            user = bundle.getParcelable("user");
        }
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Tambah Alamat");
        ab.setHomeAsUpIndicator(R.drawable.ic_back_white);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        spinnerProvinsi = findViewById(R.id.spinnerProvinsi);
        spinnerKota = findViewById(R.id.spinnerKota);
        spinnerKota.setEnabled(false);
        editTextAlamat = findViewById(R.id.editTextAlamat);
        checkBoxDefault = findViewById(R.id.checkBoxDefault);
        checkBoxDefault.setSelected(true);
        buttonSimpanAlamat = findViewById(R.id.buttonSimpanAlamat);
        buttonSimpanAlamat.setOnClickListener(this);

        getListProvinsi();

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
        if (id == R.id.buttonSimpanAlamat) {
            Address address = new Address();
            address.setAddress(editTextAlamat.getText().toString());
            address.setCityId(selectedCity.getId());
            address.setProvinsiId(selectedProvince.getId());
            address.setDefault(checkBoxDefault.isSelected());
            simpan(address);
        }
    }

    private void simpan(Address address) {
        Call<PostResult> updateProfileCall = apiInterface.postSimpanAlamat(address.getProvinsiId(),
                address.getCityId(), address.getAddress(),
                address.getPostalCode(), String.valueOf(address.isDefault()), user.getId());
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
        Common.showWarningDialog(AlamatActivity.this, message, new Common.OnDialogActionSelected() {
            @Override
            public void onPositiveClicked(DialogInterface dialog, int id) {
                dialog.dismiss();
            }

            @Override
            public void onNegativeClicked(DialogInterface dialog, int id) {

            }
        });
    }


    private void getListProvinsi() {
        Call<GetProvince> updateProfileCall = apiInterface.getListProvince();
        updateProfileCall.enqueue(new Callback<GetProvince>() {
            @Override
            public void onResponse(Call<GetProvince> call, Response<GetProvince> response) {
                Log.d(Common.TAG, response.body().getStatus());
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("success")) {
                        ArrayList<String> namaProvinsi = new ArrayList<>();
                        provinces = new ArrayList<>();
                        for (Province province : response.body().getProvinces()) {
                            namaProvinsi.add(province.getName());
                            provinces.add(province);
                        }

                        ArrayAdapter<String> adapterProvinsi = new ArrayAdapter<>(getApplicationContext(),
                                R.layout.spinner_item, namaProvinsi);
                        spinnerProvinsi.setAdapter(adapterProvinsi);
                        spinnerProvinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                selectedProvince = provinces.get(i);
                                getListCity(selectedProvince.getId());
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    } else {
                        Log.d(Common.TAG, "Gagal ambil data provinsi");
                    }
                }
            }

            @Override
            public void onFailure(Call<GetProvince> call, Throwable t) {
                Log.d(Common.TAG, "Register Error : " + t.getMessage());
            }
        });
    }


    private void getListCity(int provinceId) {
        Call<GetCity> updateProfileCall = apiInterface.getListCity(provinceId);
        updateProfileCall.enqueue(new Callback<GetCity>() {
            @Override
            public void onResponse(Call<GetCity> call, Response<GetCity> response) {
//                Log.d(Common.TAG, response.body().getStatus());
                if (response.isSuccessful()) {
                    spinnerKota.setEnabled(true);
                    if (response.body().getStatus().equals("success")) {
                        ArrayList<String> namaCity = new ArrayList<>();
                        cities = new ArrayList<>();
                        for (City city : response.body().getCities()) {
                            namaCity.add(city.getName());
                            cities.add(city);
                        }

                        ArrayAdapter<String> adapterProvinsi = new ArrayAdapter<>(getApplicationContext(),
                                R.layout.spinner_item, namaCity);
                        spinnerKota.setAdapter(adapterProvinsi);
                        spinnerKota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                selectedCity = cities.get(i);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    } else {
                        Log.d(Common.TAG, "Gagal ambil data provinsi");
                    }
                }
            }

            @Override
            public void onFailure(Call<GetCity> call, Throwable t) {
                Log.d(Common.TAG, "Register Error : " + t.getMessage());
            }
        });
    }

}
