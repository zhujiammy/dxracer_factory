package com.example.zhujia.dxracer_factory.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhujia.dxracer_factory.Data.AllData;
import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.Net.Constant;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtils;

import net.sf.json.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZHUJIA on 2018/1/24.
 *
 * 保养费用
 */

public class MaintenanCosts extends AppCompatActivity{

    private TextView field2;
    private LinearLayout view_group;
    private Spinner detail;
    private String detailId;
    private EditText cost;
    private Intent intent;
    private SharedPreferences sharedPreferences;
    private String business_id,departmentPersonId,userId;
    Map<String,String> params;
    private JSONObject reslutJSONObject;
    private Toolbar toolbar;
    private List<AllData> dicts1= new ArrayList<AllData>();
    private ArrayAdapter<AllData>arrAdapterpay1;
    private JSONArray rows;
    View view;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintenancosts);
        sharedPreferences =getSharedPreferences("Session", Context.MODE_APPEND);
        business_id=sharedPreferences.getString("business_id","");
        departmentPersonId=sharedPreferences.getString("departmentPersonId","");
        userId=sharedPreferences.getString("userId","");
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
        intent=getIntent();
        initUI();
        loadeditMoney();
        loadlist();

    }

    private void initUI(){
        field2=(TextView)findViewById(R.id.field2);
        field2.setText(intent.getStringExtra("MaintainNo"));
        view_group=(LinearLayout)findViewById(R.id.view_group);
        detail=(Spinner)findViewById(R.id.detail);
        detail.setOnItemSelectedListener(listener);
        cost=(EditText)findViewById(R.id.cost);
    }
    private void loadeditMoney(){
        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("id",intent.getStringExtra("id"));
        Log.e("TAG", "loadcategory: "+params );
        new HttpUtils().post(Constant.APPURLS+"/servlet/equipment/equipmentmaintain/editMoney/inittext",params,new HttpUtils.HttpCallback() {

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

    private void loadlist(){
        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("maintainId",intent.getStringExtra("id"));
        params.put("sorttype","asc");
        params.put("sort","undefined");
        params.put("page", "1");
        Log.e("TAG", "loadcategory: "+params );
        new HttpUtils().post(Constant.APPURLS+"/servlet/equipment/equipmentmaintaindetail/list",params,new HttpUtils.HttpCallback() {

            @Override
            public void onSuccess(String data) {
                // TODO Auto-generated method stub
                com.example.zhujia.dxracer_factory.Tools.Log.printJson("tag",data,"header");
                Message msg=Message.obtain(
                        mHandler,2,data
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

            if(TextUtils.isEmpty(cost.getText().toString())){
                Toast.makeText(getApplicationContext(),"维修费用不能为空",Toast.LENGTH_SHORT).show();
            }else {
                params=new HashMap<>();
                params.put("businessId",business_id);
                params.put("maintainId",intent.getStringExtra("id"));
                params.put("detailId",detailId);
                params.put("cost",cost.getText().toString());
                Log.e("TAG", "onOptionsItemSelected: "+params );
                new HttpUtils().post(Constant.APPURLS+"/servlet/equipment/equipmentmaintaindetail/add",params,new HttpUtils.HttpCallback() {

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

    Spinner.OnItemSelectedListener listener=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            detailId=((AllData)detail.getSelectedItem()).getStr();
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
                        JSONArray equipmentDetailList=reslutJSONObject.getJSONArray("equipmentDetailList");
                        for(int i=0;i<equipmentDetailList.length();i++){
                            JSONObject object2=equipmentDetailList.getJSONObject(i);
                            dicts1.add(new AllData(object2.getString("id"),object2.getString("name")));
                            arrAdapterpay1 = new ArrayAdapter<AllData>(getApplicationContext(), R.layout.simple_spinner_item,dicts1);
                            //设置样式
                            arrAdapterpay1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            detail.setAdapter(arrAdapterpay1);
                        }
                        break;

                    case 2:
                        reslutJSONObject=new JSONObject(msg.obj.toString());
                        if(!reslutJSONObject.isNull("rows")){
                            rows=reslutJSONObject.getJSONArray("rows");
                            view_group.removeAllViews();
                            for(int i=0;i<rows.length();i++){
                                view=View.inflate(getApplicationContext(),R.layout.layout_adds,null);
                                TextView name=(TextView)view.findViewById(R.id.name);
                                TextView cost=(TextView)view.findViewById(R.id.cost);
                                ImageView del=(ImageView)view.findViewById(R.id.del);
                                final JSONObject object2=rows.getJSONObject(i);
                                cost.setText(object2.getString("cost"));
                                JSONObject equipmentDetail=object2.getJSONObject("equipmentDetail");
                                name.setText(equipmentDetail.getString("name"));
                                final String id=object2.getString("id");
                                del.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showNormalDialog(id);
                                    }

                                });
                                view_group.addView(view);

                            }
                        }else {
                            view_group.removeAllViews();
                        }


                        break;
                    case 3:
                        reslutJSONObject=new JSONObject(msg.obj.toString());
                        if(reslutJSONObject.getString("code").equals("success")){
                            loadlist();
                            Toast.makeText(getApplicationContext(),"删除成功",Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(getApplicationContext(),"删除失败",Toast.LENGTH_SHORT).show();
                        }
                        break;

                }
            }catch (JSONException e){
                e.printStackTrace();
            }

        }
    };

    private void showNormalDialog(final String id){

        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(MaintenanCosts.this);
        normalDialog.setMessage("确认删除吗?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        params=new HashMap<>();
                        params.put("businessId",business_id);
                        params.put("id",id);
                        new HttpUtils().post(Constant.APPURLS+"/servlet/equipment/equipmentmaintaindetail/delete",params,new HttpUtils.HttpCallback() {

                            @Override
                            public void onSuccess(String data) {
                                // TODO Auto-generated method stub
                                com.example.zhujia.dxracer_factory.Tools.Log.printJson("tag",data,"header");

                                Message msg=Message.obtain(
                                        mHandler,3,data
                                );
                                mHandler.sendMessage(msg);
                            }
                        });






                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }
}
