package com.example.zhujia.dxracer_factory.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhujia.dxracer_factory.Adapter.ForeignshipmentsMG;
import com.example.zhujia.dxracer_factory.Adapter.ShipmentorderMG;
import com.example.zhujia.dxracer_factory.Adapter.SinglerowMG;
import com.example.zhujia.dxracer_factory.Adapter.SinglerowMG1;
import com.example.zhujia.dxracer_factory.Adapter.SinglerowMG2;
import com.example.zhujia.dxracer_factory.Adapter.purchasingMG;
import com.example.zhujia.dxracer_factory.Data.PidData;
import com.example.zhujia.dxracer_factory.Data.ScheduData;
import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtility;
import com.example.zhujia.dxracer_factory.Tools.Net.IHttpCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.design.widget.TabLayout.MODE_FIXED;
import static android.support.design.widget.TabLayout.MODE_SCROLLABLE;

/**
 * Created by zhujia on 2017/11/21.
 *
 * 国内出货
 */

public class Shipmentorder extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ShipmentorderMG myFragmentPagerAdapter1;
    private ForeignshipmentsMG myFragmentPagerAdapter2;
    private Toolbar toolbar;
    private String roleId;
    private SharedPreferences sharedPreferences;
    Map<String,String> params;
    private Handler mHandler;
    private Intent intent;
    private String[]str;
    private TextView text;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shipmentorder);
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
        intent=getIntent();
        roleId=sharedPreferences.getString("roleId","");
        mTabLayout=(TabLayout)findViewById(R.id.tabLayout4);
        mViewPager=(ViewPager)findViewById(R.id.viewPager4);
        text=(TextView)findViewById(R.id.text);
        if(intent.getStringExtra("type").equals("0")){
            text.setText("国内出货");
            myFragmentPagerAdapter1=new ShipmentorderMG(getSupportFragmentManager(),Shipmentorder.this);
            mViewPager.setAdapter(myFragmentPagerAdapter1);
        }else {
            text.setText("国外出货");
            myFragmentPagerAdapter2=new ForeignshipmentsMG(getSupportFragmentManager(),Shipmentorder.this);
            mViewPager.setAdapter(myFragmentPagerAdapter2);
        }

        mTabLayout.setTabMode(MODE_FIXED);
        mTabLayout.setupWithViewPager(mViewPager);


       /* one=mTabLayout.getTabAt(0);
        two=mTabLayout.getTabAt(1);
        three=mTabLayout.getTabAt(2);
        four=mTabLayout.getTabAt(3);*/
    }







}
