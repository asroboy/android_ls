package com.lapaksembako.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lapaksembako.app.action.Core;
import com.lapaksembako.app.helper.Common;
import com.lapaksembako.app.model.Balance;
import com.lapaksembako.app.model.BalanceSetting;
import com.lapaksembako.app.model.Poin;
import com.lapaksembako.app.model.Transaction;
import com.lapaksembako.app.model.TransferProof;
import com.lapaksembako.app.model.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class BuktiPembayaranActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int GALLERY = 2;
    public static final int CAMERA = 1;
    public static final String IMAGE_DIRECTORY = "lapak_sembako";

    String pathFile = "";
    ImageView imageView;
    TextView tvInvoiceNumber;
    EditText editTextNamaBank, editTextNoRek, editTextAtasNama, editTextNominalTransfer;
    ImageView imageViewPic;
    Button buttonKirim;
    Core core;
    User user;
    Transaction invoice;
    BalanceSetting balanceSetting;
    int balanceNow = 0;
    int poinNow = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bukti_pembayaran);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Upload Bukti Transfer");
        ab.setHomeAsUpIndicator(R.drawable.ic_back_white);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            user = bundle.getParcelable("user");
            invoice = bundle.getParcelable("invoice");
        }
        core = new Core(user);

        imageView = findViewById(R.id.imageViewPic);
        tvInvoiceNumber = findViewById(R.id.textView9);
        editTextNamaBank = findViewById(R.id.editTextNamaBank);
        editTextNoRek = findViewById(R.id.editTextNoRek);
        editTextAtasNama = findViewById(R.id.editTextAtasNama);
        editTextNominalTransfer = findViewById(R.id.editTextNominalTransfer);
        imageViewPic = findViewById(R.id.imageViewPic);
        buttonKirim = findViewById(R.id.buttonKirim);
        imageViewPic.setOnClickListener(this);
        buttonKirim.setOnClickListener(this);

        tvInvoiceNumber.setText("Invoice " + invoice.getTransactionCode() + "\nTotal Rp " + invoice.getTotalNominal());


        core.getBalanceSetting(new Core.OnCallCompleteListener() {
            @Override
            public void onSuccess(Intent data) {
                Bundle bundle1 = data.getBundleExtra("bundle");
                if (bundle1 != null) {
                    balanceSetting = bundle1.getParcelable("balance-setting");
                }
            }

            @Override
            public void onFailed(Intent data) {

            }
        });

        core.getBalance(user.getId(), new Core.OnCallCompleteListener() {
            @Override
            public void onSuccess(Intent data) {
                balanceNow = data.getIntExtra("balance", 0);
            }

            @Override
            public void onFailed(Intent data) {

            }
        });

        core.getPoin(user.getId(), new Core.OnCallCompleteListener() {
            @Override
            public void onSuccess(Intent data) {
                poinNow = data.getIntExtra("poin", 0);
            }

            @Override
            public void onFailed(Intent data) {

            }
        });

    }


    private void ambilGambarDariGaleri() {
        showPictureDialog();
    }


    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Pilih gambar dari");
        String[] pictureDialogItems = {
                "Galeri",
                "Kamera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    pathFile = saveImg(bitmap);
                    Toast.makeText(BuktiPembayaranActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(BuktiPembayaranActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        }
        if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(thumbnail);
            pathFile = saveImg(thumbnail);
            Toast.makeText(BuktiPembayaranActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }

    }

    public String saveImg(Bitmap bmp) {
        String filename = Calendar.getInstance()
                .getTimeInMillis() + ".jpg";
        File sd = Environment.getExternalStorageDirectory();
        File dest = new File(sd, filename);

        try {
            FileOutputStream out = new FileOutputStream(dest);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();

            return dest.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonKirim) {
            if (editTextNoRek.getText().toString().equals("")) {
                showWarning("Harap isi kolom no rekening");
            } else if (editTextNamaBank.getText().toString().equals("")) {
                showWarning("Harap isi kolom Nama Bank");
            } else if (editTextNominalTransfer.getText().toString().equals("")) {
                showWarning("Harap isi kolom nominal transfer");
            } else if (editTextAtasNama.getText().toString().equals("")) {
                showWarning("Harap isi kolom atas nama");
            } else {
                TransferProof transferProof = new TransferProof();
                transferProof.setIdMember(user.getId());
                transferProof.setAccountNumber(editTextNoRek.getText().toString());
                transferProof.setBankName(editTextNamaBank.getText().toString());
                transferProof.setNominal(Integer.parseInt(editTextNominalTransfer.getText().toString()));
                transferProof.setBankAccount(editTextAtasNama.getText().toString());
                transferProof.setVerified(false);
                core.uploadTransferProof(pathFile, transferProof, new Core.OnCallCompleteListener() {
                    @Override
                    public void onSuccess(Intent data) {
                        simpanKomisi();
//                        setResult(RESULT_OK);
//                        finish();
                    }

                    @Override
                    public void onFailed(Intent data) {
                        showWarning("Gagal mmenimpan data, Silahkan coba lagi");
                    }
                });
            }

        }

        if (id == R.id.imageViewPic) {
            ambilGambarDariGaleri();
        }
    }

    private void showWarning(String message) {
        Common.showWarningDialog(BuktiPembayaranActivity.this, message, new Common.OnDialogActionSelected() {
            @Override
            public void onPositiveClicked(DialogInterface dialog, int id) {
                dialog.dismiss();
            }

            @Override
            public void onNegativeClicked(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
    }

    double totalKomisi = 0.0;
    int totalPoin = 0;

    private void hitungKomisi() {
        SharedPreferences sp = getSharedPreferences("KOMISI", MODE_PRIVATE);
        if (sp.getBoolean("IS_FIRST_TRANSACTION", false)) {
            if (invoice.getTotalNominal() >= 150000) {
                totalKomisi = totalKomisi + balanceSetting.getMemberFirstOff();
            }
            sp.edit().putBoolean("IS_FIRST_TRANSACTION", true).commit();
        }

        double percentMemberFee = balanceSetting.getMemberFee() / 100;
        double persenCashbak = balanceSetting.getMemberCashback() / 100;
        int totalBelanjaDikurangiOngkir = (invoice.getTotalNominal() - invoice.getDeliveryPrice());
        totalKomisi = totalKomisi + (totalBelanjaDikurangiOngkir * percentMemberFee);
        totalKomisi = totalKomisi + (persenCashbak * percentMemberFee);
    }

    private void hitungPoin() {
        totalPoin = invoice.getTotalNominal() / 50000;
    }


    private void simpanKomisi() {
        hitungKomisi();
        if (totalKomisi > 0.0) {
            Balance balance = new Balance();
            balance.setPrevBalance(balanceNow);
            balance.setNextBalance(Integer.parseInt(String.valueOf(totalKomisi)));
            balance.setNominal(balanceNow + Integer.parseInt(String.valueOf(totalKomisi)));
            balance.setIdMember(user.getId());
            balance.setStatus("Received");
            core.simpanKomisi(balance, new Core.OnCallCompleteListener() {
                @Override
                public void onSuccess(Intent data) {
                    simpanPoin();
                }

                @Override
                public void onFailed(Intent data) {
                    simpanPoin();
                }
            });
        } else {
            simpanPoin();
        }
    }

    private void simpanPoin() {
        hitungPoin();
        if (totalPoin > 0) {
            Poin poin = new Poin();
            poin.setPrevBalance(poinNow);
            poin.setNextBalance(totalPoin);
            poin.setNominal(poinNow + totalPoin);
            poin.setIdMember(user.getId());
            poin.setStatus("Received");
            core.simpanPoin(poin, new Core.OnCallCompleteListener() {
                @Override
                public void onSuccess(Intent data) {
                    showKomisiPoinInfo();
                }

                @Override
                public void onFailed(Intent data) {
                    showKomisiPoinInfo();
                }
            });
        } else {
            showKomisiPoinInfo();
        }
    }


    private void showKomisiPoinInfo() {
        String message = "Terimakasih sudah berbelanja\n Anda mendapatkan " + totalPoin + " Poin dan komisi sebesar Rp " + totalKomisi;
        Common.showWarningDialog(BuktiPembayaranActivity.this, message, new Common.OnDialogActionSelected() {
            @Override
            public void onPositiveClicked(DialogInterface dialog, int id) {
                dialog.dismiss();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onNegativeClicked(DialogInterface dialog, int id) {
                dialog.dismiss();
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
