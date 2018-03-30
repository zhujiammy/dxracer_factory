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
import com.example.zhujia.dxracer_factory.Data.AllData;
import com.example.zhujia.dxracer_factory.Data.BatchwareData;
import com.example.zhujia.dxracer_factory.Data.Dict;
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
 *耗材库存量
 *
 */

public class Inventory extends AppCompatActivity implements View.OnClickListener,OnRefreshListener,OnLoadMoreListener {


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
    private List<BatchwareData> mListData=new ArrayList<>();
    private EditText partName,partNo;
    JSONObject reslutJSONObject;
    JSONArray contentjsonarry;
    JSONObject contentjsonobject;
    private InventoryAdapter adapter;
    private  int total;
    private Intent intent;
    private TextView text;
    private String url;
    private SuperRefreshRecyclerView recyclerView;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory);
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
        adapter=new InventoryAdapter(Inventory.this,getData());

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
        if(intent.getStringExtra("type").equals("1")){
            text.setText("现货库存量");
            url="/servlet/warehouse/inventory/trade/list";
        }
        if(intent.getStringExtra("type").equals("2")){
            text.setText("耗材库存量");
            url="/servlet/warehouse/inventory/batchorder/list";
        }
        if(intent.getStringExtra("type").equals("3")){
            text.setText("售后库存量");
            url="/servlet/warehouse/inventory/aftersale/list";
        }

    }


    private void loaddata(){

        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("partName",partName.getText().toString());
        params.put("partNo",partNo.getText().toString());
        //params.put("departmentPersonSessionId",departmentPersonSession);

        HttpUtility.doPostAsyn(url, params, new IHttpCallBack() {
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
        },mHandler,Inventory.this);
    }


    //加载更多
    private void initItemMoreData() {
        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("partName",partName.getText().toString());
        params.put("partNo",partNo.getText().toString());
        HttpUtility.doPostAsyn(url, params, new IHttpCallBack() {
            @Override
            public void onRequestComplete(String result, Handler handler, String errcode) {
                if ((null == result) || (result.equals(""))) {
                    // 网络连接异常

                    mHandler.sendEmptyMessage(9);

                }else {
                    JSONObject resulutJsonobj;

                    try
                    {

                        contentjsonarry=new JSONArray(result);
                        if(contentjsonarry.length()<0){
                            hasMoreData=false;
                        }
                        pageindex=pageindex+1;
                        hasMoreData=true;
                        fillDataToList(contentjsonarry);
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
        },null,Inventory.this);
    }

    private void fillDataToList(JSONArray data) throws JSONException {

        BatchwareData rechargData=null;
        for(int i=0;i<data.length();i++){
            rechargData=new BatchwareData();
            JSONObject object=data.getJSONObject(i);
            if(!object.isNull("id")){
                rechargData.setId(object.getString("id"));
            }
            if(!object.isNull("picture")){
                rechargData.setPicture(object.getString("picture"));
            }
            if(!object.isNull("part_no")){
                rechargData.setPart_no(object.getString("part_no"));
            }
            if(!object.isNull("sku_no")){
                rechargData.setPart_no(object.getString("sku_no"));
            }
            if(!object.isNull("img_url")){
                rechargData.setPicture(object.getString("img_url"));
            }

            if(!object.isNull("name")){
                rechargData.setName(object.getString("name"));
            }
            if(!object.isNull("quantity")){
                rechargData.setQuantity(object.getString("quantity"));
            }
            if(!object.isNull("locked_quantity")){
                rechargData.setLocked_quantity(object.getString("locked_quantity"));
            }
            if(!object.isNull("usable_quantity")){
                rechargData.setUsable_quantity(object.getString("usable_quantity"));
            }




            if(!object.isNull("min_storage_safe")){
                rechargData.setMin_storage_safe(object.getString("min_storage_safe"));
            }

            if(!object.isNull("max_storage_safe")){
                rechargData.setMax_storage_safe(object.getString("max_storage_safe"));

            }
            mListData.add(rechargData);
        }



    }


    @SuppressLint("HandlerLeak")
    private List<BatchwareData>getData(){
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                try{
                    switch (msg.what) {

                        case 0:
                            //返回item类型数据
                            contentjsonarry=new JSONArray(msg.obj.toString());
                            mListData.clear();
                            fillDataToList(contentjsonarry);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            recyclerView.showData();
                            recyclerView.setRefreshing(false);
                            break;
                        case 1:// 解析返回mode类型数据
                            reslutJSONObject=new JSONObject(msg.obj.toString());
                            total=reslutJSONObject.getInt("total");
                            if(total>0){
                                fillDataToList(contentjsonarry);

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
                                fillDataToList(contentjsonarry);
                            }

                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            recyclerView.showData();
                            recyclerView.setRefreshing(false);
                            break;
                        default:
                            Toast.makeText(Inventory.this, "网络异常", Toast.LENGTH_SHORT).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.seach_btn) {
            loaddata();
        }
        if(id==R.id.open_seach){
            //打开搜索界面
            if(istouch==0){
                lp= seach_view.getLayoutParams();
                lp.height=((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ViewGroup.LayoutParams.WRAP_CONTENT, getResources().getDisplayMetrics()));
                seach_view.setLayoutParams(lp);
                item.setIcon(R.drawable.up);
                istouch=1;

            }else {
                lp= seach_view.getLayoutParams();
                lp.height=((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics()));
                seach_view.setLayoutParams(lp);
                item.setIcon(R.drawable.down);
                istouch=0;

            }


        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mListData.clear();
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
