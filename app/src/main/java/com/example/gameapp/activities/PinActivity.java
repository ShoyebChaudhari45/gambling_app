package com.example.gameapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gameapp.R;

public class PinActivity extends AppCompatActivity {

    private StringBuilder pinBuilder = new StringBuilder();
    private LinearLayout pinContainer;
    private long lastBackPress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        pinContainer = findViewById(R.id.pinContainer);
        TextView txtReset = findViewById(R.id.txtReset);

        // Number buttons
        int[] btnIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3,
                R.id.btn4, R.id.btn5, R.id.btn6,
                R.id.btn7, R.id.btn8, R.id.btn9
        };

        for (int id : btnIds) {
            Button btn = findViewById(id);
            btn.setOnClickListener(v -> addDigit(btn.getText().toString()));
        }

        // Delete
        findViewById(R.id.btnDelete).setOnClickListener(v -> deleteDigit());

        // Reset PIN
        txtReset.setOnClickListener(v -> resetPin());
    }

    /* ---------------- PIN LOGIC ---------------- */

    private void addDigit(String digit) {
        if (pinBuilder.length() >= 4) return;

        pinBuilder.append(digit);
        int index = pinBuilder.length() - 1;

        // Show digit briefly
        View box = pinContainer.getChildAt(index);
        box.setBackgroundResource(R.drawable.bg_pin_box_filled);

        // Hide after 300ms
        new Handler().postDelayed(this::updatePinUI, 300);

        if (pinBuilder.length() == 4) {
            new Handler().postDelayed(this::verifyPin, 400);
        }
    }

    private void deleteDigit() {
        if (pinBuilder.length() == 0) return;

        pinBuilder.deleteCharAt(pinBuilder.length() - 1);
        updatePinUI();
    }

    private void resetPin() {
        pinBuilder.setLength(0);
        updatePinUI();
        Toast.makeText(this, "PIN reset", Toast.LENGTH_SHORT).show();
    }

    private void updatePinUI() {
        for (int i = 0; i < 4; i++) {
            if (i < pinBuilder.length()) {
                pinContainer.getChildAt(i)
                        .setBackgroundResource(R.drawable.bg_pin_box_filled);
            } else {
                pinContainer.getChildAt(i)
                        .setBackgroundResource(R.drawable.bg_pin_box);
            }
        }
    }

    private void verifyPin() {
        String enteredPin = pinBuilder.toString();

        // TODO: Replace with API verification
        if (enteredPin.equals("1234")) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Incorrect PIN", Toast.LENGTH_SHORT).show();
            resetPin();
        }
    }

    /* ---------------- BACK BUTTON SPEC ---------------- */

    @Override
    public void onBackPressed() {
        if (pinBuilder.length() > 0) {
            resetPin();
            return;
        }

        if (System.currentTimeMillis() - lastBackPress < 2000) {
            finish();
        } else {
            lastBackPress = System.currentTimeMillis();
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
    }
}
