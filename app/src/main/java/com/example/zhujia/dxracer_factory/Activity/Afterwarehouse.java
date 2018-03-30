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
 * 售后仓库
 */

public class Afterwarehouse extends AppCompatActivity implements View.OnClickListener {


    private Toolbar toolbar;
    private TextView shdd,shdsh,shyjdd,shrkd,shkcl;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.afterwarehouse);
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

        shdd=(TextView)findViewById(R.id.shdd);
        shdsh=(TextView)findViewById(R.id.shdsh);
        shyjdd=(TextView)findViewById(R.id.shyjdd);
        shrkd=(TextView)findViewById(R.id.shrkd);
        shkcl=(TextView)findViewById(R.id.shkcl);


        shdd.setOnClickListener(this);
        shdsh.setOnClickListener(this);
        shyjdd.setOnClickListener(this);
        shrkd.setOnClickListener(this);
        shkcl.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if(v==shdd){
            //售后订单
            Intent intent=new Intent(getApplicationContext(),Order.class);
            intent.putExtra("type","2");
            startActivity(intent);
        }
        if(v==shdsh){
            //售后待收货
            Intent intent=new Intent(getApplicationContext(),Stockinorderd.class);
            intent.putExtra("type","4");
            startActivity(intent);

        }
        if(v==shyjdd){
            //售后已接单
            Intent intent=new Intent(getApplicationContext(),Stockinorder.class);
            intent.putExtra("type","4");
            startActivity(intent);

        }
        if(v==shrkd){
            //售后入库单
            Intent intent=new Intent(getApplicationContext(),Spotwarehousing.class);
            intent.putExtra("type","2");
            startActivity(intent);

        }
        if(v==shkcl){
            //售后库存量
            Intent intent=new Intent(getApplicationContext(),Inventory.class);
            intent.putExtra("type","3");
            startActivity(intent);
        }
    }
}
