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
 * 新增类别
 */

public class AddEqcategory extends AppCompatActivity {

    private TextView text1;
    private Toolbar toolbar;
    private EditText name;
    private SharedPreferences sharedPreferences;
    private String business_id,departmentId,sta;
    Map<String,String> params;
    private Intent intent;
    private String url;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addeqcategory);
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
        text1.setText("新增类别");
        name=(EditText)findViewById(R.id.name);
        if(intent.getStringExtra("type")!=null){
            url="/servlet/equipment/equipmentcategory/update";
            name.setText(intent.getStringExtra("name"));
        }else {
            url="/servlet/equipment/equipmentcategory/add";
        }

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

            if(TextUtils.isEmpty(name.getText().toString())){
                Toast.makeText(getApplicationContext(),"类别名称不能为空",Toast.LENGTH_SHORT).show();
            }else {
                params=new HashMap<>();
                params.put("businessId",business_id);
                params.put("name",name.getText().toString());
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
                },mHandler,AddEqcategory.this);
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
