package com.example.gameapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gameapp.R;
import com.example.gameapp.api.ApiClient;
import com.example.gameapp.api.ApiService;
import com.example.gameapp.models.response.SupportResponse;
import com.example.gameapp.session.SessionManager;
import com.google.android.material.card.MaterialCardView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupportActivity extends AppCompatActivity {

    private MaterialCardView rowCall, rowWhatsapp, rowEmail, rowTelegram, rowProof;
    private TextView txtCallNumber, txtWhatsappNumber, txtEmail, txtNoData;
    private ProgressBar progressBar;

    private SupportResponse.SupportData supportData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        initViews();
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        fetchSupportData();
    }

    private void initViews() {
        rowCall = findViewById(R.id.rowCall);
        rowWhatsapp = findViewById(R.id.rowWhatsapp);
        rowEmail = findViewById(R.id.rowEmail);
        rowTelegram = findViewById(R.id.rowTelegram);
        rowProof = findViewById(R.id.rowProof);

        txtCallNumber = findViewById(R.id.txtCallNumber);
        txtWhatsappNumber = findViewById(R.id.txtWhatsappNumber);
        txtEmail = findViewById(R.id.txtEmail);
        txtNoData = findViewById(R.id.txtNoData);

        progressBar = findViewById(R.id.progressBar);
    }

    private void fetchSupportData() {
        progressBar.setVisibility(View.VISIBLE);
        hideAllRows();

        String token = "Bearer " + SessionManager.getToken(this);

        ApiClient.getClient()
                .create(ApiService.class)
                .getSupport(token)
                .enqueue(new Callback<SupportResponse>() {

                    @Override
                    public void onResponse(Call<SupportResponse> call,
                                           Response<SupportResponse> response) {

                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().isStatus()
                                && response.body().getData() != null) {

                            supportData = response.body().getData();
                            displaySupportOptions();

                        } else {
                            showNoDataMessage();
                        }
                    }

                    @Override
                    public void onFailure(Call<SupportResponse> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SupportActivity.this,
                                "Failed to load support details",
                                Toast.LENGTH_SHORT).show();
                        showNoDataMessage();
                    }
                });
    }

    // ================= DYNAMIC UI =================

    private void displaySupportOptions() {

        boolean hasAnyData = false;

        if (supportData.hasValidContact()) {
            rowCall.setVisibility(View.VISIBLE);
            txtCallNumber.setText(supportData.getContactNo());
            rowCall.setOnClickListener(v ->
                    dialNumber(supportData.getContactNo()));
            hasAnyData = true;
        }

        if (supportData.hasValidWhatsapp()) {
            rowWhatsapp.setVisibility(View.VISIBLE);
            txtWhatsappNumber.setText(supportData.getWhatsappNo());
            rowWhatsapp.setOnClickListener(v ->
                    openWhatsApp(supportData.getWhatsappNo()));
            hasAnyData = true;
        }

        if (supportData.hasValidEmail()) {
            rowEmail.setVisibility(View.VISIBLE);
            txtEmail.setText(supportData.getEmailId());
            rowEmail.setOnClickListener(v ->
                    sendEmail(supportData.getEmailId()));
            hasAnyData = true;
        }

        if (supportData.hasValidTelegram()) {
            rowTelegram.setVisibility(View.VISIBLE);
            rowTelegram.setOnClickListener(v ->
                    openLink(supportData.getTelegramLink()));
            hasAnyData = true;
        }

        if (supportData.hasValidProof()) {
            rowProof.setVisibility(View.VISIBLE);
            rowProof.setOnClickListener(v ->
                    openLink(supportData.getProofLink()));
            hasAnyData = true;
        }

        if (!hasAnyData) {
            showNoDataMessage();
        }
    }

    private void hideAllRows() {
        rowCall.setVisibility(View.GONE);
        rowWhatsapp.setVisibility(View.GONE);
        rowEmail.setVisibility(View.GONE);
        rowTelegram.setVisibility(View.GONE);
        rowProof.setVisibility(View.GONE);
        txtNoData.setVisibility(View.GONE);
    }

    private void showNoDataMessage() {
        hideAllRows();
        txtNoData.setVisibility(View.VISIBLE);
    }

    // ================= ACTIONS =================

    private void dialNumber(String number) {
        startActivity(new Intent(Intent.ACTION_DIAL,
                Uri.parse("tel:" + number)));
    }

    private void openWhatsApp(String number) {
        String clean = number.replaceAll("[^0-9]", "");
        String url = "https://wa.me/" + clean;
        openLink(url);
    }

    private void sendEmail(String email) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + email));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Support Request");
        startActivity(intent);
    }

    private void openLink(String link) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
        } catch (Exception e) {
            Toast.makeText(this,
                    "Invalid link",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
