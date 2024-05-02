package com.example.myapplication.ui.safety;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.databinding.SafetyContactBinding;
import com.example.myapplication.ui.safety.SafetyDBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class Contact_Safety extends Fragment {

    private SafetyContactBinding binding; // 바인딩변수 선언
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private SafetyDBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = SafetyContactBinding.inflate(inflater, container, false); // 바인딩 초기화
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // UI 요소 초기화
        listView = view.findViewById(R.id.listView);

        // 데이터베이스 헬퍼 초기화
        dbHelper = new SafetyDBHelper(requireContext()); // requireContext()를 사용하여 Fragment의 Context를 가져옵니다.

        // 모든 게시글을 가져와서 리스트에 추가
        List<String> contactsList = dbHelper.getAllContacts();

        // 어댑터를 사용하여 리스트에 데이터 연결
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, contactsList);
        listView.setAdapter(adapter);

        // fab 클릭 시에 이 액션을 트리거하도록 설정
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FAB를 클릭할 때 네비게이션 액션을 트리거하여 navigation_board_regform로 이동
                Navigation.findNavController(v).navigate(R.id.action_navigation_safety_contact_to_navigation_safety_regform);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
