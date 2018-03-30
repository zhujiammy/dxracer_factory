package com.example.zhujia.dxracer_factory.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.zhujia.dxracer_factory.Adapter.SinglerowMG;
import com.example.zhujia.dxracer_factory.Adapter.SinglerowMG2;
import com.example.zhujia.dxracer_factory.R;

import static android.support.design.widget.TabLayout.MODE_FIXED;
import static android.support.design.widget.TabLayout.MODE_SCROLLABLE;

/**
 * Created by zhujia on 2017/11/24.
 *
 *
 * 滞留仓库
 */

public class Detainedwarehouse extends AppCompatActivity implements View.OnClickListener {

    private String roleId;
    private SharedPreferences sharedPreferences;
    private Toolbar toolbar;
    private TextView orderback;



    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detainedwarehouse);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sharedPreferences =getSharedPreferences("Session",
                Context.MODE_APPEND);
        roleId=sharedPreferences.getString("roleId","");
        orderback=(TextView)findViewById(R.id.orderback);
        orderback.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Intent intent;

        if(v==orderback){
            //滞留订单
            intent=new Intent(getApplicationContext(),OrderBack.class);
            startActivity(intent);
        }
    }
}
