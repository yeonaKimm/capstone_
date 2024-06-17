package com.example.myapplication.ui.recruit;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.R;

import java.util.List;
import android.util.Log; // Log 클래스를 가져옵니다.

public class BuyAdapter extends RecyclerView.Adapter<BuyAdapter.BuyViewHolder> {

    private static final String TAG = "BuyAdapter"; // 로그 태그 설정
    private List<BuyList_Item_Recruit> buyList;
    private Context context;
    private OnItemClickListener mListener;

    public BuyAdapter(Context context, List<BuyList_Item_Recruit> buyList, OnItemClickListener mlistener) {
        this.context = context;
        this.buyList = buyList;
        this.mListener = mlistener;
    }

    @NonNull
    @Override
    public BuyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recruit_buylist_item, parent, false);
        return new BuyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyViewHolder holder, int position) {
        BuyList_Item_Recruit item = buyList.get(position);
        holder.itemTopic.setText(item.getTopic());
        holder.itemPrice.setText(String.valueOf(item.getPrice()));
        holder.itemPeople.setText(String.valueOf(item.getPeople()));

        if (item.getImageUri() != null && !item.getImageUri().isEmpty()) {
            Glide.with(context)
                    .load(item.getImageUri())
                    .placeholder(R.drawable.ic_image)
                    .error(R.drawable.ic_rice)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.itemImage);
        } else {
            holder.itemImage.setImageDrawable(null);
        }

        holder.recruitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyList_Item_Recruit selectedItem = buyList.get(holder.getAdapterPosition());

                // 로그로 선택한 아이템의 데이터를 출력합니다.
                Log.d(TAG, "Item clicked: " + selectedItem.getTopic() + ", " + selectedItem.getContent() + ", " + selectedItem.getImageUri());

                Bundle bundle = new Bundle();
                bundle.putParcelable("selectedItem", selectedItem);
                Navigation.findNavController(v).navigate(R.id.action_navigation_recruit_buylist_to_navigation_recruit_buyprint, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return buyList.size();
    }

    public static class BuyViewHolder extends RecyclerView.ViewHolder {
        private TextView itemTopic;
        private TextView itemPrice;
        private TextView itemPeople;
        private TextView itemContent;
        private ImageView itemImage;
        private Button recruitButton;

        public BuyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTopic = itemView.findViewById(R.id.item_topic);
            itemContent = itemView.findViewById(R.id.item_content);
            itemPrice = itemView.findViewById(R.id.item_price);
            itemPeople = itemView.findViewById(R.id.item_people);
            itemImage = itemView.findViewById(R.id.item_image);
            recruitButton = itemView.findViewById(R.id.recruit);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(BuyList_Item_Recruit item);
    }
}
