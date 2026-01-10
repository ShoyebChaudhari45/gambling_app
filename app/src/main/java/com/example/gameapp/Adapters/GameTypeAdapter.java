package com.example.gameapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gameapp.R;
import com.example.gameapp.models.response.GamesResponse;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class GameTypeAdapter extends RecyclerView.Adapter<GameTypeAdapter.ViewHolder> {

    private final Context context;
    private final List<GamesResponse.Game> games;
    private final OnGameClickListener listener;

    public interface OnGameClickListener {
        void onGameClick(GamesResponse.Game game);
    }

    public GameTypeAdapter(Context context, List<GamesResponse.Game> games, OnGameClickListener listener) {
        this.context = context;
        this.games = games;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_game_type, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GamesResponse.Game game = games.get(position);

        holder.txtGameName.setText(game.getName());

        Glide.with(context)
                .load(game.getImage())
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .into(holder.imgGame);

        holder.cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onGameClick(game);
            }
        });
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        ImageView imgGame;
        TextView txtGameName;

        ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardGameType);
            imgGame = itemView.findViewById(R.id.imgGameType);
            txtGameName = itemView.findViewById(R.id.txtGameTypeName);
        }
    }
}