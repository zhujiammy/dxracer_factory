package com.example.zhujia.dxracer_factory.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhujia.dxracer_factory.Adapter.OrderitemAdapter;
import com.example.zhujia.dxracer_factory.Adapter.SchedulingsheetAdapter;
import com.example.zhujia.dxracer_factory.Data.ScheduData;
import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtility;
import com.example.zhujia.dxracer_factory.Tools.Net.IHttpCallBack;
import com.example.zhujia.dxracer_factory.Tools.OnLoadMoreListener;
import com.example.zhujia.dxracer_factory.Tools.OnRefreshListener;
import com.example.zhujia.dxracer_factory.Tools.SuperRefreshRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhujia on 2017/11/22.
 *
 * 订单明细
 */

public class Orderitem extends AppCompatActivity implements View.OnClickListener,OnRefreshListener,OnLoadMoreListener {


    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private String business_id,supplierId;
    Map<String,String> params;
    private int pageindex=1;
    private int istouch=0;
    boolean hasMoreData;
    private Handler mHandler;
    private List<ScheduData> mListData=new ArrayList<>();
    JSONObject reslutJSONObject;
    JSONArray contentjsonarry;
    JSONObject contentjsonobject;
    private OrderitemAdapter adapter;
    private  int total;
    private Intent intent;
    private SuperRefreshRecyclerView recyclerView;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderitem);
        intent=getIntent();
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
        sharedPreferences =getSharedPreferences("Session", Context.MODE_APPEND);
        business_id=sharedPreferences.getString("business_id","");
        initUI();
        loaddata();//加载列表数据
        adapter=new OrderitemAdapter(this,getData());

    }


    private void initUI(){

        //初始化
        recyclerView= (SuperRefreshRecyclerView)findViewById(R.id.recyclerview);
        recyclerView.init(this,this);
        recyclerView.setRefreshEnabled(true);
        recyclerView.setLoadingMoreEnable(true);
        recyclerView.setHasFixedSize(true);
    }

    private void loaddata(){

        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("page", String.valueOf(pageindex));
        params.put("orderId",intent.getStringExtra("orderId"));
        Log.e("TAG", "loaddata: "+intent.getStringExtra("orderid") );
        HttpUtility.doPostAsyn("/servlet/order/orderitem/item/list", params, new IHttpCallBack() {
            @Override
            public void onRequestComplete(String result, Handler handler, String errcode) {
                if ((null == result) || (result.equals(""))) {
                    // 网络连接异常
                    mHandler.sendEmptyMessage(9);

                }else {


                    Message msg=Message.obtain(
                            handler,0,result
                    );
                    mHandler.sendMessage(msg);

                }
            }
        },mHandler,Orderitem.this);
    }


    //加载更多
    private void initItemMoreData() {
        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("page", String.valueOf(pageindex+1));
        params.put("orderId",intent.getStringExtra("orderid"));
        HttpUtility.doPostAsyn("/servlet/order/orderitem/item/list", params, new IHttpCallBack() {
            @Override
            public void onRequestComplete(String result, Handler handler, String errcode) {
                if ((null == result) || (result.equals(""))) {
                    // 网络连接异常

                    mHandler.sendEmptyMessage(9);

                }else {
                    JSONObject resulutJsonobj;

                    try
                    {

                        resulutJsonobj=new JSONObject(result);
                        contentjsonarry=contentjsonobject.getJSONArray("resultList");
                        if(contentjsonarry.length()<0){
                            hasMoreData=false;
                        }
                        pageindex=pageindex+1;
                        hasMoreData=true;
                        fillDataToList(resulutJsonobj);
                        if(!hasMoreData){
                            mHandler.sendEmptyMessage(4);
                        }else {
                            mHandler.sendEmptyMessage(3);
                        }


                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        },null,Orderitem.this);
    }

    private void fillDataToList(JSONObject data) throws JSONException {
        contentjsonobject=data.getJSONObject("rows");
        contentjsonarry=contentjsonobject.getJSONArray("resultList");
        ScheduData rechargData=null;
        for(int i=0;i<contentjsonarry.length();i++){
            rechargData=new ScheduData();
            JSONObject object=contentjsonarry.getJSONObject(i);
            rechargData.setProductCode(object.getString("productCode"));
            rechargData.setQuantity(object.getString("quantity"));
            mListData.add(rechargData);
        }

    }

    @SuppressLint("HandlerLeak")
    private List<ScheduData>getData(){
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                try{
                    switch (msg.what) {

                        case 0:
                            //返回item类型数据
                            reslutJSONObject=new JSONObject(msg.obj.toString());
                            fillDataToList(reslutJSONObject);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            recyclerView.showData();
                            recyclerView.setRefreshing(false);
                            recyclerView.setLoadingMore(false);
                            recyclerView.setLoadingMoreEnable(false);
                            break;
                        case 1:// 解析返回mode类型数据
                            reslutJSONObject=new JSONObject(msg.obj.toString());
                            total=reslutJSONObject.getInt("total");
                            if(total>0){
                                fillDataToList(reslutJSONObject);

                            }
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            recyclerView.showData();
                            recyclerView.setRefreshing(false);
                            break;
                        case 3:
                            adapter.notifyDataSetChanged();
                            recyclerView.setLoadingMore(false);
                            break;
                        case 4:
                            adapter.notifyDataSetChanged();
                            break;
                        case 5:
                            //返回item类型数据
                            reslutJSONObject=new JSONObject(msg.obj.toString());
                            mListData.clear();
                            total=reslutJSONObject.getInt("total");
                            if(total>0){
                                fillDataToList(reslutJSONObject);
                            }

                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            recyclerView.showData();
                            recyclerView.setRefreshing(false);
                            break;
                        default:
                            Toast.makeText(Orderitem.this, "网络异常", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };
        return mListData;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mListData.clear();
                pageindex=1;
                loaddata();
            }
        },1000);
    }

    @Override
    public void onLoadMore() {
        initItemMoreData();
    }
}
