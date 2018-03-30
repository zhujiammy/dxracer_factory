package com.example.zhujia.dxracer_factory.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.zhujia.dxracer_factory.R;

/**
 * Created by zhujia on 2017/11/24.
 *
 *
 * 人事管理
 */

public class Personnelmatters extends AppCompatActivity implements View.OnClickListener {


    private Toolbar toolbar;
    private TextView yglb,wdxx,wylz;
    private  Intent intent;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personnelmatters);
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

        yglb=(TextView)findViewById(R.id.yglb);
        wdxx=(TextView)findViewById(R.id.wdxx);
        wylz=(TextView)findViewById(R.id.wylz);


        yglb.setOnClickListener(this);
        wdxx.setOnClickListener(this);
        wylz.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if(v==yglb){
            //员工列表
            intent=new Intent(getApplicationContext(),Employeelist.class);
            startActivity(intent);
        }
        if(v==wdxx){
            //我的信息
            intent=new Intent(getApplicationContext(),Myindex.class);
            startActivity(intent);

        }
        if(v==wylz){
            //我要离职

        }

    }
}
