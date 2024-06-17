package com.example.myapplication.ui.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.google.android.libraries.places.api.model.AutocompletePrediction;

import java.util.ArrayList;
import java.util.List;

public class PlaceAutocompleteAdapter extends RecyclerView.Adapter<PlaceAutocompleteAdapter.PlaceViewHolder> {

    public interface PlaceAutoCompleteInterface {
        void onPlaceClick(ArrayList<PlaceAutocomplete> resultList, int position);
    }

    private List<AutocompletePrediction> resultList = new ArrayList<>();
    private Context context;
    private int layout;
    private PlaceAutoCompleteInterface listener;

    public PlaceAutocompleteAdapter(Context context, int layout, PlaceAutoCompleteInterface listener) {
        this.context = context;
        this.layout = layout;
        this.listener = listener;
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new PlaceViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, final int position) {
        holder.address.setText(resultList.get(position).getFullText(null));
        holder.predictedRow.setOnClickListener(v -> listener.onPlaceClick(getPlaceAutocompleteList(), position));
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public void setPredictionList(List<AutocompletePrediction> predictionList) {
        this.resultList = predictionList;
        notifyDataSetChanged();
    }

    private ArrayList<PlaceAutocomplete> getPlaceAutocompleteList() {
        ArrayList<PlaceAutocomplete> placeAutocompletes = new ArrayList<>();
        for (AutocompletePrediction prediction : resultList) {
            placeAutocompletes.add(new PlaceAutocomplete(prediction.getPlaceId(), prediction.getFullText(null)));
        }
        return placeAutocompletes;
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder {
        public TextView address;
        public RelativeLayout predictedRow;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.tv_address);
            predictedRow = itemView.findViewById(R.id.predicteRow);
        }
    }

    public static class PlaceAutocomplete {
        public CharSequence placeId;
        public CharSequence description;

        PlaceAutocomplete(CharSequence placeId, CharSequence description) {
            this.placeId = placeId;
            this.description = description;
        }

        @Override
        public String toString() {
            return description.toString();
        }
    }
}
