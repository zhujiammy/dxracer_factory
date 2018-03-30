package com.example.zhujia.dxracer_factory.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.zhujia.dxracer_factory.R;

import org.w3c.dom.Text;

/**
 * Created by zhujia on 2017/11/22.
 *
 * 详情页面
 */

public class BatchorderDetail extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TextView orderitem,purchaseplanitem,productionplanlog,orderlog;
    private String orderId,orderno;
    private Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.batchorderdetail);
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
        orderno=intent.getStringExtra("orderno");
        initUI();
        event();

    }


    private void initUI(){

        orderitem=(TextView)findViewById(R.id.orderitem);
        purchaseplanitem=(TextView)findViewById(R.id.purchaseplanitem);
        productionplanlog=(TextView)findViewById(R.id.productionplanlog);
        orderlog=(TextView)findViewById(R.id.orderlog);

    }

    private void event(){

        orderitem.setOnClickListener(this);
        purchaseplanitem.setOnClickListener(this);
        productionplanlog.setOnClickListener(this);
        orderlog.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==orderitem){
            //订单明细
            intent=new Intent(this,Orderitem.class);
            intent.putExtra("orderId",orderId);
            Log.e("TAG", "loaddata: "+orderId );
            startActivity(intent);
        }
        if(v==purchaseplanitem){
            //部件清单
            intent=new Intent(this,Purchaseplanitem.class);
            intent.putExtra("orderId",orderId);
            Log.e("TAG", "loaddata: "+orderId );
            intent.putExtra("type","0");
            startActivity(intent);
        }
        if(v==productionplanlog){
            //排单日志
            intent=new Intent(this,Productionplanlog.class);
            intent.putExtra("orderNo",orderno);
            Log.e("TAG", "loaddata: "+orderId );
            intent.putExtra("type","0");
            startActivity(intent);
        }
        if(v==orderlog){
            //订单日志
            intent=new Intent(this,Orderlog.class);
            intent.putExtra("orderId",orderId);
            Log.e("TAG", "loaddata: "+orderId );
            startActivity(intent);
        }
    }
}
