package com.lapaksembako.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LupaPasswordActivity extends AppCompatActivity {

    TextView textViewResetPassword, textViewMasuk;
    EditText editTextEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password);
        getSupportActionBar().hide();

        editTextEmail = findViewById(R.id.editTextEmail);
        textViewMasuk = findViewById(R.id.textViewMasuk);
        textViewResetPassword = findViewById(R.id.textViewResetPassword);
        textViewResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendResetPassword();
            }
        });

        textViewMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LupaPasswordActivity.this, LoginActivity.class));
                finish();
            }
        });
    }


    private void sendResetPassword() {

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
