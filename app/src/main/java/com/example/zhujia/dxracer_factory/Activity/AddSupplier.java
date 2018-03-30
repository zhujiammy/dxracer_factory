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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZHUJIA on 2018/1/11.
 *
 * 新增维修商
 */

public class AddSupplier extends AppCompatActivity {

    private TextView text1;
    private Toolbar toolbar;
    private EditText supplierCode,supplierName,supplierAddress,supplierTel,supplierConPerson,supplierConPersonTel,field1,field2,field3,field4;
    private Spinner status;
    private SharedPreferences sharedPreferences;
    private String business_id,departmentId,sta;
    List<AllData> dicts1 = new ArrayList<AllData>();
    Map<String,String> params;
    private Intent intent;
    private String url;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addsupplier);
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
        initUI();
    }

    private void initUI(){
        text1=(TextView)findViewById(R.id.text1);
        text1.setText("新增维修商");
        status=(Spinner)findViewById(R.id.status);
        status.setOnItemSelectedListener(listener);
        supplierCode=(EditText)findViewById(R.id.supplierCode);
        supplierName=(EditText)findViewById(R.id.supplierName);
        supplierAddress=(EditText)findViewById(R.id.supplierAddress);
        supplierTel=(EditText)findViewById(R.id.supplierTel);
        supplierConPerson=(EditText)findViewById(R.id.supplierConPerson);
        supplierConPersonTel=(EditText)findViewById(R.id.supplierConPersonTel);
        field1=(EditText)findViewById(R.id.field1);
        field2=(EditText)findViewById(R.id.field2);
        field3=(EditText)findViewById(R.id.field3);
        field4=(EditText)findViewById(R.id.field4);
        dicts1.add(new AllData("Y","正常"));
        dicts1.add(new AllData("N","停用"));
        ArrayAdapter<AllData> arrAdapterpay4 = new ArrayAdapter<AllData>(getApplicationContext(), R.layout.simple_spinner_item,dicts1);
        //设置样式
        arrAdapterpay4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status.setAdapter(arrAdapterpay4);
        if(intent.getStringExtra("type")!=null){
            url="/servlet/equipment/equipmentsupplier/update";
            supplierCode.setText(intent.getStringExtra("supplierCode"));
            supplierName.setText(intent.getStringExtra("supplierName"));
            supplierAddress.setText(intent.getStringExtra("supplierAddress"));
            supplierTel.setText(intent.getStringExtra("supplierTel"));
            supplierConPerson.setText(intent.getStringExtra("supplierConPerson"));
            supplierConPersonTel.setText(intent.getStringExtra("supplierConPersonTel"));
            if(intent.getStringExtra("field1").equals("null")){
                field1.setText("");
            }else {
                field1.setText(intent.getStringExtra("field1"));
            }

            if(intent.getStringExtra("field2").equals("null")){
                field2.setText("");
            }else {
                field2.setText(intent.getStringExtra("field2"));
            }
            if(intent.getStringExtra("field3").equals("null")){
                field3.setText("");
            }else {
                field3.setText(intent.getStringExtra("field3"));
            }
            if(intent.getStringExtra("field4").equals("null")){
                field4.setText("");
            }else {
                field4.setText(intent.getStringExtra("field4"));
            }

            if(intent.getStringExtra("status").equals("Y")){
                status.setSelection(0,true);
            }else {
                status.setSelection(1,true);
            }

        }else {
            url="/servlet/equipment/equipmentsupplier/add";
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

            if(TextUtils.isEmpty(supplierCode.getText().toString())){
                Toast.makeText(getApplicationContext(),"维修商代码不能为空",Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(supplierName.getText().toString())){
                Toast.makeText(getApplicationContext(),"维修商名称不能为空",Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(supplierAddress.getText().toString())){
                Toast.makeText(getApplicationContext(),"维修商联系地址不能为空",Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(supplierTel.getText().toString())){
                Toast.makeText(getApplicationContext(),"维修商联系电话不能为空",Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(supplierConPerson.getText().toString())){
                Toast.makeText(getApplicationContext(),"维修商联系人不能为空",Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(supplierConPersonTel.getText().toString())){
                Toast.makeText(getApplicationContext(),"维修商联系人手机不能为空",Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(field1.getText().toString())){
                Toast.makeText(getApplicationContext(),"开户银行不能为空",Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(field2.getText().toString())){
                Toast.makeText(getApplicationContext(),"银行账户不能为空",Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(field3.getText().toString())){
                Toast.makeText(getApplicationContext(),"银行账号不能为空",Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(field4.getText().toString())){
                Toast.makeText(getApplicationContext(),"预计付款天数不能为空",Toast.LENGTH_SHORT).show();
            }else {
                params=new HashMap<>();
                params.put("businessId",business_id);
                params.put("supplierCode",supplierCode.getText().toString());
                params.put("supplierName",supplierName.getText().toString());
                params.put("supplierAddress",supplierAddress.getText().toString());
                params.put("supplierTel",supplierTel.getText().toString());
                params.put("supplierConPerson",supplierConPerson.getText().toString());
                params.put("supplierConPersonTel",supplierConPersonTel.getText().toString());
                params.put("field1",field1.getText().toString());
                params.put("field2",field2.getText().toString());
                params.put("field3",field3.getText().toString());
                params.put("field4",field4.getText().toString());
                params.put("status",sta);
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
                },mHandler,AddSupplier.this);
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
                }
            }catch (JSONException e){
                e.printStackTrace();
            }

        }
    };
}
