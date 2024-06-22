package com.example.myapplication.ui.board;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.databinding.BoardVoteprintBinding;
import com.example.myapplication.ui.recruit.BuyCommentAdapter;
import com.example.myapplication.ui.recruit.BuyCommentList_Item_Recruit;
import com.example.myapplication.ui.recruit.BuyList_Item_Recruit;
import com.example.myapplication.ui.board.VoteList_Item_Board;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VotePrint_Board extends Fragment {

    private BoardVoteprintBinding binding; // 바인딩 변수 선언

    private List<BoardCommentList_Item_Board> commentList;
    private BoardCommentAdapter boardcommentAdapter;
    private int voteCountOption1 = 0;
    private int voteCountOption2 = 0;

    private View barFillOption1;
    private View barFillOption2;

    private TextView tvBarLabel1;
    private TextView tvBarLabel2;

    public static VotePrint_Board newInstance() {
        return new VotePrint_Board();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BoardVoteprintBinding.inflate(inflater, container, false); // 바인딩 초기화
        View rootView = binding.getRoot(); // root view 가져오기

        // XML에서 뷰 참조
        barFillOption1 = rootView.findViewById(R.id.barFillOption1);
        barFillOption2 = rootView.findViewById(R.id.barFillOption2);
        tvBarLabel1 = rootView.findViewById(R.id.tvBarLabel1);
        tvBarLabel2 = rootView.findViewById(R.id.tvBarLabel2);

        // 초기 막대 그래프 설정
        updateGraph();

        // 막대 그래프 클릭 이벤트 설정
        rootView.findViewById(R.id.barContainer1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voteCountOption1++;
                updateGraph();
            }
        });

        rootView.findViewById(R.id.barContainer2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voteCountOption2++;
                updateGraph();
            }
        });

        // 번들에서 전달된 데이터 가져오기
        if (getArguments() != null) {
            VoteList_Item_Board selectedItem = getArguments().getParcelable("selectedItem");
            if (selectedItem != null) {
                // UI 요소에 데이터 설정
                binding.itemTopic.setText(selectedItem.getTopic());
                binding.itemContent.setText(selectedItem.getContent());
                binding.itemOption1.setText(selectedItem.getOption1());
                binding.itemOption2.setText(selectedItem.getOption2());

                // Glide를 사용하여 이미지 로드
                Glide.with(requireContext())
                        .load(selectedItem.getImageUri())
                        .placeholder(R.drawable.ic_image) // 로딩 중 이미지
                        .error(R.drawable.ic_error) // 오류 시 이미지
                        .into(binding.itemImage);
            }
            commentList = new ArrayList<>();
            boardcommentAdapter = new BoardCommentAdapter(commentList);
            binding.commentRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            binding.commentRecyclerView.setAdapter(boardcommentAdapter);

            binding.sendBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendComment();
                }
            }); }


        return rootView;
    }

    private void updateGraph() {
        // 전체 투표 수 계산
        int totalVotes = voteCountOption1 + voteCountOption2;

        // 비율 계산
        float ratioOption1 = totalVotes > 0 ? (float) voteCountOption1 / totalVotes : 0;
        float ratioOption2 = totalVotes > 0 ? (float) voteCountOption2 / totalVotes : 0;

        // 막대 그래프 비율 조정
        LinearLayout.LayoutParams paramsOption1 = (LinearLayout.LayoutParams) barFillOption1.getLayoutParams();
        paramsOption1.weight = ratioOption1;
        barFillOption1.setLayoutParams(paramsOption1);

        LinearLayout.LayoutParams paramsOption2 = (LinearLayout.LayoutParams) barFillOption2.getLayoutParams();
        paramsOption2.weight = ratioOption2;
        barFillOption2.setLayoutParams(paramsOption2);

        // 막대 그래프 색상 조정
        int option1Color = ratioOption1 > 0 ? Color.parseColor("#B6DDFF") : Color.TRANSPARENT;
        int option2Color = ratioOption2 > 0 ? Color.parseColor("#B6DDFF") : Color.TRANSPARENT;

        barFillOption1.setBackgroundColor(option1Color);
        barFillOption2.setBackgroundColor(option2Color);

        // 막대 레이블 업데이트
        tvBarLabel1.setText(String.format(Locale.getDefault(), "%.0f%%", ratioOption1 * 100));
        tvBarLabel2.setText(String.format(Locale.getDefault(), "%.0f%%", ratioOption2 * 100));
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
        binding = null; // 바인딩 해제
    }
}
