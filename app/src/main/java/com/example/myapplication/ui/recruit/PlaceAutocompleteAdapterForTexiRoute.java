package com.example.myapplication.ui.recruit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.google.android.libraries.places.api.model.AutocompletePrediction;

import java.util.List;

public class PlaceAutocompleteAdapterForTexiRoute extends RecyclerView.Adapter<PlaceAutocompleteAdapterForTexiRoute.PlaceViewHolder> {

    private final Context context;
    private List<AutocompletePrediction> predictionList;
    private final PlaceAutoCompleteInterface placeAutoCompleteInterface;
    private boolean isSettingCurrentLocation;

    public PlaceAutocompleteAdapterForTexiRoute(Context context, PlaceAutoCompleteInterface placeAutoCompleteInterface) {
        this.context = context;
        this.placeAutoCompleteInterface = placeAutoCompleteInterface;
    }

    public void setPredictionList(List<AutocompletePrediction> predictionList) {
        this.predictionList = predictionList;
        notifyDataSetChanged();
    }

    public void setSettingCurrentLocation(boolean isSettingCurrentLocation) {
        this.isSettingCurrentLocation = isSettingCurrentLocation;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        holder.bind(predictionList.get(position));
    }

    @Override
    public int getItemCount() {
        return predictionList == null ? 0 : predictionList.size();
    }

    public interface PlaceAutoCompleteInterface {
        void onPlaceClick(List<AutocompletePrediction> resultList, int position);
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder {

        private final TextView addressText;

        PlaceViewHolder(View itemView) {
            super(itemView);
            addressText = itemView.findViewById(R.id.tv_address);
            itemView.setOnClickListener(v -> placeAutoCompleteInterface.onPlaceClick(predictionList, getAdapterPosition()));
        }

        void bind(AutocompletePrediction prediction) {
            addressText.setText(prediction.getPrimaryText(null).toString());
        }
    }
}
