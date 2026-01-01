package com.example.gameapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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

    private void setupSupportRows() {

        bindRow(findViewById(R.id.rowCall), "call");
        bindRow(findViewById(R.id.rowWhatsapp), "whatsapp");
        bindRow(findViewById(R.id.rowEmail), "email");
        bindRow(findViewById(R.id.rowTelegram), "telegram");
        bindRow(findViewById(R.id.rowProof), "proof");
    }

    private void bindRow(View view, String tag) {

        TextView title = view.findViewById(R.id.txtTitle);
        TextView value = view.findViewById(R.id.txtValue);
        ImageView icon = view.findViewById(R.id.imgIcon);

        switch (tag) {

            case "call":
                title.setText("Call");
                value.setText("+91 9000000000");
                icon.setImageResource(R.drawable.ic_call);
                view.setOnClickListener(v ->
                        Toast.makeText(this, "Call clicked", Toast.LENGTH_SHORT).show());
                break;

            case "whatsapp":
                title.setText("WhatsApp");
                value.setText("+91 9000000000");
                icon.setImageResource(R.drawable.ic_whatsapp);
                view.setOnClickListener(v ->
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://wa.me/919000000000"))));
                break;

            case "email":
                title.setText("Email");
                value.setText("support@example.com");
                icon.setImageResource(R.drawable.ic_email);
                break;

            case "telegram":
                title.setText("Telegram");
                value.setText("https://t.me/example");
                icon.setImageResource(R.drawable.ic_telegram);
                break;

            case "proof":
                title.setText("Withdraw Proof");
                value.setText("View Proof");
                break;
        }
    }
}
