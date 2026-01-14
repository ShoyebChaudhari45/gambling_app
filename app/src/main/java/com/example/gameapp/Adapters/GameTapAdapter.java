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
import com.example.gameapp.models.response.GameItem;
import com.example.gameapp.models.response.TapsResponse;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class GameTapAdapter
        extends RecyclerView.Adapter<GameTapAdapter.Holder> {

    public interface OnGameTapClickListener {
        void onGameTapClick(TapsResponse.Tap tap, String type);
    }

    private final Context context;
    private final List<GameItem> list;
    private final OnGameTapClickListener listener;

    public GameTapAdapter(Context context,
                          List<GameItem> list,
                          OnGameTapClickListener listener) {
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

        GameItem item = list.get(position);

        TapsResponse.Tap openTap  = item.getOpenTap();
        TapsResponse.Tap closeTap = item.getCloseTap();

        // ================= GAME NAME =================
        h.txtGameName.setText(item.getGameName().toUpperCase());

        // RESULT (STATIC)
        h.txtResultCode.setText("***-**-***");

        // ================= TIME LOGIC =================
        String openTime  = "--:--";
        String closeTime = "--:--";

        if (openTap != null && openTap.getEndTime() != null) {
            openTime = openTap.getEndTime();
        }

        if (closeTap != null && closeTap.getEndTime() != null) {
            closeTime = closeTap.getEndTime();
        }

        h.txtOpenTime.setText(openTime);
        h.txtCloseTime.setText(closeTime);

        // ================= STATUS DECISION =================
        String status;
        TapsResponse.Tap playableTap = null;
        String tapType = null;

        if (openTap != null) {
            status = openTap.getStatus();
            playableTap = openTap;
            tapType = "open";
        } else if (closeTap != null) {
            status = closeTap.getStatus();
            playableTap = closeTap;
            tapType = "close";
        } else {
            status = "closed";
        }

        // ================= STATUS UI =================
        setupStatus(h.txtStatus, h.cardStatus, h.txtPlayGame, status);

        // ================= PLAY BUTTON =================
        boolean isPlayable = "open".equalsIgnoreCase(status);

        setupPlayButton(h.btnPlay, h.imgPlayIcon, isPlayable);

        // ================= CLICK =================
        if (isPlayable && playableTap != null) {
            TapsResponse.Tap finalTap = playableTap;
            String finalType = tapType;

            h.cardGame.setOnClickListener(v ->
                    listener.onGameTapClick(finalTap, finalType));

            h.btnPlay.setOnClickListener(v ->
                    listener.onGameTapClick(finalTap, finalType));
        } else {
            h.cardGame.setOnClickListener(null);
            h.btnPlay.setOnClickListener(null);
        }
    }

    // ================= STATUS UI =================
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

            default:
                txtStatus.setText("CLOSED");
                txtStatus.setTextColor(0xFFD32F2F);
                cardStatus.setCardBackgroundColor(0xFFFFEBEE);
                txtPlayGame.setTextColor(0xFF999999);
                break;
        }
    }

    // ================= PLAY BUTTON =================
    private void setupPlayButton(MaterialCardView btnPlay,
                                 ImageView imgIcon,
                                 boolean enabled) {

        btnPlay.setClickable(enabled);
        btnPlay.setFocusable(enabled);

        if (enabled) {
            btnPlay.setCardBackgroundColor(0xFFD32F2F);
            btnPlay.setCardElevation(4f);
            btnPlay.setAlpha(1f);
            imgIcon.setAlpha(1f);
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

    // ================= HOLDER =================
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
