package com.example.zhujia.dxracer_factory.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhujia.dxracer_factory.Adapter.SchedulingsheetAdapter;
import com.example.zhujia.dxracer_factory.Adapter.purchaseordersAdapter;
import com.example.zhujia.dxracer_factory.Data.AllData;
import com.example.zhujia.dxracer_factory.Data.Dict;
import com.example.zhujia.dxracer_factory.Data.MData;
import com.example.zhujia.dxracer_factory.Data.ScheduData;
import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtility;
import com.example.zhujia.dxracer_factory.Tools.Net.IHttpCallBack;
import com.example.zhujia.dxracer_factory.Tools.OnLoadMoreListener;
import com.example.zhujia.dxracer_factory.Tools.OnRefreshListener;
import com.example.zhujia.dxracer_factory.Tools.SuperRefreshRecyclerView;

import net.sf.json.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhujia on 2017/11/21.
 *
 * 全部采购单
 */

public class purchaseorder extends Fragment implements View.OnClickListener,OnRefreshListener,OnLoadMoreListener {
    private LinearLayout seach_view;
    private ViewGroup.LayoutParams lp;
    private SharedPreferences sharedPreferences;
    private String business_id,supplierId;
    Map<String,String> params;
    private int pageindex=1;
    private int istouch=0;
    boolean hasMoreData;
    private Handler mHandler;
    private View view;
    List<Dict> dicts = new ArrayList<Dict>();
    List<AllData> dicts1 = new ArrayList<AllData>();
    private List<MData> mListData=new ArrayList<>();
    JSONObject reslutJSONObject;
    JSONArray contentjsonarry;
    JSONObject contentjsonobject;
    private purchaseordersAdapter adapter;
    private  int total;
    private Intent intent;
    private SuperRefreshRecyclerView recyclerView;
    private String username;
    private LinearLayout lin;
    private EditText orderNo,purchaseOrderNo,originalPurchaseOrderNo,purchasePlanNo;
    private Spinner supplierNo,status;
    private String supplierNos,sta;
    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.purchaseorders,container,false);
        setHasOptionsMenu(true);
        intent=getActivity().getIntent();
        sharedPreferences =getActivity().getSharedPreferences("Session", Context.MODE_APPEND);
        business_id=sharedPreferences.getString("business_id","");
        username=sharedPreferences.getString("username","");
        supplierId=sharedPreferences.getString("supplierId","");
        initUI();
        loaddata1();
        loaddata();//加载列表数据

        adapter=new purchaseordersAdapter(getContext(),getData());
        return view;
    }

    private void initUI(){

        //初始化
        seach_view=(LinearLayout)view.findViewById(R.id.seach_view);
        recyclerView= (SuperRefreshRecyclerView)view.findViewById(R.id.recyclerview);
        recyclerView.init(this,this);
        recyclerView.setRefreshEnabled(true);
        recyclerView.setLoadingMoreEnable(true);
        recyclerView.setHasFixedSize(true);
        orderNo=(EditText)view.findViewById(R.id.orderNo);
        purchaseOrderNo=(EditText)view.findViewById(R.id.purchaseOrderNo);
        originalPurchaseOrderNo=(EditText)view.findViewById(R.id.originalPurchaseOrderNo);
        purchasePlanNo=(EditText)view.findViewById(R.id.purchasePlanNo);

        supplierNo=(Spinner)view.findViewById(R.id.supplierNo);
        status=(Spinner)view.findViewById(R.id.status);
        dicts1.add(new AllData("1","请选择"));
        dicts1.add(new AllData("created","待确认"));
        dicts1.add(new AllData("confirmed","已确认"));
        dicts1.add(new AllData("finished","已入库"));
        dicts1.add(new AllData("returned","已退货"));
        dicts1.add(new AllData("canceled","已取消"));
        ArrayAdapter<AllData> arrAdapterpay4 = new ArrayAdapter<AllData>(getContext(), R.layout.simple_spinner_item,dicts1);
        //设置样式
        arrAdapterpay4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status.setAdapter(arrAdapterpay4);
        status.setOnItemSelectedListener(listener1);
        lin=(LinearLayout)view.findViewById(R.id.lin);
        supplierNo.setOnItemSelectedListener(listener);

    }



    private void loaddata1(){
        params=new HashMap<>();
        params.put("businessId",business_id);
        HttpUtility.doPostAsyn("/servlet/purchase/purchaseorder", params, new IHttpCallBack() {
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
        },mHandler,getActivity());
    }
    private void loaddata(){

        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("page", String.valueOf(pageindex));
        params.put("sorttype","desc");
        params.put("sort","createTime");
        if(supplierNos==null||supplierNos.equals("请选择")){
            params.put("supplierNo","");
        }else {
            params.put("supplierNo",supplierNos);
        }
        params.put("purchaseOrderType","");

        params.put("orderNo",orderNo.getText().toString());
        params.put("purchaseOrderNo",purchaseOrderNo.getText().toString());
        params.put("originalPurchaseOrderNo",originalPurchaseOrderNo.getText().toString());
        params.put("purchasePlanNo",purchasePlanNo.getText().toString());

        if(sta==null||sta.equals("1")){
            params.put("status","");
        }else {
            params.put("status",sta);
        }
        HttpUtility.doPostAsyn("/servlet/purchase/purchaseorder/list", params, new IHttpCallBack() {
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
        },mHandler,getActivity());
    }


    //加载更多
    private void initItemMoreData() {
        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("page", String.valueOf(pageindex+1));
        params.put("sorttype","desc");
        params.put("sort","createTime");
        params.put("purchaseOrderType","");
        if(supplierNos==null||supplierNos.equals("请选择")){
            params.put("supplierNo","");
        }else {
            params.put("supplierNo",supplierNos);
        }

        params.put("orderNo",orderNo.getText().toString());
        params.put("purchaseOrderNo",purchaseOrderNo.getText().toString());
        params.put("originalPurchaseOrderNo",originalPurchaseOrderNo.getText().toString());
        params.put("purchasePlanNo",purchasePlanNo.getText().toString());
        if(sta==null||sta.equals("1")){
            params.put("status","");
        }else {
            params.put("status",sta);
        }

        HttpUtility.doPostAsyn("/servlet/purchase/purchaseorder/list", params, new IHttpCallBack() {
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


                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        },null,getActivity());
    }

    private void fillDataToList(JSONObject data) throws JSONException {

            JSONArray rows=data.getJSONArray("rows");
            MData rechargData=null;
            for(int i=0;i<rows.length();i++){
                rechargData=new MData();
                JSONObject object=rows.getJSONObject(i);
                rechargData.setId(object.getInt("id"));
                rechargData.setPurchaseOrderNo(object.getString("purchaseOrderNo"));

                if(!object.isNull("orderNo")){
                    rechargData.setOrderNo(object.getString("orderNo"));
                }else {
                    rechargData.setOrderNo("-");
                }
                rechargData.setSupplierNo(object.getString("supplierNo"));
                rechargData.setPurchaseOrderType(object.getString("purchaseOrderType"));
                rechargData.setManual(object.getString("manual"));
                rechargData.setStatus(object.getString("status"));
                rechargData.setPlanProductionDate(object.getString("planProductionDate"));

               if(!object.isNull("plateNumber")){
                   rechargData.setPlateNumber(object.getString("plateNumber"));
               }else {
                   rechargData.setPlateNumber("-");
               }
                if(!object.isNull("sendTime")){
                    rechargData.setSendTime(object.getString("sendTime"));
                }else {
                    rechargData.setSendTime("-");
                }
                if(!object.isNull("receivePerson")){
                    JSONObject receivePerson=object.getJSONObject("receivePerson");
                    rechargData.setReceivePerson(receivePerson.getString("personCode")+" "+receivePerson.getString("realName"));
                }else {
                    rechargData.setReceivePerson("-");
                }
                if(!object.isNull("forkliftPerson")){
                    JSONObject forkliftPerson=object.getJSONObject("forkliftPerson");
                    rechargData.setForkliftPerson(forkliftPerson.getString("personCode")+" "+forkliftPerson.getString("realName"));
                }else {
                    rechargData.setForkliftPerson("-");
                }






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
                            JSONArray dealerList=rows.getJSONArray("supplierList");
                            dicts.add(new Dict(1,"请选择"));
                            for(int i=0;i<dealerList.length();i++){
                                String dealerName1=dealerList.getString(i);
                                dicts.add(new Dict(1,dealerName1));
                                ArrayAdapter<Dict> arrAdapterpay3 = new ArrayAdapter<Dict>(getActivity(), R.layout.simple_spinner_item,dicts);
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
                            Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };
        return mListData;
    }


    //经销商名称

    Spinner.OnItemSelectedListener listener=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            supplierNos=((Dict)supplierNo.getSelectedItem()).getText();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    Spinner.OnItemSelectedListener listener1=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            sta=((AllData)status.getSelectedItem()).getStr();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main,menu);
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
                orderNo.setText("");
                purchaseOrderNo.setText("");
               originalPurchaseOrderNo.setText("");
               purchasePlanNo.setText("");
                loaddata();
            }
        },1000);
    }

    @Override
    public void onLoadMore() {
        initItemMoreData();
    }

    @Override
    public void onClick(View v) {


    }
}
