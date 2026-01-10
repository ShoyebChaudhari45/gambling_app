package com.example.gameapp.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

    // 🔥 FIX: RadioButton instead of MaterialButton
    private RadioButton btnOpen, btnClose;
    private MaterialButton btnProceed;
    private MaterialCardView cardOpen, cardClose;

    private String gameName, gameType, tapType;
    private boolean isOpenSelected = true; // default

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
        btnBack.setOnClickListener(v -> finish());

        btnOpen.setOnClickListener(v -> {
            isOpenSelected = true;
            updateOpenCloseSelection(true);
        });

        btnClose.setOnClickListener(v -> {
            isOpenSelected = false;
            updateOpenCloseSelection(false);
        });

        btnProceed.setOnClickListener(v -> validateAndProceed());
    }

    private void updateOpenCloseSelection(boolean isOpen) {
        if (isOpen) {
            cardOpen.setCardBackgroundColor(getColor(R.color.dark_blue));
            cardOpen.setCardElevation(6f);

            cardClose.setCardBackgroundColor(getColor(R.color.light_blue));
            cardClose.setCardElevation(0f);

            btnOpen.setChecked(true);
            btnClose.setChecked(false);
        } else {
            cardClose.setCardBackgroundColor(getColor(R.color.dark_blue));
            cardClose.setCardElevation(6f);

            cardOpen.setCardBackgroundColor(getColor(R.color.light_blue));
            cardOpen.setCardElevation(0f);

            btnOpen.setChecked(false);
            btnClose.setChecked(true);
        }
    }

    private String getCurrentDateFormatted() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd-MMM-yyyy", Locale.ENGLISH);
        return sdf.format(calendar.getTime());
    }

    private void validateAndProceed() {
        String digits = etDigits.getText().toString().trim();
        String points = etPoints.getText().toString().trim();

        if (digits.isEmpty()) {
            toast("Please enter digits");
            etDigits.requestFocus();
            return;
        }

        if (points.isEmpty()) {
            toast("Please enter points");
            etPoints.requestFocus();
            return;
        }

        int pointsValue = Integer.parseInt(points);
        if (pointsValue <= 0) {
            toast("Points must be greater than 0");
            return;
        }

        int balance = Integer.parseInt(txtBalance.getText().toString());
        if (pointsValue > balance) {
            toast("Insufficient balance");
            return;
        }

        String bidType = isOpenSelected ? "Open" : "Close";
        submitBid(digits, pointsValue, bidType);
    }

    private void submitBid(String digits, int points, String bidType) {
        toast("Bid placed: " + digits + " - " + points + " pts (" + bidType + ")");
        // TODO API call
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
