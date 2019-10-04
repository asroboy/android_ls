package com.lapaksembako.app;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.lapaksembako.app.action.Core;
import com.lapaksembako.app.api.ApiClient;
import com.lapaksembako.app.api.ApiInterface;
import com.lapaksembako.app.api_model.GetAddress;
import com.lapaksembako.app.api_model.PostCart;
import com.lapaksembako.app.helper.Common;
import com.lapaksembako.app.helper.OnListFragmentInteractionListener;
import com.lapaksembako.app.model.Address;
import com.lapaksembako.app.model.Item;
import com.lapaksembako.app.model.Transaction;
import com.lapaksembako.app.model.TransactionDetail;
import com.lapaksembako.app.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PesananActivity extends AppCompatActivity implements View.OnClickListener {

    Spinner spinner;
    ArrayList<String> spinnerArray = new ArrayList<>();
    TextView textViewGantiAlamat, textViewSaldoKomisi, textViewInfo, textViewLabelAlamat, textViewSelesai;
    User user;
    TableLayout tableProduct, tableRingkasan;
    int totalBelanja = 0;
    Core core;
    int deliveryPrice = 0;
    int balance = 0;
    int ID_BARANG = 111;
    int ID_ONGKOS_KIRIM = 112;
    int ID_KOMISI = 113;

    EditText editTextJumlahPakaiKomisi;
    TextView textViewTotal, textViewAlamat;
    Switch switch1;
    ArrayList<Item> items;
    Address alamat;
    int cartId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesanan);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_back_white);
        ab.setTitle("Pesanan");

        tableProduct = findViewById(R.id.tableProduct);
        tableRingkasan = findViewById(R.id.tableRingkasan);
        textViewTotal = findViewById(R.id.textViewTotal);
        textViewSaldoKomisi = findViewById(R.id.textViewSaldoKomisi);
        editTextJumlahPakaiKomisi = findViewById(R.id.editTextJumlahPakaiKomisi);
        textViewAlamat = findViewById(R.id.textViewAlamat);
        textViewInfo = findViewById(R.id.textViewInfo);
        textViewLabelAlamat = findViewById(R.id.textViewLabelAlamat);
        textViewSelesai = findViewById(R.id.textViewSelesai);
        textViewSelesai.setOnClickListener(this);

        switch1 = findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    getKomisi();
                }
            }
        });

        editTextJumlahPakaiKomisi.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                tambahInfoRingkasan(ID_KOMISI, "Dari Komisi", Integer.parseInt(editTextJumlahPakaiKomisi.getText().toString()));
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.toString().equals("")) {
                    editTextJumlahPakaiKomisi.setText(String.valueOf(0));
                }
                int jlmKomisi = Integer.parseInt(s.toString());
                if (jlmKomisi > balance) {
                    Toast.makeText(getApplicationContext(), "Saldo tidak cukup", Toast.LENGTH_SHORT).show();
                    editTextJumlahPakaiKomisi.setText(String.valueOf(balance));
                }

            }
        });

        final Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            user = bundle.getParcelable("user");
            items = bundle.getParcelableArrayList("items");
            totalBelanja = bundle.getInt("total");
            cartId = bundle.getInt("cart_id");
        }

        core = new Core(user);
        tambahInfoRingkasan(ID_BARANG, "Barang", totalBelanja);
        textViewTotal.setText("Rp " + totalBelanja);

        spinnerArray.add("COD");
        spinnerArray.add("Ambil ke toko");
        spinnerArray.add("Kirim / Transfer Pembayaran");

        textViewGantiAlamat = findViewById(R.id.textViewGantiAlamat);
        textViewGantiAlamat.setOnClickListener(this);
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        spinnerArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);

        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    textViewInfo.setVisibility(View.GONE);
                } else {
                    textViewInfo.setVisibility(View.VISIBLE);
                }

                if (i == 0 || i == 2) {
                    textViewLabelAlamat.setVisibility(View.VISIBLE);
                    textViewAlamat.setVisibility(View.VISIBLE);
                    textViewGantiAlamat.setVisibility(View.VISIBLE);

                    if (deliveryPrice == 0) {
                        core.getDeliveryPrice(new Core.OnCallCompleteListener() {
                            @Override
                            public void onSuccess(Intent data) {
                                deliveryPrice = data.getIntExtra("delivery_price", 0);
                                tambahInfoRingkasan(ID_ONGKOS_KIRIM, "Ongkos kirim", deliveryPrice);
                                totalBelanja = totalBelanja + deliveryPrice;
                                textViewTotal.setText("Rp " + totalBelanja);
                            }

                            @Override
                            public void onFailed(Intent data) {

                            }
                        });

                        if (alamat == null)
                            getDefaultAddress();
                    }


                } else {
                    textViewLabelAlamat.setVisibility(View.GONE);
                    textViewAlamat.setVisibility(View.GONE);
                    textViewGantiAlamat.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        for (Item item : items) {
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);

            TextView tv1 = new TextView(this);
            tv1.setText(item.getNama());
            tv1.setLayoutParams(lp);

            TextView tv2 = new TextView(this);
            tv2.setText(String.valueOf(item.getQuantity()));
            tv2.setLayoutParams(lp);

            TextView tv3 = new TextView(this);
            int hrgTot = item.getHarga() * item.getQuantity();
            tv3.setText("Rp " + hrgTot);
            tv3.setLayoutParams(lp);
            tv3.setGravity(Gravity.RIGHT);

            tr.addView(tv1);
            tr.addView(tv2);
            tr.addView(tv3);

            tableProduct.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }

    }

    private void getDefaultAddress() {
        core.getDefaultAddress(new Core.OnCallCompleteListener() {
            @Override
            public void onSuccess(Intent data) {
                Bundle bundle = data.getBundleExtra("bundle");
                if (bundle != null) {
                    alamat = bundle.getParcelable("alamat");
                    textViewAlamat.setText(alamat.getAddress());
                }

            }

            @Override
            public void onFailed(Intent data) {

            }
        });
    }

    private void tambahInfoRingkasan(int id, String nama, int nominal) {
        if (tableRingkasan.findViewById(id) != null) {
            tableRingkasan.removeView(tableRingkasan.findViewById(id));
        }
        TableRow tr = new TableRow(PesananActivity.this);
        tr.setId(id);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);

        TextView tv1 = new TextView(PesananActivity.this);
        tv1.setText(nama);
        tv1.setLayoutParams(lp);

        TextView tv2 = new TextView(PesananActivity.this);
        tv2.setText("Rp " + nominal);
        tv2.setLayoutParams(lp);
        tv2.setGravity(Gravity.RIGHT);

        tr.addView(tv1);
        tr.addView(tv2);

        tableRingkasan.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            getDefaultAddress();
        }
    }

    private void getKomisi() {
        core.getBalance(user.getId(), new Core.OnCallCompleteListener() {
            @Override
            public void onSuccess(Intent data) {
                balance = data.getIntExtra("balance", 0);
                textViewSaldoKomisi.setText("Saldo komisi anda Rp " + balance);
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
        if (id == R.id.textViewGantiAlamat) {
            Intent intent = new Intent(PesananActivity.this, ListAlamatActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", user);
            intent.putExtra("bundle", bundle);
            startActivityForResult(intent, 1);
        }

        if (id == R.id.textViewSelesai) {
            createTransaction();
        }
    }


    private void createTransaction() {
        final Transaction transaction = new Transaction();
        transaction.setAddress(alamat.getAddress());
        transaction.setIdCity(alamat.getCityId());
        transaction.setIdProvince(alamat.getProvinsiId());
        transaction.setIdMember(user.getId());
        Date now = new Date();
        String transactionCode = user.getId() + "-" + now.getTime();
        transaction.setTransactionCode(transactionCode);
        transaction.setDeliveryPrice(deliveryPrice);
        transaction.setPostalCode(alamat.getPostalCode());
        transaction.setTotalNominal(totalBelanja);
        transaction.setIdDistrict(0);
        transaction.setStatus("Pending");
        transaction.setPaymentStatus("Unpaid");

        core.createTransaction(transaction, new Core.OnCallCompleteListener() {
            @Override
            public void onSuccess(Intent data) {
                String transactionCode = data.getStringExtra("code");
                List<TransactionDetail> transactionDetailList = new ArrayList<>();
                for (Item item : items) {
                    TransactionDetail transactionDetail = new TransactionDetail();
                    transactionDetail.setIdProduct(item.getId());
                    transactionDetail.setTransactionCode(transactionCode);
                    transactionDetail.setQuantity(item.getQuantity());
                    transactionDetail.setProductPrice(item.getHarga());
                    transactionDetail.setTotalPrice(item.getQuantity() * item.getHarga());
                    transactionDetailList.add(transactionDetail);
                    deleteItemCart(cartId, item.getId(), null);
                }

                core.createTransactionDetail(transactionDetailList, new Core.OnCallCompleteListener() {
                    @Override
                    public void onSuccess(Intent data) {
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailed(Intent data) {

                    }
                });

            }

            @Override
            public void onFailed(Intent data) {

            }
        });
    }

    private void deleteItemCart(int idCart, int idItem, final Core.OnCallCompleteListener listener) {
        Call<PostCart> getCartCall = ApiClient.getClient().create(ApiInterface.class).deleteItemFromCart(idCart, idItem);
        getCartCall.enqueue(new Callback<PostCart>() {
            @Override
            public void onResponse(Call<PostCart> call, Response<PostCart> response) {
            }

            @Override
            public void onFailure(Call<PostCart> call, Throwable t) {
            }
        });

    }

}
