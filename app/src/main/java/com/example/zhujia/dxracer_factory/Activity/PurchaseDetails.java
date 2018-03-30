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
 * 详情
 */

public class PurchaseDetails extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TextView purchaseorderitem,purchaseorderlog;
    private String purchaseOrderId;
    private Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchasedetails);
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
        purchaseOrderId=intent.getStringExtra("purchaseOrderId");
        initUI();
        event();

    }


    private void initUI(){
        purchaseorderitem=(TextView)findViewById(R.id.purchaseorderitem);
        purchaseorderlog=(TextView)findViewById(R.id.purchaseorderlog);

    }

    private void event(){
        purchaseorderitem.setOnClickListener(this);
        purchaseorderlog.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==purchaseorderitem){
            //采购详情
            intent=new Intent(this,Purchaseorderitem.class);
            intent.putExtra("purchaseOrderId",purchaseOrderId);

            startActivity(intent);

        }
        if(v==purchaseorderlog){
            //操作日志
            intent=new Intent(this,Productionplanlog.class);
            intent.putExtra("purchaseOrderId",purchaseOrderId);
            intent.putExtra("type","4");
            startActivity(intent);
        }
    }
}
