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

import com.example.gameapp.Adapters.GameAdapter;
import com.example.gameapp.R;
import com.example.gameapp.api.ApiClient;
import com.example.gameapp.api.ApiService;
import com.example.gameapp.models.GameModel;
import com.example.gameapp.models.response.GameResponse;
import com.example.gameapp.models.response.TapsResponse;
import com.example.gameapp.session.SessionManager;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageButton btnMenu;
    private RecyclerView rvGames;

    private final List<TapsResponse.Tap> allTaps = new ArrayList<>();
    private final List<GameModel> games = new ArrayList<>();
    private GameAdapter gameAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        setupRecyclerView();
        setupDrawer();

        loadTaps();
    }

    // ================= INIT =================

    private void initViews() {
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        btnMenu = findViewById(R.id.btnMenu);
        rvGames = findViewById(R.id.rvGames);
    }

    private void setupRecyclerView() {
        rvGames.setLayoutManager(new LinearLayoutManager(this));

        gameAdapter = new GameAdapter(
                this,
                games,
                this::openGameDetails
        );
        rvGames.setAdapter(gameAdapter);
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
                // already on home
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

            } else if (id == R.id.nav_win_history) {
                startActivity(new Intent(this, WinHistoryActivity.class));

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

    private void loadTaps() {
        ApiClient.getClient()
                .create(ApiService.class)
                .getTaps("Bearer " + SessionManager.getToken(this))
                .enqueue(new Callback<TapsResponse>() {

                    @Override
                    public void onResponse(Call<TapsResponse> call,
                                           Response<TapsResponse> response) {

                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().data != null
                                && !response.body().data.isEmpty()
                                && response.body().data.get(0).times != null) {

                            allTaps.clear();
                            allTaps.addAll(response.body().data.get(0).times);
                        }

                        loadGames();
                    }

                    @Override
                    public void onFailure(Call<TapsResponse> call, Throwable t) {
                        loadGames();
                    }
                });
    }

    private void loadGames() {
        ApiClient.getClient()
                .create(ApiService.class)
                .getGames("Bearer " + SessionManager.getToken(this))
                .enqueue(new Callback<GameResponse>() {

                    @Override
                    public void onResponse(Call<GameResponse> call,
                                           Response<GameResponse> response) {

                        if (!response.isSuccessful()
                                || response.body() == null
                                || response.body().data == null) {
                            toast("Failed to load games");
                            return;
                        }

                        games.clear();

                        for (GameResponse.Game g : response.body().data) {
                            games.add(new GameModel(g.name, g.image));
                        }

                        gameAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<GameResponse> call, Throwable t) {
                        toast("Network error");
                    }
                });
    }

    // ================= CLICK =================

    private void openGameDetails(GameModel game) {
        Intent i = new Intent(this, GameDetailsActivity.class);
        i.putExtra("game_name", game.getName());
        i.putExtra("game_image", game.getImage());
        i.putExtra("taps", new Gson().toJson(allTaps));
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
