package com.example.gameapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gameapp.R;
import com.google.android.material.button.MaterialButton;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText edtName = findViewById(R.id.edtName);
        EditText edtMobile = findViewById(R.id.edtMobile);
        EditText edtPassword = findViewById(R.id.edtPassword);
        EditText edtPin = findViewById(R.id.edtPin);
        MaterialButton btnSignup = findViewById(R.id.btnSignup);
        TextView txtLogin = findViewById(R.id.txtLogin);

        btnSignup.setOnClickListener(v -> {
            if (edtName.getText().toString().trim().isEmpty()
                    || edtMobile.getText().toString().trim().isEmpty()
                    || edtPassword.getText().toString().trim().isEmpty()
                    || edtPin.getText().toString().trim().isEmpty()) {

                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show();
                return;
            }

            // TODO: API Register
            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });

        // Already have account → Login
        txtLogin.setOnClickListener(v ->
                startActivity(new Intent(this, LoginActivity.class))
        );
    }
    @Override
    public void onBackPressed() {
        // Go back to Login screen
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

}
