package com.example.zhujia.dxracer_factory.Activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhujia.dxracer_factory.Adapter.AllEquipmentMaintainAdapter;
import com.example.zhujia.dxracer_factory.Adapter.EquipmentMaintainAdapter;
import com.example.zhujia.dxracer_factory.Adapter.EquipmentmaintenanceAdapter;
import com.example.zhujia.dxracer_factory.Adapter.MySalaryByMonthAdapter;
import com.example.zhujia.dxracer_factory.Adapter.MyequipmentAdapter;
import com.example.zhujia.dxracer_factory.Adapter.MysalaryDetailAdapter;
import com.example.zhujia.dxracer_factory.Data.AllData;
import com.example.zhujia.dxracer_factory.Data.MData;
import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.MyradionGroup;
import com.example.zhujia.dxracer_factory.Tools.Net.Constant;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtility;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtils;
import com.example.zhujia.dxracer_factory.Tools.Net.IHttpCallBack;
import com.example.zhujia.dxracer_factory.Tools.OnLoadMoreListener;
import com.example.zhujia.dxracer_factory.Tools.OnRefreshListener;
import com.example.zhujia.dxracer_factory.Tools.SuperRefreshRecyclerView;
import com.hmy.popwindow.PopWindow;

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
 * Created by zhujia on 2017/11/22.
 *月工资条
 *
 */

public class MySalaryByMonth extends AppCompatActivity implements View.OnClickListener,OnRefreshListener,OnLoadMoreListener {


    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private String business_id,departmentId,departmentPersonId;
    Map<String,String> params;
    private int pageindex=1;
    private TextView text1;
    boolean hasMoreData;
    private int istouch=0;
    private Handler mHandler;
    JSONObject reslutJSONObject;
    JSONArray contentjsonarry;
    JSONObject contentjsonobject;
    private List<MData> mListData=new ArrayList<>();
    private MySalaryByMonthAdapter adapter;
    private  int total;
    private Intent intent;
    private String SupplierNo;
    private SuperRefreshRecyclerView recyclerView;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mysalarybymonth);
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
        departmentPersonId=sharedPreferences.getString("departmentPersonId","");

        initUI();

        loaddata();//加载列表数据
        adapter=new MySalaryByMonthAdapter(MySalaryByMonth.this,getData());
    }

    private void initUI(){
        //初始化
        recyclerView= (SuperRefreshRecyclerView)findViewById(R.id.recyclerview);
        recyclerView.init(this,this);
        recyclerView.setRefreshEnabled(true);
        recyclerView.setLoadingMoreEnable(true);
        recyclerView.setHasFixedSize(true);
        text1=(TextView)findViewById(R.id.text1);
        text1.setText("月工资条");
    }


    private void loaddata(){

        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("personId", departmentPersonId);
        Log.e("TAG", "loaddata: "+params );
        new HttpUtils().post(Constant.APPURLS+"/servlet/salary/salarypayroll/mySalaryByMonthhtm",params,new HttpUtils.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                // TODO Auto-generated method stub
                com.example.zhujia.dxracer_factory.Tools.Log.printJson("tag",data,"header");
                Message msg=Message.obtain(
                        mHandler,0,data
                );
                mHandler.sendMessage(msg);
            }

            @Override
            public void onError(String msg) {
                // TODO Auto-generated method stub
                super.onError(msg);
                Toast.makeText(MySalaryByMonth.this, "error", Toast.LENGTH_LONG).show();
            }
        });
    }





    private void fillDataToList(JSONObject data) throws JSONException {

        if(!data.isNull("salaryPayrollList")){
            contentjsonarry=data.getJSONArray("salaryPayrollList");
            MData rechargData=null;
            for(int i=0;i<contentjsonarry.length();i++){
                rechargData=new MData();
                JSONObject object=contentjsonarry.getJSONObject(i);

                rechargData.setSalaryDate(object.getString("salaryDate"));
                rechargData.setRealName(object.getString("realName"));
                rechargData.setDepartmentName(object.getString("departmentName"));
                rechargData.setPositionName(object.getString("positionName"));
                rechargData.setPlanTotalFee(object.getString("planTotalFee"));
                rechargData.setActTotalResult(object.getString("actTotalFee"));
                rechargData.setTax(object.getString("tax"));
                rechargData.setBaseSalarySocialSecurity(object.getString("baseSalarySocialSecurity"));
                rechargData.setAccumulationFundCompany(object.getString("accumulationFundCompany"));
                rechargData.setAccumulationFundPersosn(object.getString("accumulationFundPersosn"));
                rechargData.setSocialSecurityCompany(object.getString("socialSecurityCompany"));
                rechargData.setSocialSecurityPerson(object.getString("socialSecurityPerson"));

                JSONObject socialSecurityPersonDetailMap=object.getJSONObject("socialSecurityPersonDetailMap");
                rechargData.setPension_person(socialSecurityPersonDetailMap.getString("pension_person"));
                rechargData.setBase_care_person(socialSecurityPersonDetailMap.getString("base_care_person"));
                rechargData.setUnemployment_person(socialSecurityPersonDetailMap.getString("unemployment_person"));

                JSONObject socialSecurityCompanyDetailMap=object.getJSONObject("socialSecurityCompanyDetailMap");
                rechargData.setPension_company(socialSecurityCompanyDetailMap.getString("pension_company"));
                rechargData.setBirth_company(socialSecurityCompanyDetailMap.getString("birth_company"));
                rechargData.setInjury_company(socialSecurityCompanyDetailMap.getString("injury_company"));
                rechargData.setTreatment_company(socialSecurityCompanyDetailMap.getString("treatment_company"));
                rechargData.setUnemployment_company(socialSecurityCompanyDetailMap.getString("unemployment_company"));
                rechargData.setBase_care_company(socialSecurityCompanyDetailMap.getString("base_care_company"));

/*

                JSONArray detailJson=object.getJSONArray("detailJson");
                JSONObject salary_total1=detailJson.getJSONObject(0);
                JSONObject salary_total2=detailJson.getJSONObject(1);
                JSONObject salary_total3=detailJson.getJSONObject(2);
                JSONObject salary_total4=detailJson.getJSONObject(3);
                JSONObject salary_total5=detailJson.getJSONObject(4);
                JSONObject salary_total6=detailJson.getJSONObject(5);
                rechargData.setSalary_total1(salary_total1.getString("salary_total"));
                rechargData.setSalary_total2(salary_total2.getString("salary_total"));
                rechargData.setSalary_total3(salary_total3.getString("salary_total"));
                rechargData.setSalary_total4(salary_total4.getString("salary_total"));
                rechargData.setSalary_total5(salary_total5.getString("salary_total"));*/
                rechargData.setSalary_total6(object.getJSONArray("detailJson").toString());

                mListData.add(rechargData);
            }
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
                            Toast.makeText(getApplicationContext(),"到底啦！",Toast.LENGTH_SHORT).show();
                            break;
                        case 4:
                            adapter.notifyDataSetChanged();
                            break;

                        default:
                            Toast.makeText(MySalaryByMonth.this, "网络异常", Toast.LENGTH_SHORT).show();
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
    protected void onResume() {
        super.onResume();
        loaddata();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.seach, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity i   n AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

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

    }
}
