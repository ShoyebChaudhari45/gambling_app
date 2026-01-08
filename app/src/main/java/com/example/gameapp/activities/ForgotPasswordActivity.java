package com.example.gameapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gameapp.R;
import com.example.gameapp.api.ApiClient;
import com.example.gameapp.api.ApiService;
import com.example.gameapp.models.request.ForgotPasswordRequest;
import com.example.gameapp.models.response.CommonResponse;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String TAG = "FORGOT_PASSWORD";

    EditText edtEmail;
    MaterialButton btnSendOtp;
    View progressContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        edtEmail = findViewById(R.id.edtEmail);
        btnSendOtp = findViewById(R.id.btnSendOtp);
        progressContainer = findViewById(R.id.progressContainer);

        btnSendOtp.setOnClickListener(v -> sendOtp());

        findViewById(R.id.txtBackToLogin).setOnClickListener(v -> finish());
    }

    private void sendOtp() {

        String email = edtEmail.getText().toString().trim();

        if (email.isEmpty()) {
            toast("Enter your email address");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            toast("Enter a valid email address");
            return;
        }

        showLoader(true);

        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.email = email;

        Log.d(TAG, "Sending OTP to: " + email);

        ApiClient.getClient()
                .create(ApiService.class)
                .forgotPassword(request)
                .enqueue(new Callback<CommonResponse>() {

                    @Override
                    public void onResponse(Call<CommonResponse> call,
                                           Response<CommonResponse> response) {

                        showLoader(false);
                        Log.d(TAG, "Response Code: " + response.code());

                        if (response.isSuccessful() && response.body() != null) {
                            String msg = response.body().message != null
                                    ? response.body().message
                                    : "OTP sent to your email";
                            toast(msg);

                            // Pass email to ResetPasswordActivity
                            Intent i = new Intent(
                                    ForgotPasswordActivity.this,
                                    ResetPasswordActivity.class
                            );
                            i.putExtra("email", email);
                            startActivity(i);
                            finish();

                        } else {
                            try {
                                String error = response.errorBody() != null
                                        ? response.errorBody().string()
                                        : "Failed to send OTP";
                                Log.e(TAG, error);
                                toast("Email not found or invalid");
                            } catch (Exception e) {
                                toast("Failed to send OTP");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {
                        showLoader(false);
                        Log.e(TAG, "Network Error", t);
                        toast("Network error. Please try again");
                    }
                });
    }

    private void showLoader(boolean show) {
        if (progressContainer != null) {
            progressContainer.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        btnSendOtp.setEnabled(!show);
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}