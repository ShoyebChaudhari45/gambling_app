package com.example.gameapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gameapp.R;

public class SupportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        setupSupportRows();
    }

    // ================= SET DATA =================
    private void setupSupportRows() {

        ViewGroup root = findViewById(R.id.main);

        for (int i = 0; i < root.getChildCount(); i++) {
            View view = root.getChildAt(i);
            if (view.getTag() != null) {
                bindRow(view);
            }
        }
    }

    private void bindRow(View view) {

        TextView title = view.findViewById(R.id.txtTitle);
        TextView value = view.findViewById(R.id.txtValue);
        ImageView icon = view.findViewById(R.id.imgIcon);

        String tag = view.getTag().toString();

        // ================= CALL =================
        if (tag.equals("call")) {

            title.setText("Call");
            value.setText("+91 8905513093");
            icon.setImageResource(R.drawable.ic_call);

            view.setOnClickListener(v -> {
                Toast.makeText(this, "Calling...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:+918905513093"));
                startActivity(intent);
            });

        }
        // ================= WHATSAPP =================
        else if (tag.equals("whatsapp")) {

            title.setText("WhatsApp");
            value.setText("+91 8442017014");
            icon.setImageResource(R.drawable.ic_whatsapp);

            view.setOnClickListener(v -> {
                Toast.makeText(this, "Opening WhatsApp", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://wa.me/918442017014"));
                startActivity(intent);
            });

        }
        // ================= TELEGRAM =================
        else if (tag.equals("telegram")) {

            title.setText("Telegram");
            value.setText("https://t.me/kalyan777");
            icon.setImageResource(R.drawable.ic_telegram);

            view.setOnClickListener(v -> {
                Toast.makeText(this, "Opening Telegram", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://t.me/kalyan777"));
                startActivity(intent);
            });

        }
    }
}
