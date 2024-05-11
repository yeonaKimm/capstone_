package com.example.myapplication.ui.recruit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.databinding.RecruitBuyregBinding;
import com.example.myapplication.ui.recruit.BuyRecruitDBHelper;

public class BuyReg_Recruit extends Fragment {

    private RecruitBuyregBinding binding; // 바인딩변수 선언
    private String[] peopleOptions = {"--명", "1", "2", "3", "4"};

    public static BuyReg_Recruit newInstance() {
        return new BuyReg_Recruit();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = RecruitBuyregBinding.inflate(inflater, container, false); // 바인딩 초기화

        // 인원 수 Spinner와 Adapter 설정
        Spinner spinnerPeople = binding.getRoot().findViewById(R.id.spinner_people);
        ArrayAdapter<CharSequence> peopleAdapter = new ArrayAdapter<CharSequence>(requireContext(), android.R.layout.simple_spinner_item, peopleOptions) {
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
        peopleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPeople.setAdapter(peopleAdapter);


        // register 클릭 시에 이 액션을 트리거하도록 설정함
        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topic = binding.topic.getText().toString();
                int price = Integer.parseInt(binding.price.getText().toString());
                int people = Integer.parseInt(peopleOptions[spinnerPeople.getSelectedItemPosition()]);
                String content = binding.content.getText().toString();

                // topic과 content를 데이터베이스에 삽입
                BuyRecruitDBHelper dbHelper = new BuyRecruitDBHelper(getContext());
                dbHelper.insertBuy(topic, price, people, content);

                // NavController를 가져옴
                NavController navController = Navigation.findNavController(v);

                // 액션을 트리거하여 navigation_recruit_buyprint로 이동
                navController.navigate(R.id.action_navigation_recruit_buyreg_to_navigation_recruit_buyprint);
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
