package com.example.gameapp.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.CompoundButtonCompat;

import com.example.gameapp.R;
import com.example.gameapp.session.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BidActivity extends AppCompatActivity {

    private TextView txtTitle, txtBalance, txtCurrentDate;
    private ImageButton btnBack;
    private EditText etDigits, etPoints;

    private RadioButton btnOpen, btnClose;
    private MaterialButton btnProceed;
    private MaterialCardView cardOpen, cardClose;

    private String gameName, gameType, tapType;
    private boolean isOpenSelected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid);

        getIntentData();
        initViews();
        setupUI();
        setupClickListeners();
    }

    private void getIntentData() {
        gameName = getIntent().getStringExtra("game_name");
        gameType = getIntent().getStringExtra("game_type");
        tapType = getIntent().getStringExtra("tap_type");
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        txtTitle = findViewById(R.id.txtTitle);
        txtBalance = findViewById(R.id.txtBalance);
        txtCurrentDate = findViewById(R.id.txtCurrentDate);

        btnOpen = findViewById(R.id.btnOpen);
        btnClose = findViewById(R.id.btnClose);
        cardOpen = findViewById(R.id.cardOpen);
        cardClose = findViewById(R.id.cardClose);

        etDigits = findViewById(R.id.etDigits);
        etPoints = findViewById(R.id.etPoints);
        btnProceed = findViewById(R.id.btnProceed);
    }

    private void setupUI() {
        txtTitle.setText(gameType != null ? gameType : "Single Digit");
        txtCurrentDate.setText(getCurrentDateFormatted());

        String balance = String.valueOf(SessionManager.getBalance(this));
        txtBalance.setText(balance != null ? balance : "0");

        updateOpenCloseSelection(true);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> onBackPressed());

        cardOpen.setOnClickListener(v -> {
            isOpenSelected = true;
            updateOpenCloseSelection(true);
            btnOpen.setChecked(true);
        });

        cardClose.setOnClickListener(v -> {
            isOpenSelected = false;
            updateOpenCloseSelection(false);
            btnClose.setChecked(true);
        });

        btnOpen.setOnClickListener(v -> {
            isOpenSelected = true;
            updateOpenCloseSelection(true);
        });

        btnClose.setOnClickListener(v -> {
            isOpenSelected = false;
            updateOpenCloseSelection(false);
        });

        btnProceed.setOnClickListener(v -> proceedWithBid());
    }

    private void updateOpenCloseSelection(boolean isOpen) {
        if (isOpen) {
            // Open selected - dark blue with elevation
            cardOpen.setCardBackgroundColor(getColor(R.color.dark_blue));
            cardOpen.setCardElevation(4f);
            cardOpen.setStrokeWidth(0);

            // Close unselected - light gray/blue
            cardClose.setCardBackgroundColor(0xFFE8EAF6);
            cardClose.setCardElevation(0f);
            cardClose.setStrokeWidth(2);
            cardClose.setStrokeColor(0xFFB0BEC5);

            // Text colors - WHITE when selected
            btnOpen.setTextColor(getColor(android.R.color.white));
            btnClose.setTextColor(0xFF607D8B);

            // Radio button tint - WHITE when selected
            CompoundButtonCompat.setButtonTintList(btnOpen,
                    ColorStateList.valueOf(getColor(android.R.color.white)));
            CompoundButtonCompat.setButtonTintList(btnClose,
                    ColorStateList.valueOf(0xFF9E9E9E));

            // Check states
            btnOpen.setChecked(true);
            btnClose.setChecked(false);
        } else {
            // Close selected - dark blue with elevation
            cardClose.setCardBackgroundColor(getColor(R.color.dark_blue));
            cardClose.setCardElevation(4f);
            cardClose.setStrokeWidth(0);

            // Open unselected - light gray/blue
            cardOpen.setCardBackgroundColor(0xFFE8EAF6);
            cardOpen.setCardElevation(0f);
            cardOpen.setStrokeWidth(2);
            cardOpen.setStrokeColor(0xFFB0BEC5);

            // Text colors - WHITE when selected
            btnClose.setTextColor(getColor(android.R.color.white));
            btnOpen.setTextColor(0xFF607D8B);

            // Radio button tint - WHITE when selected
            CompoundButtonCompat.setButtonTintList(btnClose,
                    ColorStateList.valueOf(getColor(android.R.color.white)));
            CompoundButtonCompat.setButtonTintList(btnOpen,
                    ColorStateList.valueOf(0xFF9E9E9E));

            // Check states
            btnOpen.setChecked(false);
            btnClose.setChecked(true);
        }
    }

    private String getCurrentDateFormatted() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd-MMM-yyyy", Locale.ENGLISH);
        return sdf.format(calendar.getTime());
    }

    private void proceedWithBid() {
        String digits = etDigits.getText().toString().trim();
        String points = etPoints.getText().toString().trim();
        String bidType = isOpenSelected ? "Open" : "Close";

        // No validations - just proceed
        submitBid(digits, points, bidType);
    }

    private void submitBid(String digits, String points, String bidType) {
        toast("Bid placed: " + digits + " - " + points + " pts (" + bidType + ")");
        // TODO API call
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}