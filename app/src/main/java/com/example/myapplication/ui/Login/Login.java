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

import com.example.myapplication.MainActivity;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.common.model.ClientError;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.common.util.Utility;
import com.kakao.sdk.user.model.User;
import com.example.myapplication.R;
import com.example.myapplication.MainActivity;
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
                    Log.d(TAG, "카카오 로그인 성공");
                    getUserInfoAndNavigate();
                } else {
                    if (throwable instanceof ClientError && "Cancelled".equals(((ClientError) throwable).getReason())) {
                        Log.e(TAG, "Login cancelled by user.");
                    } else {
                        Log.e(TAG, "Login failed", throwable);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Login.this, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
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

    private void getUserInfoAndNavigate() {
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                if (user != null) {
                    String userId = String.valueOf(user.getId());
                    boolean userExists = databaseHelper.isUserExists(userId);

                    if (userExists) {
                        // 이미 가입한 사용자 -> MainActivity로 이동
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Login 액티비티 종료
                    } else {
                        // 신규 사용자 -> 데이터베이스에 사용자 정보 저장 후 SetNicknameActivity로 이동
                        String email = user.getKakaoAccount().getEmail();
                        String name = user.getKakaoAccount().getProfile().getNickname();
                        String gender = user.getKakaoAccount().getGender() != null ? user.getKakaoAccount().getGender().name() : "Not Provided";
                        String ageRange = user.getKakaoAccount().getAgeRange() != null ? user.getKakaoAccount().getAgeRange().name() : "Not Provided";
                        String birthyear = user.getKakaoAccount().getBirthyear();
                        String nickname = user.getKakaoAccount().getProfile().getNickname();
                        String profilePicture = user.getKakaoAccount().getProfile().getThumbnailImageUrl() != null ? user.getKakaoAccount().getProfile().getThumbnailImageUrl() : "default_profile_picture_url"; // 기본 값 설정
                        String rankId = "1"; // 기본 회원 등급 코드 설정 (예: "1" = Basic)

                        // 사용자 정보를 데이터베이스에 저장
                        boolean isInserted = databaseHelper.insertUser(userId, email, name, gender, ageRange, birthyear, nickname, profilePicture, rankId);

                        if (isInserted) {
                            Log.d(TAG, "사용자 정보가 데이터베이스에 저장되었습니다.");
                            // 닉네임 설정 페이지로 이동
                            Intent intent = new Intent(Login.this, SetNicknameActivity.class);
                            intent.putExtra("USER_ID", userId);
                            startActivity(intent);
                            finish(); // Login 액티비티 종료
                        } else {
                            Log.e(TAG, "Failed to insert user data into database.");
                        }
                    }
                } else {
                    Log.e(TAG, "Failed to get user info", throwable);
                }
                return null;
            }
        });
    }
}
