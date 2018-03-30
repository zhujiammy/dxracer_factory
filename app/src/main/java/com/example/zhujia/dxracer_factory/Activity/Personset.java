package com.example.zhujia.dxracer_factory.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhujia.dxracer_factory.Data.Dict;
import com.example.zhujia.dxracer_factory.Data.ScheduData;
import com.example.zhujia.dxracer_factory.LoginActivitys;
import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.Net.Constant;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtility;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtils;
import com.example.zhujia.dxracer_factory.Tools.Net.IHttpCallBack;
import com.example.zhujia.dxracer_factory.Tools.OnLoadMoreListener;
import com.example.zhujia.dxracer_factory.Tools.OnRefreshListener;
import com.example.zhujia.dxracer_factory.Tools.SuperRefreshRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhujia on 2017/11/22.
 *
 * 人员配置
 */

public class Personset extends AppCompatActivity implements View.OnClickListener,OnRefreshListener,OnLoadMoreListener {


    int cra;
    private Toolbar toolbar;
    ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    private String business_id,supplierId,departmentId;
    Map<String,String> params;
    List<Dict> dicts = new ArrayList<Dict>();
    private int pageindex=1;
    private int istouch=0;
    boolean hasMoreData;
    private List<ScheduData> mListData=new ArrayList<>();
    JSONObject reslutJSONObject;
    JSONArray contentjsonarry;
    JSONObject contentjsonobject;
    List<String>itemId =new ArrayList<>();
    List<String>personIds=new ArrayList<>();
    List<String>list=new ArrayList<>();
    private  int total;
    private Intent intent;
    private LinearLayout group;
    private  ArrayAdapter<Dict> arrAdapterpay3;
    private SuperRefreshRecyclerView recyclerView;
    private TextView stepName,modelCraftProcedurename,modelCraftTypename;
    private ImageView stepImg;
    private int id;
    private List<Spinner>spinners=new ArrayList<>();
    View view1;
    private Spinner fzr;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personset);
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

        initUI();
        loaddata();//加载列表数据
        loadchose(business_id);


    }


    private void initUI(){
        group=(LinearLayout)findViewById(R.id.group);

    }

    private void loadchose(String businessId){
        params=new HashMap<>();
        params.put("businessId",businessId);
        params.put("departmentId",departmentId);
      /*  HttpUtility.doPostAsyn("/servlet/productionconfig/productionorderpersondetail/add/inittext", params, new IHttpCallBack() {
            @Override
            public void onRequestComplete(String result, Handler handler, String errcode) {
                if ((null == result) || (result.equals(""))) {
                    // 网络连接异常
                    mHandler.sendEmptyMessage(9);

                }else {

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
            }
        },mHandler,getApplicationContext());*/

        new HttpUtils().post(Constant.APPURLS+"/servlet/productionconfig/productionorderpersondetail/add/inittext",params,new HttpUtils.HttpCallback() {

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

    private void loaddata(){

        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("orderPersonId",intent.getStringExtra("orderPersonId"));
        params.put("craftId",intent.getStringExtra("craftId"));
        Log.e("TAG", "loaddata: "+intent.getStringExtra("orderPersonId")+""+ intent.getStringExtra("craftId"));
      /*  HttpUtility.doPostAsyn("/servlet/productionconfig/productionorderpersondetail/list", params, new IHttpCallBack() {
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
        },mHandler,Personset.this);*/

        new HttpUtils().post(Constant.APPURLS+"/servlet/productionconfig/productionorderpersondetail/list",params,new HttpUtils.HttpCallback() {

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


    //加载更多
    private void initItemMoreData() {
        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("page", String.valueOf(pageindex+1));
        params.put("orderId",intent.getStringExtra("orderid"));
/*        HttpUtility.doPostAsyn("/servlet/productionconfig/productionorderpersondetail/list", params, new IHttpCallBack() {
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
                        fillDataToList(contentjsonarry);

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        },null,Personset.this);*/

        new HttpUtils().post(Constant.APPURLS+"/servlet/productionconfig/productionorderpersondetail/list",params,new HttpUtils.HttpCallback() {

            @Override
            public void onSuccess(String data) {
                // TODO Auto-generated method stub
                com.example.zhujia.dxracer_factory.Tools.Log.printJson("tag",data,"header");
                if ((null == data) || (data.equals(""))) {
                    // 网络连接异常

                    mHandler.sendEmptyMessage(9);

                }else {
                    JSONObject resulutJsonobj;

                    try
                    {

                        resulutJsonobj=new JSONObject(data);
                        contentjsonarry=contentjsonobject.getJSONArray("resultList");
                        if(contentjsonarry.length()<0){
                            hasMoreData=false;
                        }
                        pageindex=pageindex+1;
                        hasMoreData=true;
                        fillDataToList(contentjsonarry);

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    private void fillDataToList(final JSONArray data) throws JSONException {

        for(int i=0;i<data.length();i++){
            view1= LayoutInflater.from(getApplicationContext()).inflate(R.layout.personset_data,null);
            stepName=(TextView)view1.findViewById(R.id.stepName);
            modelCraftProcedurename=(TextView)view1.findViewById(R.id.modelCraftProcedurename);
            modelCraftTypename=(TextView)view1.findViewById(R.id.modelCraftTypename);
            stepImg=(ImageView)view1.findViewById(R.id.stepImg);
            LinearLayout lin_btn=(LinearLayout)view1.findViewById(R.id.lin_btn);
            fzr=(Spinner)view1.findViewById(R.id.fzr);
            spinners.add(fzr);
            fzr.setAdapter(arrAdapterpay3);
            final int finalI1 = i;
            JSONObject object=data.getJSONObject(i);
            JSONObject modelCraftBom=object.getJSONObject("modelCraftBom");
            //rechargData.setBusiness_id(modelCraftBom.getString("businessId"));
            //rechargData.setId(modelCraftBom.getString("id"));
            //rechargData.setStencilId(modelCraftBom.getString("stencilId"));
            JSONObject modelCraftStep=modelCraftBom.getJSONObject("modelCraftStep");
            itemId.add(modelCraftBom.getString("id"));
            if(modelCraftStep.getString("stepImg")!=null){
                Glide.with(getApplicationContext()).load(Constant.APPURLIMG+modelCraftStep.getString("stepImg")).into(stepImg);
            }else {
                stepImg.setImageDrawable(getApplication().getResources().getDrawable(R.mipmap.img_def));
            }
            stepName.setText(modelCraftStep.getString("stepName"));
            JSONObject modelCraftProcedure=modelCraftBom.getJSONObject("modelCraftProcedure");
            modelCraftProcedurename.setText(modelCraftProcedure.getString("name"));
            JSONObject modelCraftType=modelCraftProcedure.getJSONObject("modelCraftType");
            modelCraftTypename.setText(modelCraftType.getString("name"));
            if(!object.isNull("productionOrderPersonDetail")){
                JSONObject productionOrderPersonDetail=object.getJSONObject("productionOrderPersonDetail");
                id=productionOrderPersonDetail.getInt("personId");
            }

            int c=arrAdapterpay3.getCount();
            if(cra!=0){
                for(int j=0;j<c;j++){
                    if(id==arrAdapterpay3.getItem(j).getId()){
                        fzr.setAdapter(arrAdapterpay3);
                        fzr.setSelection(j,true);
                        personIds.add(String.valueOf(id));
                    }
                }
            }else {
                personIds.add(String.valueOf(0));
            }

            final int finalI = i;

            fzr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    cra= ((Dict)spinners.get(finalI).getSelectedItem()).getId();
             /*       personIds.add(cra);
                    personIds.remove(personIds.size()-1);*/
                    Log.e("TAG", "onItemSelected: "+cra );
                    list.clear();
                    if(cra!=0){
                        for(int j=0;j<dicts.size();j++){
                            if(dicts.get(j).getId()==cra){
                                personIds.remove(finalI1);
                                personIds.add(finalI1, String.valueOf(cra));

                            }

                        }

                        list.addAll(personIds);
                        Log.e("TAG", "parms: "+ listToString(list,','));
                    }



                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });




            group.addView(view1);

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
        // as you specify a parent activity in AndroidManifest.xml.s
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.save_btn) {
                if(cra!=0){
                    progressDialog = new ProgressDialog(Personset.this,R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("正在保存.....");
                    progressDialog.show();
                    params=new HashMap<>();
                    params.put("businessId",business_id);
                    params.put("orderPersonId",intent.getStringExtra("orderPersonId"));
                    params.put("craftId",intent.getStringExtra("craftId"));
                    params.put("itemIds",listToString(itemId,','));
                    params.put("personIds",listToString(personIds,','));

                    // Log.e("TAG", "parms: "+ personIds);
                    HttpUtility.doPostAsyn("/servlet/productionconfig/productionorderpersondetail/bind", params, new IHttpCallBack() {
                        @Override
                        public void onRequestComplete(String result, Handler handler, String errcode) {
                            if ((null == result) || (result.equals(""))) {
                                // 网络连接异常

                                mHandler.sendEmptyMessage(9);

                            }else {


                                Message msg=Message.obtain(
                                        handler,3,result
                                );
                                mHandler.sendMessage(msg);

                            }
                        }
                    },null,Personset.this);
                    Log.e("TAG", "personIds: "+ listToString(personIds,','));
                }else {
                    Toast.makeText(getApplicationContext(),"所有步骤必须选择对应的人，请完善！",Toast.LENGTH_LONG).show();
                }




        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("HandlerLeak")

        private  Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                try{
                    switch (msg.what) {

                        case 0:
                            //返回item类型数据
                            contentjsonarry=new JSONArray(msg.obj.toString());
                            fillDataToList(contentjsonarry);

                            break;
                        case 2:
                            //返回item类型数据
                            JSONObject reslutJSONObject=new JSONObject(msg.obj.toString());
                            JSONObject rows=reslutJSONObject.getJSONObject("rows");
                            JSONArray receiverList=rows.getJSONArray("personList");
                            dicts.clear();
                            dicts.add(new Dict(0,"请选择"));
                            for(int i=0;i<receiverList.length();i++){
                                JSONObject object=receiverList.getJSONObject(i);
                                String personCode=object.getString("personCode")+" "+object.getString("realName");
                                int id=object.getInt("id");
                                dicts.add(new Dict(id,personCode));
                              arrAdapterpay3 = new ArrayAdapter<Dict>(Personset.this, R.layout.simple_spinner_item,dicts);
                                //设置样式
                                arrAdapterpay3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            }

                            break;
                        case 3:
                            JSONObject reslutJSONObjects=new JSONObject(msg.obj.toString());
                            String result_code=reslutJSONObjects.getString("code");
                            String message=reslutJSONObjects.getString("message");
                            if(result_code.equals("success")){

                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                                onRefresh();
                                finish();

                            }else {
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();

                            }
                            break;



                        default:
                            Toast.makeText(Personset.this, "网络异常", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };




    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mListData.clear();
                pageindex=1;
                loaddata();
            }
        },1000);
    }

    @Override
    public void onLoadMore() {
        initItemMoreData();
    }

    public String listToString(List list, char separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(separator);
        }
        return sb.toString().substring(0,sb.toString().length()-1);
    }
}
