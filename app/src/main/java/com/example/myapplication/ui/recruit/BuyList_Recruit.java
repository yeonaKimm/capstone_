package com.example.myapplication.ui.recruit;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.R;
import com.example.myapplication.databinding.RecruitBuylistBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class BuyList_Recruit extends Fragment {

    private RecruitBuylistBinding binding;
    private BuyRecruitDBHelper buydbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = RecruitBuylistBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

        buydbHelper = new BuyRecruitDBHelper(requireContext());

        List<BuyList_Item_Recruit> buysList = buydbHelper.getAllBuys();

        BuyAdapter buyadapter = new BuyAdapter(requireContext(), buysList, new BuyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BuyList_Item_Recruit item) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("selectedItem", item);
                Navigation.findNavController(view).navigate(R.id.action_navigation_recruit_buylist_to_navigation_recruit_buyprint, bundle);
            }
        });

        recyclerView.setAdapter(buyadapter);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_navigation_recruit_buylist_to_navigation_recruit_buyreg);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static class BuyAdapter extends RecyclerView.Adapter<BuyAdapter.BuyViewHolder> {

        private List<BuyList_Item_Recruit> buyList;
        private Context context;
        private OnItemClickListener mListener;

        public BuyAdapter(Context context, List<BuyList_Item_Recruit> buyList, OnItemClickListener mlistener) {
            this.context = context;
            this.buyList = buyList;
            this.mListener = mlistener;
        }

        @NonNull
        @Override
        public BuyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.recruit_buylist_item, parent, false);
            return new BuyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BuyViewHolder holder, int position) {
            BuyList_Item_Recruit item = buyList.get(position);
            holder.itemTopic.setText(item.getTopic());
            holder.itemPrice.setText(String.valueOf(item.getPrice()));
            holder.itemPeople.setText(String.format("%d명 모집!", item.getPeople()));

            if (item.getImageUri() != null && !item.getImageUri().isEmpty()) {
                Glide.with(context)
                        .load(item.getImageUri())
                        .placeholder(R.drawable.ic_image)
                        .error(R.drawable.ic_error)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.itemImage);
            } else {
                holder.itemImage.setImageDrawable(null);
            }

            if (item.isClosed()) {
                holder.recruitButton.setText("마감");
            } else {
                holder.recruitButton.setText("모집");
            }

            holder.recruitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(buyList.get(holder.getAdapterPosition()));
                }
            });
        }

        @Override
        public int getItemCount() {
            return buyList.size();
        }

        public static class BuyViewHolder extends RecyclerView.ViewHolder {
            public TextView itemTopic;
            public TextView itemPrice;
            public TextView itemPeople;
            public ImageView itemImage;
            public Button recruitButton;

            public BuyViewHolder(@NonNull View itemView) {
                super(itemView);
                itemTopic = itemView.findViewById(R.id.item_topic);
                itemPrice = itemView.findViewById(R.id.item_price);
                itemPeople = itemView.findViewById(R.id.item_people);
                itemImage = itemView.findViewById(R.id.item_image);
                recruitButton = itemView.findViewById(R.id.recruit);
            }
        }

        public interface OnItemClickListener {
            void onItemClick(BuyList_Item_Recruit item);
        }
    }
}
