package com.example.zhujia.dxracer_factory.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhujia.dxracer_factory.Adapter.AllmaterialAdapter;
import com.example.zhujia.dxracer_factory.Adapter.BillentryAdapter;
import com.example.zhujia.dxracer_factory.Adapter.FormaterialAdapter;
import com.example.zhujia.dxracer_factory.Adapter.InventoryAdapter;
import com.example.zhujia.dxracer_factory.Adapter.OrderitemAdapter;
import com.example.zhujia.dxracer_factory.Adapter.OutboundorderAdapter;
import com.example.zhujia.dxracer_factory.Adapter.PendingshipmentAdapter;
import com.example.zhujia.dxracer_factory.Adapter.ProductionplanlogAdapter;
import com.example.zhujia.dxracer_factory.Adapter.PurchaseplanitemAdapter;
import com.example.zhujia.dxracer_factory.Adapter.SchedulingsheetAdapter;
import com.example.zhujia.dxracer_factory.Adapter.StockinorderAdapter;
import com.example.zhujia.dxracer_factory.Adapter.StockinordersAdapter;
import com.example.zhujia.dxracer_factory.Adapter.TobewarehousesAdapter;
import com.example.zhujia.dxracer_factory.Data.AllData;
import com.example.zhujia.dxracer_factory.Data.BatchwareData;
import com.example.zhujia.dxracer_factory.Data.Dict;
import com.example.zhujia.dxracer_factory.Data.MData;
import com.example.zhujia.dxracer_factory.Data.ScheduData;
import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtility;
import com.example.zhujia.dxracer_factory.Tools.Net.IHttpCallBack;
import com.example.zhujia.dxracer_factory.Tools.OnLoadMoreListener;
import com.example.zhujia.dxracer_factory.Tools.OnRefreshListener;
import com.example.zhujia.dxracer_factory.Tools.SuperRefreshRecyclerView;
import com.example.zhujia.dxracer_factory.Tools.insertComma;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhujia on 2017/11/22.
 *成品待入库
 *
 */

public class Tobewarehouses extends AppCompatActivity implements View.OnClickListener,OnRefreshListener,OnLoadMoreListener {


    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private String business_id,departmentPersonSession;
    Map<String,String> params;
    private int pageindex=1;
    private int istouch=0;
    boolean hasMoreData;
    private Handler mHandler;
    private LinearLayout seach_view;
    private ViewGroup.LayoutParams lp;
    private List<MData> mListData=new ArrayList<>();
    private EditText partName,partNo;
    JSONObject reslutJSONObject;
    JSONArray contentjsonarry;
    JSONObject contentjsonobject;
    private TobewarehousesAdapter adapter;
    private  int total;
    private Intent intent;
    private TextView text;
    private String url;
    private SuperRefreshRecyclerView recyclerView;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tobewarehouses);
        intent=getIntent();
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
        departmentPersonSession=sharedPreferences.getString("departmentPersonId","");
        initUI();
        loaddata();//加载列表数据
        adapter=new TobewarehousesAdapter(Tobewarehouses.this,getData());

    }


    private void initUI(){

        //初始化
        recyclerView= (SuperRefreshRecyclerView)findViewById(R.id.recyclerview);
        recyclerView.init(this,this);
        recyclerView.setRefreshEnabled(true);
        recyclerView.setLoadingMoreEnable(true);
        recyclerView.setHasFixedSize(true);
        seach_view=(LinearLayout)findViewById(R.id.seach_view);
        partName=(EditText)findViewById(R.id.partName);
        partNo=(EditText)findViewById(R.id.partNo);
        text=(TextView)findViewById(R.id.text);
    }


    private void loaddata(){

        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("page", String.valueOf(pageindex));
        params.put("sorttype","asc");
        params.put("sort","storageNo");
        //params.put("departmentPersonSessionId",departmentPersonSession);

        HttpUtility.doPostAsyn("servlet/production/productionorderstorage/new/list", params, new IHttpCallBack() {
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
        },mHandler,Tobewarehouses.this);
    }


    //加载更多
    private void initItemMoreData() {
        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("sorttype","asc");
        params.put("sort","storageNo");
        params.put("page", String.valueOf(pageindex+1));
        HttpUtility.doPostAsyn("servlet/production/productionorderstorage/new/list", params, new IHttpCallBack() {
            @Override
            public void onRequestComplete(String result, Handler handler, String errcode) {
                if ((null == result) || (result.equals(""))) {
                    // 网络连接异常

                    mHandler.sendEmptyMessage(9);

                }else {
                    JSONObject resulutJsonobj;

                    try
                    {

                        contentjsonobject=new JSONObject(result);
                        contentjsonarry=contentjsonobject.getJSONArray("rows");
                        if(contentjsonarry.length()<0){
                            hasMoreData=false;
                        }
                        pageindex=pageindex+1;
                        hasMoreData=true;
                        fillDataToList(contentjsonobject);
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
        },null,Tobewarehouses.this);
    }

    private void fillDataToList(JSONObject data) throws JSONException {

        contentjsonarry=data.getJSONArray("rows");
        MData rechargData=null;
        for(int i=0;i<contentjsonarry.length();i++){
            rechargData=new MData();
            JSONObject object=contentjsonarry.getJSONObject(i);
            rechargData.setId(object.getInt("id"));
            rechargData.setStorageNo(object.getString("storageNo"));
            rechargData.setFcno(object.getString("fcno"));
            rechargData.setQuantity(object.getString("quantity"));
            rechargData.setProductionOrderNo(object.getString("productionOrderNo"));
            rechargData.setPickOrderNo(object.getString("pickOrderNo"));
            rechargData.setOrderNo(object.getString("orderNo"));
            mListData.add(rechargData);
        }



    }


    @SuppressLint("HandlerLeak")
    private List<MData>getData(){
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                try{
                    switch (msg.what) {

                        case 0:
                            //返回item类型数据
                            contentjsonobject=new JSONObject(msg.obj.toString());
                            mListData.clear();
                            fillDataToList(contentjsonobject);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            recyclerView.showData();
                            recyclerView.setRefreshing(false);
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
                            Toast.makeText(Tobewarehouses.this, "网络异常", Toast.LENGTH_SHORT).show();
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
                adapter.notifyDataSetChanged();
                partName.setText("");
                partNo.setText("");
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
