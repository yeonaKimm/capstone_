package com.example.myapplication.ui.Login;

import android.app.Application;

import com.example.myapplication.R;
import com.kakao.sdk.common.KakaoSdk;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        KakaoSdk.init(this, getString(R.string.kakao_app_key));
    }
}
