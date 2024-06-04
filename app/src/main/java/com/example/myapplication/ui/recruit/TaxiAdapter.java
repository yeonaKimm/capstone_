package com.example.myapplication.ui.recruit;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
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

        // 모집 버튼 클릭 이벤트 처리
        holder.recruitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 클릭한 아이템 가져오기
                TaxiList_Item_Recruit selectedItem = taxiList.get(holder.getAdapterPosition());

                // 로그를 추가하여 데이터 확인
                Log.d("TaxiAdapter", "Selected Item: Date=" + selectedItem.getDate() + ", Time=" + selectedItem.getTime() + ", People=" + selectedItem.getPeople());

                // 상세 화면으로 이동하고 데이터 전달
                Bundle bundle = new Bundle();
                bundle.putParcelable("selectedItem", selectedItem); // 선택한 항목 데이터를 번들에 담음
                Navigation.findNavController(v).navigate(R.id.action_navigation_recruit_taxilist_to_navigation_recruit_taxiprint, bundle);
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
