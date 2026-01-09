package com.example.gameapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.gameapp.R;
import com.example.gameapp.models.response.TapsResponse;

import java.util.List;

public class TapAdapter extends RecyclerView.Adapter<TapAdapter.Holder> {

    Context c;
    List<TapsResponse.Tap> list;

    public TapAdapter(Context c, List<TapsResponse.Tap> list) {
        this.c = c;
        this.list = list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup p, int v) {
        return new Holder(LayoutInflater.from(c)
                .inflate(R.layout.item_tap, p, false));
    }

    @Override
    public void onBindViewHolder(Holder h, int p) {
        TapsResponse.Tap t = list.get(p);
        h.txt.setText(t.type.toUpperCase() + " • " + t.endTime);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        TextView txt;
        Holder(View v) {
            super(v);
            txt = v.findViewById(R.id.txtTap);
        }
    }
}
