package com.example.myapplication.ui.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.google.android.libraries.places.api.model.AutocompletePrediction;

import java.util.ArrayList;
import java.util.List;

public class PlaceAutocompleteAdapter extends RecyclerView.Adapter<PlaceAutocompleteAdapter.PredictionHolder> {
    private List<AutocompletePrediction> predictionList;
    private Context context;
    private PlaceAutoCompleteInterface placeAutoCompleteInterface;

    public interface PlaceAutoCompleteInterface {
        void onPlaceClick(ArrayList<AutocompletePrediction> resultList, int position, boolean isCurrentLocation);
    }

    public PlaceAutocompleteAdapter(Context context, PlaceAutoCompleteInterface placeAutoCompleteInterface) {
        this.context = context;
        this.placeAutoCompleteInterface = placeAutoCompleteInterface;
        this.predictionList = new ArrayList<>();
    }

    public void setPredictionList(List<AutocompletePrediction> predictionList, boolean isCurrentLocation) {
        this.predictionList = predictionList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PredictionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search, parent, false);
        return new PredictionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PredictionHolder holder, int position) {
        holder.prediction.setText(predictionList.get(position).getFullText(null));
    }

    @Override
    public int getItemCount() {
        return predictionList.size();
    }

    class PredictionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView prediction;

        PredictionHolder(View itemView) {
            super(itemView);
            prediction = itemView.findViewById(R.id.tv_address);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            placeAutoCompleteInterface.onPlaceClick((ArrayList<AutocompletePrediction>) predictionList, getAdapterPosition(), false);
        }
    }
}
