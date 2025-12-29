package com.example.gameapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gameapp.R;
import com.example.gameapp.models.GameModel;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.VH> {

    List<GameModel> list;

    public GameAdapter(List<GameModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_game_card, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int i) {
        GameModel m = list.get(i);
        h.name.setText(m.name);
        h.result.setText(m.result);
        h.time.setText(m.time);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView name, result, time;
        Button play;

        VH(View v) {
            super(v);
            name = v.findViewById(R.id.txtGameName);
            result = v.findViewById(R.id.txtResult);
            time = v.findViewById(R.id.txtTime);
            play = v.findViewById(R.id.btnPlay);
        }
    }
}
