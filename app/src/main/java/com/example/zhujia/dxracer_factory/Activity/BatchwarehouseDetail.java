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

public class BatchwarehouseDetail extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TextView stockinorderitem,stockinorderlog;
    private String stockInOrderId;
    private Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.batchwarehousedetail);
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
        stockInOrderId=intent.getStringExtra("stockInOrderId");
        initUI();
        event();

    }


    private void initUI(){

        stockinorderitem=(TextView)findViewById(R.id.stockinorderitem);
        stockinorderlog=(TextView)findViewById(R.id.stockinorderlog);

    }

    private void event(){

        stockinorderitem.setOnClickListener(this);
        stockinorderlog.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==stockinorderitem){
            //入库单详情
            intent=new Intent(this,Stockinorderitem.class);
            intent.putExtra("stockInOrderId",stockInOrderId);
            startActivity(intent);
        }
        if(v==stockinorderlog){
            //操作日志
            intent=new Intent(this,Stockinorderlog.class);
            intent.putExtra("stockInOrderId",stockInOrderId);
            startActivity(intent);
        }
    }
}
