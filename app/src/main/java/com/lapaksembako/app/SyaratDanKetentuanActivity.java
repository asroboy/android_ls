package com.lapaksembako.app;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.lapaksembako.app.action.Core;
import com.lapaksembako.app.model.OtherPage;

public class SyaratDanKetentuanActivity extends AppCompatActivity {


    Core core;
    TextView textViewContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syarat_dan_ketentuan);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_back_white);
        core = new Core();

        textViewContent = findViewById(R.id.textViewContent);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
}
