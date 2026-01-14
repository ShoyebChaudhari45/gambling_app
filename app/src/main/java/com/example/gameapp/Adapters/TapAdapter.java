package com.example.gameapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gameapp.R;
import com.example.gameapp.models.response.TapsResponse;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class TapAdapter extends RecyclerView.Adapter<TapAdapter.Holder> {

    private final Context context;
    private final List<TapsResponse.Tap> list;
    private OnTapClickListener listener;

    public interface OnTapClickListener {
        void onTapClick(TapsResponse.Tap tap);
    }

    public TapAdapter(Context context, List<TapsResponse.Tap> list) {
        this.context = context;
        this.list = list;
    }

    public TapAdapter(Context context, List<TapsResponse.Tap> list, OnTapClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_tap, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int position) {
        TapsResponse.Tap tap = list.get(position);

        // GAME NAME
        h.txtGameName.setText(
                tap.getGameName() != null
                        ? tap.getGameName().toUpperCase()
                        : "-"
        );

        // RESULT CODE (STATIC)
        h.txtResultCode.setText("***-**-***");

        // =============================
        // ✅ CORRECT TIME MAPPING
        // API sirf end_time deta hai
        // open  -> Open time
        // close -> Close time
        // =============================
        String openTime = "--:--";
        String closeTime = "--:--";

        if ("open".equalsIgnoreCase(tap.getType())) {
            openTime = tap.getEndTime() != null ? tap.getEndTime() : "--:--";
        } else if ("close".equalsIgnoreCase(tap.getType())) {
            closeTime = tap.getEndTime() != null ? tap.getEndTime() : "--:--";
        }

        h.txtOpenTime.setText(openTime);
        h.txtCloseTime.setText(closeTime);

        // STATUS UI
        setupStatus(h.txtStatus, h.cardStatus, h.txtPlayGame, tap.getStatus());

        // PLAY BUTTON UI
        setupPlayButton(h.btnPlay, h.imgPlayIcon, tap.getStatus());

        // CLICK HANDLING (ONLY OPEN)
        if (listener != null && isClickable(tap.getStatus())) {
            h.cardGame.setOnClickListener(v -> listener.onTapClick(tap));
            h.btnPlay.setOnClickListener(v -> listener.onTapClick(tap));
        } else {
            h.cardGame.setOnClickListener(null);
            h.btnPlay.setOnClickListener(null);
        }
    }

    // ✅ ONLY OPEN GAME IS CLICKABLE
    private boolean isClickable(String status) {
        return status != null && status.equalsIgnoreCase("open");
    }

    // STATUS BADGE + TEXT COLOR
    private void setupStatus(TextView txtStatus,
                             MaterialCardView cardStatus,
                             TextView txtPlayGame,
                             String status) {

        if (status == null) status = "closed";

        switch (status.toLowerCase()) {

            case "open":
                txtStatus.setText("OPEN");
                txtStatus.setTextColor(0xFF4CAF50);
                cardStatus.setCardBackgroundColor(0xFFE8F5E9);
                txtPlayGame.setTextColor(0xFF4CAF50);
                break;

            case "upcoming":
                txtStatus.setText("UPCOMING");
                txtStatus.setTextColor(0xFF4CAF50);
                cardStatus.setCardBackgroundColor(0xFFE8F5E9);
                txtPlayGame.setTextColor(0xFF999999);
                break;

            case "closed":
            default:
                txtStatus.setText("CLOSED");
                txtStatus.setTextColor(0xFFD32F2F);
                cardStatus.setCardBackgroundColor(0xFFFFEBEE);
                txtPlayGame.setTextColor(0xFF999999);
                break;
        }
    }

    // PLAY BUTTON ENABLE / DISABLE
    private void setupPlayButton(MaterialCardView btnPlay,
                                 ImageView imgIcon,
                                 String status) {

        boolean clickable = isClickable(status);

        btnPlay.setClickable(clickable);
        btnPlay.setFocusable(clickable);

        if (clickable) {
            btnPlay.setCardBackgroundColor(0xFFD32F2F);
            btnPlay.setCardElevation(4f);
            btnPlay.setAlpha(1.0f);
            imgIcon.setAlpha(1.0f);
        } else {
            btnPlay.setCardBackgroundColor(0xFFBDBDBD);
            btnPlay.setCardElevation(0f);
            btnPlay.setAlpha(0.5f);
            imgIcon.setAlpha(0.5f);
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class Holder extends RecyclerView.ViewHolder {

        MaterialCardView cardGame, cardStatus, btnPlay;
        TextView txtGameName, txtResultCode,
                txtOpenTime, txtCloseTime,
                txtStatus, txtPlayGame;
        ImageView imgPlayIcon;

        Holder(@NonNull View v) {
            super(v);

            cardGame = v.findViewById(R.id.cardGame);
            txtGameName = v.findViewById(R.id.txtGameName);
            txtResultCode = v.findViewById(R.id.txtResultCode);
            txtOpenTime = v.findViewById(R.id.txtOpenTime);
            txtCloseTime = v.findViewById(R.id.txtCloseTime);
            txtStatus = v.findViewById(R.id.txtStatus);
            cardStatus = v.findViewById(R.id.cardStatus);
            btnPlay = v.findViewById(R.id.btnPlay);
            imgPlayIcon = v.findViewById(R.id.imgPlayIcon);
            txtPlayGame = v.findViewById(R.id.txtPlayGame);
        }
    }
}
