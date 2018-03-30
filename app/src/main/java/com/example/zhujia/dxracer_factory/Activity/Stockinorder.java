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

import com.example.zhujia.dxracer_factory.Adapter.OrderitemAdapter;
import com.example.zhujia.dxracer_factory.Adapter.ProductionplanlogAdapter;
import com.example.zhujia.dxracer_factory.Adapter.PurchaseplanitemAdapter;
import com.example.zhujia.dxracer_factory.Adapter.SchedulingsheetAdapter;
import com.example.zhujia.dxracer_factory.Adapter.StockinorderAdapter;
import com.example.zhujia.dxracer_factory.Adapter.StockinorderAdapters;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhujia on 2017/11/22.
 *
 *
 */

public class Stockinorder extends AppCompatActivity implements View.OnClickListener,OnRefreshListener,OnLoadMoreListener {


    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private String business_id,departmentId;
    Map<String,String> params;
    private int pageindex=1;
    private int istouch=0;
    private LinearLayout seach_view;
    private ViewGroup.LayoutParams lp;
    boolean hasMoreData;
    List<Dict> dicts = new ArrayList<Dict>();
    private Handler mHandler;
    private List<BatchwareData> mListData=new ArrayList<>();
    JSONObject reslutJSONObject;
    JSONArray contentjsonarry;
    JSONObject contentjsonobject;
    private StockinorderAdapter adapter;
    private  int total;
    private Intent intent;
    private String SupplierNo;
    private SuperRefreshRecyclerView recyclerView;
    private TextView text;
    private String type;
    private EditText stockInOrderNo,orderNo,purchaseOrderNo;
    private TextView dateTimeRange2;
    private Spinner supplierNo;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stockinorder);
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
        loaddata1();
        loaddata();//加载列表数据
        adapter=new StockinorderAdapter(this,getData());

    }


    private void initUI(){

        //初始化
        recyclerView= (SuperRefreshRecyclerView)findViewById(R.id.recyclerview);
        recyclerView.init(this,this);
        recyclerView.setRefreshEnabled(true);
        recyclerView.setLoadingMoreEnable(true);
        recyclerView.setHasFixedSize(true);
        text=(TextView) findViewById(R.id.text1);
        supplierNo=(Spinner)findViewById(R.id.supplierNo);
        supplierNo.setOnItemSelectedListener(listener);
        stockInOrderNo=(EditText) findViewById(R.id.stockInOrderNo);
        orderNo=(EditText) findViewById(R.id.orderNo);
        purchaseOrderNo=(EditText) findViewById(R.id.purchaseOrderNo);
        dateTimeRange2=(TextView)findViewById(R.id.dateTimeRange2);
        seach_view=(LinearLayout)findViewById(R.id.seach_view);
        if(type.equals("1")){
            text.setText("耗材待收货");
        }
        if(type.equals("2")){
            text.setText("部件待收货");
        }
        if(type.equals("3")){
            text.setText("现货待收货");
        }
        if(type.equals("4")){
            text.setText("售后已接单");
        }


    }

    private void loaddata1(){
        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("departmentId",departmentId);
        HttpUtility.doPostAsyn("/servlet/stock/stockinorder", params, new IHttpCallBack() {
            @Override
            public void onRequestComplete(String result, Handler handler, String errcode) {
                if ((null == result) || (result.equals(""))) {

                    // 网络连接异常

                    mHandler.sendEmptyMessage(9);

                }else {

                    Message msg=Message.obtain(
                            handler,2,result
                    );
                    mHandler.sendMessage(msg);

                }
            }
        },mHandler,getApplicationContext());
    }


    private void loaddata(){

        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("sorttype","desc");
        params.put("sort","stockInOrderNo");
        params.put("orderNo",orderNo.getText().toString());
        params.put("stockInOrderNo",stockInOrderNo.getText().toString());
        params.put("purchaseOrderNo",purchaseOrderNo.getText().toString());
        params.put("dateTimeRange2",dateTimeRange2.getText().toString());
        params.put("planProductionBeginTime","");
        params.put("planProductionEndTime","");

        params.put("page", String.valueOf(pageindex));
        if(SupplierNo==null||SupplierNo.equals("请选择")){
            params.put("purchaseOrder.supplierNo","");
        }else {
            params.put("purchaseOrder.supplierNo",SupplierNo);
        }
        if(type.equals("1")){
            params.put("purchaseOrder.manual","Y");
            params.put("stockInStatus","created");
            params.put("purchaseOrder.purchaseOrderType","PIA");
        }
        if(type.equals("2")){
            params.put("purchaseOrder.manual","N");
            params.put("stockInStatus","created");
            params.put("purchaseOrder.purchaseOrderType","PIA");
        }
        if(type.equals("3")){
            params.put("purchaseOrder.purchaseOrderType","PIC");
            params.put("stockInStatus","created");
        }
        if(type.equals("4")){
            params.put("purchaseOrder.purchaseOrderType","PID");
            params.put("stockInStatus","working");
        }

        HttpUtility.doPostAsyn("/servlet/stock/stockinorder/list", params, new IHttpCallBack() {
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
        },mHandler,Stockinorder.this);
    }


    //加载更多
    private void initItemMoreData() {
        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("page", String.valueOf(pageindex+1));
        params.put("sorttype","desc");
        params.put("sort","createTime");
        params.put("orderNo",orderNo.getText().toString());
        params.put("stockInOrderNo",stockInOrderNo.getText().toString());
        params.put("purchaseOrderNo",purchaseOrderNo.getText().toString());
        params.put("dateTimeRange2",dateTimeRange2.getText().toString());
        params.put("planProductionBeginTime","");
        params.put("planProductionEndTime","");
        if(SupplierNo==null||SupplierNo.equals("请选择")){
            params.put("purchaseOrder.supplierNo","");
        }else {
            params.put("purchaseOrder.supplierNo",SupplierNo);
        }
        params.put("purchaseOrder.purchaseOrderType","PIA");
        if(type.equals("1")){
            params.put("purchaseOrder.manual","Y");
            params.put("stockInStatus","created");
        }
        if(type.equals("2")){
            params.put("purchaseOrder.manual","N");
            params.put("stockInStatus","created");
        }
        if(type.equals("3")){
            params.put("purchaseOrder.purchaseOrderType","PIC");
            params.put("stockInStatus","created");
        }
        HttpUtility.doPostAsyn("/servlet/stock/stockinorder/list", params, new IHttpCallBack() {
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
                        if(!contentjsonobject.isNull("resultList")){
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
                        }else {
                            recyclerView.setLoadingMore(false);
                        }


                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        },null,Stockinorder.this);
    }

    private void fillDataToList(JSONObject data) throws JSONException {

        contentjsonobject=data.getJSONObject("rows");
        if(!contentjsonobject.isNull("resultList")){
            contentjsonarry=contentjsonobject.getJSONArray("resultList");
            BatchwareData rechargData=null;
            for(int i=0;i<contentjsonarry.length();i++){
                rechargData=new BatchwareData();
                JSONObject object=contentjsonarry.getJSONObject(i);
                rechargData.setId(object.getString("id"));
                rechargData.setBusinessId(object.getString("businessId"));
                rechargData.setStockInOrderNo(object.getString("stockInOrderNo"));
                if(!object.isNull("orderNo")){
                    rechargData.setOrderNo(object.getString("orderNo"));
                }
                rechargData.setPurchaseOrderId(object.getString("purchaseOrderId"));
                rechargData.setPurchaseOrderNo(object.getString("purchaseOrderNo"));
                JSONObject purchaseOrder=object.getJSONObject("purchaseOrder");
                if(!purchaseOrder.isNull("plateNumber")){
                    rechargData.setPlateNumber(purchaseOrder.getString("plateNumber"));
                }
                if(!purchaseOrder.isNull("sendTime")){
                    rechargData.setSendTime(purchaseOrder.getString("sendTime"));
                }
                if(!purchaseOrder.isNull("planProductionDate")){
                    rechargData.setPlanProductionDate(purchaseOrder.getString("planProductionDate"));
                }
                rechargData.setStockInStatus(object.getString("stockInStatus"));



                mListData.add(rechargData);
            }
        }


    }

    Spinner.OnItemSelectedListener listener=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            SupplierNo=((Dict)supplierNo.getSelectedItem()).getText();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

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
                        case 2:
                            //返回item类型数据
                            reslutJSONObject=new JSONObject(msg.obj.toString());
                            JSONObject rows=reslutJSONObject.getJSONObject("rows");
                            JSONArray supplierList=rows.getJSONArray("supplierList");
                            dicts.add(new Dict(1,"请选择"));
                            for(int i=0;i<supplierList.length();i++){
                                String suppno=supplierList.get(i).toString();
                                dicts.add(new Dict(1,suppno));
                                ArrayAdapter<Dict> arrAdapterpay3 = new ArrayAdapter<Dict>(getApplicationContext(), R.layout.simple_spinner_item,dicts);
                                //设置样式
                                arrAdapterpay3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                supplierNo.setAdapter(arrAdapterpay3);
                            }

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
                            Toast.makeText(Stockinorder.this, "网络异常", Toast.LENGTH_SHORT).show();
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
