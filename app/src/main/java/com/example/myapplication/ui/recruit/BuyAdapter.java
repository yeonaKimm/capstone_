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

public class BuyAdapter extends RecyclerView.Adapter<BuyAdapter.BuyViewHolder> {

    private List<BuyList_Item_Recruit> buyList;
    private Context context;
    private OnItemClickListener mListener; // OnItemClickListener를 멤버 변수로 추가

    // BuyAdapter 생성자에서 OnItemClickListener를 전달받도록 수정
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
        //holder.itemContent.setText(item.getContent());
        holder.itemPrice.setText(String.valueOf(item.getPrice()));
        holder.itemPeople.setText(String.valueOf(item.getPeople()));

        // Glide를 사용하여 이미지 로드
        if (item.getImageUri() != null && !item.getImageUri().isEmpty()) {
            Glide.with(context)
                    .load(item.getImageUri())
                    .placeholder(R.drawable.ic_image) // 로딩 중 이미지
                    .error(R.drawable.ic_rice) // 오류 시 이미지
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // 디스크 캐시 전략
                    .into(holder.itemImage);
        } else {
            holder.itemImage.setImageResource(R.drawable.water); // 기본 이미지 설정
        }

        // 아이템 클릭 리스너 설정
        holder.recruitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 클릭한 아이템 가져오기
                BuyList_Item_Recruit selectedItem = buyList.get(holder.getAdapterPosition());

                // 상세 화면으로 이동하고 데이터 전달
                Bundle bundle = new Bundle();
                bundle.putParcelable("selectedItem", selectedItem); // 선택한 항목 데이터를 번들에 담음
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

    // 외부에서 이 인터페이스를 구현하여 클릭 이벤트를 처리할 수 있도록 설정
    public interface OnItemClickListener {
        void onItemClick(BuyList_Item_Recruit item);
    }
}
