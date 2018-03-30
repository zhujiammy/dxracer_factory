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

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TextView orderitem,purchaseplanitem,productionplanlog;
    private String orderId,orderNo;
    private Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        orderNo=intent.getStringExtra("orderNo");
        initUI();
        event();

    }


    private void initUI(){
        orderitem=(TextView)findViewById(R.id.orderitem);
        purchaseplanitem=(TextView)findViewById(R.id.purchaseplanitem);
        productionplanlog=(TextView)findViewById(R.id.productionplanlog);

    }

    private void event(){
        orderitem.setOnClickListener(this);
        purchaseplanitem.setOnClickListener(this);
        productionplanlog.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==orderitem){
            //订单明细
            intent=new Intent(this,Orderitem.class);
            intent.putExtra("orderId",orderId);

            startActivity(intent);

        }
        if(v==purchaseplanitem){
            //部件列表
            intent=new Intent(this,Purchaseplanitem.class);
            intent.putExtra("orderId",orderId);
            intent.putExtra("type","1");
            startActivity(intent);
        }
        if(v==productionplanlog){
            //操作日志
            intent=new Intent(this,Productionplanlog.class);
            intent.putExtra("orderNo",orderNo);
            intent.putExtra("type","1");
            startActivity(intent);
        }
    }
}
