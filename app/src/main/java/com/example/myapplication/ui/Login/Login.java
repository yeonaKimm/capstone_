package com.example.myapplication.ui.Login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.ui.Login.DatabaseHelper;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivitySetNicknameBinding;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.common.model.ClientError;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.common.util.Utility;
import com.kakao.sdk.user.model.User;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class Login extends AppCompatActivity {

    private static final String TAG = "Login";
    private ImageView loginButton;
    private DatabaseHelper databaseHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        loginButton = findViewById(R.id.login);
        databaseHelper = new DatabaseHelper(this);

        // 카카오 키 해시 로그 출력
        String keyHash = Utility.INSTANCE.getKeyHash(this);
        Log.d("Kakao Key Hash", keyHash);

        // 카카오톡 로그인 콜백
        Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                if (oAuthToken != null) {
                    // 로그인 성공 시 로직
                    getUserInfoAndSaveToDB();
                } else if (throwable instanceof ClientError && "Cancelled".equals(((ClientError) throwable).getReason())) {
                    Log.e(TAG, "Login cancelled by user.");
                    // 사용자가 로그인을 취소한 경우 처리 로직
                    // 예: 메인 화면으로 돌아가기, 사용자에게 로그인 취소 알림 등
                } else {
                    Log.e(TAG, "Login failed", throwable);
                    // 다른 오류 처리 로직
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
    }

    private void getUserInfoAndSaveToDB() {
        UserApiClient.getInstance().me((user, throwable) -> {
            if (user != null) {
                String userId = String.valueOf(user.getId());
                String email = user.getKakaoAccount().getEmail();
                String name = user.getKakaoAccount().getProfile().getNickname();
                String gender = user.getKakaoAccount().getGender() != null ? user.getKakaoAccount().getGender().name() : "Not Provided";
                String ageRange = user.getKakaoAccount().getAgeRange() != null ? user.getKakaoAccount().getAgeRange().name() : "Not Provided";
                String birthyear = user.getKakaoAccount().getBirthyear();
                String nickname = user.getKakaoAccount().getProfile().getNickname();
                String profilePicture = user.getKakaoAccount().getProfile().getThumbnailImageUrl();
                String rankId = "1"; // 기본 회원 등급 코드 설정 (예: "1" = Basic)

                // 사용자 정보를 데이터베이스에 저장
                saveUserToDatabase(userId, email, name, gender, ageRange, birthyear, nickname, profilePicture, rankId);

                // 닉네임 설정 페이지로 이동
                Intent intent = new Intent(Login.this, ActivitySetNicknameBinding.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
                finish();
            } else {
                Log.e(TAG, "Failed to get user info", throwable);
            }
            return null;
        });
    }

    private void saveUserToDatabase(String userId, String email, String name, String gender, String ageRange, String birthyear, String nickname, String profilePicture, String rankId) {
        boolean isInserted = databaseHelper.insertUser(userId, email, name, gender, ageRange, birthyear, nickname, profilePicture, rankId);
        if (isInserted) {
            Log.d("Database", "User information saved successfully");
        } else {
            Log.d("Database", "Failed to save user information");
        }
    }
}
