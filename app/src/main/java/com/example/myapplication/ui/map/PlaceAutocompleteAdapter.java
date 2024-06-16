package com.example.myapplication.ui.map;

import android.content.Context;
import android.graphics.Typeface;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.google.android.libraries.places.api.model.AutocompletePrediction;

import java.util.ArrayList;
import java.util.List;

public class PlaceAutocompleteAdapter extends RecyclerView.Adapter<PlaceAutocompleteAdapter.PlaceViewHolder> implements Filterable {
    public interface PlaceAutoCompleteInterface {
        void onPlaceClick(ArrayList<PlaceAutocomplete> resultList, int position);
    }

    private Context context;
    private PlaceAutoCompleteInterface listener;
    private static final CharacterStyle STYLE_BOLD = new StyleSpan(Typeface.BOLD);
    private ArrayList<PlaceAutocomplete> resultList;
    private LayoutInflater layoutInflater;

    public PlaceAutocompleteAdapter(Context context, int resource, PlaceAutoCompleteInterface listener) {
        this.context = context;
        this.listener = listener;
        this.resultList = new ArrayList<>();
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setResults(List<AutocompletePrediction> predictions) {
        resultList.clear();
        for (AutocompletePrediction prediction : predictions) {
            resultList.add(new PlaceAutocomplete(prediction.getPlaceId(), prediction.getFullText(null).toString()));
        }
        notifyDataSetChanged();
    }

    public void clearList() {
        resultList.clear();
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        // Autocomplete filter implementation
        return null;
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = layoutInflater.inflate(R.layout.item_search, parent, false);
        return new PlaceViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, final int position) {
        holder.address.setText(resultList.get(position).description);
        holder.parentLayout.setOnClickListener(v -> listener.onPlaceClick(resultList, position));
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public PlaceAutocomplete getItem(int position) {
        return resultList.get(position);
    }

    public static class PlaceViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout parentLayout;
        public TextView address;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.predictedRow);
            address = itemView.findViewById(R.id.tv_address);
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
