package com.example.zhujia.dxracer_factory.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.example.zhujia.dxracer_factory.R;


/**
 * Created by DXSW5 on 2017/9/5.
 */

public class Notice extends AppCompatActivity {
    private WebView webView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        Intent intent=getIntent();
        String content=intent.getStringExtra("content");
        webView=(WebView)findViewById(R.id.web);
        webView.loadDataWithBaseURL("about:blank",content,"text/html","utf-8",null);
    }
}
