package com.example.myapplication.ui.safety;

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
import com.example.myapplication.databinding.SafetyRegformBinding;
import com.example.myapplication.ui.safety.SafetyDBHelper;

public class RegForm_Safety extends Fragment {

    private SafetyRegformBinding binding; // 바인딩변수 선언

    public static RegForm_Safety newInstance() {
        return new RegForm_Safety();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = SafetyRegformBinding.inflate(inflater, container, false); // 바인딩 초기화

        // register 클릭 시에 이 액션을 트리거하도록 설정함
        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.name.getText().toString();
                String contact = binding.contact.getText().toString();

                // topic과 contact를 데이터베이스에 삽입
                SafetyDBHelper dbHelper = new SafetyDBHelper(getContext());
                dbHelper.insertContact(name, contact);

                // NavController를 가져옴
                NavController navController = Navigation.findNavController(v);

                // 액션을 트리거하여 navigation_board_printform로 이동
                navController.navigate(R.id.action_navigation_safety_regform_to_navigation_safety_contact);
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
