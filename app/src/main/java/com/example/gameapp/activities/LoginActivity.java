package com.example.gameapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.example.gameapp.R;
import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {

    private EditText edtMobile, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        setContentView(R.layout.activity_login);

        edtMobile = findViewById(R.id.edtMobile);
        edtPassword = findViewById(R.id.edtPassword);
        MaterialButton btnLogin = findViewById(R.id.btnLogin);
        TextView txtSignup = findViewById(R.id.txtSignup); // 👈 ADD THIS

        btnLogin.setOnClickListener(v -> {
            String mobile = edtMobile.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (mobile.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter mobile & password", Toast.LENGTH_SHORT).show();
                return;
            }

            // TODO: API Login validation
            startActivity(new Intent(this, PinActivity.class));
            finish();
        });

        // 👉 SIGNUP CLICK
        txtSignup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);

        });
    }

    private long lastBackPressTime = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastBackPressTime < 2000) {
            super.onBackPressed(); // Exit app
        } else {
            lastBackPressTime = System.currentTimeMillis();
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
    }
}
