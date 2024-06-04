package com.example.myapplication.ui.Login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.common.model.ClientError;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.common.util.Utility;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class Login extends AppCompatActivity {

    private static final String TAG = "Login";
    private ImageView loginButton;
    private Button deleteUserButton;
    private DatabaseHelper databaseHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        databaseHelper = new DatabaseHelper(this);

        loginButton = findViewById(R.id.login);
        deleteUserButton = findViewById(R.id.deleteUserButton);

        // 카카오 키 해시 로그 출력
        String keyHash = Utility.INSTANCE.getKeyHash(this);
        Log.d("Kakao Key Hash", keyHash);

        // 카카오톡 로그인 콜백
        Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                if (oAuthToken != null) {
                    // 로그인 성공 시 로직
                    if (isFirstLogin()) {
                        Intent intent = new Intent(Login.this, SetNicknameActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                    }
                    finish();
                } else if (throwable instanceof ClientError && "Cancelled".equals(((ClientError) throwable).getReason())) {
                    Log.e(TAG, "Login cancelled by user.");
                } else {
                    Log.e(TAG, "Login failed", throwable);
                    // 로그인 실패 시 토스트 메시지 출력
                    runOnUiThread(() -> Toast.makeText(Login.this, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show());
                }
                return null;
            }
        };

        // 로그인 버튼 클릭 리스너
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(Login.this)) {
                    UserApiClient.getInstance().loginWithKakaoTalk(Login.this, callback);
                } else {
                    UserApiClient.getInstance().loginWithKakaoAccount(Login.this, callback);
                }
            }
        });

        // 사용자 정보 삭제 버튼 클릭 리스너
        deleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHelper.deleteAllUsers();
                clearLoginState();
                Toast.makeText(Login.this, "모든 사용자 정보가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isFirstLogin() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return !preferences.getBoolean("isRegistered", false);
    }

    private void clearLoginState() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
