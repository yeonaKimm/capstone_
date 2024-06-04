package com.example.myapplication.ui.recruit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import java.util.List;

public class TaxiAdapter extends RecyclerView.Adapter<TaxiAdapter.TaxiViewHolder> {

    private List<TaxiList_Item_Recruit> taxiList;
    private Context context;
    private OnItemClickListener listener;

    public TaxiAdapter(Context context, List<TaxiList_Item_Recruit> taxiList, OnItemClickListener listener) {
        this.context = context;
        this.taxiList = taxiList;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(TaxiList_Item_Recruit item);
    }

    @NonNull
    @Override
    public TaxiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recruit_taxilist_item, parent, false);
        return new TaxiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaxiViewHolder holder, int position) {
        TaxiList_Item_Recruit item = taxiList.get(position);
        holder.itemDate.setText(item.getDate());
        holder.itemTime.setText(item.getTime());
        holder.itemPeople.setText(String.valueOf(item.getPeople()));
        holder.recruitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taxiList.size();
    }

    public static class TaxiViewHolder extends RecyclerView.ViewHolder {
        private TextView itemDate;
        private TextView itemPeople;
        private TextView itemTime;
        private Button recruitButton;

        public TaxiViewHolder(@NonNull View itemView) {
            super(itemView);
            itemDate = itemView.findViewById(R.id.item_date);
            itemPeople = itemView.findViewById(R.id.item_people);
            itemTime = itemView.findViewById(R.id.item_time);
            recruitButton = itemView.findViewById(R.id.recruit);
        }
    }
}
