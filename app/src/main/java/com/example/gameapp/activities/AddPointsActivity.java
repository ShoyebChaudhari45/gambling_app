package com.example.gameapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

        // Back arrow click
        toolbar.setNavigationOnClickListener(v -> {
            Toast.makeText(this, "Back clicked", Toast.LENGTH_SHORT).show();
            finish();
        });

        // ================= VIEWS =================
        EditText edtPoints = findViewById(R.id.enterBox)
                .findViewById(android.R.id.edit);

        Button btnAddPoints = findViewById(R.id.btnAddPoints);

        // Quick amount buttons
        Button btn500 = findViewById(R.id.grid).findViewWithTag("500");
        Button btn1000 = findViewById(R.id.grid).findViewWithTag("1000");
        Button btn2000 = findViewById(R.id.grid).findViewWithTag("2000");
        Button btn5000 = findViewById(R.id.grid).findViewWithTag("5000");
        Button btn10000 = findViewById(R.id.grid).findViewWithTag("10000");

        View.OnClickListener quickClick = v -> {
            Button b = (Button) v;
            String amount = b.getText().toString();
            Toast.makeText(this, "Selected: " + amount, Toast.LENGTH_SHORT).show();
        };

        btn500.setOnClickListener(quickClick);
        btn1000.setOnClickListener(quickClick);
        btn2000.setOnClickListener(quickClick);
        btn5000.setOnClickListener(quickClick);
        btn10000.setOnClickListener(quickClick);

        // ================= ADD POINTS =================
        btnAddPoints.setOnClickListener(v -> {
            Toast.makeText(
                    this,
                    "Add Points clicked",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }
}
