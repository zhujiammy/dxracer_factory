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
 * 申请保养
 */

public class Applymaintenance extends AppCompatActivity implements View.OnClickListener {

    private TextView text1,startTime;
    private Toolbar toolbar;
    private EditText content;
    private Spinner maintenancePeopleOut;
    List<AllData> dicts2= new ArrayList<AllData>();
    private java.util.Calendar cal;
    private int year,month,day;
    private SharedPreferences sharedPreferences;
    private String business_id,departmentPersonId,userId;
    Map<String,String> params;
    private Intent intent;
    JSONObject reslutJSONObject;
    private String maintenancePeopleOutId;
    ArrayAdapter<AllData> arrAdapterpay2;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applymaintenance);
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
        text1.setText("申请保养");
        content=(EditText)findViewById(R.id.content);
        maintenancePeopleOut=(Spinner) findViewById(R.id.maintenancePeopleOut);
        maintenancePeopleOut.setOnItemSelectedListener(listener3);
    }


    private void loadcategory(){
        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("equipmentManager",departmentPersonId);
        Log.e("TAG", "loadcategory: "+params );
        new HttpUtils().post(Constant.APPURLS+"/servlet/equipment/equipmentmaintenance/add/inittext",params,new HttpUtils.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                // TODO Auto-generated method stub
                com.example.zhujia.dxracer_factory.Tools.Log.printJson("tag",data,"header");
                Message msg=Message.obtain(
                        mHandler,1,data
                );
                mHandler.sendMessage(msg);
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

            if(TextUtils.isEmpty(startTime.getText().toString())){
                Toast.makeText(getApplicationContext(),"保养时间不能为空",Toast.LENGTH_SHORT).show();
            }else if(maintenancePeopleOutId==null||maintenancePeopleOutId.equals("0")){
                Toast.makeText(getApplicationContext(),"设备维修商不能为空",Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(content.getText().toString())){
                Toast.makeText(getApplicationContext(),"保养内容不能为空",Toast.LENGTH_SHORT).show();
            }else {
                params=new HashMap<>();
                params.put("businessId",business_id);
                params.put("maintenancePeopleOut",maintenancePeopleOutId);
                params.put("startTime",startTime.getText().toString());
                params.put("content",content.getText().toString());
                params.put("originator",departmentPersonId);
                params.put("equipmentId",intent.getStringExtra("equipmentId"));
                new HttpUtils().post(Constant.APPURLS+"/servlet/equipment/equipmentmaintain/add",params,new HttpUtils.HttpCallback() {

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








    Spinner.OnItemSelectedListener listener3=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            maintenancePeopleOutId=((AllData)maintenancePeopleOut.getSelectedItem()).getStr();
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
                        reslutJSONObject=new JSONObject(msg.obj.toString());
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
            DatePickerDialog dialog=new DatePickerDialog(Applymaintenance.this,DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,listener,year,month,day);
            dialog.show();
        }
    }






}
