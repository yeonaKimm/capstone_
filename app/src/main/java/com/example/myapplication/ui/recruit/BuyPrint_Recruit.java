package com.example.myapplication.ui.recruit;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.R;
import com.example.myapplication.ui.Login.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class BuyPrint_Recruit extends Fragment {

    private ImageView itemImage, itemProfileImage;
    private TextView itemTopic, itemContent, itemPrice, itemPeople, itemNickname, itemGenderAge;
    private TextView enterButton;
    private BuyRecruitDBHelper dbHelper;
    private BuyList_Item_Recruit selectedItem;

    private EditText priceEditText, divisionEditText, paymentEditText, promiseEditText, accountEditText, dateEditText, timeEditText, placeEditText;
    private RecyclerView commentRecyclerView;
    private BuyCommentAdapter commentAdapter;
    private BuyCommentRecruitDBHelper commentDbHelper;
    private Spinner bankSpinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recruit_buyprint, container, false);

        itemImage = view.findViewById(R.id.item_image);
        itemProfileImage = view.findViewById(R.id.itemProfileImage);
        itemTopic = view.findViewById(R.id.item_topic);
        itemContent = view.findViewById(R.id.item_content);
        itemPrice = view.findViewById(R.id.item_price);
        itemPeople = view.findViewById(R.id.item_people);
        itemNickname = view.findViewById(R.id.itemNickname);
        itemGenderAge = view.findViewById(R.id.itemGenderAge);
        enterButton = view.findViewById(R.id.enter);

        priceEditText = view.findViewById(R.id.price);
        divisionEditText = view.findViewById(R.id.division);
        paymentEditText = view.findViewById(R.id.payment);
        promiseEditText = view.findViewById(R.id.promise);
        accountEditText = view.findViewById(R.id.account);
        dateEditText = view.findViewById(R.id.date);
        timeEditText = view.findViewById(R.id.time);
        placeEditText = view.findViewById(R.id.place);
        commentRecyclerView = view.findViewById(R.id.commentRecyclerView);
        bankSpinner = view.findViewById(R.id.spinner_bank);

        dbHelper = new BuyRecruitDBHelper(getContext());
        commentDbHelper = new BuyCommentRecruitDBHelper(getContext());

        commentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadComments();

        if (getArguments() != null) {
            selectedItem = getArguments().getParcelable("selectedItem");

            if (selectedItem != null) {
                Glide.with(this)
                        .load(selectedItem.getImageUri())
                        .placeholder(R.drawable.grade_babe)
                        .error(R.drawable.ic_error)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(itemImage);

                itemTopic.setText(selectedItem.getTopic());
                itemContent.setText(selectedItem.getContent());
                itemPrice.setText(String.format("%,d원", selectedItem.getPrice()));
                itemPeople.setText(String.valueOf(selectedItem.getPeople()));

                DatabaseHelper db = new DatabaseHelper(getContext());
                User user = db.getUserById(selectedItem.getUserId());
                if (user != null) {
                    itemNickname.setText(user.getNickname());
                    itemGenderAge.setText(String.format("%s, %s", user.getGender(), user.getAgeRange()));
                    Glide.with(this)
                            .load(user.getProfilePicture())
                            .placeholder(R.drawable.grade_babe)
                            .error(R.drawable.ic_error)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(itemProfileImage);
                }

                if (selectedItem.isClosed()) {
                    enterButton.setText("모집마감");
                    enterButton.setEnabled(false);
                } else {
                    enterButton.setText("참여하기");
                    enterButton.setEnabled(true);
                    enterButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dbHelper.updateRecruitStatus(selectedItem.getId(), true);
                            selectedItem.setClosed(true);
                            enterButton.setText("모집마감");
                            enterButton.setEnabled(false);
                        }
                    });
                }
            }
        }

        paymentEditText.setEnabled(false); // 정산가 EditText는 사용자가 직접 입력하지 못하게 설정

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculatePayment();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        priceEditText.addTextChangedListener(textWatcher);
        divisionEditText.addTextChangedListener(textWatcher);

        // Spinner 설정
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.bank_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bankSpinner.setAdapter(adapter);

        ImageView sendButton = view.findViewById(R.id.sendBt);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveComment();
            }
        });

        return view;
    }

    private void calculatePayment() {
        String priceStr = priceEditText.getText().toString();
        String divisionStr = divisionEditText.getText().toString();

        if (!priceStr.isEmpty() && !divisionStr.isEmpty()) {
            try {
                int price = Integer.parseInt(priceStr);
                int division = Integer.parseInt(divisionStr) + 1; // 인원수 + 1
                int payment = price / division;

                paymentEditText.setText(String.format("%,d", payment));
            } catch (NumberFormatException e) {
                paymentEditText.setText("");
            }
        } else {
            paymentEditText.setText("");
        }
    }

    private void loadComments() {
        Cursor cursor = commentDbHelper.getAllComments();
        List<BuyCommentList_Item_Recruit> comments = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            int promiseColumnIndex = cursor.getColumnIndex(BuyCommentRecruitDBHelper.COLUMN_PROMISE);
            int accountColumnIndex = cursor.getColumnIndex(BuyCommentRecruitDBHelper.COLUMN_ACCOUNT);
            int bankColumnIndex = cursor.getColumnIndex(BuyCommentRecruitDBHelper.COLUMN_BANK);
            int dateColumnIndex = cursor.getColumnIndex(BuyCommentRecruitDBHelper.COLUMN_DATE);
            int timeColumnIndex = cursor.getColumnIndex(BuyCommentRecruitDBHelper.COLUMN_TIME);
            int placeColumnIndex = cursor.getColumnIndex(BuyCommentRecruitDBHelper.COLUMN_PLACE);
            int priceColumnIndex = cursor.getColumnIndex(BuyCommentRecruitDBHelper.COLUMN_PRICE);
            int divisionColumnIndex = cursor.getColumnIndex(BuyCommentRecruitDBHelper.COLUMN_DIVISION);
            int paymentColumnIndex = cursor.getColumnIndex(BuyCommentRecruitDBHelper.COLUMN_PAYMENT);

            // 모든 컬럼이 존재하는지 확인
            if (promiseColumnIndex != -1 && accountColumnIndex != -1 && bankColumnIndex != -1 && dateColumnIndex != -1 &&
                    timeColumnIndex != -1 && placeColumnIndex != -1 && priceColumnIndex != -1 && divisionColumnIndex != -1 && paymentColumnIndex != -1) {
                do {
                    String promise = cursor.getString(promiseColumnIndex);
                    String account = cursor.getString(accountColumnIndex);
                    String bank = cursor.getString(bankColumnIndex);
                    String date = cursor.getString(dateColumnIndex);
                    String time = cursor.getString(timeColumnIndex);
                    String place = cursor.getString(placeColumnIndex);
                    String price = cursor.getString(priceColumnIndex);
                    String division = cursor.getString(divisionColumnIndex);
                    String payment = cursor.getString(paymentColumnIndex);

                    comments.add(new BuyCommentList_Item_Recruit(promise, account, bank, date, time, place, price, division, payment));
                } while (cursor.moveToNext());
            } else {
                Log.e("BuyPrint_Recruit", "One or more columns not found in cursor");
            }

            cursor.close();
        }

        commentAdapter = new BuyCommentAdapter(comments);
        commentRecyclerView.setAdapter(commentAdapter);
    }

    private void saveComment() {
        String promiseContent = promiseEditText.getText().toString();
        String accountContent = accountEditText.getText().toString();
        String bankContent = bankSpinner.getSelectedItem().toString();
        String dateContent = dateEditText.getText().toString();
        String timeContent = timeEditText.getText().toString();
        String placeContent = placeEditText.getText().toString();
        String priceContent = priceEditText.getText().toString();
        String divisionContent = divisionEditText.getText().toString();
        String paymentContent = paymentEditText.getText().toString();

        if (!promiseContent.isEmpty() && !accountContent.isEmpty() && !bankContent.isEmpty() &&
                !dateContent.isEmpty() && !timeContent.isEmpty() && !placeContent.isEmpty() &&
                !priceContent.isEmpty() && !divisionContent.isEmpty() && !paymentContent.isEmpty()) {

            commentDbHelper.insertComment(promiseContent, accountContent, bankContent, dateContent, timeContent, placeContent, priceContent, divisionContent, paymentContent);
            loadComments();
        }
    }
}
