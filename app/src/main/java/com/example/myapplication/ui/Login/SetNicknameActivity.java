package com.example.myapplication.ui.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.example.myapplication.ui.map.join_map;

public class SetNicknameActivity extends AppCompatActivity {

    private EditText nicknameEditText;
    private Button nextButton;
    private ImageView cancelButton;
    private DatabaseHelper databaseHelper;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_nickname);

        nicknameEditText = findViewById(R.id.nicknameEditText);
        nextButton = findViewById(R.id.nextButton);
        cancelButton = findViewById(R.id.cancelButton);
        databaseHelper = new DatabaseHelper(this);

        userId = getIntent().getStringExtra("USER_ID");
        String nickname = databaseHelper.getUserNickname(userId);

        if (nickname != null) {
            nicknameEditText.setText(nickname);
        } else {
            nicknameEditText.setHint("닉네임을 입력하세요.");
        }

        nicknameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                nextButton.setEnabled(charSequence.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        nextButton.setOnClickListener(view -> {
            String newNickname = nicknameEditText.getText().toString();
            databaseHelper.updateUserNickname(userId, newNickname);

            // 회원가입 완료 상태 저장
            SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isRegistered", true);
            editor.apply();

            // 다음 액티비티로 이동
            Intent intent = new Intent(SetNicknameActivity.this, join_map.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
            finish();
        });

        cancelButton.setOnClickListener(view -> {
            finish();
        });

        nextButton.setEnabled(nicknameEditText.getText().length() > 0);
    }
}
