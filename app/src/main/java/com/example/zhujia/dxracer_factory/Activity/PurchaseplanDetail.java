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

public class PurchaseplanDetail extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TextView purchaseplanitem,purchaseorder,purchaseplanlog;
    private String purchasePlanId;
    private Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchasepladetail);
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
        purchasePlanId=intent.getStringExtra("purchasePlanId");
        initUI();
        event();

    }


    private void initUI(){

        purchaseplanitem=(TextView)findViewById(R.id.purchaseplanitem);
        purchaseorder=(TextView)findViewById(R.id.purchaseorder);
        purchaseplanlog=(TextView)findViewById(R.id.purchaseplanlog);

    }

    private void event(){

        purchaseplanitem.setOnClickListener(this);
        purchaseorder.setOnClickListener(this);
        purchaseplanlog.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==purchaseplanitem){
            //部件清单
            intent=new Intent(this,Purchaseplanitem.class);
            intent.putExtra("purchasePlanId",purchasePlanId);
            intent.putExtra("type","2");
            startActivity(intent);
        }
        if(v==purchaseorder){
            //采购单清单
            intent=new Intent(this,Purchaseorder.class);
            intent.putExtra("purchasePlanId",purchasePlanId);
            startActivity(intent);
        }
        if(v==purchaseplanlog){
            //操作日志
            intent=new Intent(this,Productionplanlog.class);
            intent.putExtra("purchasePlanId",purchasePlanId);
            intent.putExtra("type","3");
            startActivity(intent);
        }
    }
}
