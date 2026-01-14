package com.example.gameapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gameapp.R;
import com.example.gameapp.models.response.StarlineRatesResponse;

import java.util.List;

public class StarlineRateAdapter extends RecyclerView.Adapter<StarlineRateAdapter.ViewHolder> {

    private final Context context;
    private final List<StarlineRatesResponse.StarlineRate> rates;

    public StarlineRateAdapter(Context context, List<StarlineRatesResponse.StarlineRate> rates) {
        this.context = context;
        this.rates = rates;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_starline_rate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StarlineRatesResponse.StarlineRate rate = rates.get(position);

        // Set game name
        holder.txtGameName.setText(getGameDisplayName(rate.getGame()));

        // Set digit range
        holder.txtDigitRange.setText(rate.getDigit());
    }

    @Override
    public int getItemCount() {
        return rates.size();
    }

    private String getGameDisplayName(String game) {
        switch (game.toUpperCase()) {
            case "JODI":
                return "Single Digit";
            case "PATTE":
                return "Single Pana";
            case "TP":
                return "Triple Pana";
            default:
                return game;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtGameName, txtDigitRange;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtGameName = itemView.findViewById(R.id.txtGameName);
            txtDigitRange = itemView.findViewById(R.id.txtDigitRange);
        }
    }
}