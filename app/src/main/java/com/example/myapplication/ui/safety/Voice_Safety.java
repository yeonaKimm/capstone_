package com.example.myapplication.ui.safety;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.SafetyVoiceBinding;

import java.util.ArrayList;
import java.util.List;

public class Voice_Safety extends Fragment {

    private SafetyVoiceBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SafetyVoiceBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<Voice_Item_Safety> voiceItemList = new ArrayList<>();
        voiceItemList.add(new Voice_Item_Safety("시킨 거 없는데요.", R.raw.deliveryno));
        voiceItemList.add(new Voice_Item_Safety("앞에 두고 가주세요.", R.raw.front));
        voiceItemList.add(new Voice_Item_Safety("가세요.", R.raw.goaway));
        voiceItemList.add(new Voice_Item_Safety("여기 아니에요.", R.raw.hereno));
        voiceItemList.add(new Voice_Item_Safety("아, 뭐하세요?", R.raw.hey));
        voiceItemList.add(new Voice_Item_Safety("가시라고요.", R.raw.heygo));
        voiceItemList.add(new Voice_Item_Safety("관심 없습니다.", R.raw.imok));
        voiceItemList.add(new Voice_Item_Safety("아니요. 괜찮아요", R.raw.noimok));
        voiceItemList.add(new Voice_Item_Safety("누구세요.", R.raw.nuguseyo));
        voiceItemList.add(new Voice_Item_Safety("누구신데요.", R.raw.nugusindeyo));
        voiceItemList.add(new Voice_Item_Safety("네?", R.raw.pardon));
        voiceItemList.add(new Voice_Item_Safety("이미 결제했어요.", R.raw.payok));
        voiceItemList.add(new Voice_Item_Safety("경찰에 신고합니다.", R.raw.police));
        voiceItemList.add(new Voice_Item_Safety("네 잠시만요.", R.raw.wait));
        voiceItemList.add(new Voice_Item_Safety("야 배달왔다.", R.raw.yadelivery));
        voiceItemList.add(new Voice_Item_Safety("네~", R.raw.yes));

        VoiceAdapter adapter = new VoiceAdapter(requireContext(), voiceItemList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
