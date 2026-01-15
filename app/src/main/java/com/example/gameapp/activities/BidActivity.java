package com.example.gameapp.activities;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.CompoundButtonCompat;

import com.bumptech.glide.Glide;
import com.example.gameapp.R;
import com.example.gameapp.api.ApiClient;
import com.example.gameapp.api.ApiService;
import com.example.gameapp.models.request.LotteryRateRequest;
import com.example.gameapp.models.response.LotteryRateResponse;
import com.example.gameapp.models.response.UserDetailsResponse;
import com.example.gameapp.session.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BidActivity extends AppCompatActivity {

    private static final String TAG = "BidActivity";

    private TextView txtTitle, txtBalance, txtCurrentDate;
    private ImageButton btnBack;
    private ImageView imgGameType;
    private EditText etDigits, etPoints;

    private RadioButton btnOpen, btnClose;
    private MaterialButton btnProceed;
    private MaterialCardView cardOpen, cardClose;

    private String gameName, gameType, tapType, gameImage;
    private int tapId;
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

    // ===================== INTENT =====================
    private void getIntentData() {
        gameName = getIntent().getStringExtra("game_name");
        gameType = getIntent().getStringExtra("game_type");
        tapType  = getIntent().getStringExtra("tap_type");
        gameImage = getIntent().getStringExtra("game_image");
        tapId = getIntent().getIntExtra("tap_id", -1);
    }

    // ===================== INIT =====================
    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        txtTitle = findViewById(R.id.txtTitle);
        txtBalance = findViewById(R.id.txtBalance);
        txtCurrentDate = findViewById(R.id.txtCurrentDate);
        imgGameType = findViewById(R.id.imgGameType);

        btnOpen = findViewById(R.id.btnOpen);
        btnClose = findViewById(R.id.btnClose);
        cardOpen = findViewById(R.id.cardOpen);
        cardClose = findViewById(R.id.cardClose);

        etDigits = findViewById(R.id.etDigits);
        etPoints = findViewById(R.id.etPoints);
        btnProceed = findViewById(R.id.btnProceed);

        etDigits.setFilters(new InputFilter[]{
                (source, start, end, dest, dstart, dend) -> {
                    for (int i = start; i < end; i++) {
                        if (!Character.isLetterOrDigit(source.charAt(i))) {
                            return "";
                        }
                    }
                    return null;
                }
        });
    }

    // ===================== UI =====================
    private void setupUI() {
        txtTitle.setText(gameType != null ? gameType : "Game");
        txtBalance.setText(String.valueOf(SessionManager.getBalance(this)));
        txtCurrentDate.setText(getCurrentDateFormatted());

        if (gameImage != null && !gameImage.isEmpty()) {
            Glide.with(this)
                    .load(gameImage)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(imgGameType);
        }

        updateOpenCloseSelection(true);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        cardOpen.setOnClickListener(v -> {
            isOpenSelected = true;
            updateOpenCloseSelection(true);
        });

        cardClose.setOnClickListener(v -> {
            isOpenSelected = false;
            updateOpenCloseSelection(false);
        });

        btnProceed.setOnClickListener(v -> validateAndConfirmBid());
    }

    // ===================== OPEN / CLOSE =====================
    private void updateOpenCloseSelection(boolean isOpen) {
        if (isOpen) {
            cardOpen.setCardBackgroundColor(getColor(R.color.dark_blue));
            btnOpen.setChecked(true);
            btnClose.setChecked(false);
        } else {
            cardClose.setCardBackgroundColor(getColor(R.color.dark_blue));
            btnOpen.setChecked(false);
            btnClose.setChecked(true);
        }

        CompoundButtonCompat.setButtonTintList(
                btnOpen, ColorStateList.valueOf(getColor(android.R.color.white)));
        CompoundButtonCompat.setButtonTintList(
                btnClose, ColorStateList.valueOf(getColor(android.R.color.white)));
    }

    // ===================== VALIDATION + CONFIRMATION =====================
    private void validateAndConfirmBid() {

        if (tapId == -1) {
            toast("Invalid game time");
            return;
        }

        String digits = etDigits.getText().toString().trim();
        String pointsStr = etPoints.getText().toString().trim();

        if (digits.isEmpty()) {
            toast("Enter digits");
            return;
        }

        if (pointsStr.isEmpty()) {
            toast("Enter points");
            return;
        }

        int points;
        try {
            points = Integer.parseInt(pointsStr);
        } catch (Exception e) {
            toast("Invalid points");
            return;
        }

        String type = gameType;

        showConfirmationDialog(digits, points, type);
    }

    // ===================== CONFIRMATION POPUP =====================
    private void showConfirmationDialog(String digits, int points, String type) {

        new AlertDialog.Builder(this)
                .setTitle("Confirm Bid")
                .setMessage(
                        "Game: " + gameName +
                                "\nType: " + type +
                                "\nDigits: " + digits +
                                "\nPoints: " + points +
                                "\nSession: " + (isOpenSelected ? "OPEN" : "CLOSE")
                )
                .setPositiveButton("Confirm", (dialog, which) ->
                        submitBid(digits, points, type)
                )
                .setNegativeButton("Cancel", null)
                .show();
    }

    // ===================== API =====================
    private void submitBid(String digits, int points, String type) {

        LotteryRateRequest request = new LotteryRateRequest(
                tapId,
                type,
                digits,
                points
        );

        ApiService api = ApiClient.getClient().create(ApiService.class);

        api.placeBid(
                "Bearer " + SessionManager.getToken(this),
                "application/json",
                request
        ).enqueue(new Callback<LotteryRateResponse>() {

            @Override
            public void onResponse(Call<LotteryRateResponse> call,
                                   Response<LotteryRateResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    // ✅ FIRST: Refresh wallet balance
                    refreshWalletBalance(response.body().getMessage());
                } else {
                    try {
                        String error = response.errorBody().string();
                        Log.e(TAG, "Bid Error: " + error);
                        toast("Bid failed");
                    } catch (Exception e) {
                        toast("Bid failed");
                    }
                }
            }

            @Override
            public void onFailure(Call<LotteryRateResponse> call, Throwable t) {
                Log.e(TAG, "Network error", t);
                toast("Network error");
            }
        });
    }

    // ===================== WALLET REFRESH + SUCCESS POPUP =====================
    private void refreshWalletBalance(String bidMessage) {

        ApiClient.getClient()
                .create(ApiService.class)
                .getUserDetails(
                        "Bearer " + SessionManager.getToken(this),
                        "application/json"
                )
                .enqueue(new Callback<UserDetailsResponse>() {

                    @Override
                    public void onResponse(Call<UserDetailsResponse> call,
                                           Response<UserDetailsResponse> response) {

                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().data != null) {

                            int newBalance = response.body().data.balance;

                            // ✅ Update SessionManager
                            SessionManager.saveBalance(BidActivity.this, newBalance);

                            // ✅ Update UI
                            txtBalance.setText(String.valueOf(newBalance));

                            // ✅ THEN: Show success popup with updated balance
                            showSuccessDialog(bidMessage, newBalance);
                        } else {
                            // Fallback if API fails
                            showSuccessDialog(bidMessage, SessionManager.getBalance(BidActivity.this));
                        }
                    }

                    @Override
                    public void onFailure(Call<UserDetailsResponse> call, Throwable t) {
                        // Fallback if network fails
                        showSuccessDialog(bidMessage, SessionManager.getBalance(BidActivity.this));
                    }
                });
    }

    // ===================== SUCCESS DIALOG =====================
    private void showSuccessDialog(String message, int currentBalance) {
        new AlertDialog.Builder(this)
                .setTitle("✅ Bid Success")
                .setCancelable(true)   // user can tap outside to close
                .show();
    }

    // ===================== HELPERS =====================
    private String getCurrentDateFormatted() {
        return new SimpleDateFormat(
                "EEE dd-MMM-yyyy",
                Locale.ENGLISH
        ).format(Calendar.getInstance().getTime());
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
