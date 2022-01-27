package com.android.water.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.water.R;
import com.android.water.helper.SPUtils;


/**
 * desc   : 闪屏界面
 */
public final class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if ((Boolean) SPUtils.get(SplashActivity.this, "isLogin", false)) {//判断是否登录
                    if ((Boolean) SPUtils.get(SplashActivity.this, "isAutoLogin", false)) {//是否设置了自动登录
                        startActivity(new Intent(SplashActivity.this, HomeActivity.class));

                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));


                }
                finish();
            }
        }, 3000);
    }


}