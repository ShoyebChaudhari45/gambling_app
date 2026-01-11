package com.example.gameapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gameapp.Adapters.GameTapAdapter;
import com.example.gameapp.R;
import com.example.gameapp.api.ApiClient;
import com.example.gameapp.api.ApiService;
import com.example.gameapp.models.response.TapsResponse;
import com.example.gameapp.session.SessionManager;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageButton btnMenu;
    private RecyclerView rvGameTaps;
    private static final String TAG = "HomeActivity";

    private final List<TapsResponse.Tap> gameTaps = new ArrayList<>();
    private GameTapAdapter gameTapAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        setupRecyclerView();
        setupDrawer();
        setupActionButtons();

        loadGameTaps();
    }

    // ================= INIT =================

    private void initViews() {
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        btnMenu = findViewById(R.id.btnMenu);
        rvGameTaps = findViewById(R.id.rvGameTaps);
    }

    private void setupRecyclerView() {
        rvGameTaps.setLayoutManager(new LinearLayoutManager(this));

        gameTapAdapter = new GameTapAdapter(
                this,
                gameTaps,
                this::openGameDetails
        );
        rvGameTaps.setAdapter(gameTapAdapter);
    }

    // ================= ACTION BUTTONS =================

    private void setupActionButtons() {
        findViewById(R.id.btnWhatsApp).setOnClickListener(v -> {
            // Open WhatsApp
            toast("Opening WhatsApp...");
        });

        findViewById(R.id.btnTelegram).setOnClickListener(v -> {
            // Open Telegram
            toast("Opening Telegram...");
        });

        findViewById(R.id.btnStarline).setOnClickListener(v -> {
            // Open Starline
            toast("Opening Starline...");
        });

        findViewById(R.id.btnAddPoints).setOnClickListener(v -> {
            startActivity(new Intent(this, AddPointsActivity.class));
        });

        findViewById(R.id.btnWithdraw).setOnClickListener(v -> {
            startActivity(new Intent(this, WithdrawActivity.class));
        });
    }

    // ================= DRAWER =================

    private void setupDrawer() {
        btnMenu.setOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.START)
        );

        navigationView.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawer(GravityCompat.START);

            int id = item.getItemId();

            if (id == R.id.nav_home) {
                return true;
            }

            if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));

            } else if (id == R.id.nav_add_funds) {
                startActivity(new Intent(this, AddPointsActivity.class));

            } else if (id == R.id.nav_withdraw) {
                startActivity(new Intent(this, WithdrawActivity.class));

            } else if (id == R.id.nav_wallet) {
                startActivity(new Intent(this, WalletStatementActivity.class));

            } else if (id == R.id.nav_bid_history) {
                startActivity(new Intent(this, BidHistoryActivity.class));



            } else if (id == R.id.nav_game_rates) {
                startActivity(new Intent(this, GameRatesActivity.class));

            } else if (id == R.id.nav_support) {
                startActivity(new Intent(this, SupportActivity.class));

            } else if (id == R.id.nav_change_password) {
                startActivity(new Intent(this, ChangePasswordActivity.class));

            } else if (id == R.id.nav_share) {
                shareApp();

            } else if (id == R.id.nav_logout) {
                SessionManager.logout(this);
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }

            return true;
        });
    }

    // ================= API FLOW =================

    private void loadGameTaps() {

        Log.d(TAG, "loadGameTaps: API call started");

        ApiClient.getClient()
                .create(ApiService.class)
                .getTaps("Bearer " + SessionManager.getToken(this))
                .enqueue(new Callback<TapsResponse>() {

                    @Override
                    public void onResponse(Call<TapsResponse> call,
                                           Response<TapsResponse> response) {

                        Log.d(TAG, "onResponse: HTTP CODE = " + response.code());

                        if (response.isSuccessful() && response.body() != null) {

                            Log.d(TAG, "onResponse: API SUCCESS");
                            Log.d(TAG, "onResponse: RAW DATA = " + new Gson().toJson(response.body()));

                            if (response.body().getData() == null) {
                                Log.e(TAG, "onResponse: data is NULL");
                                toast("No data found");
                                return;
                            }

                            gameTaps.clear();

                            for (TapsResponse.GameData game : response.body().getData()) {

                                Log.d(TAG, "Game name = " + game.getName());

                                if (game.getTimes() != null) {
                                    for (TapsResponse.Tap tap : game.getTimes()) {

                                        Log.d(TAG, "Tap item => "
                                                + "type=" + tap.getType()
                                                + ", endTime=" + tap.getEndTime()
                                                + ", status=" + tap.getStatus()
                                        );

                                        // Inject game name
                                        tap.setGameName(game.getName());
                                        gameTaps.add(tap);
                                    }
                                } else {
                                    Log.w(TAG, "No times found for game: " + game.getName());
                                }
                            }

                            Log.d(TAG, "Total taps loaded = " + gameTaps.size());
                            gameTapAdapter.notifyDataSetChanged();

                        } else {
                            Log.e(TAG, "API FAILED: " + response.message());
                            toast("Failed to load games");
                        }
                    }

                    @Override
                    public void onFailure(Call<TapsResponse> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t.getMessage(), t);
                        toast("Network error");
                    }
                });
    }




    // ================= CLICK =================

    private void openGameDetails(TapsResponse.Tap tap) {
        Log.d(TAG, "openGameDetails: "
                + "type=" + tap.getType()
                + ", gameName=" + tap.getGameName()
                + ", endTime=" + tap.getEndTime()
                + ", status=" + tap.getStatus()
        );

        Intent i = new Intent(this, GameTypesActivity.class);
        i.putExtra("tap_id", tap.getId());
        i.putExtra("tap_type", tap.getType());
        i.putExtra("game_name", tap.getGameName());
        i.putExtra("end_time", tap.getEndTime());
        i.putExtra("status", tap.getStatus());
        startActivity(i);
    }


    // ================= SHARE =================

    private void shareApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,
                "Download the app now:\nhttps://play.google.com/store/apps/details?id=" + getPackageName());
        startActivity(Intent.createChooser(intent, "Share App"));
    }

    // ================= UI =================

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}