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
                    // 로그인 성공 시 사용자 정보 가져오기
                    UserApiClient.getInstance().me((user, meError) -> {
                        if (meError != null) {
                            Log.e(TAG, "Failed to get user info", meError);
                            runOnUiThread(() -> Toast.makeText(Login.this, "사용자 정보를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show());
                        } else if (user != null) {
                            String userId = String.valueOf(user.getId());
                            String email = user.getKakaoAccount().getEmail();
                            String nickname = user.getKakaoAccount().getProfile().getNickname();
                            String gender = user.getKakaoAccount().getGender() != null ? user.getKakaoAccount().getGender().toString() : "N/A";
                            String ageRange = user.getKakaoAccount().getAgeRange() != null ? user.getKakaoAccount().getAgeRange().toString() : "N/A";
                            String birthyear = user.getKakaoAccount().getBirthyear();
                            String profileImageUrl = user.getKakaoAccount().getProfile().getProfileImageUrl();

                            Log.d(TAG, "User ID: " + userId + ", Nickname: " + nickname + ", Gender: " + gender + ", Age Range: " + ageRange);

                            // 사용자 정보 데이터베이스에 저장
                            boolean isInserted = databaseHelper.insertUser(userId, email, nickname, gender, ageRange, birthyear, nickname, profileImageUrl, "1");

                            if (isInserted) {
                                Log.d(TAG, "User information inserted successfully");
                            } else {
                                Log.e(TAG, "Failed to insert user information");
                            }

                            // 다음 액티비티로 이동
                            Intent intent;
                            if (isFirstLogin()) {
                                intent = new Intent(Login.this, SetNicknameActivity.class);
                            } else {
                                intent = new Intent(Login.this, MainActivity.class);
                            }
                            intent.putExtra("USER_ID", userId);
                            startActivity(intent);
                            finish();
                        }
                        return null;
                    });
                } else if (throwable instanceof ClientError && "Cancelled".equals(((ClientError) throwable).getReason())) {
                    Log.e(TAG, "Login cancelled by user.");
                } else {
                    Log.e(TAG, "Login failed", throwable);
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
