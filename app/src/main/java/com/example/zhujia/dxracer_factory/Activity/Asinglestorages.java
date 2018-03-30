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

import com.example.zhujia.dxracer_factory.Adapter.AsinglestoragesAdapter;
import com.example.zhujia.dxracer_factory.Adapter.OrderitemAdapter;
import com.example.zhujia.dxracer_factory.Adapter.ProductionplanlogAdapter;
import com.example.zhujia.dxracer_factory.Adapter.PurchaseplanitemAdapter;
import com.example.zhujia.dxracer_factory.Adapter.SchedulingsheetAdapter;
import com.example.zhujia.dxracer_factory.Adapter.StockinorderAdapter;
import com.example.zhujia.dxracer_factory.Adapter.StockinorderAdapters;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhujia on 2017/11/22.
 *
 *
 */

public class Asinglestorages extends AppCompatActivity implements View.OnClickListener,OnRefreshListener,OnLoadMoreListener {


    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private String business_id,departmentId;
    Map<String,String> params;
    private int pageindex=1;
    private int istouch=0;
    private LinearLayout seach_view;
    private ViewGroup.LayoutParams lp;
    boolean hasMoreData;
    List<AllData> dicts = new ArrayList<AllData>();
    private Handler mHandler;
    private List<MData> mListData=new ArrayList<>();
    JSONObject reslutJSONObject;
    JSONArray contentjsonarry;
    JSONObject contentjsonobject;
    private AsinglestoragesAdapter adapter;
    private  int total;
    private Intent intent;
    private SuperRefreshRecyclerView recyclerView;
    private TextView text;
    private String type;
    private EditText storageNo,fcno,productionOrderNo,pickOrderNo,orderNo;
    private TextView dateTimeRange2;
    private Spinner status;
    private String sta;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asinglestorages);
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
        departmentId=sharedPreferences.getString("departmentId","");
        type=intent.getStringExtra("type");
        initUI();
        loaddata();//加载列表数据
        adapter=new AsinglestoragesAdapter(Asinglestorages.this,getData());

    }


    private void initUI(){

        //初始化
        recyclerView= (SuperRefreshRecyclerView)findViewById(R.id.recyclerview);
        recyclerView.init(this,this);
        recyclerView.setRefreshEnabled(true);
        recyclerView.setLoadingMoreEnable(true);
        recyclerView.setHasFixedSize(true);
        text=(TextView) findViewById(R.id.text1);
        status=(Spinner)findViewById(R.id.status);
        status.setOnItemSelectedListener(listener);
        storageNo=(EditText) findViewById(R.id.storageNo);
        orderNo=(EditText) findViewById(R.id.orderNo);
        productionOrderNo=(EditText)findViewById(R.id.productionOrderNo);
        pickOrderNo=(EditText)findViewById(R.id.pickOrderNo);
        fcno=(EditText) findViewById(R.id.fcno);
        dateTimeRange2=(TextView)findViewById(R.id.dateTimeRange2);
        seach_view=(LinearLayout)findViewById(R.id.seach_view);
        dicts.add(new AllData("1","全部"));
        dicts.add(new AllData("create","待入库"));
        dicts.add(new AllData("working","已接单"));
        dicts.add(new AllData("finished","已入库"));

        ArrayAdapter<AllData> arrAdapterpay3 = new ArrayAdapter<AllData>(getApplicationContext(), R.layout.simple_spinner_item,dicts);
        //设置样式
        arrAdapterpay3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status.setAdapter(arrAdapterpay3);



    }




    private void loaddata(){


       /* sorttype:desc
        sort:storageNo
        storageNo:1
        fcno:2
        productionOrderNo:3
        pickOrderNo:4
        orderNo:5
        status:create
        dateTimeRange2:2017-12-13 ~ 2017-12-13
        finishedStartDate:2017-12-13 00:00:00
        finishedEndDate:2017-12-13 23:59:59*/

        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("page", String.valueOf(pageindex));
        params.put("sorttype","desc");
        params.put("sort","storageNo");
        params.put("storageNo",storageNo.getText().toString());
        params.put("fcno",fcno.getText().toString());
        params.put("productionOrderNo",productionOrderNo.getText().toString());
        params.put("pickOrderNo",pickOrderNo.getText().toString());
        params.put("orderNo",orderNo.getText().toString());
        params.put("dateTimeRange2",dateTimeRange2.getText().toString());
        params.put("finishedStartDate","");
        params.put("finishedEndDate","");
        if(sta==null||sta.equals("1")){
            params.put("status","");
        }else {
            params.put("status",sta);
        }

        HttpUtility.doPostAsyn("/servlet/production/productionorderstorage/list", params, new IHttpCallBack() {
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
        },mHandler,Asinglestorages.this);
    }


    //加载更多
    private void initItemMoreData() {
        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("page", String.valueOf(pageindex+1));
        params.put("sorttype","desc");
        params.put("sort","storageNo");
        params.put("storageNo",storageNo.getText().toString());
        params.put("fcno",fcno.getText().toString());
        params.put("productionOrderNo",productionOrderNo.getText().toString());
        params.put("pickOrderNo",pickOrderNo.getText().toString());
        params.put("orderNo",orderNo.getText().toString());
        params.put("dateTimeRange2",dateTimeRange2.getText().toString());
        params.put("finishedStartDate","");
        params.put("finishedEndDate","");
        if(sta==null||sta.equals("1")){
            params.put("status","");
        }else {
            params.put("status",sta);
        }

        HttpUtility.doPostAsyn("/servlet/production/productionorderstorage/list", params, new IHttpCallBack() {
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
                        if(!resulutJsonobj.isNull("rows")){
                            contentjsonarry=resulutJsonobj.getJSONArray("rows");
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
                        }else {
                            recyclerView.setLoadingMore(false);
                        }


                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        },null,Asinglestorages.this);
    }

    private void fillDataToList(JSONObject data) throws JSONException {

        if(!data.isNull("rows")){
            contentjsonarry=data.getJSONArray("rows");
            MData rechargData=null;
            for(int i=0;i<contentjsonarry.length();i++){
                rechargData=new MData();
                JSONObject object=contentjsonarry.getJSONObject(i);
                rechargData.setId(object.getInt("id"));
                rechargData.setBusinessId(object.getInt("businessId"));
                rechargData.setStorageNo(object.getString("storageNo"));
                rechargData.setFcno(object.getString("fcno"));
                rechargData.setQuantity(object.getString("quantity"));
                rechargData.setProductionOrderNo(object.getString("productionOrderNo"));
                rechargData.setPickOrderNo(object.getString("pickOrderNo"));
                rechargData.setOrderNo(object.getString("orderNo"));
                rechargData.setStatus(object.getString("status"));
                if(!object.isNull("grabPersonName")){
                    rechargData.setGrabPersonName(object.getString("grabPersonName"));
                }else {
                    rechargData.setGrabPersonName("-");
                }

                if(!object.isNull("finishTime")){
                    rechargData.setFinishTime(object.getString("finishTime"));
                }else {
                    rechargData.setFinishTime("-");
                }
                if(!object.isNull("forkliftPersonName")){
                    rechargData.setForkliftPersonName(object.getString("forkliftPersonName"));
                }else {
                    rechargData.setForkliftPersonName("-");
                }


                mListData.add(rechargData);
            }
        }


    }

    Spinner.OnItemSelectedListener listener=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            sta=((AllData)status.getSelectedItem()).getStr();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

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
                            reslutJSONObject=new JSONObject(msg.obj.toString());
                            mListData.clear();
                            fillDataToList(reslutJSONObject);
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
                            Toast.makeText(Asinglestorages.this, "网络异常", Toast.LENGTH_SHORT).show();
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
                adapter.notifyDataSetChanged();
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
