package com.example.myapplication.ui.recruit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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
import com.example.myapplication.databinding.RecruitShareregBinding;

import java.io.IOException;

public class ShareReg_Recruit extends Fragment {

    private RecruitShareregBinding binding; // 바인딩변수 선언
    private String[] quantityOptions = {"수량", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView photoImageView;
    private ImageView removePhotoButton;
    private Uri selectedImageUri;

    public static ShareReg_Recruit newInstance() {
        return new ShareReg_Recruit();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = RecruitShareregBinding.inflate(inflater, container, false); // 바인딩 초기화

        // 인원 수 Spinner와 Adapter 설정
        Spinner spinnerQuantity = binding.getRoot().findViewById(R.id.spinner_quantity);
        ArrayAdapter<CharSequence> quantityAdapter = new ArrayAdapter<CharSequence>(requireContext(), android.R.layout.simple_spinner_item, quantityOptions) {
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
        quantityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQuantity.setAdapter(quantityAdapter);

        photoImageView = binding.getRoot().findViewById(R.id.photo);
        removePhotoButton = binding.getRoot().findViewById(R.id.remove_photoBt);

        photoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        removePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePhoto();
            }
        });

        // register 클릭 시에 이 액션을 트리거하도록 설정함
        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topic = binding.topic.getText().toString();
                String place = binding.place.getText().toString();
                int quantity = Integer.parseInt(quantityOptions[spinnerQuantity.getSelectedItemPosition()]);
                String content = binding.content.getText().toString();

                // topic과 content를 데이터베이스에 삽입
                ShareRecruitDBHelper dbHelper = new ShareRecruitDBHelper(getContext());
                dbHelper.insertShare(topic, place, quantity, content, selectedImageUri != null ? selectedImageUri.toString() : null);

                // NavController를 가져옴
                NavController navController = Navigation.findNavController(v);

                // 액션을 트리거하여 navigation_recruit_shareprint로 이동
                navController.navigate(R.id.action_navigation_recruit_sharereg_to_navigation_recruit_sharelist);
            }

        });

        return binding.getRoot();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData(); // 선택한 사진의 URI
            Log.d("ShareReg_Recruit", "Image URI selected: " + selectedImageUri); // 로그 추가
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                photoImageView.setImageBitmap(bitmap);
                removePhotoButton.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void removePhoto() {
        photoImageView.setImageResource(R.drawable.ic_gallery);
        removePhotoButton.setVisibility(View.GONE);
        selectedImageUri = null; // 선택된 이미지 URI를 null로 설정
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
