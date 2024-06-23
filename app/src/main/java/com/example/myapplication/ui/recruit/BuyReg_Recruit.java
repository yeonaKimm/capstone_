package com.example.myapplication.ui.recruit;

import android.content.Context;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.databinding.RecruitBuyregBinding;
import com.example.myapplication.ui.Login.DatabaseHelper;

import java.io.IOException;

public class BuyReg_Recruit extends Fragment {

    private RecruitBuyregBinding binding;
    private String[] peopleOptions = {"--명", "1", "2", "3", "4"};
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView photoImageView;
    private ImageView removePhotoButton;
    private Uri selectedImageUri;

    public static BuyReg_Recruit newInstance() {
        return new BuyReg_Recruit();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = RecruitBuyregBinding.inflate(inflater, container, false);

        Spinner spinnerPeople = binding.getRoot().findViewById(R.id.spinner_people);
        ArrayAdapter<CharSequence> peopleAdapter = new ArrayAdapter<CharSequence>(requireContext(), android.R.layout.simple_spinner_item, peopleOptions) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
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

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
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
            selectedImageUri = data.getData();
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
        selectedImageUri = null;
    }

    private void register() {
        String topic = binding.topic.getText().toString().trim();
        String priceStr = binding.price.getText().toString().trim().replace(",", "");
        String peopleStr = binding.spinnerPeople.getSelectedItem().toString().trim();
        String content = binding.content.getText().toString().trim();

        if (topic.isEmpty() || priceStr.isEmpty() || peopleStr.equals("--명") || content.isEmpty()) {
            Toast.makeText(getContext(), "모든 필드를 채워주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        int price = Integer.parseInt(priceStr);
        int people = Integer.parseInt(peopleStr);

        if (price <= 0 || people <= 0) {
            Toast.makeText(getContext(), "유효한 가격 및 인원 수를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        BuyRecruitDBHelper dbHelper = new BuyRecruitDBHelper(getContext());
        dbHelper.insertBuy(topic, price, people, content, selectedImageUri != null ? selectedImageUri.toString() : null, getUserIdFromPreferences());

        NavController navController = Navigation.findNavController(binding.getRoot());
        navController.navigate(R.id.action_navigation_recruit_buyreg_to_navigation_recruit_buylist);
    }

    private String getUserIdFromPreferences() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        return databaseHelper.getUserIdFromPreferences();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
