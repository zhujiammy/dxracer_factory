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
 * 设备管理
 */

public class Devicemanagement extends AppCompatActivity implements View.OnClickListener {


    private Toolbar toolbar;
    private TextView sbwxs,lbgl,pjgl,wdsb,sysb,wdwxsq,sywxsq,wdbysq,sybysq;
    private  Intent intent;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.devicemanagement);
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

        sbwxs=(TextView)findViewById(R.id.sbwxs);
        lbgl=(TextView)findViewById(R.id.lbgl);
        pjgl=(TextView)findViewById(R.id.pjgl);
        wdsb=(TextView)findViewById(R.id.wdsb);
        sysb=(TextView)findViewById(R.id.sysb);
        wdwxsq=(TextView)findViewById(R.id.wdwxsq);
        sywxsq=(TextView)findViewById(R.id.sywxsq);
        wdbysq=(TextView)findViewById(R.id.wdbysq);
        sybysq=(TextView)findViewById(R.id.sybysq);


        sbwxs.setOnClickListener(this);
        lbgl.setOnClickListener(this);
        pjgl.setOnClickListener(this);
        wdsb.setOnClickListener(this);
        sysb.setOnClickListener(this);
        wdwxsq.setOnClickListener(this);
        sywxsq.setOnClickListener(this);
        wdbysq.setOnClickListener(this);
        sybysq.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if(v==sbwxs){
            //设备维修商
            intent=new Intent(getApplicationContext(),Equipmentsupplier.class);
            startActivity(intent);
        }
        if(v==lbgl){
            //类别管理
            intent=new Intent(getApplicationContext(),EquipmentCategory.class);
            startActivity(intent);
        }
        if(v==pjgl){
            //配件管理
            intent=new Intent(getApplicationContext(),Equipmentdetail.class);
            startActivity(intent);
        }
        if(v==wdsb){
            //我的设备
            intent=new Intent(getApplicationContext(),Myequipment.class);
            startActivity(intent);
        }
        if(v==sysb){
            //所有设备
            intent=new Intent(getApplicationContext(),Equipment.class);
            startActivity(intent);
        }
        if(v==wdwxsq){
            //我的维修申请
            intent=new Intent(getApplicationContext(),Equipmentmaintenance.class);
            startActivity(intent);
        }
        if(v==sywxsq){
            //所有维修申请
            intent=new Intent(getApplicationContext(),AllEquipmentmaintenance.class);
            startActivity(intent);
        }
        if(v==wdbysq){
            //我的保养申请
            intent=new Intent(getApplicationContext(),EquipmentMaintain.class);
            startActivity(intent);

        }
        if(v==sybysq){
            //所有保养申请
            intent=new Intent(getApplicationContext(),AllEquipmentMaintain.class);
            startActivity(intent);


        }

    }
}
