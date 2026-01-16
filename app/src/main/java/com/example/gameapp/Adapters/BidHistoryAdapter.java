package com.example.gameapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gameapp.R;
import com.example.gameapp.models.response.BidItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BidHistoryAdapter extends RecyclerView.Adapter<BidHistoryAdapter.BidViewHolder> {

    private Context context;
    private List<BidItem> bidList;
    private SimpleDateFormat inputDateFormat;
    private SimpleDateFormat outputDateFormat;

    public BidHistoryAdapter(Context context, List<BidItem> bidList) {
        this.context = context;
        this.bidList = bidList;
        this.inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault());
        this.outputDateFormat = new SimpleDateFormat("dd MMM, h:mm a", Locale.getDefault());
    }

    @NonNull
    @Override
    public BidViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bid_history, parent, false);
        return new BidViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BidViewHolder holder, int position) {
        BidItem bid = bidList.get(position);

        // Set bid type
        holder.txtBidType.setText(bid.getType());

        // Set input value
        holder.txtInputValue.setText(bid.getInputValue());

        // Set price
        holder.txtPrice.setText(String.valueOf(bid.getPrice()));

        // Format and set date
        String formattedDate = formatDate(bid.getCreatedOn());
        holder.txtDate.setText(formattedDate);
    }

    @Override
    public int getItemCount() {
        return bidList.size();
    }

    private String formatDate(String dateString) {
        try {
            Date date = inputDateFormat.parse(dateString);
            if (date != null) {
                return outputDateFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateString;
    }

    static class BidViewHolder extends RecyclerView.ViewHolder {
        TextView txtBidType, txtInputValue, txtPrice, txtDate;

        public BidViewHolder(@NonNull View itemView) {
            super(itemView);
            txtBidType = itemView.findViewById(R.id.txtBidType);
            txtInputValue = itemView.findViewById(R.id.txtInputValue);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtDate = itemView.findViewById(R.id.txtDate);
        }
    }
}