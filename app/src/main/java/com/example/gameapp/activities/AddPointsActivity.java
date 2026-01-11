package com.example.gameapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gameapp.R;
import com.example.gameapp.api.ApiClient;
import com.example.gameapp.api.ApiService;
import com.example.gameapp.models.request.DepositRequest;
import com.example.gameapp.models.response.DepositResponse;
import com.example.gameapp.session.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPointsActivity extends AppCompatActivity {

    private long lastBackPressedTime = 0;

    private EditText edtPoints;
    private TextView txtPoints;
    private ProgressDialog progressDialog;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_points);

        // ================= VIEWS =================
        edtPoints = findViewById(R.id.enterBox)
                .findViewById(android.R.id.edit);

        txtPoints = findViewById(R.id.txtPoints);

        Button btnAddPoints = findViewById(R.id.btnAddPoints);
        ImageButton btnBack = findViewById(R.id.btnBack);

        Button btn500 = findViewById(R.id.btn500);
        Button btn1000 = findViewById(R.id.btn1000);
        Button btn2000 = findViewById(R.id.btn2000);
        Button btn5000 = findViewById(R.id.btn5000);
        Button btn10000 = findViewById(R.id.btn10000);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);

        apiService = ApiClient.getClient().create(ApiService.class);

        // ================= SHOW BALANCE =================
        txtPoints.setText(String.valueOf(SessionManager.getBalance(this)));

        // ================= BACK =================
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        });

        // ================= QUICK BUTTONS =================
        btn500.setOnClickListener(v -> edtPoints.setText("500"));
        btn1000.setOnClickListener(v -> edtPoints.setText("1000"));
        btn2000.setOnClickListener(v -> edtPoints.setText("2000"));
        btn5000.setOnClickListener(v -> edtPoints.setText("5000"));
        btn10000.setOnClickListener(v -> edtPoints.setText("10000"));

        // ================= ADD POINTS =================
        btnAddPoints.setOnClickListener(v -> {
            String amountStr = edtPoints.getText().toString().trim();

            if (amountStr.isEmpty()) {
                Toast.makeText(this, "Enter amount", Toast.LENGTH_SHORT).show();
                return;
            }

            int amount = Integer.parseInt(amountStr);

            if (amount < 300) {
                Toast.makeText(this, "Minimum deposit ₹300", Toast.LENGTH_SHORT).show();
                return;
            }

            callDepositApi(amount);
        });
    }

    // ================= API CALL =================
    private void callDepositApi(int amount) {

        progressDialog.show();

        DepositRequest request = new DepositRequest(amount);

        apiService.depositAmount(
                "Bearer " + SessionManager.getToken(this),
                request
        ).enqueue(new Callback<DepositResponse>() {
            @Override
            public void onResponse(Call<DepositResponse> call, Response<DepositResponse> response) {

                progressDialog.dismiss();

                if (response.isSuccessful() && response.body() != null) {

                    int currentBalance = SessionManager.getBalance(AddPointsActivity.this);
                    int newBalance = currentBalance + amount;

                    SessionManager.saveBalance(AddPointsActivity.this, newBalance);

                    txtPoints.setText(String.valueOf(newBalance));
                    edtPoints.setText("");

                    Toast.makeText(
                            AddPointsActivity.this,
                            response.body().getMessage(),
                            Toast.LENGTH_LONG
                    ).show();

                } else {
                    Toast.makeText(
                            AddPointsActivity.this,
                            "Deposit failed",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<DepositResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(
                        AddPointsActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    // ================= BACK PRESS =================
    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastBackPressedTime < 2000) {
            finish();
        } else {
            lastBackPressedTime = currentTime;
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
    }
}
