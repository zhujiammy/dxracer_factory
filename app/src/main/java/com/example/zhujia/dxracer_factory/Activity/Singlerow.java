package com.example.zhujia.dxracer_factory.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
import android.widget.Toast;

import com.example.zhujia.dxracer_factory.Adapter.SinglerowMG;
import com.example.zhujia.dxracer_factory.Adapter.SinglerowMG1;
import com.example.zhujia.dxracer_factory.Adapter.SinglerowMG2;
import com.example.zhujia.dxracer_factory.Adapter.SinglerowMG3;
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
 * 排单生产
 */

public class Singlerow extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private SinglerowMG myFragmentPagerAdapter;
    private SinglerowMG1 myFragmentPagerAdapter1;
    private SinglerowMG2 myFragmentPagerAdapter2;
    private SinglerowMG3 myFragmentPagerAdapter3;
    private TabLayout.Tab one;
    private TabLayout.Tab two;
    private TabLayout.Tab three;
    private TabLayout.Tab four;
    private Toolbar toolbar;
    private String roleId;
    private SharedPreferences sharedPreferences;
    Map<String,String> params;
    private Handler mHandler;
    private String[]str;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singlerow);
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
        mTabLayout=(TabLayout)findViewById(R.id.tabLayout2);
        mViewPager=(ViewPager)findViewById(R.id.viewPager2);
        if(roleId.equals("6")){
            //生产主管
            myFragmentPagerAdapter1=new SinglerowMG1(getSupportFragmentManager(),Singlerow.this);
            mViewPager.setAdapter(myFragmentPagerAdapter1);
            mTabLayout.setTabMode(MODE_FIXED);
            mTabLayout.setupWithViewPager(mViewPager);
        }else if(roleId.equals("8")||roleId.equals("71")||roleId.equals("104")){
            //仓库主管
            myFragmentPagerAdapter=new SinglerowMG(getSupportFragmentManager(),Singlerow.this);
            mViewPager.setAdapter(myFragmentPagerAdapter);
            mTabLayout.setTabMode(MODE_FIXED);
            mTabLayout.setupWithViewPager(mViewPager);
        }else if(roleId.equals("7")){
            //生产部部长
            myFragmentPagerAdapter2=new SinglerowMG2(getSupportFragmentManager(),Singlerow.this);
            mViewPager.setAdapter(myFragmentPagerAdapter2);
            mTabLayout.setTabMode(MODE_SCROLLABLE);
            mTabLayout.setupWithViewPager(mViewPager);
        }else if(roleId.equals("54")){
            //成品仓库管理员
            myFragmentPagerAdapter3=new SinglerowMG3(getSupportFragmentManager(),Singlerow.this);
            mViewPager.setAdapter(myFragmentPagerAdapter3);
            mTabLayout.setTabMode(MODE_SCROLLABLE);
            mTabLayout.setupWithViewPager(mViewPager);
        }

       /* one=mTabLayout.getTabAt(0);
        two=mTabLayout.getTabAt(1);
        three=mTabLayout.getTabAt(2);
        four=mTabLayout.getTabAt(3);*/
    }







}
