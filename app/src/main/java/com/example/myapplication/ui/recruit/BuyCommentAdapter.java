package com.example.myapplication.ui.recruit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class BuyCommentAdapter extends RecyclerView.Adapter<BuyCommentAdapter.CommentViewHolder> {

    private List<BuyCommentList_Item_Recruit> commentList;

    public BuyCommentAdapter(List<BuyCommentList_Item_Recruit> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recruit_buycommentlist_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        BuyCommentList_Item_Recruit comment = commentList.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        private TextView commentTextView;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
        }

        public void bind(BuyCommentList_Item_Recruit comment) {
            commentTextView.setText(comment.getContent());
        }
    }
}
