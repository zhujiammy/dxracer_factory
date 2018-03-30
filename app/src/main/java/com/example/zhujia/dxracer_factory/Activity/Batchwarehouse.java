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
 * 批量仓库
 */

public class Batchwarehouse extends AppCompatActivity implements View.OnClickListener {

    private String roleId;
    private SharedPreferences sharedPreferences;
    private Toolbar toolbar;
    private TextView Goodsreceived,Unitreceived,Receivedorder,Billentry,Formaterial,Pendingshipment,Allmaterial,Outboundorder,Inventory,Tobewarehouse,Asinglestorage,Flatinventory;



    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.batchwarehouse);
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
        Goodsreceived=(TextView)findViewById(R.id.Goodsreceived);
        Unitreceived=(TextView)findViewById(R.id.Unitreceived);
        Receivedorder=(TextView)findViewById(R.id.Receivedorder);
        Billentry=(TextView)findViewById(R.id.Billentry);
        Formaterial=(TextView)findViewById(R.id.Formaterial);
        Pendingshipment=(TextView)findViewById(R.id.Pendingshipment);
        Allmaterial=(TextView)findViewById(R.id.Allmaterial);
        Outboundorder=(TextView)findViewById(R.id.Outboundorder);
        Inventory=(TextView)findViewById(R.id.Inventory);
        Tobewarehouse=(TextView)findViewById(R.id.Tobewarehouse);
        Asinglestorage=(TextView)findViewById(R.id.Asinglestorage);
        Flatinventory=(TextView)findViewById(R.id.Flatinventory);


        Goodsreceived.setOnClickListener(this);
        Unitreceived.setOnClickListener(this);
        Receivedorder.setOnClickListener(this);
        Billentry.setOnClickListener(this);
        Formaterial.setOnClickListener(this);
        Pendingshipment.setOnClickListener(this);
        Allmaterial.setOnClickListener(this);
        Outboundorder.setOnClickListener(this);
        Inventory.setOnClickListener(this);
        Tobewarehouse.setOnClickListener(this);
        Asinglestorage.setOnClickListener(this);
        Flatinventory.setOnClickListener(this);

        if(roleId.equals("8")){
            //仓库主管
            Tobewarehouse.setVisibility(View.GONE);
            Asinglestorage.setVisibility(View.GONE);
            Flatinventory.setVisibility(View.GONE);

        }else if(roleId.equals("7")||roleId.equals("54")){
            //生产部部长
            Goodsreceived.setVisibility(View.GONE);
            Unitreceived.setVisibility(View.GONE);
            Receivedorder.setVisibility(View.GONE);
            Billentry.setVisibility(View.GONE);
            Formaterial.setVisibility(View.GONE);
            Pendingshipment.setVisibility(View.GONE);
            Allmaterial.setVisibility(View.GONE);
            Outboundorder.setVisibility(View.GONE);
            Inventory.setVisibility(View.GONE);

        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        if(v==Goodsreceived){
            //耗材待收货
            intent=new Intent(getApplicationContext(),Stockinorder.class);
            intent.putExtra("type","1");
            startActivity(intent);
        }
        if(v==Unitreceived){
            //部件待收货
            intent=new Intent(getApplicationContext(),Stockinorder.class);
            intent.putExtra("type","2");
            startActivity(intent);
        }
        if(v==Receivedorder){
            //部件已接单
            intent=new Intent(getApplicationContext(),Stockinorders.class);
            startActivity(intent);
        }
        if(v==Billentry){
            //部件入库单
            intent=new Intent(getApplicationContext(),Billentry.class);
            startActivity(intent);
        }
        if(v==Formaterial){
            //待领料单据
            intent=new Intent(getApplicationContext(),Formaterial.class);
            startActivity(intent);
        }
        if(v==Pendingshipment){
            //领料待出库
           intent=new Intent(getApplicationContext(),Pendingshipment.class);
            startActivity(intent);

        }
        if(v==Allmaterial){
            //所有领料单
           intent=new Intent(getApplicationContext(),Allmaterial.class);
            startActivity(intent);

        }
        if(v==Outboundorder){
            //耗材出库单

            intent=new Intent(getApplicationContext(),Outboundorder.class);
            startActivity(intent);

        }
        if(v==Inventory){
            //耗材库存量
            intent=new Intent(getApplicationContext(),Inventory.class);
            intent.putExtra("type","2");
            startActivity(intent);

        }
        if(v==Tobewarehouse){
            //成品待入库
            intent=new Intent(getApplicationContext(),Tobewarehouses.class);
            startActivity(intent);
        }
        if(v==Asinglestorage){
            //成品入库单
            intent=new Intent(getApplicationContext(),Asinglestorages.class);
            startActivity(intent);

        }
        if(v==Flatinventory){
            //成品库存量
            intent=new Intent(getApplicationContext(),Flatinventorys.class);
            startActivity(intent);

        }
    }
}
