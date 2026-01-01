package com.example.gameapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.gameapp.R;

public class AddPointsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_points);

        // ================= TOOLBAR =================
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> finish());

        // ================= VIEWS =================
        EditText edtPoints = findViewById(R.id.enterBox)
                .findViewById(android.R.id.edit);

        Button btnAddPoints = findViewById(R.id.btnAddPoints);
        GridLayout grid = findViewById(R.id.grid);

        // ================= QUICK AMOUNT BUTTONS =================
        for (int i = 0; i < grid.getChildCount(); i++) {
            View view = grid.getChildAt(i);

            if (view instanceof Button) {
                Button button = (Button) view;

                button.setOnClickListener(v -> {
                    String amount = button.getText().toString();
                    edtPoints.setText(amount);
                    Toast.makeText(
                            this,
                            "Selected: " + amount,
                            Toast.LENGTH_SHORT
                    ).show();
                });
            }
        }

        // ================= ADD POINTS BUTTON =================
        btnAddPoints.setOnClickListener(v -> {
            String amount = edtPoints.getText().toString().trim();

            if (amount.isEmpty()) {
                Toast.makeText(this, "Please enter points", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(
                    this,
                    "Add Points clicked: " + amount,
                    Toast.LENGTH_SHORT
            ).show();
        });
    }
}
