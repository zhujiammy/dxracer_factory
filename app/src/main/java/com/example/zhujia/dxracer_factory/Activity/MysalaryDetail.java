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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhujia.dxracer_factory.Adapter.AllEquipmentMaintainAdapter;
import com.example.zhujia.dxracer_factory.Adapter.EquipmentMaintainAdapter;
import com.example.zhujia.dxracer_factory.Adapter.EquipmentmaintenanceAdapter;
import com.example.zhujia.dxracer_factory.Adapter.MyequipmentAdapter;
import com.example.zhujia.dxracer_factory.Adapter.MysalaryDetailAdapter;
import com.example.zhujia.dxracer_factory.Adapter.SinglerowMG1;
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

import static android.support.design.widget.TabLayout.MODE_FIXED;

/**
 * Created by zhujia on 2017/11/22.
 *工资结构
 *
 */

public class MysalaryDetail extends AppCompatActivity implements View.OnClickListener{

    private String roleId;

    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private String business_id,departmentId,departmentPersonId;
    Map<String,String> params;
    private int pageindex=1;
    private TextView text1,sum,real_name,text_btn,department_name,type,position_name,performance_salary,base_salary_value,post_salary;
    boolean hasMoreData;
    private int istouch=0;
    JSONObject reslutJSONObject;
    private View view,view1;
    JSONArray contentjsonarry;
    JSONObject contentjsonobject;
    private LinearLayout content;
    private List<MData> mListData=new ArrayList<>();
    private  int total;
    private Intent intent;
    private String SupplierNo;
    private LinearLayout view_group;
    private String TYPE;
    private SuperRefreshRecyclerView recyclerView;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences =getSharedPreferences("Session", Context.MODE_APPEND);
        business_id=sharedPreferences.getString("business_id","");
        departmentId=sharedPreferences.getString("departmentId","");
        departmentPersonId=sharedPreferences.getString("departmentPersonId","");
        roleId=sharedPreferences.getString("roleId","");
        intent=getIntent();
        TYPE=intent.getStringExtra("type");
        if(TYPE.equals("计件工资")){
            setContentView(R.layout.mysalarydetail);
        }else  if(TYPE.equals("计时工资")) {
            setContentView(R.layout.mysalarydetails);
        }

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
        initUI();
        loaddata();//加载列表数据
    }

    private void initUI(){
        text1=(TextView)findViewById(R.id.text1);
        text1.setText("工资结构");
        text_btn=(TextView)findViewById(R.id.text_btn);
        text_btn.setOnClickListener(this);
        content=(LinearLayout)findViewById(R.id.content);
        sum=(TextView)findViewById(R.id.sum);
        view_group=(LinearLayout)findViewById(R.id.view_group);
        if(TYPE.equals("计件工资")){
            real_name=(TextView)findViewById(R.id.real_name);
            department_name=(TextView)findViewById(R.id.department_name);
            type=(TextView)findViewById(R.id.type);
            position_name=(TextView)findViewById(R.id.position_name);
            performance_salary=(TextView)findViewById(R.id.performance_salary);
        }
        if(TYPE.equals("计时工资")){
            real_name=(TextView)findViewById(R.id.real_name);
            department_name=(TextView)findViewById(R.id.department_name);
            type=(TextView)findViewById(R.id.type);
            position_name=(TextView)findViewById(R.id.position_name);
            performance_salary=(TextView)findViewById(R.id.performance_salary);
            base_salary_value=(TextView)findViewById(R.id.base_salary_value);
            post_salary=(TextView)findViewById(R.id.post_salary);
        }

    }



    private void loaddata(){

        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("personId", departmentPersonId);
        Log.e("TAG", "loaddata: "+params );
        new HttpUtils().post(Constant.APPURLS+"/servlet/salary/mysalary/detailhtm",params,new HttpUtils.HttpCallback() {
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
                Toast.makeText(MysalaryDetail.this, "error", Toast.LENGTH_LONG).show();
            }
        });
    }





    private void fillDataToList(JSONObject data) throws JSONException {


        JSONObject departmentPersonSalay=data.getJSONObject("departmentPersonSalay");
        if(!data.isNull("cardResultList")){
            contentjsonarry=data.getJSONArray("cardResultList");
            MData rechargData=null;
            view_group.removeAllViews();
            for(int i=0;i<contentjsonarry.length();i++){
                rechargData=new MData();
                JSONObject object=contentjsonarry.getJSONObject(i);
                JSONObject card=object.getJSONObject("card");
               /* rechargData.setSalaryMonth(card.getString("salaryMonth"));
                rechargData.setField1(card.getString("field1"));
                rechargData.setActTotalResult(card.getString("actTotalResult"));*/
               if(departmentPersonSalay.getString("type").equals("计时工资")){
                   view=View.inflate(getApplicationContext(),R.layout.layout_view,null);
                   TextView salaryMonth=(TextView)view.findViewById(R.id.salaryMonth);
                   TextView field1=(TextView)view.findViewById(R.id.field1);
                   TextView actTotalResult=(TextView) view.findViewById(R.id.actTotalResult);
                   TextView salaryDayBase=(TextView)view.findViewById(R.id.salaryDayBase);
                   TextView salaryDayDuty=(TextView)view.findViewById(R.id.salaryDayDuty);
                   TextView workDay=(TextView)view.findViewById(R.id.workDay);
                   TextView baseSalaryMonth=(TextView)view.findViewById(R.id.baseSalaryMonth);
                   TextView postSalaryMonth=(TextView)view.findViewById(R.id.postSalaryMonth);

                    salaryDayBase.setText(card.getString("salaryDayBase"));
                   salaryDayDuty.setText(card.getString("salaryDayDuty"));
                   workDay.setText(card.getString("workDay"));
                   baseSalaryMonth.setText(card.getString("baseSalaryMonth"));
                   postSalaryMonth.setText(card.getString("postSalaryMonth"));
                   salaryMonth.setText(card.getString("salaryMonth"));
                   field1.setText(card.getString("field1"));
                   if(!card.isNull("actTotalResult")){
                       actTotalResult.setText(card.getString("actTotalResult"));
                   }else {
                       actTotalResult.setText("-");
                   }

                   LinearLayout li=(LinearLayout)view.findViewById(R.id.lay);
                   JSONArray detailList=object.getJSONArray("detailList");
                   for(int j=0;j<detailList.length();j++){
                       view1=View.inflate(getApplicationContext(),R.layout.layoutview,null);
                       TextView title=(TextView)view1.findViewById(R.id.title);
                       TextView text=(TextView)view1.findViewById(R.id.text);
                       JSONObject object1=detailList.getJSONObject(j);
                       title.setText(object1.getString("salary_type"));
                       if(!object1.isNull("salary_total")){
                           text.setText(object1.getString("salary_total"));
                       }else {
                           text.setText("-");
                       }

                        li.addView(view1);
                   }
                   view_group.addView(view);
               }


                mListData.add(rechargData);
            }
        }


    }


    @SuppressLint("HandlerLeak")

    private Handler  mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                try{
                    switch (msg.what) {

                        case 0:
                            //返回item类型数据
                            reslutJSONObject=new JSONObject(msg.obj.toString());
                            JSONObject departmentPersonSalay=reslutJSONObject.getJSONObject("departmentPersonSalay");
                            real_name.setText(departmentPersonSalay.getString("real_name"));
                            department_name.setText(departmentPersonSalay.getString("department_name"));
                            position_name.setText(departmentPersonSalay.getString("position_name"));
                            type.setText(departmentPersonSalay.getString("type"));
                            performance_salary.setText(departmentPersonSalay.getString("performance_salary_id"));
                            if(!departmentPersonSalay.isNull("base_salary")){
                                base_salary_value.setText(departmentPersonSalay.getString("base_salary"));
                            }
                            if(!departmentPersonSalay.isNull("post_salary_id")){
                                post_salary.setText(departmentPersonSalay.getString("post_salary_id"));
                            }
                            if(roleId.equals("104")){
                                sum.setText("绩效工资(月)+实际每月计件工资");
                            }else if(roleId.equals("42")||roleId.equals("32")||roleId.equals("100")){
                                String sums= String.valueOf(departmentPersonSalay.getDouble("performance_salary_id")+departmentPersonSalay.getDouble("base_salary")+departmentPersonSalay.getDouble("post_salary_id"));
                                sum.setText(sums);
                            }
                            fillDataToList(reslutJSONObject);
                            break;


                        case 1:
                            //返回item类型数据
                            reslutJSONObject=new JSONObject(msg.obj.toString());
                            JSONObject type=reslutJSONObject.getJSONObject("departmentPersonSalay");
                            TYPE=type.getString("type");

                            break;

                        default:
                            Toast.makeText(MysalaryDetail.this, "网络异常", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };


    @Override
    public void onClick(View v) {


        if(v==text_btn){
            if(istouch==0){
                content.setVisibility(View.VISIBLE);
                istouch=1;
            }else {
                content.setVisibility(View.GONE);
                istouch=0;

            }
        }
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


}
