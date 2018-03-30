package com.example.zhujia.dxracer_factory.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhujia.dxracer_factory.Data.AllData;
import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtility;
import com.example.zhujia.dxracer_factory.Tools.Net.IHttpCallBack;

import net.sf.json.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZHUJIA on 2018/1/11.
 *
 * 新增配件
 */

public class AddEqdetail extends AppCompatActivity {

    private TextView text1;
    private Toolbar toolbar;
    private EditText name;
    private Spinner category;
    List<AllData> dicts1 = new ArrayList<AllData>();
    private String categoryId;
    private SharedPreferences sharedPreferences;
    private String business_id,departmentId,sta;
    Map<String,String> params;
    private Intent intent;
    private String url;
    ArrayAdapter<AllData> arrAdapterpay4;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addeqdetail);
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
        intent=getIntent();
        loadcategory();
        initUI();
    }

    private void initUI(){
        text1=(TextView)findViewById(R.id.text1);
        text1.setText("新增配件");
        name=(EditText)findViewById(R.id.name);
        category=(Spinner)findViewById(R.id.category);
        category.setOnItemSelectedListener(listener);
        if(intent.getStringExtra("type")!=null){
            url="/servlet/equipment/equipmentdetail/update";
            name.setText(intent.getStringExtra("name"));

        }else {
            url="/servlet/equipment/equipmentdetail/add";
        }

    }


    private void loadcategory(){
        params=new HashMap<>();
        params.put("businessId",business_id);
        HttpUtility.doPostAsyn("/servlet/equipment/equipmentdetail/add/inittext", params, new IHttpCallBack() {
            @Override
            public void onRequestComplete(String result, Handler handler, String errcode) {
                if ((null == result) || (result.equals(""))) {
                    // 网络连接异常
                    mHandler.sendEmptyMessage(9);

                }else {


                    Message msg=Message.obtain(
                            handler,1,result
                    );
                    mHandler.sendMessage(msg);

                }
            }
        },mHandler,AddEqdetail.this);
    }





    Spinner.OnItemSelectedListener listener=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            categoryId=((AllData)category.getSelectedItem()).getStr();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
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

            if(TextUtils.isEmpty(name.getText().toString())){
                Toast.makeText(getApplicationContext(),"配件名称不能为空",Toast.LENGTH_SHORT).show();
            }else if(categoryId.equals("0")){
                Toast.makeText(getApplicationContext(),"请选择所属类别",Toast.LENGTH_SHORT).show();
            }
            else {
                params=new HashMap<>();
                params.put("businessId",business_id);
                params.put("name",name.getText().toString());
                params.put("categoryId",categoryId);
                if(intent.getStringExtra("type")!=null){
                    params.put("id",intent.getStringExtra("id"));
                }

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
                },mHandler,AddEqdetail.this);
            }



        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what){
                    case 0:
                        JSONObject object=new JSONObject(msg.obj.toString());
                        String message=object.getString("message");
                        if(object.getString("code").equals("success")){
                            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 1:
                        JSONObject object1=new JSONObject(msg.obj.toString());
                        dicts1.add(new AllData("0","请选择"));
                        JSONArray categorylist=object1.getJSONArray("equipmentCategoryList");
                        for(int i=0;i<categorylist.length();i++){
                            JSONObject object2=categorylist.getJSONObject(i);
                            dicts1.add(new AllData(object2.getString("id"),object2.getString("name")));
                            arrAdapterpay4 = new ArrayAdapter<AllData>(getApplicationContext(), R.layout.simple_spinner_item,dicts1);
                            //设置样式
                            arrAdapterpay4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            category.setAdapter(arrAdapterpay4);
                        }

                        if(intent.getStringExtra("type")!=null){
                            int i=arrAdapterpay4.getCount();
                            for(int j=0;j<i;j++){
                                if(intent.getStringExtra("categoryName").equals(arrAdapterpay4.getItem(j).getText())){
                                    category.setAdapter(arrAdapterpay4);
                                    category.setSelection(j,true);
                                }
                            }
                        }

                        break;
                }
            }catch (JSONException e){
                e.printStackTrace();
            }

        }
    };
}
