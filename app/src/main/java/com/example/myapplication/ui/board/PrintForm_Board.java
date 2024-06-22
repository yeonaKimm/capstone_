package com.example.myapplication.ui.board;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.databinding.BoardPrintformBinding;
import com.example.myapplication.ui.recruit.BuyCommentAdapter;
import com.example.myapplication.ui.recruit.BuyCommentList_Item_Recruit;

import java.util.List;

public class PrintForm_Board extends Fragment {

    private BoardPrintformBinding binding; // 바인딩변수 선언
    private List<BoardCommentList_Item_Board> commentList;
    private BoardCommentAdapter boardcommentAdapter;

    public static PrintForm_Board newInstance() {
        return new PrintForm_Board();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BoardPrintformBinding.inflate(inflater, container, false); // 바인딩 초기화

        // 번들에서 전달된 데이터 가져오기
        if (getArguments() != null) {
            PostList_Item_Board selectedItem = getArguments().getParcelable("selectedItem");
            if (selectedItem != null) {
                // UI 요소에 데이터 설정
                binding.itemTopic.setText(selectedItem.getTopic());
                binding.itemContent.setText(selectedItem.getContent());

                // Glide를 사용하여 이미지 로드
                Glide.with(requireContext())
                        .load(selectedItem.getImageUri())
                        .placeholder(R.drawable.ic_image) // 로딩 중 이미지
                        .error(R.drawable.ic_error) // 오류 시 이미지
                        .into(binding.itemImage);
            }
        }

        return binding.getRoot();
    }

    private void sendComment() {
        String commentContent = binding.commentET.getText().toString().trim();
        if (!commentContent.isEmpty()) {
            // 새로운 댓글을 생성하고 어댑터에 추가
            BoardCommentList_Item_Board newComment = new BoardCommentList_Item_Board(commentContent);
            commentList.add(newComment);
            boardcommentAdapter.notifyDataSetChanged();
            binding.commentET.setText("");
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}