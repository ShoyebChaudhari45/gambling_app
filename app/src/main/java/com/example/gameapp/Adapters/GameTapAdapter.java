package com.example.gameapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gameapp.R;
import com.example.gameapp.models.response.TapsResponse;

import java.util.List;

public class GameTapAdapter extends RecyclerView.Adapter<GameTapAdapter.TapVH> {

    public interface OnTapClickListener {
        void onTapClick(TapsResponse.Tap tap);
    }

    private final Context context;
    private final List<TapsResponse.Tap> list;
    private final OnTapClickListener listener;

    public GameTapAdapter(Context context,
                          List<TapsResponse.Tap> list,
                          OnTapClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TapVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_tap, parent, false);
        return new TapVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TapVH h, int pos) {

        TapsResponse.Tap tap = list.get(pos);

        // ✅ GAME NAME (FROM PARENT)
        h.txtGameName.setText(
                tap.getGameName() != null
                        ? tap.getGameName().toUpperCase()
                        : "-"
        );

        // ✅ TYPE (OPEN / CLOSE)
        h.txtResult.setText(
                tap.getType() != null
                        ? tap.getType().toUpperCase()
                        : "-"
        );

        // ✅ END TIME (BIG)
        h.txtOpenTime.setText(
                tap.getEndTime() != null
                        ? tap.getEndTime()
                        : "--:--"
        );

        // ✅ STATUS
        h.txtStatus.setText(
                tap.getStatus() != null
                        ? tap.getStatus().toUpperCase()
                        : "-"
        );

        h.itemView.setOnClickListener(v -> listener.onTapClick(tap));
        h.btnPlay.setOnClickListener(v -> listener.onTapClick(tap));
        h.btnChart.setOnClickListener(v -> listener.onTapClick(tap));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    // ================= VIEW HOLDER =================
    static class TapVH extends RecyclerView.ViewHolder {

        TextView txtGameName, txtResult, txtOpenTime, txtStatus;
        View btnPlay, btnChart;

        TapVH(@NonNull View v) {
            super(v);
            txtGameName = v.findViewById(R.id.txtGameName);
            txtResult = v.findViewById(R.id.txtResult);
            txtOpenTime = v.findViewById(R.id.txtOpenTime);
            txtStatus = v.findViewById(R.id.txtStatus);
            btnPlay = v.findViewById(R.id.btnPlay);
            btnChart = v.findViewById(R.id.btnChart);
        }
    }
}
