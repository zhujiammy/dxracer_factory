package com.example.zhujia.dxracer_factory.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhujia.dxracer_factory.Data.AllData;
import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.ImageService;
import com.example.zhujia.dxracer_factory.Tools.Net.Constant;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtility;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtils;
import com.example.zhujia.dxracer_factory.Tools.Net.IHttpCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZHUJIA on 2018/1/11.
 *
 * 审核
 */

public class EquipmentMaintaincheck extends AppCompatActivity implements View.OnClickListener {

    private TextView text1,startTime,field2;
    private Toolbar toolbar;
    private EditText content;
    private Spinner equipment,originator,maintenancePeopleOut,status;
    List<AllData> dicts1= new ArrayList<AllData>();
    List<AllData> dicts2= new ArrayList<AllData>();
    List<AllData> dicts3= new ArrayList<AllData>();
    List<AllData> dicts4= new ArrayList<AllData>();
    private String categoryId;
    private java.util.Calendar cal;
    private int year,month,day;
    private SharedPreferences sharedPreferences;
    private String business_id,departmentPersonId,sta,userId;
    Map<String,String> params;
    private Intent intent;
    JSONObject reslutJSONObject;
    private String url,equipmentId,originatorId,maintenancePeopleOutId,statusid;
    private int istouch;
    ArrayAdapter<AllData> arrAdapterpay4,arrAdapterpay1,arrAdapterpay2,arrAdapterpay3;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipmentmaintaincheck);
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
        departmentPersonId=sharedPreferences.getString("departmentPersonId","");
        userId=sharedPreferences.getString("userId","");
        intent=getIntent();
        initUI();
        loadcategory();
        getDate();//获取当前日期
    }

    @SuppressLint("WrongConstant")
    private void getDate(){
        cal= java.util.Calendar.getInstance();
        year=cal.get(java.util.Calendar.YEAR);
        month=cal.get(java.util.Calendar.MONTH);
        day=cal.get(java.util.Calendar.DAY_OF_MONTH);

    }

    private void initUI(){
        text1=(TextView)findViewById(R.id.text1);
        startTime=(TextView)findViewById(R.id.startTime);
        startTime.setOnClickListener(this);
        text1.setText("审核");
        content=(EditText)findViewById(R.id.content);
        field2=(TextView)findViewById(R.id.field2);
        equipment=(Spinner) findViewById(R.id.equipment);
        originator=(Spinner) findViewById(R.id.originator);
        status=(Spinner)findViewById(R.id.status);
        status.setOnItemSelectedListener(listener4);
        dicts4.add(new AllData("0","请选择"));
        dicts4.add(new AllData("B","审核通过"));
        dicts4.add(new AllData("C","审核不通过"));
        arrAdapterpay4 = new ArrayAdapter<AllData>(getApplicationContext(), R.layout.simple_spinner_item,dicts4);
        //设置样式
        arrAdapterpay4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status.setAdapter(arrAdapterpay4);

        maintenancePeopleOut=(Spinner) findViewById(R.id.maintenancePeopleOut);



        equipment.setOnItemSelectedListener(listener1);
        originator.setOnItemSelectedListener(listener2);
        maintenancePeopleOut.setOnItemSelectedListener(listener3);
        originator.setEnabled(false);
        maintenancePeopleOut.setEnabled(false);

        url="/servlet/equipment/equipment/update";

        /* intent.putExtra("field2",data.get(position).getField2());
        intent.putExtra("equipmentNo",data.get(position).getEquipmentNo());
        intent.putExtra("startTime",data.get(position).getStartTime());
        intent.putExtra("equipmentNo",data.get(position).getRealName());
        intent.putExtra("supplierName",data.get(position).getSupplierName());
        intent.putExtra("content",data.get(position).getContent());*/
        field2.setText(intent.getStringExtra("MaintainNo"));
        startTime.setText(intent.getStringExtra("startTime"));
        content.setText(intent.getStringExtra("content"));



    }


    private void loadcategory(){
        params=new HashMap<>();
        params.put("businessId",business_id);
        Log.e("TAG", "loadcategory: "+params );
        new HttpUtils().post(Constant.APPURLS+"/servlet/equipment/equipmentmaintenance/add/inittext",params,new HttpUtils.HttpCallback() {
            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                super.onStart();
            }
            @Override
            public void onSuccess(String data) {
                // TODO Auto-generated method stub
                com.example.zhujia.dxracer_factory.Tools.Log.printJson("tag",data,"header");
                Message msg=Message.obtain(
                        mHandler,1,data
                );
                mHandler.sendMessage(msg);
            }

            @Override
            public void onError(String msg) {
                // TODO Auto-generated method stub
                super.onError(msg);
                Toast.makeText(EquipmentMaintaincheck.this, "error", Toast.LENGTH_LONG).show();
            }
        });
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id==R.id.save_btn){
            //保存

            if(equipmentId==null||equipmentId.equals("0")){
                Toast.makeText(getApplicationContext(),"设备编号不能为空",Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(content.getText().toString())){
                Toast.makeText(getApplicationContext(),"故障描述不能为空",Toast.LENGTH_SHORT).show();
            }else if(statusid==null||statusid.equals("0")){
                Toast.makeText(getApplicationContext(),"审核状态不能为空",Toast.LENGTH_SHORT).show();
            }else {
                params=new HashMap<>();
                params.put("businessId",business_id);
                params.put("equipmentId",equipmentId);
                params.put("startTime",startTime.getText().toString());
                params.put("content",content.getText().toString());
                params.put("status",statusid);
                params.put("id",intent.getStringExtra("id"));

                new HttpUtils().post(Constant.APPURLS+"/servlet/equipment/equipmentmaintain/check",params,new HttpUtils.HttpCallback() {

                    @Override
                    public void onSuccess(String data) {
                        // TODO Auto-generated method stub
                        com.example.zhujia.dxracer_factory.Tools.Log.printJson("tag",data,"header");
                        Message msg=Message.obtain(
                                mHandler,0,data
                        );
                        mHandler.sendMessage(msg);
                    }


                });
            }




        }

        return super.onOptionsItemSelected(item);
    }






    Spinner.OnItemSelectedListener listener1=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            equipmentId=((AllData)equipment.getSelectedItem()).getStr();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    Spinner.OnItemSelectedListener listener2=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            originatorId=((AllData)originator.getSelectedItem()).getStr();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    Spinner.OnItemSelectedListener listener3=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            maintenancePeopleOutId=((AllData)maintenancePeopleOut.getSelectedItem()).getStr();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    Spinner.OnItemSelectedListener listener4=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            statusid=((AllData)status.getSelectedItem()).getStr();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };




    @SuppressLint("HandlerLeak")
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what){
                    case 0:
                        reslutJSONObject=new JSONObject(msg.obj.toString());
                        if(reslutJSONObject.getString("code").equals("success")){
                            Toast.makeText(getApplicationContext(),reslutJSONObject.getString("message"),Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(getApplicationContext(),reslutJSONObject.getString("message"),Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 1:
                        dicts1.add(new AllData("0","请选择"));
                        reslutJSONObject=new JSONObject(msg.obj.toString());
                        JSONArray equipmentList=reslutJSONObject.getJSONArray("equipmentList");
                        for(int i=0;i<equipmentList.length();i++){
                            JSONObject object2=equipmentList.getJSONObject(i);
                            dicts1.add(new AllData(object2.getString("id"),object2.getString("equipmentNo")+"--"+object2.getString("equipmentName")));
                            arrAdapterpay1 = new ArrayAdapter<AllData>(getApplicationContext(), R.layout.simple_spinner_item,dicts1);
                            //设置样式
                            arrAdapterpay1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            equipment.setAdapter(arrAdapterpay1);
                        }

                        dicts2.add(new AllData("0","请选择"));
                        JSONArray equipmentSupplierList=reslutJSONObject.getJSONArray("equipmentSupplierList");
                        for(int i=0;i<equipmentSupplierList.length();i++){
                            JSONObject object2=equipmentSupplierList.getJSONObject(i);
                            dicts2.add(new AllData(object2.getString("id"),object2.getString("supplierName")));
                            arrAdapterpay2 = new ArrayAdapter<AllData>(getApplicationContext(), R.layout.simple_spinner_item,dicts2);
                            //设置样式
                            arrAdapterpay2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            maintenancePeopleOut.setAdapter(arrAdapterpay2);
                        }

                        dicts3.add(new AllData("0","请选择"));
                        JSONArray departmentPersonList=reslutJSONObject.getJSONArray("departmentPersonList");
                        for(int i=0;i<departmentPersonList.length();i++){
                            JSONObject object2=departmentPersonList.getJSONObject(i);
                            dicts3.add(new AllData(object2.getString("id"),object2.getString("realName")));
                            arrAdapterpay3 = new ArrayAdapter<AllData>(getApplicationContext(), R.layout.simple_spinner_item,dicts3);
                            //设置样式
                            arrAdapterpay3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            originator.setAdapter(arrAdapterpay3);
                        }

                        if(arrAdapterpay1.getCount()!=0){
                            int i=arrAdapterpay1.getCount();
                            for(int j=0;j<i;j++){
                                if(intent.getStringExtra("equipmentId").equals(arrAdapterpay1.getItem(j).getStr())){
                                    equipment.setAdapter(arrAdapterpay1);
                                    equipment.setSelection(j,true);
                                }
                            }
                        }

                        int c=arrAdapterpay3.getCount();
                        for(int j=0;j<c;j++){
                            if(intent.getStringExtra("realName").equals(arrAdapterpay3.getItem(j).getText())){
                                originator.setAdapter(arrAdapterpay3);
                                originator.setSelection(j,true);
                            }
                        }

                        int d=arrAdapterpay2.getCount();
                        for(int j=0;j<d;j++){
                            if(intent.getStringExtra("supplierName").equals(arrAdapterpay2.getItem(j).getText())){
                                maintenancePeopleOut.setAdapter(arrAdapterpay2);
                                maintenancePeopleOut.setSelection(j,true);
                            }
                        }

                        break;

                }
            }catch (JSONException e){
                e.printStackTrace();
            }

        }
    };

    @Override
    public void onClick(View v) {

        if(v==startTime){
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
                        startTime.setText(startime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            };
            DatePickerDialog dialog=new DatePickerDialog(EquipmentMaintaincheck.this,DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,listener,year,month,day);
            dialog.show();
        }
    }






}
