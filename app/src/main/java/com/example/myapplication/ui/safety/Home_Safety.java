package com.example.myapplication.ui.safety;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.myapplication.R;
import com.example.myapplication.databinding.SafetyHomeBinding;

public class Home_Safety extends Fragment {

    private SafetyHomeBinding binding; // 바인딩 변수 선언
    private String selectedTime;
    private String selectedContact;

    // 예상 소요 시간과 비상 연락처의 목록
    private String[] timeOptions = {"--분", "10분", "20분", "30분", "40분", "50분"};
    private String[] contactOptions = {"저장명", "엄마", "아빠", "언니", "오빠"};

    private final ActivityResultLauncher<Intent> startForResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    String currentLocation = data.getStringExtra("currentLocation");
                    String destination = data.getStringExtra("destination");
                    if (currentLocation != null && !currentLocation.isEmpty()) {
                        binding.startET.setText(currentLocation);
                    }
                    if (destination != null && !destination.isEmpty()) {
                        binding.endET.setText(destination);
                    }
                }
            }
    );

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = SafetyHomeBinding.inflate(inflater, container, false); // 바인딩 초기화

        // Spinner와 Adapter 설정
        Spinner spinnerTime = binding.getRoot().findViewById(R.id.spinner_time);
        ArrayAdapter<CharSequence> timeAdapter = new ArrayAdapter<CharSequence>(requireContext(), android.R.layout.simple_spinner_item, timeOptions) {
            @Override
            public boolean isEnabled(int position) {
                // 첫 번째 항목 선택을 불가능하게 설정
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                // 첫 번째 항목 선택을 불가능하게 설정한 경우, 텍스트 색상을 회색으로 변경
                if (position == 0) {
                    textView.setTextColor(getResources().getColor(android.R.color.darker_gray));
                } else {
                    textView.setTextColor(getResources().getColor(android.R.color.black));
                }
                return view;
            }
        };
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(timeAdapter);

        // 비상 연락처 스피너 설정
        Spinner spinnerContact = binding.getRoot().findViewById(R.id.spinner_contact);
        ArrayAdapter<CharSequence> contactAdapter = new ArrayAdapter<CharSequence>(requireContext(), android.R.layout.simple_spinner_item, contactOptions) {
            @Override
            public boolean isEnabled(int position) {
                // 첫 번째 항목 선택을 불가능하게 설정
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                // 첫 번째 항목 선택을 불가능하게 설정한 경우, 텍스트 색상을 회색으로 변경
                if (position == 0) {
                    textView.setTextColor(getResources().getColor(android.R.color.darker_gray));
                } else {
                    textView.setTextColor(getResources().getColor(android.R.color.black));
                }
                return view;
            }
        };
        contactAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerContact.setAdapter(contactAdapter);

        // 출발 위치 클릭 이벤트 처리
        binding.startET.setOnClickListener(v -> openHomeRouteActivity());
        // 도착 위치 클릭 이벤트 처리
        binding.endET.setOnClickListener(v -> openHomeRouteActivity());

        // 확인 버튼 클릭 이벤트 처리
        binding.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTime = (String) spinnerTime.getSelectedItem();
                selectedContact = (String) spinnerContact.getSelectedItem();

                Toast.makeText(requireContext(), "소요시간과 비상연락처가 저장되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }

    private void openHomeRouteActivity() {
        Intent intent = new Intent(getActivity(), HomeRouteActivity.class);
        intent.putExtra("selectedTime", selectedTime);
        intent.putExtra("selectedContact", selectedContact);
        startForResultLauncher.launch(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
