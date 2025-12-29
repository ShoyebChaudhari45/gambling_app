package com.example.gameapp.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gameapp.R;
import com.example.gameapp.Adapters.GameRateAdapter;
import com.example.gameapp.models.GameRateModel;

import java.util.ArrayList;
import java.util.List;

public class GameRatesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_rates);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        RecyclerView rv = findViewById(R.id.rvRates);
        rv.setLayoutManager(new LinearLayoutManager(this));

        // ===== DUMMY DATA =====
        List<GameRateModel> list = new ArrayList<>();
        list.add(new GameRateModel("Single Digit", "10-95"));
        list.add(new GameRateModel("Jodi Digit", "10-950"));
        list.add(new GameRateModel("Single Pana", "10-1500"));
        list.add(new GameRateModel("Double Digit", "10-3000"));
        list.add(new GameRateModel("Triple Digit", "10-7000"));
        list.add(new GameRateModel("Half Sangam", "10-10000"));
        list.add(new GameRateModel("Full Sangam", "10-100000"));

        rv.setAdapter(new GameRateAdapter(list));
    }
}
