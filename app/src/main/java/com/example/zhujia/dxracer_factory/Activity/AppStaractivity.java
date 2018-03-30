package com.example.zhujia.dxracer_factory.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.example.zhujia.dxracer_factory.LoginActivitys;
import com.example.zhujia.dxracer_factory.R;

/**
 * Created by DXSW5 on 2017/7/14.
 */

public class AppStaractivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 避免从桌面启动程序后，会重新实例化入口类的activity
        if (!this.isTaskRoot()) {
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                    finish();
                    return;
                }
            }
        }
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.startlog);
        //immediately 为 true, 每次强制访问服务器更新
        new Handler().postDelayed(runnable,3000);

    }
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            Intent intent=new Intent(AppStaractivity.this, LoginActivitys.class);
            startActivity(intent);
            finish();
        }
    };
}
