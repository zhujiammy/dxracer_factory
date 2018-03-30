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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhujia.dxracer_factory.Adapter.SchedulingsheetAdapter;
import com.example.zhujia.dxracer_factory.Data.Dict;
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
 * 排单计划
 */

public class Schedulingsheet extends Fragment implements View.OnClickListener,OnRefreshListener,OnLoadMoreListener {
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
    private List<ScheduData> mListData=new ArrayList<>();
    JSONObject reslutJSONObject;
    JSONArray contentjsonarry;
    JSONObject contentjsonobject;
    private SchedulingsheetAdapter adapter;
    private  int total;
    private Intent intent;
    private SuperRefreshRecyclerView recyclerView;
    private String username;
    private LinearLayout lin;
    private ImageView open;
    private TextView normalOutput,overtimeOutput,productionConfigstartDate,productionConfigendDate,holidayStr,orderNo,planDate;
    private Spinner dealerName;
    private String DealerName;
    private java.util.Calendar cal;
    private int year,month,day;

    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.schedulingsheet,container,false);
        setHasOptionsMenu(true);
        intent=getActivity().getIntent();
        sharedPreferences =getActivity().getSharedPreferences("Session", Context.MODE_APPEND);
        business_id=sharedPreferences.getString("business_id","");
        username=sharedPreferences.getString("username","");
        supplierId=sharedPreferences.getString("supplierId","");
        initUI();
        loaddata1();
        loaddata();//加载列表数据
        getDate();//获取当前日期
       adapter=new SchedulingsheetAdapter(getContext(),getData());
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
        normalOutput=(TextView)view.findViewById(R.id.normalOutput);
        dealerName=(Spinner)view.findViewById(R.id.dealerName);
        orderNo=(TextView)view.findViewById(R.id.orderNo);
        planDate=(TextView)view.findViewById(R.id.planDate);
        overtimeOutput=(TextView)view.findViewById(R.id.overtimeOutput);
        productionConfigstartDate=(TextView)view.findViewById(R.id.productionConfigstartDate);
        productionConfigendDate=(TextView)view.findViewById(R.id.productionConfigendDate);
        holidayStr=(TextView)view.findViewById(R.id.holidayStr);
        lin=(LinearLayout)view.findViewById(R.id.lin);
        open=(ImageView)view.findViewById(R.id.open);
        open.setOnClickListener(this);
        dealerName.setOnItemSelectedListener(listener);
        planDate.setOnClickListener(this);
    }


    @SuppressLint("WrongConstant")
    private void getDate(){
        cal= java.util.Calendar.getInstance();
        year=cal.get(java.util.Calendar.YEAR);
        month=cal.get(java.util.Calendar.MONTH);
        day=cal.get(java.util.Calendar.DAY_OF_MONTH);

    }

    private void loaddata1(){
        params=new HashMap<>();
        params.put("businessId",business_id);
        HttpUtility.doPostAsyn("/servlet/production/productionplan", params, new IHttpCallBack() {
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
        params.put("sorttype","asc");
        params.put("sort","planDate,seq");
        if(DealerName==null||DealerName.equals("请选择")){
            params.put("order.dealerName","");
        }else {
            params.put("order.dealerName",DealerName);
        }
        params.put("orderNo",orderNo.getText().toString());
        params.put("planDate",planDate.getText().toString());
        HttpUtility.doPostAsyn("/servlet/production/productionplan/list", params, new IHttpCallBack() {
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
        params.put("sorttype","asc");
        params.put("sort","planDate,seq");
        if(DealerName==null||DealerName.equals("请选择")){
            params.put("order.dealerName","");
        }else {
            params.put("order.dealerName",DealerName);
        }
        params.put("orderNo",orderNo.getText().toString());
        params.put("planDate",planDate.getText().toString());
        HttpUtility.doPostAsyn("/servlet/production/productionplan/list", params, new IHttpCallBack() {
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
        },null,getActivity());
    }

    private void fillDataToList(JSONObject data) throws JSONException {
        contentjsonobject=data.getJSONObject("rows");

        if(!contentjsonobject.isNull("resultList")){
            contentjsonarry=contentjsonobject.getJSONArray("resultList");
            ScheduData rechargData=null;
            for(int i=0;i<contentjsonarry.length();i++){
                rechargData=new ScheduData();
                JSONObject object=contentjsonarry.getJSONObject(i);
                rechargData.setId(object.getString("id"));
                rechargData.setOrderNo(object.getString("orderNo"));
                rechargData.setPlanDate(object.getString("planDate"));
                JSONObject order=object.getJSONObject("order");
                rechargData.setDealerName(order.getString("dealerName"));
                rechargData.setTotalQuantity(object.getString("totalQuantity"));
                rechargData.setOrderId(object.getString("orderId"));
                if(!object.isNull("purchasePlan")){
                    JSONObject purchasePlan=object.getJSONObject("purchasePlan");
                    rechargData.setPurchasePlan("1");
                    rechargData.setOrderstatus(purchasePlan.getString("orderStatus"));
                }else {
                    rechargData.setPurchasePlan("null");
                }
                mListData.add(rechargData);
            }
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
                            JSONArray dealerList=rows.getJSONArray("dealerList");
                            dicts.add(new Dict(1,"请选择"));
                            for(int i=0;i<dealerList.length();i++){
                                JSONObject object=dealerList.getJSONObject(i);
                                String dealerName1=object.getString("dealerName");
                                dicts.add(new Dict(1,dealerName1));
                                ArrayAdapter<Dict> arrAdapterpay3 = new ArrayAdapter<Dict>(getActivity(), R.layout.simple_spinner_item,dicts);
                                //设置样式
                                arrAdapterpay3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                dealerName.setAdapter(arrAdapterpay3);
                            }

                            productionConfigendDate.setText(rows.getString("productionConfigendDate")+"如遇休息日，则顺延");
                            productionConfigstartDate.setText(rows.getString("productionConfigstartDate"));
                            holidayStr.setText(rows.getString("holidayStr"));
                            JSONObject productionConfig=rows.getJSONObject("productionConfig");
                            normalOutput.setText(productionConfig.getString("normalOutput"));
                            overtimeOutput.setText(productionConfig.getString("overtimeOutput"));
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
            DealerName=((Dict)dealerName.getSelectedItem()).getText();
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
                planDate.setText("");
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
        if(v==open){
            if(istouch==0){
                lin.setVisibility(View.VISIBLE);
                open.setImageDrawable(getResources().getDrawable(R.drawable.up1));
                istouch=1;


            }else {
                lin.setVisibility(View.GONE);
                open.setImageDrawable(getResources().getDrawable(R.drawable.down1));
                istouch=0;

            }


        }
        if(v==planDate){
            //选择日期
            DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    String date=(i+"-"+(++i1)+"-"+i2);
                    DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                    Date date1 = null;
                    try {
                        date1 = format1.parse(date);
                        String startime = format1.format(date1);
                        planDate.setText(startime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            };
            DatePickerDialog dialog=new DatePickerDialog(getActivity(),DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,listener,year,month,day);
            dialog.show();
        }
    }
}
