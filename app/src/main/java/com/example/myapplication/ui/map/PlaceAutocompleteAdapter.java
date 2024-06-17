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

public class PlaceAutocompleteAdapter extends RecyclerView.Adapter<PlaceAutocompleteAdapter.PlaceAutocompleteViewHolder> {
    private List<AutocompletePrediction> mResultList;
    private final Context mContext;
    private final PlaceAutoCompleteInterface mPlaceAutoCompleteInterface;

    public PlaceAutocompleteAdapter(Context context, int resource, PlaceAutoCompleteInterface placeAutoCompleteInterface) {
        mContext = context;
        mPlaceAutoCompleteInterface = placeAutoCompleteInterface;
        mResultList = new ArrayList<>();
    }

    @NonNull
    @Override
    public PlaceAutocompleteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_search, parent, false);
        return new PlaceAutocompleteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceAutocompleteViewHolder holder, int position) {
        holder.textView.setText(mResultList.get(position).getFullText(null));
        holder.itemView.setOnClickListener(v -> mPlaceAutoCompleteInterface.onPlaceClick(new ArrayList<>(mResultList), position));
    }

    @Override
    public int getItemCount() {
        return mResultList.size();
    }

    public void setPredictionList(List<AutocompletePrediction> predictions) {
        mResultList = predictions;
        notifyDataSetChanged();
    }

    public interface PlaceAutoCompleteInterface {
        void onPlaceClick(ArrayList<AutocompletePrediction> resultList, int position);
    }

    public static class PlaceAutocompleteViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;

        public PlaceAutocompleteViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.tv_address);
        }
    }
}
