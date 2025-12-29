package com.example.gameapp.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.gameapp.R;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // ================= INIT VIEWS =================
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);

        // ================= TOOLBAR =================
        setSupportActionBar(toolbar);

        // ================= DRAWER TOGGLE =================
        toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // ================= MENU CLICK HANDLING =================
        navigationView.setNavigationItemSelectedListener(item -> {

            drawerLayout.closeDrawer(GravityCompat.START);

            int id = item.getItemId();

            if (id == R.id.nav_home) {
                // already on home
                Toast.makeText(this, "Already on Home", Toast.LENGTH_SHORT).show();


            } else if (id == R.id.nav_profile) {
                // startActivity(new Intent(this, ProfileActivity.class));
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();

            } else if (id == R.id.nav_add_funds) {
                // startActivity(new Intent(this, AddFundsActivity.class));
                Toast.makeText(this, "Add Funds", Toast.LENGTH_SHORT).show();


            } else if (id == R.id.nav_withdraw) {
                // startActivity(new Intent(this, WithdrawActivity.class));
                Toast.makeText(this, "Withdraw", Toast.LENGTH_SHORT).show();


            } else if (id == R.id.nav_wallet) {
                // wallet screen
                Toast.makeText(this, "Wallet", Toast.LENGTH_SHORT).show();


            } else if (id == R.id.nav_bid_history) {
                // bid history
                Toast.makeText(this, "Bid History", Toast.LENGTH_SHORT).show();


                // bid history

            } else if (id == R.id.nav_win_history) {
                // win history
                Toast.makeText(this, "Win History", Toast.LENGTH_SHORT).show();

            } else if (id == R.id.nav_game_rates) {
                // game rates
                Toast.makeText(this, "Game Rates", Toast.LENGTH_SHORT).show();


            } else if (id == R.id.nav_support) {
                // support
                Toast.makeText(this, "Support", Toast.LENGTH_SHORT).show();


            } else if (id == R.id.nav_logout) {
                // logout logic
                // clear session & go to login\
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();

            }

            return true;
        });
    }

    // ================= BACK PRESS =================
    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
