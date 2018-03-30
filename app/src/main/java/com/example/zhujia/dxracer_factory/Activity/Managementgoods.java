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
 * 物品管理
 */

public class Managementgoods extends AppCompatActivity implements View.OnClickListener {


    private Toolbar toolbar;
    private TextView wply,wdwp,ylwp,yhp,nyp,yhpbb,nypbb;
    private  Intent intent;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.managementgoods);
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

        wply=(TextView)findViewById(R.id.wply);
        wdwp=(TextView)findViewById(R.id.wdwp);
        ylwp=(TextView)findViewById(R.id.ylwp);
        yhp=(TextView)findViewById(R.id.yhp);
        nyp=(TextView)findViewById(R.id.nyp);
        yhpbb=(TextView)findViewById(R.id.yhpbb);
        nypbb=(TextView)findViewById(R.id.npybb);


        wply.setOnClickListener(this);
        wdwp.setOnClickListener(this);
        ylwp.setOnClickListener(this);

        yhp.setOnClickListener(this);
        nyp.setOnClickListener(this);
        yhpbb.setOnClickListener(this);
        nypbb.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if(v==wply){
            //物品领用
            intent=new Intent(getApplicationContext(),Officegoodstake.class);
            startActivity(intent);
        }
        if(v==wdwp){
            //我的物品


        }
        if(v==ylwp){
            //已领物品
            intent=new Intent(getApplicationContext(),Officegoodsstaff.class);
            startActivity(intent);

        }
        if(v==yhp){
            //易耗品列表
            intent=new Intent(getApplicationContext(),Officeconsumable.class);
            startActivity(intent);

        }
        if(v==nyp){
            //耐用品列表
            intent=new Intent(getApplicationContext(),Officegoodsdurable.class);
            startActivity(intent);

        }
        if(v==yhpbb){
            //易耗品报表
            intent=new Intent(getApplicationContext(),Consumable.class);
            startActivity(intent);
        }
        if(v==nypbb){
            //耐用品报表
            intent=new Intent(getApplicationContext(),Durable.class);
            startActivity(intent);

        }

    }
}
