package com.example.zhujia.dxracer_factory.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.zhujia.dxracer_factory.R;

import org.w3c.dom.Text;

/**
 * Created by zhujia on 2017/11/22.
 *
 * 详情页面
 */

public class MyindexDetail extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TextView grzl,wjgl,htgl,lxdz;
    private String personId;
    private Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myindexdetail);
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
        intent=getIntent();
        personId=intent.getStringExtra("personId");
        initUI();
        event();

    }


    private void initUI(){

        grzl=(TextView)findViewById(R.id.grzl);
        wjgl=(TextView)findViewById(R.id.wjgl);
        htgl=(TextView)findViewById(R.id.htgl);
        lxdz=(TextView)findViewById(R.id.lxdz);

    }

    private void event(){

        grzl.setOnClickListener(this);
        wjgl.setOnClickListener(this);
        htgl.setOnClickListener(this);
        lxdz.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==grzl){
            //个人资料

            intent=new Intent(getApplicationContext(),PersonFile.class);
            intent.putExtra("personId",personId);
            startActivity(intent);
        }
        if(v==wjgl){
            //文件管理
            intent=new Intent(getApplicationContext(),PersonCert.class);
            intent.putExtra("personId",personId);
            startActivity(intent);

        }
        if(v==htgl){
            //合同管理
            intent=new Intent(getApplicationContext(),Personcontact.class);
            intent.putExtra("personId",personId);
            startActivity(intent);
        }
        if(v==lxdz){
            //联系地址
            intent=new Intent(getApplicationContext(),PersonAddress.class);
            intent.putExtra("personId",personId);
            startActivity(intent);

        }
    }
}
