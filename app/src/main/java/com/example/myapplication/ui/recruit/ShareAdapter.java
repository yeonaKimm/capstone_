package com.example.myapplication.ui.recruit;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ShareViewHolder> {

    private static final String TAG = "ShareAdapter"; // 로그 태그 설정
    private List<ShareList_Item_Recruit> shareList;
    private Context context;
    private OnItemClickListener mListener;

    public ShareAdapter(Context context, List<ShareList_Item_Recruit> shareList, OnItemClickListener mlistener) {
        this.context = context;
        this.shareList = shareList;
        this.mListener = mlistener;
    }

    @NonNull
    @Override
    public ShareViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recruit_sharelist_item, parent, false);
        return new ShareViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShareViewHolder holder, int position) {
        ShareList_Item_Recruit item = shareList.get(position);
        holder.itemTopic.setText(item.getTopic());
        holder.itemContent.setText(item.getContent());
        holder.itemPlace.setText(String.valueOf(item.getPlace()));
        holder.itemQuantity.setText(String.valueOf(item.getQuantity()));

        if (item.getImageUri() != null && !item.getImageUri().isEmpty()) {
            Glide.with(context)
                    .load(item.getImageUri())
                    .placeholder(R.drawable.ic_image)
                    .error(R.drawable.ic_error)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.itemImage);
        } else {
            holder.itemImage.setImageDrawable(null);
        }

        holder.recruitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareList_Item_Recruit selectedItem = shareList.get(holder.getAdapterPosition());

                // 로그로 선택한 아이템의 데이터를 출력합니다.
                Log.d(TAG, "Item clicked: " + selectedItem.getTopic() + ", " + selectedItem.getContent() + ", " + selectedItem.getImageUri());

                Bundle bundle = new Bundle();
                bundle.putParcelable("selectedItem", selectedItem);
                Navigation.findNavController(v).navigate(R.id.action_navigation_recruit_sharelist_to_navigation_recruit_shareprint, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shareList.size();
    }

    public static class ShareViewHolder extends RecyclerView.ViewHolder {
        private TextView itemTopic;
        private TextView itemPlace;
        private TextView itemQuantity;
        private TextView itemContent;
        private ImageView itemImage;
        private Button recruitButton;

        public ShareViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTopic = itemView.findViewById(R.id.item_topic);
            itemContent = itemView.findViewById(R.id.item_content);
            itemPlace = itemView.findViewById(R.id.item_place);
            itemQuantity = itemView.findViewById(R.id.item_quantity);
            itemImage = itemView.findViewById(R.id.item_image);
            recruitButton = itemView.findViewById(R.id.recruit);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ShareList_Item_Recruit item);
    }
}
