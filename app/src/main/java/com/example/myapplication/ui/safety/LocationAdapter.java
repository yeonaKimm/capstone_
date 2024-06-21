package com.example.myapplication.ui.safety;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private List<String> locations;
    private final OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(String location);
    }

    public LocationAdapter(List<String> locations, OnItemClickListener onItemClickListener) {
        this.locations = locations;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String location = locations.get(position);
        holder.locationTextView.setText(location);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(location));
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public void updateLocations(List<String> newLocations) {
        locations = newLocations;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView locationTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            locationTextView = itemView.findViewById(R.id.tv_address);
        }
    }
}
