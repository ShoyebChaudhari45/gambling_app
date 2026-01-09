package com.example.gameapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gameapp.R;
import com.example.gameapp.api.ApiClient;
import com.example.gameapp.api.ApiService;
import com.example.gameapp.models.request.UpdateProfileRequest;
import com.example.gameapp.models.response.GenericResponse;
import com.example.gameapp.models.response.UserDetailsResponse;
import com.example.gameapp.session.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "PROFILE_ACTIVITY";

    // Views
    private TextInputEditText etName, etEmail, etMobile;
    private TextView tvUserType, tvMemberSince;
    private View progressContainer, viewHeader;
    private ImageButton btnBack;
    private MaterialCardView profileCard;
    private MaterialButton btnEdit, btnSubmit, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();
        setListeners();
        fetchUserDetails();
    }

    // ================= INITIALIZATION =================

    private void initViews() {
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);

        tvUserType = findViewById(R.id.tvUserType);
        tvMemberSince = findViewById(R.id.tvMemberSince);

        progressContainer = findViewById(R.id.progressContainer);
        viewHeader = findViewById(R.id.viewHeader);
        btnBack = findViewById(R.id.btnBack);
        profileCard = findViewById(R.id.profileCard);

        btnEdit = findViewById(R.id.btnEdit);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnLogout = findViewById(R.id.btnLogout);

        // Initially disabled
        etName.setEnabled(false);
        etEmail.setEnabled(false);
        btnSubmit.setVisibility(View.GONE);
    }

    private void setListeners() {

        btnBack.setOnClickListener(v -> goToHomePage());
        viewHeader.setOnClickListener(v -> goToHomePage());
        profileCard.setOnClickListener(v -> goToHomePage());

        btnEdit.setOnClickListener(v -> enableEditMode());

        btnSubmit.setOnClickListener(v -> submitProfileUpdate());

        btnLogout.setOnClickListener(v -> logout());
    }

    // ================= NAVIGATION =================

    private void goToHomePage() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    // ================= FETCH PROFILE =================

    private void fetchUserDetails() {

        showLoader(true);

        String token = "Bearer " + SessionManager.getToken(this);
        ApiService api = ApiClient.getClient().create(ApiService.class);

        api.getUserDetails(token, "application/json")
                .enqueue(new Callback<UserDetailsResponse>() {

                    @Override
                    public void onResponse(Call<UserDetailsResponse> call,
                                           Response<UserDetailsResponse> response) {

                        showLoader(false);
                        Log.d(TAG, "Fetch Profile HTTP: " + response.code());

                        if (response.isSuccessful() && response.body() != null) {

                            UserDetailsResponse.User user = response.body().data;

                            if (user == null) {
                                toast("User data not found");
                                return;
                            }

                            etName.setText(nullSafe(user.name));
                            etEmail.setText(nullSafe(user.email));
                            etMobile.setText(nullSafe(user.mobileNo));
                            tvUserType.setText(capitalize(user.userType));
                            tvMemberSince.setText(formatDate(user.createdAt));
                            return;
                        }

                        toast("Failed to load profile");
                    }

                    @Override
                    public void onFailure(Call<UserDetailsResponse> call, Throwable t) {
                        showLoader(false);
                        Log.e(TAG, "Fetch Profile Error", t);
                        toast("Network error");
                    }
                });
    }

    // ================= EDIT PROFILE =================

    private void enableEditMode() {
        etName.setEnabled(true);
        etEmail.setEnabled(true);
        btnSubmit.setVisibility(View.VISIBLE);
    }

    private void submitProfileUpdate() {

        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Name required");
            return;
        }

        if (email.isEmpty()) {
            etEmail.setError("Email required");
            return;
        }

        showLoader(true);

        String token = "Bearer " + SessionManager.getToken(this);
        UpdateProfileRequest request = new UpdateProfileRequest(name, email);

        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.updateProfile(token, "application/json", request)
                .enqueue(new Callback<GenericResponse>() {

                    @Override
                    public void onResponse(Call<GenericResponse> call,
                                           Response<GenericResponse> response) {

                        showLoader(false);

                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().status) {

                            toast("Profile updated successfully");

                            // Disable edit mode
                            etName.setEnabled(false);
                            etEmail.setEnabled(false);
                            btnSubmit.setVisibility(View.GONE);
                            return;
                        }

                        toast(response.body() != null
                                ? response.body().message
                                : "Update failed");
                    }

                    @Override
                    public void onFailure(Call<GenericResponse> call, Throwable t) {
                        showLoader(false);
                        toast("Network error");
                    }
                });
    }

    // ================= HELPERS =================

    private String nullSafe(String value) {
        return value == null ? "" : value;
    }

    private String formatDate(String iso) {
        if (iso == null || iso.length() < 7) return "N/A";
        try {
            return iso.substring(0, 7).replace("-", " ");
        } catch (Exception e) {
            return "N/A";
        }
    }

    private String capitalize(String value) {
        if (value == null || value.isEmpty()) return "";
        return value.substring(0, 1).toUpperCase() + value.substring(1);
    }

    private void showLoader(boolean show) {
        progressContainer.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    // ================= LOGOUT =================

    private void logout() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (d, w) -> {
                    SessionManager.logout(this);
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
