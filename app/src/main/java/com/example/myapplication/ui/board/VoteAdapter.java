package com.example.myapplication.ui.board;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.R;

import java.util.List;

public class VoteAdapter extends RecyclerView.Adapter<VoteAdapter.VoteViewHolder> {

    private List<VoteList_Item_Board> voteList;
    private Context context;
    private OnItemClickListener mListener; // OnItemClickListener를 멤버 변수로 추가

    // VoteAdapter 생성자에서 OnItemClickListener를 전달받도록 수정
    public VoteAdapter(Context context, List<VoteList_Item_Board> voteList, OnItemClickListener mlistener) {
        this.context = context;
        this.voteList = voteList;
        this.mListener = mlistener;
    }

    @NonNull
    @Override
    public VoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.board_votelist_item, parent, false);
        return new VoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoteViewHolder holder, int position) {
        VoteList_Item_Board item = voteList.get(position);
        holder.itemTopic.setText(item.getTopic());
        holder.itemContent.setText(item.getContent());

        // Glide를 사용하여 이미지 로드
        if (item.getImageUri() != null && !item.getImageUri().isEmpty()) {
            Glide.with(context)
                    .load(item.getImageUri())
                    .placeholder(R.drawable.ic_image) // 로딩 중 이미지
                    .error(R.drawable.ic_error) // 오류 시 이미지
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // 디스크 캐시 전략
                    .into(holder.itemImage);
        } else {
            holder.itemImage.setImageDrawable(null); // 이미지 없음
        }

        // 아이템 클릭 리스너 설정
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 클릭한 아이템 가져오기
                VoteList_Item_Board selectedItem = voteList.get(holder.getAdapterPosition());

                // 상세 화면으로 이동하고 데이터 전달
                Bundle bundle = new Bundle();
                bundle.putParcelable("selectedItem", selectedItem); // 선택한 항목 데이터를 번들에 담음
                Navigation.findNavController(v).navigate(R.id.action_navigation_board_votelist_to_navigation_board_voteprint, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return voteList.size();
    }

    public static class VoteViewHolder extends RecyclerView.ViewHolder {
        private TextView itemTopic;
        private TextView itemContent;
        private ImageView itemImage;

        public VoteViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTopic = itemView.findViewById(R.id.item_topic);
            itemContent = itemView.findViewById(R.id.item_content);
            itemImage = itemView.findViewById(R.id.item_image);
        }
    }

    // 외부에서 이 인터페이스를 구현하여 클릭 이벤트를 처리할 수 있도록 설정
    public interface OnItemClickListener {
        void onItemClick(VoteList_Item_Board item);
    }
}
