package com.example.myapplication.ui.recruit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.databinding.RecruitBuyregBinding;

import java.io.IOException;

public class BuyReg_Recruit extends Fragment {

    private RecruitBuyregBinding binding; // 바인딩변수 선언
    private String[] peopleOptions = {"--명", "1", "2", "3", "4"};
    private static final int PICK_IMAGE_REQUEST = 1; // 이미지 선택 요청 코드
    private ImageView photoImageView; // 사진을 표시할 ImageView
    private ImageView removePhotoButton; // 사진 제거 버튼

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

        // 사진 관련 뷰 초기화
        photoImageView = binding.getRoot().findViewById(R.id.photo);
        removePhotoButton = binding.getRoot().findViewById(R.id.remove_photoBt);

        // photo ImageView에 클릭 리스너를 설정하여 사진 선택 기능을 실행
        photoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(); // 갤러리 열기
            }
        });

        // removePhotoButton에 클릭 리스너를 설정하여 사진 제거 기능을 실행
        removePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePhoto(); // 사진 제거
            }
        });

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

    // 갤러리를 열어 사진을 선택할 수 있는 메서드
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // 사용자가 사진을 선택한 후 호출되는 메서드
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData(); // 선택한 사진의 URI
            try {
                // 선택한 사진을 Bitmap으로 변환하여 ImageView에 설정
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                photoImageView.setImageBitmap(bitmap);
                removePhotoButton.setVisibility(View.VISIBLE); // 사진 제거 버튼 보이기
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 사진을 제거하는 메서드
    private void removePhoto() {
        photoImageView.setImageResource(R.drawable.ic_gallery); // 기본 이미지로 설정
        removePhotoButton.setVisibility(View.GONE); // 사진 제거 버튼 숨기기
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
