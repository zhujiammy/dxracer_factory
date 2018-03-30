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
 * 现货仓库
 */

public class Spotwarehouses extends AppCompatActivity implements View.OnClickListener {


    private Toolbar toolbar;
    private TextView xhdd,dsh,rk,kc;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spotwarehouses);
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
        xhdd=(TextView)findViewById(R.id.xhdd);
        dsh=(TextView)findViewById(R.id.dsh);
        rk=(TextView)findViewById(R.id.rk);
        kc=(TextView)findViewById(R.id.kc);


        xhdd.setOnClickListener(this);
        dsh.setOnClickListener(this);
        rk.setOnClickListener(this);
        kc.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if(v==xhdd){
            //现货订单
            Intent intent=new Intent(getApplicationContext(),Order.class);
            intent.putExtra("type","1");
            startActivity(intent);
        }
        if(v==dsh){
            //现货待收货
            Intent intent=new Intent(getApplicationContext(),Stockinorderd.class);
            intent.putExtra("type","3");
            startActivity(intent);
        }
        if(v==rk){
            //现货入库单
            Intent intent=new Intent(getApplicationContext(),Spotwarehousing.class);
            intent.putExtra("type","1");
            startActivity(intent);

        }
        if(v==kc){
            //现货库存
            Intent intent=new Intent(getApplicationContext(),Inventory.class);
            intent.putExtra("type","1");
            startActivity(intent);
        }
    }
}
