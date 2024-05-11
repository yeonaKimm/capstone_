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

import com.example.myapplication.R;
import com.example.myapplication.databinding.BoardVoteregBinding;

public class VoteReg_Board extends Fragment {

    private BoardVoteregBinding binding; // 바인딩변수 선언

    public static VoteReg_Board newInstance() {
        return new VoteReg_Board();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BoardVoteregBinding.inflate(inflater, container, false); // 바인딩 초기화

        // register 클릭 시에 이 액션을 트리거하도록 설정함
        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topic = binding.topic.getText().toString();
                String content = binding.content.getText().toString();

                // topic과 content를 데이터베이스에 삽입
                VoteDBHelper dbHelper = new VoteDBHelper(getContext());
                dbHelper.insertVote(topic, content);

                // NavController를 가져옴
                NavController navController = Navigation.findNavController(v);

                // 액션을 트리거하여 navigation_board_printform로 이동
                navController.navigate(R.id.action_navigation_board_votereg_to_navigation_board_voteprint);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
