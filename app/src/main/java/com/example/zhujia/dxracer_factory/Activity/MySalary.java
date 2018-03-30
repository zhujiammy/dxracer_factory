package com.example.zhujia.dxracer_factory.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.Net.Constant;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhujia on 2017/11/24.
 *
 *
 * 我的工资
 */

public class MySalary extends AppCompatActivity implements View.OnClickListener {


    private Toolbar toolbar;
    private TextView gzjg,mrhz,ygzt;
    private  Intent intent;
    Map<String,String> params;
    private String TYPE;
    private SharedPreferences sharedPreferences;
    private String business_id,departmentId,departmentPersonId;



    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mysalary);
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
        sharedPreferences =getSharedPreferences("Session", Context.MODE_APPEND);
        business_id=sharedPreferences.getString("business_id","");
        departmentId=sharedPreferences.getString("departmentId","");
        departmentPersonId=sharedPreferences.getString("departmentPersonId","");


        loadtype();//请求薪资方式
        gzjg=(TextView)findViewById(R.id.gzjg);
        mrhz=(TextView)findViewById(R.id.mrhz);
        ygzt=(TextView)findViewById(R.id.ygzt);


        gzjg.setOnClickListener(this);
        mrhz.setOnClickListener(this);
        ygzt.setOnClickListener(this);

    }

    private void loadtype(){
        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("personId", departmentPersonId);
        Log.e("TAG", "loaddata: "+params );
        new HttpUtils().post(Constant.APPURLS+"/servlet/salary/mysalary/detailhtm",params,new HttpUtils.HttpCallback() {
            @Override
            public void onSuccess(String data) {

                // TODO Auto-generated method stub
                com.example.zhujia.dxracer_factory.Tools.Log.printJson("tag",data,"header");
                Message msg=Message.obtain(
                        mHandler,1,data
                );
                mHandler.sendMessage(msg);
            }

            @Override
            public void onError(String msg) {

                // TODO Auto-generated method stub
                super.onError(msg);
                Toast.makeText(MySalary.this, "error", Toast.LENGTH_LONG).show();
            }
        });
    }


    @SuppressLint("HandlerLeak")

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            try{
                switch (msg.what) {

                    case 1:
                        //返回item类型数据
                      JSONObject  reslutJSONObject=new JSONObject(msg.obj.toString());
                        JSONObject type=reslutJSONObject.getJSONObject("departmentPe'rsonSalay");
                        TYPE=type.getString("type");
                        break;

                    default:
                        Toast.makeText(MySalary.this, "网络异常", Toast.LENGTH_SHORT).show();
                        break;
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    };
    @Override
    public void onClick(View v) {

        if(v==gzjg){
            //工资结构
            intent=new Intent(getApplicationContext(),MysalaryDetail.class);
            intent.putExtra("type",TYPE);
            Log.e("TAG", "onClick: "+TYPE);
            startActivity(intent);
        }

        if(v==mrhz){
            //每日汇总
            intent=new Intent(getApplicationContext(),MysalaryReport.class);
            startActivity(intent);
        }
        if(v==ygzt){
            //月工资条
            intent=new Intent(getApplicationContext(),MySalaryByMonth.class);
            startActivity(intent);
        }

    }
}
