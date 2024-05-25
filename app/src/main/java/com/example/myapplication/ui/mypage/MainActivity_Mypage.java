package com.example.myapplication.ui.mypage;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.Navigation;
import com.example.myapplication.R;

public class MainActivity_Mypage extends Fragment {


    public static MainActivity_Mypage newInstance() {
        return new MainActivity_Mypage();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mypage_main, container, false);

        view.findViewById(R.id.imageViewSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_navigation_recruit_to_navigation_mypage);
            }
        });

        return view;
    }


}
