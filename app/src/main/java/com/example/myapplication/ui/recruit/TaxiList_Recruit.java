package com.example.myapplication.ui.recruit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.databinding.RecruitTaxilistBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class TaxiList_Recruit extends Fragment {

    private RecruitTaxilistBinding binding;
    private TaxiRecruitDBHelper taxidbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = RecruitTaxilistBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

        taxidbHelper = new TaxiRecruitDBHelper(requireContext());

        List<TaxiList_Item_Recruit> taxisList = taxidbHelper.getAllTaxis();

        // 변경된 부분: 어댑터 생성 시에 리스트와 OnItemClickListener를 전달
        TaxiAdapter taxiadapter = new TaxiAdapter(requireContext(), taxisList, new TaxiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TaxiList_Item_Recruit item) {
                // 클릭 이벤트 처리
            }
        });

        recyclerView.setAdapter(taxiadapter);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_navigation_recruit_taxilist_to_navigation_recruit_taxireg);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}