package com.lapaksembako.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lapaksembako.app.action.Core;
import com.lapaksembako.app.helper.Common;
import com.lapaksembako.app.helper.Preferences;
import com.lapaksembako.app.model.OtherPage;

public class TermOfUseActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonYukMulai;
    CheckBox checkBox;
    TextView textViewContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_of_use);
        buttonYukMulai = findViewById(R.id.buttonYukMulai);
        checkBox = findViewById(R.id.checkBox);
        textViewContent = findViewById(R.id.textViewContent);
        buttonYukMulai.setOnClickListener(this);
        Core core = new Core();

        core.getKebijakanPrivasi(new Core.OnCallCompleteListener() {
            @Override
            public void onSuccess(Intent data) {
                OtherPage op = data.getBundleExtra("bundle").getParcelable("page");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (op.getContent() != null)
                        textViewContent.setText(Html.fromHtml(op.getContent(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    if (op.getContent() != null)
                        textViewContent.setText(Html.fromHtml(op.getContent()));
                }
            }

            @Override
            public void onFailed(Intent data) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonYukMulai) {
            if (checkBox.isChecked()) {
                flagIsAgreeTos();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Common.showWarningDialog(TermOfUseActivity.this, "Anda harus menyetujui syarat dan ketentuan untuk menggunakan aplikasi Lapak Sembako", new Common.OnDialogActionSelected() {
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
    }

    private void flagIsAgreeTos() {
        Preferences preferences = new Preferences();
        preferences.init(getApplicationContext());
        preferences.saveBoolean(Common.IS_AGREE_TOS, true);
    }
}
