package com.example.gameapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.gameapp.R;

public class WinHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_history);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        TextView fromDate = findViewById(R.id.txtFromDate);
        TextView toDate = findViewById(R.id.txtToDate);
        Button submit = findViewById(R.id.btnSubmit);

        fromDate.setOnClickListener(v ->
                Toast.makeText(this, "Select From Date", Toast.LENGTH_SHORT).show());

        toDate.setOnClickListener(v ->
                Toast.makeText(this, "Select To Date", Toast.LENGTH_SHORT).show());

        submit.setOnClickListener(v ->
                Toast.makeText(this, "Submit clicked", Toast.LENGTH_SHORT).show());
    }
}
