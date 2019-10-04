package com.lapaksembako.app;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.lapaksembako.app.api.ApiClient;
import com.lapaksembako.app.api.ApiInterface;
import com.lapaksembako.app.api_model.GetAddress;
import com.lapaksembako.app.api_model.GetAkunBank;
import com.lapaksembako.app.api_model.GetUserDaftar;
import com.lapaksembako.app.api_model.PostResult;
import com.lapaksembako.app.helper.Common;
import com.lapaksembako.app.helper.DownloadImageTask;
import com.lapaksembako.app.model.Address;
import com.lapaksembako.app.model.AkunBank;
import com.lapaksembako.app.model.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class EditProfilActivity extends AppCompatActivity implements View.OnClickListener {


    Button buttonSimpan, buttonTambahAlamat, buttonTambahAkun;
    EditText etNamaLengkap, etNoHp, etEmail;
    TextView alamatSekarang, akunBank;
    ImageView imageViewProfile;

    ApiInterface apiInterface;
    User user;
    String alamat = "";
    String bank = "";

    public static final int GALLERY = 2;
    public static final int CAMERA = 1;
    public static final String IMAGE_DIRECTORY = "lapak_sembako";

    String pathFile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        requestMultiplePermissions();

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_back_white);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        imageViewProfile = findViewById(R.id.imageView10);
        buttonSimpan = findViewById(R.id.buttonSimpan);
        buttonTambahAlamat = findViewById(R.id.buttonTambahAlamat);
        buttonTambahAkun = findViewById(R.id.buttonTambahAkun);
        etNamaLengkap = findViewById(R.id.editTextNama);
        etNoHp = findViewById(R.id.editTextHandphone);
        etEmail = findViewById(R.id.editTextEmail);
        alamatSekarang = findViewById(R.id.textViewAlamatSekarang);
        akunBank = findViewById(R.id.textView38);

        buttonTambahAlamat.setOnClickListener(this);
        buttonSimpan.setOnClickListener(this);
        buttonTambahAkun.setOnClickListener(this);
        imageViewProfile.setOnClickListener(this);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            user = bundle.getParcelable("user");
            alamat = bundle.getString("alamat");
            bank = bundle.getString("bank");

            etNamaLengkap.setText(user.getNama());
            etNoHp.setText(user.getPhone());
            etEmail.setText(user.getEmail());
            alamatSekarang.setText(alamat.equals("") ? "---" : alamat);
            akunBank.setText(bank);

            if (user.getProfilePic() != null) {
                Log.d(Common.TAG, "Profile pic : " + user.getProfilePic());
                if (!user.getProfilePic().equals("")) {
                    new DownloadImageTask(imageViewProfile)
                            .execute(Common.BASE_URL_PROFILE + user.getProfilePic());
                }

            }
        }


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonTambahAlamat) {
            tambahAlamat();
        }
        if (id == R.id.buttonTambahAkun) {
            tambahAkunBank();
        }
        if (id == R.id.buttonSimpan) {
            user.setNama(etNamaLengkap.getText().toString());
            user.setPhone(etNoHp.getText().toString());
            user.setEmail(etEmail.getText().toString());
            if (!pathFile.equals("")) {
                uploadProfilePic(pathFile, user);
            } else {
                simpan(user);
            }
        }
        if (id == R.id.imageView10) {
            ambilGambarDariGaleri();
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

    private void uploadProfilePic(String filePath, final User user) {
        Log.d(Common.TAG, "FilePath " + filePath);
        File file = new File(filePath);
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("foto", file.getName(), fileReqBody);
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(user.getId()));
        Call<PostResult> callS = apiInterface.uploadImageProfile(part, requestBody);
        callS.enqueue(new Callback<PostResult>() {
            @Override
            public void onResponse(Call<PostResult> call, Response<PostResult> response) {
                Log.d(Common.TAG, "Response : " + response.body());
                Log.d(Common.TAG, "Response : " + response.raw().toString());
                if (response.isSuccessful()) {
                    simpan(user);
                } else {
                    Log.d(Common.TAG, response.message());
                    Log.d(Common.TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<PostResult> call, Throwable t) {
                Log.e(Common.TAG, t.getMessage());
            }
        });
    }


    private void simpan(final User user) {
        Call<GetUserDaftar> updateProfileCall = apiInterface.postUpdateProfile(user.getNama(),
                user.getPhone(), user.getEmail(), user.getId());
        updateProfileCall.enqueue(new Callback<GetUserDaftar>() {
            @Override
            public void onResponse(Call<GetUserDaftar> call, Response<GetUserDaftar> response) {
                Log.d(Common.TAG, response.body().getStatus());
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("success")) {
                        Common.logD("UPDATE SUCCESS");
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("user_update", user);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Log.d(Common.TAG, "Update Gagal : " + response.body().getMessage());
                        showWarning("Update gagal dilakukan, silahkan coba lagi. Kesalahan: " + response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<GetUserDaftar> call, Throwable t) {
                Log.d(Common.TAG, "Register Error : " + t.getMessage());
            }
        });
    }


    private void tambahAlamat() {
        Intent intent = new Intent(EditProfilActivity.this, AlamatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);
        intent.putExtras(bundle);
        startActivityForResult(intent, Common.REQUEST_ADD_ADDRESS);
    }

    private void tambahAkunBank() {
        Intent intent = new Intent(EditProfilActivity.this, TambahAkunBankActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);
        intent.putExtras(bundle);
        startActivityForResult(intent, Common.REQUEST_ADD_BANK);
    }

    private void showWarning(String message) {
        Common.showWarningDialog(EditProfilActivity.this, message, new Common.OnDialogActionSelected() {
            @Override
            public void onPositiveClicked(DialogInterface dialog, int id) {
                dialog.dismiss();
            }

            @Override
            public void onNegativeClicked(DialogInterface dialog, int id) {

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
                    Toast.makeText(EditProfilActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    imageViewProfile.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(EditProfilActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        }
        if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imageViewProfile.setImageBitmap(thumbnail);
            pathFile = saveImg(thumbnail);
            Toast.makeText(EditProfilActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }

        if (requestCode == Common.REQUEST_ADD_ADDRESS) {
            getLstAddress();
        }

        if (requestCode == Common.REQUEST_ADD_BANK) {
            getAkunBank();
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


    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private void requestMultiplePermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }


                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }


    private void getLstAddress() {
        Call<GetAddress> flashSaleCall = apiInterface.getListDefaultAdddress(user.getId());
        flashSaleCall.enqueue(new Callback<GetAddress>() {
            @Override
            public void onResponse(Call<GetAddress> call, Response<GetAddress> response) {
                if (response.isSuccessful()) {
                    Log.d(Common.TAG, response.body().getStatus() + " " + response.body().getAddress().size());
                    if (response.body().getAddress() != null) {
                        if (response.body().getAddress().size() > 0) {
                            Address a = response.body().getAddress().get(0);
                            alamatSekarang.setText(a.getAddress());
                            alamat = a.getAddress();
                        }

                    }
                } else {
                    Log.e(Common.TAG, "Request gagal " + response.message());
                }

            }

            @Override
            public void onFailure(Call<GetAddress> call, Throwable t) {
                Log.d(Common.TAG, "Failure " + t.getMessage());
            }
        });
    }


    private void getAkunBank() {
        Call<GetAkunBank> flashSaleCall = apiInterface.getAkunBank(user.getId());
        flashSaleCall.enqueue(new Callback<GetAkunBank>() {
            @Override
            public void onResponse(Call<GetAkunBank> call, Response<GetAkunBank> response) {
                if (response.isSuccessful()) {
                    Log.d(Common.TAG, response.body().getStatus() + " " + response.body().getAkunBanks().size());
                    if (response.body().getAkunBanks() != null) {
                        if (response.body().getAkunBanks().size() > 0) {
                            AkunBank a = response.body().getAkunBanks().get(0);
                            akunBank.setText(a.getBankAccount() + " - " + a.getAccountNumber());
                        }

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
