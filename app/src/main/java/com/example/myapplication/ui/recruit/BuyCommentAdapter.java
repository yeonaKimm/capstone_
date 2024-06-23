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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recruit_plus_comment, parent, false);
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
        private TextView promiseTextView, accountTextView, bankTextView, dateTextView, timeTextView, placeTextView, priceTextView, divisionTextView, paymentTextView;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            promiseTextView = itemView.findViewById(R.id.commentTextView);
            accountTextView = itemView.findViewById(R.id.account);
            bankTextView = itemView.findViewById(R.id.bank);
            dateTextView = itemView.findViewById(R.id.date);
            timeTextView = itemView.findViewById(R.id.time);
            placeTextView = itemView.findViewById(R.id.place);
            priceTextView = itemView.findViewById(R.id.price);
            divisionTextView = itemView.findViewById(R.id.division);
            paymentTextView = itemView.findViewById(R.id.payment);
        }

        public void bind(BuyCommentList_Item_Recruit comment) {
            promiseTextView.setText(comment.getPromise());
            accountTextView.setText(comment.getAccount());
            bankTextView.setText(comment.getBank());
            dateTextView.setText(comment.getDate());
            timeTextView.setText(comment.getTime());
            placeTextView.setText(comment.getPlace());
            priceTextView.setText(comment.getPrice());
            divisionTextView.setText(comment.getDivision());
            paymentTextView.setText(comment.getPayment());
        }
    }
}
