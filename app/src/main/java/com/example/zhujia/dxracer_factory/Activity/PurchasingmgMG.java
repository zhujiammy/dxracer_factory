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
 * 采购管理
 */

public class PurchasingmgMG extends AppCompatActivity implements View.OnClickListener {


    private Toolbar toolbar;
    private TextView cgjh,cgdlb;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchasingmgmg);
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

        cgjh=(TextView)findViewById(R.id.cgjh);
        cgdlb=(TextView)findViewById(R.id.cgdlb);



        cgjh.setOnClickListener(this);
        cgdlb.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if(v==cgjh){
            //采购计划
            Intent intent=new Intent(getApplicationContext(),Purchaseplan.class);
            startActivity(intent);
        }
        if(v==cgdlb){
            //采购单列表
            Intent intent=new Intent(getApplicationContext(),purchasing.class);
            startActivity(intent);

        }

    }
}
