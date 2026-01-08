package com.example.gameapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gameapp.R;
import com.example.gameapp.api.ApiClient;
import com.example.gameapp.api.ApiService;
import com.example.gameapp.models.request.ResetPasswordRequest;
import com.example.gameapp.models.request.ResendOtpRequest;
import com.example.gameapp.models.response.CommonResponse;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {

    private static final String TAG = "RESET_PASSWORD";

    EditText edtOtp, edtNewPassword, edtConfirmPassword;
    MaterialButton btnResetPassword;
    TextView txtResendOtp;
    View progressContainer;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // Get email from intent
        userEmail = getIntent().getStringExtra("email");

        edtOtp = findViewById(R.id.edtOtp);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        txtResendOtp = findViewById(R.id.txtResendOtp);
        progressContainer = findViewById(R.id.progressContainer);

        btnResetPassword.setOnClickListener(v -> resetPassword());
        txtResendOtp.setOnClickListener(v -> resendOtp());
    }

    private void resetPassword() {

        String otp = edtOtp.getText().toString().trim();
        String newPass = edtNewPassword.getText().toString().trim();
        String confirmPass = edtConfirmPassword.getText().toString().trim();

        // Validation
        if (otp.isEmpty()) {
            toast("Enter OTP");
            return;
        }

        if (otp.length() != 6) {
            toast("OTP must be 6 digits");
            return;
        }

        if (newPass.isEmpty()) {
            toast("Enter new password");
            return;
        }

        if (newPass.length() < 6) {
            toast("Password must be at least 6 characters");
            return;
        }

        if (!newPass.equals(confirmPass)) {
            toast("Passwords do not match");
            return;
        }

        showLoader(true);

        ResetPasswordRequest request = new ResetPasswordRequest();
        request.email = userEmail;
        request.otp = otp;
        request.new_password = newPass;
        request.new_password_confirmation = confirmPass;

        Log.d(TAG, "Resetting password for: " + userEmail);

        ApiClient.getClient()
                .create(ApiService.class)
                .resetPassword(request)
                .enqueue(new Callback<CommonResponse>() {

                    @Override
                    public void onResponse(Call<CommonResponse> call,
                                           Response<CommonResponse> response) {

                        showLoader(false);
                        Log.d(TAG, "Response Code: " + response.code());

                        if (response.isSuccessful() && response.body() != null) {
                            String msg = response.body().getMessage() != null
                                    ? response.body().getMessage()
                                    : "Password reset successful";
                            toast(msg);

                            // Navigate to Login
                            Intent i = new Intent(
                                    ResetPasswordActivity.this,
                                    LoginActivity.class
                            );
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();

                        } else {
                            try {
                                String error = response.errorBody() != null
                                        ? response.errorBody().string()
                                        : "Failed to reset password";
                                Log.e(TAG, error);

                                if (error.contains("otp") || error.contains("invalid")) {
                                    toast("Invalid or expired OTP");
                                } else {
                                    toast("Failed to reset password");
                                }
                            } catch (Exception e) {
                                toast("Failed to reset password");
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

    private void resendOtp() {

        if (userEmail == null || userEmail.isEmpty()) {
            toast("Email not found. Please try again");
            finish();
            return;
        }

        showLoader(true);

        ResendOtpRequest request = new ResendOtpRequest();
        request.email = userEmail;

        Log.d(TAG, "Resending OTP to: " + userEmail);

        ApiClient.getClient()
                .create(ApiService.class)
                .resendOtp(request)
                .enqueue(new Callback<CommonResponse>() {

                    @Override
                    public void onResponse(Call<CommonResponse> call,
                                           Response<CommonResponse> response) {

                        showLoader(false);

                        if (response.isSuccessful() && response.body() != null) {
                            String msg = response.body().getMessage() != null
                                    ? response.body().getMessage()
                                    : "OTP resent successfully";
                            toast(msg);
                        } else {
                            toast("Failed to resend OTP");
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {
                        showLoader(false);
                        Log.e(TAG, "Resend OTP Error", t);
                        toast("Network error");
                    }
                });
    }

    private void showLoader(boolean show) {
        if (progressContainer != null) {
            progressContainer.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        btnResetPassword.setEnabled(!show);
        txtResendOtp.setEnabled(!show);
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}