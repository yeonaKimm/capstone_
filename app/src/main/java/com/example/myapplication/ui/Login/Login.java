package com.example.myapplication.ui.Login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.common.model.ClientError;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.common.util.Utility;
import com.example.myapplication.MainActivity;
import com.example.myapplication.ui.Login.SetNicknameActivity;
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
                    Log.d(TAG, "카카오 로그인 성공");
                    getUserInfoAndSaveToDB();
                } else if (throwable instanceof ClientError && "Cancelled".equals(((ClientError) throwable).getReason())) {
                    Log.e(TAG, "Login cancelled by user.");
                    Toast.makeText(Login.this, "로그인이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "Login failed", throwable);
                    Toast.makeText(Login.this, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
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
                String gender = user.getKakaoAccount().getGender() != null ? user.getKakaoAccount().getGender().name() : "Unknown";
                String ageRange = user.getKakaoAccount().getAgeRange() != null ? user.getKakaoAccount().getAgeRange().name() : "Unknown";
                String birthyear = user.getKakaoAccount().getBirthyear();
                String profilePicture = user.getKakaoAccount().getProfile().getProfileImageUrl();
                String rankId = "1"; // 기본 등급 코드 설정

                if (!databaseHelper.isUserExists(userId)) {
                    boolean isInserted = databaseHelper.insertUser(userId, email, name, gender, ageRange, birthyear, name, profilePicture, rankId);
                    if (isInserted) {
                        Log.d(TAG, "회원가입 성공");
                        Intent intent = new Intent(Login.this, SetNicknameActivity.class);
                        intent.putExtra("USER_ID", userId);
                        startActivity(intent);
                    } else {
                        Log.e(TAG, "회원가입 실패");
                        Toast.makeText(Login.this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, "이미 가입한 회원");
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    intent.putExtra("USER_ID", userId);
                    startActivity(intent);
                }
                finish();
            } else {
                Log.e(TAG, "사용자 정보 요청 실패", throwable);
                Toast.makeText(Login.this, "사용자 정보 요청에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
            return null;
        });
    }
}
