package com.example.myapplication.ui.board;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.databinding.BoardVoteregBinding;

import java.io.IOException;

public class VoteReg_Board extends Fragment {

    private BoardVoteregBinding binding; // 바인딩변수 선언
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView photoImageView;
    private ImageView removePhotoButton;
    private Uri selectedImageUri;

    public static VoteReg_Board newInstance() {
        return new VoteReg_Board();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BoardVoteregBinding.inflate(inflater, container, false); // 바인딩 초기화

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
                String content = binding.content.getText().toString();
                String option1 = binding.option1.getText().toString();
                String option2 = binding.option2.getText().toString();

                // topic과 content, imageuri, option1, option2를 데이터베이스에 삽입
                VoteDBHelper dbHelper = new VoteDBHelper(getContext());
                dbHelper.insertVote(topic, content, selectedImageUri != null ? selectedImageUri.toString() : null, option1, option2);

                // NavController를 가져옴
                NavController navController = Navigation.findNavController(v);

                // 액션을 트리거하여 navigation_board_printform로 이동
                navController.navigate(R.id.action_navigation_board_votereg_to_navigation_board_votelist);
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
            Log.d("VoteReg_Board", "Image URI selected: " + selectedImageUri); // 로그 추가
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
