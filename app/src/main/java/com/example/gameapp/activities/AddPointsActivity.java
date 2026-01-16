package com.example.gameapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gameapp.R;
import com.example.gameapp.api.ApiClient;
import com.example.gameapp.api.ApiService;
import com.example.gameapp.models.request.DepositRequest;
import com.example.gameapp.models.response.DepositResponse;
import com.example.gameapp.models.response.PriceResponse;
import com.example.gameapp.session.SessionManager;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPointsActivity extends AppCompatActivity {

    private EditText edtPoints;
    private TextView txtPoints;
    private LinearLayout gridPrices;
    private ProgressDialog progressDialog;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_points);

        // ===== VIEWS =====
        edtPoints = findViewById(R.id.enterBox)
                .findViewById(android.R.id.edit);

        txtPoints   = findViewById(R.id.txtPoints);
        gridPrices  = findViewById(R.id.grid);
        ImageButton btnBack = findViewById(R.id.btnBack);
        MaterialButton btnAddPoints = findViewById(R.id.btnAddPoints);

        apiService = ApiClient.getClient().create(ApiService.class);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);

        // ===== SHOW BALANCE =====
        txtPoints.setText(String.valueOf(SessionManager.getBalance(this)));

        // ===== LOAD PRICES FROM BACKEND =====
        loadQuickPrices();

        // ===== BACK BUTTON =====
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
            finish();
        });

        // ===== ADD POINTS =====
        btnAddPoints.setOnClickListener(v -> {

            String amountStr = edtPoints.getText().toString().trim();

            if (amountStr.isEmpty()) {
                Toast.makeText(this, "Enter amount", Toast.LENGTH_SHORT).show();
                return;
            }

            int amount = Integer.parseInt(amountStr);

            // ⭐ MINIMUM LIMIT REMOVED ⭐
            // No check for min 300

            callDepositApi(amount);
        });
    }

    // =====================================================
    // LOAD QUICK SELECT PRICES
    // =====================================================
    private void loadQuickPrices() {

        apiService.getPrices(
                "Bearer " + SessionManager.getToken(this),
                "application/json"
        ).enqueue(new Callback<PriceResponse>() {

            @Override
            public void onResponse(Call<PriceResponse> call,
                                   Response<PriceResponse> response) {

                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().data != null) {

                    gridPrices.removeAllViews();

                    LinearLayout currentRow = null;
                    int count = 0;

                    for (String price : response.body().data) {

                        if (count % 3 == 0) {
                            currentRow = new LinearLayout(AddPointsActivity.this);
                            currentRow.setOrientation(LinearLayout.HORIZONTAL);
                            currentRow.setWeightSum(3);

                            LinearLayout.LayoutParams rowParams =
                                    new LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                    );
                            rowParams.bottomMargin = 12;
                            currentRow.setLayoutParams(rowParams);

                            gridPrices.addView(currentRow);
                        }

                        currentRow.addView(createPriceButton(price));
                        count++;
                    }

                } else {
                    Toast.makeText(AddPointsActivity.this,
                            "Price list not available",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PriceResponse> call, Throwable t) {
                Toast.makeText(AddPointsActivity.this,
                        "Failed to load prices",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // =====================================================
    // CREATE PRICE BUTTON
    // =====================================================
    private MaterialButton createPriceButton(String amount) {

        MaterialButton btn = new MaterialButton(this);
        btn.setText("₹" + amount);
        btn.setAllCaps(false);
        btn.setTextSize(16);
        btn.setCornerRadius(14);
        btn.setStrokeWidth(2);
        btn.setStrokeColorResource(R.color.dark_blue);
        btn.setTextColor(getResources().getColor(R.color.dark_blue));
        btn.setBackgroundTintList(
                getResources().getColorStateList(android.R.color.white)
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1f
                );
        params.setMargins(6, 6, 6, 6);
        btn.setLayoutParams(params);

        btn.setOnClickListener(v -> edtPoints.setText(amount));

        return btn;
    }

    // =====================================================
    // DEPOSIT API
    // =====================================================
    private void callDepositApi(int amount) {

        progressDialog.show();

        DepositRequest request = new DepositRequest(amount);

        apiService.depositAmount(
                "Bearer " + SessionManager.getToken(this),
                request
        ).enqueue(new Callback<DepositResponse>() {

            @Override
            public void onResponse(Call<DepositResponse> call,
                                   Response<DepositResponse> response) {

                progressDialog.dismiss();

                if (response.isSuccessful() && response.body() != null) {

                    int newBalance =
                            SessionManager.getBalance(AddPointsActivity.this) + amount;

                    SessionManager.saveBalance(AddPointsActivity.this, newBalance);

                    txtPoints.setText(String.valueOf(newBalance));
                    edtPoints.setText("");

                    Toast.makeText(AddPointsActivity.this,
                            response.body().getMessage(),
                            Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(AddPointsActivity.this,
                            "Deposit failed",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DepositResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(AddPointsActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // =====================================================
    // BACK PRESS → HOME
    // =====================================================
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        finish();
    }
}
