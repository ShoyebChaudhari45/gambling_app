package com.example.gameapp.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gameapp.R;
import com.example.gameapp.Adapters.TapAdapter;
import com.example.gameapp.models.response.TapsResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class GameDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_game_details);

        ImageView img = findViewById(R.id.imgGame);
        TextView title = findViewById(R.id.txtGameTitle);
        RecyclerView rv = findViewById(R.id.rvTaps);

        String name = getIntent().getStringExtra("game_name");
        String image = getIntent().getStringExtra("game_image");
        String tapsJson = getIntent().getStringExtra("taps");

        title.setText(name);

        Glide.with(this)
                .load(image)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .into(img);

        List<TapsResponse.Tap> taps =
                new Gson().fromJson(tapsJson,
                        new TypeToken<List<TapsResponse.Tap>>(){}.getType());

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new TapAdapter(this, taps));
    }
}
