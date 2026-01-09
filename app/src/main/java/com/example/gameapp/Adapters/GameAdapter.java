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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.gameapp.R;
import com.example.gameapp.models.GameModel;

import java.util.List;


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
import com.example.gameapp.models.GameModel;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameVH> {

    public interface OnGameClickListener {
        void onGameClick(GameModel game);
    }

    private Context context;
    private List<GameModel> list;
    private OnGameClickListener listener;

    public GameAdapter(Context context, List<GameModel> list, OnGameClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GameVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_game, parent, false);
        return new GameVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GameVH h, int pos) {
        GameModel game = list.get(pos);

        h.txtName.setText(game.getName());

        Glide.with(context)
                .load(game.getImage())
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .into(h.img);

        h.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onGameClick(game);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class GameVH extends RecyclerView.ViewHolder {
        TextView txtName;
        ImageView img;

        public GameVH(@NonNull View v) {
            super(v);
            txtName = v.findViewById(R.id.txtGameName);
            img = v.findViewById(R.id.imgGame);
        }
    }
}

