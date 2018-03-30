package com.example.zhujia.dxracer_factory.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.Net.Constant;

/**
 * Created by ZHUJIA on 2018/1/16.
 */

public class WebviewActivity extends AppCompatActivity {

    private WebView webView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webviewactivity);
        Intent intent=getIntent();
        String id=intent.getStringExtra("id");
        webView=(WebView)findViewById(R.id.web);
        webView.loadUrl(Constant.AppURLDetail+"servlet/equipment/equipment/scan/"+id);
    }
}
