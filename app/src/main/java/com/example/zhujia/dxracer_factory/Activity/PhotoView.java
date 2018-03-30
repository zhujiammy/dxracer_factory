package com.example.zhujia.dxracer_factory.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.Net.Constant;

/**
 * Created by zhujia on 2017/12/18.
 */

public class PhotoView extends AppCompatActivity {
    private uk.co.senab.photoview.PhotoView photoview;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photoview);
        photoview=(uk.co.senab.photoview.PhotoView)findViewById(R.id.photoview);
        Intent intent=getIntent();
        String urlimg=intent.getStringExtra("photoview");
        if(!urlimg.equals("null")){
            Glide.with(getApplicationContext()).load(Constant.APPURLIMG+urlimg).into(photoview);
        }

    }
}
