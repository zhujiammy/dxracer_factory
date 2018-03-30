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

public class OrderDetail extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TextView stockinorderitem,stockinorderlog;
    private String orderId;
    private Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderdetail);
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
        orderId=intent.getStringExtra("orderId");
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
            //订单明细
            intent=new Intent(this,Orderitems.class);
            intent.putExtra("orderId",orderId);
            startActivity(intent);
        }
        if(v==stockinorderlog){
            //订单日志
            intent=new Intent(this,Orderlog.class);
            intent.putExtra("orderId",orderId);
            startActivity(intent);
        }
    }
}
