package com.example.zhujia.dxracer_factory.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhujia.dxracer_factory.Adapter.AllmaterialAdapter;
import com.example.zhujia.dxracer_factory.Adapter.BillentryAdapter;
import com.example.zhujia.dxracer_factory.Adapter.EmployeelistAdapter;
import com.example.zhujia.dxracer_factory.Adapter.FormaterialAdapter;
import com.example.zhujia.dxracer_factory.Adapter.OrderAdapter;
import com.example.zhujia.dxracer_factory.Adapter.OrderitemAdapter;
import com.example.zhujia.dxracer_factory.Adapter.PendingshipmentAdapter;
import com.example.zhujia.dxracer_factory.Adapter.ProductionplanlogAdapter;
import com.example.zhujia.dxracer_factory.Adapter.PurchaseplanitemAdapter;
import com.example.zhujia.dxracer_factory.Adapter.SchedulingsheetAdapter;
import com.example.zhujia.dxracer_factory.Adapter.StockinorderAdapter;
import com.example.zhujia.dxracer_factory.Adapter.StockinordersAdapter;
import com.example.zhujia.dxracer_factory.Data.AllData;
import com.example.zhujia.dxracer_factory.Data.BatchwareData;
import com.example.zhujia.dxracer_factory.Data.Dict;
import com.example.zhujia.dxracer_factory.Data.MData;
import com.example.zhujia.dxracer_factory.Data.ScheduData;
import com.example.zhujia.dxracer_factory.Data.SpotData;
import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.ListDataSave;
import com.example.zhujia.dxracer_factory.Tools.Net.Constant;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtility;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtils;
import com.example.zhujia.dxracer_factory.Tools.Net.IHttpCallBack;
import com.example.zhujia.dxracer_factory.Tools.OnLoadMoreListener;
import com.example.zhujia.dxracer_factory.Tools.OnRefreshListener;
import com.example.zhujia.dxracer_factory.Tools.SuperRefreshRecyclerView;
import com.example.zhujia.dxracer_factory.Tools.insertComma;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhujia on 2017/11/22.
 *员工列表
 *
 */

public class Employeelist extends AppCompatActivity implements View.OnClickListener,OnRefreshListener,OnLoadMoreListener {


    ListDataSave dataSave;
    ListDataSave dataSave1;
    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private String business_id,departmentPersonSession;
    Map<String,String> params;
    private int pageindex=1;
    private int istouch=0;
    boolean hasMoreData;
    private Handler mHandler;
    private CoordinatorLayout seach_view;
    private LinearLayout lin;
    private ViewGroup.LayoutParams lp;
    private List<MData> mListData=new ArrayList<>();
    private EditText dealerName,orderNo,field7;
    JSONObject reslutJSONObject;
    JSONArray contentjsonarry;
    JSONObject contentjsonobject;
    private EmployeelistAdapter adapter;
    private EditText personCode,realName,phone,bqq,enterpriseMailbox;
    private  int total;
    private Spinner sex,departmentId,positionId;
    private String STA,departmentid,positionid,department,position;
    private Intent intent;
    private  int DID,POID;
    private TextView texts;
    private static ArrayAdapter<Dict> arrAdapterpay3;
    private String url;
    List<AllData> dicts = new ArrayList<AllData>();
    List<Dict>dictList=new ArrayList<>();
    List<Dict>dictList1=new ArrayList<>();
    private SuperRefreshRecyclerView recyclerView;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employeelist);
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
        departmentPersonSession=sharedPreferences.getString("departmentPersonId","");
        dataSave = new ListDataSave(getApplicationContext(), "departmentPositionList");
        dataSave1 = new ListDataSave(getApplicationContext(), "departmentPositionList1");
        initUI();
        load();
        loaddata();//加载列表数据
        adapter=new EmployeelistAdapter(Employeelist.this,getData());


    }



    private void load(){
        params=new HashMap<>();
        params.put("businessId",business_id);
        HttpUtility.doPostAsyn("/servlet/organization/departmentperson/initpage", params, new IHttpCallBack() {
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
        },mHandler,Employeelist.this);
    }

    private void initUI(){

        //初始化
        recyclerView= (SuperRefreshRecyclerView)findViewById(R.id.recyclerview);
        recyclerView.init(this,this);
        recyclerView.setRefreshEnabled(true);
        recyclerView.setLoadingMoreEnable(true);
        recyclerView.setHasFixedSize(true);
        seach_view=(CoordinatorLayout)findViewById(R.id.seach_view);
        personCode=(EditText)findViewById(R.id.personCode);
        realName=(EditText)findViewById(R.id.realName);
        phone=(EditText)findViewById(R.id.phone);
        bqq=(EditText)findViewById(R.id.bqq);
        enterpriseMailbox=(EditText)findViewById(R.id.enterpriseMailbox);

        sex=(Spinner)findViewById(R.id.sex);
        sex.setOnItemSelectedListener(listener);
        departmentId=(Spinner)findViewById(R.id.departmentId);
        departmentId.setOnItemSelectedListener(listener2);
        positionId=(Spinner)findViewById(R.id.positionId);
        positionId.setOnItemSelectedListener(listener1);
        dicts.add(new AllData("1","请选择"));
        dicts.add(new AllData("M","男"));
        dicts.add(new AllData("W","女"));
        ArrayAdapter<AllData> arrAdapterpay3 = new ArrayAdapter<AllData>(getApplicationContext(), R.layout.simple_spinner_item,dicts);
        //设置样式
        arrAdapterpay3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sex.setAdapter(arrAdapterpay3);
    }




    //性别
    Spinner.OnItemSelectedListener listener=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            STA=((AllData)sex.getSelectedItem()).getStr();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    //所属部门
    Spinner.OnItemSelectedListener listener1=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            positionid= String.valueOf(((Dict)positionId.getSelectedItem()).getId());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    //所属岗位
    Spinner.OnItemSelectedListener listener2=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            departmentid= String.valueOf(((Dict)departmentId.getSelectedItem()).getId());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    private void loaddata(){

        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("page", String.valueOf(pageindex));
        params.put("personCode",personCode.getText().toString());
        params.put("realName",realName.getText().toString());
        params.put("phone",phone.getText().toString());
        params.put("bqq",bqq.getText().toString());
        params.put("enterpriseMailbox",enterpriseMailbox.getText().toString());
        if(STA==null||STA.equals("1")){
            params.put("sex","");
        }else {
            params.put("sex",STA);
        }

        if(departmentid==null||departmentid.equals("0")){
            params.put("departmentId","");
        }else {
            params.put("departmentId",departmentid);
        }
       if(positionid==null||positionid.equals("0")){
           params.put("positionId","");
       }else {
           params.put("positionId",positionid);
       }

        params.put("status","Y");

        new HttpUtils().post(Constant.APPURLS+"/servlet/organization/departmentperson/list",params,new HttpUtils.HttpCallback() {
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
                Toast.makeText(Employeelist.this, "error", Toast.LENGTH_LONG).show();
            }
        });
    }


    //加载更多
    private void initItemMoreData() {
        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("page", String.valueOf(pageindex+1));
        params.put("personCode",personCode.getText().toString());
        params.put("realName",realName.getText().toString());
        params.put("phone",phone.getText().toString());
        params.put("bqq",bqq.getText().toString());
        params.put("enterpriseMailbox",enterpriseMailbox.getText().toString());
        if(STA==null||STA.equals("1")){
            params.put("sex","");
        }else {
            params.put("sex",STA);
        }

        if(departmentid==null||departmentid.equals("0")){
            params.put("id","");
        }else {
            params.put("id",departmentid);
        }
        if(positionid==null||positionid.equals("0")){
            params.put("positionId","");
        }else {
            params.put("positionId",positionid);
        }
        params.put("status","Y");

        HttpUtility.doPostAsyn("/servlet/organization/departmentperson/list", params, new IHttpCallBack() {
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
                        if(!resulutJsonobj.isNull("rows")){
                            contentjsonarry=resulutJsonobj.getJSONArray("rows");
                            if(contentjsonarry.length()<0){
                                hasMoreData=false;
                            }
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
        },null,Employeelist.this);
    }

    private void fillDataToList(JSONObject data) throws JSONException {

        if(!data.isNull("rows")){
            contentjsonarry=data.getJSONArray("rows");
            MData rechargData=null;
            for(int i=0;i<contentjsonarry.length();i++){
                rechargData=new MData();
                JSONObject object=contentjsonarry.getJSONObject(i);
                rechargData.setId(object.getInt("id"));
                rechargData.setFile(object.getString("file"));
                rechargData.setDepartmentId(object.getString("departmentId"));
                rechargData.setPositionId(object.getString("positionId"));
                rechargData.setRealName(object.getString("realName"));

                rechargData.setPersonCode(object.getString("personCode"));

                rechargData.setSex(object.getString("sex"));
                rechargData.setPhone(object.getString("phone"));
                if(!object.isNull("bqq")){
                    rechargData.setBqq(object.getString("bqq"));
                }else {
                    rechargData.setBqq("-");
                }
                if(!object.isNull("enterpriseMailbox")){
                    rechargData.setEnterpriseMailbox(object.getString("enterpriseMailbox"));
                }else {
                    rechargData.setEnterpriseMailbox("-");
                }


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
                            JSONObject object=new JSONObject(msg.obj.toString());
                            JSONObject rows=object.getJSONObject("rows");

                            JSONArray departmentPositionList=rows.getJSONArray("departmentPositionList");
                            dictList.clear();
                            dictList.add(new Dict(0,"请选择"));
                            for(int i=0;i<departmentPositionList.length();i++){
                                JSONObject object1=departmentPositionList.getJSONObject(i);
                                String positionName=object1.getString("positionName");
                                int id=object1.getInt("id");
                                dictList.add(new Dict(id,positionName));
                                arrAdapterpay3 = new ArrayAdapter<Dict>(getApplicationContext(), R.layout.simple_spinner_item,dictList);
                                //设置样式
                                arrAdapterpay3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                positionId.setAdapter(arrAdapterpay3);
                            }
                            dataSave.setDataList("dictList", dictList);

                            JSONArray departmentList=rows.getJSONArray("departmentList");
                            dictList1.clear();
                            dictList1.add(new Dict(0,"请选择"));
                            for(int i=0;i<departmentList.length();i++){
                                JSONObject object1=departmentList.getJSONObject(i);
                                String positionName=object1.getString("departmentCode")+" "+object1.getString("departmentName");
                                int id=object1.getInt("id");
                                dictList1.add(new Dict(id,positionName));
                                arrAdapterpay3 = new ArrayAdapter<Dict>(getApplicationContext(), R.layout.simple_spinner_item,dictList1);
                                //设置样式
                                arrAdapterpay3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                departmentId.setAdapter(arrAdapterpay3);
                            }
                            dataSave1.setDataList("dictList1", dictList1);

                            break;
                        case 1:
                            //返回item类型数据
                            reslutJSONObject=new JSONObject(msg.obj.toString());
                            mListData.clear();
                            fillDataToList(reslutJSONObject);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            recyclerView.showData();
                            recyclerView.setRefreshing(false);
                            break;
                        case 2:// 解析返回mode类型数据
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
                            Toast.makeText(Employeelist.this, "网络异常", Toast.LENGTH_SHORT).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
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
                lp.height=((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics()));
                seach_view.setLayoutParams(lp);
                item.setIcon(R.drawable.up);
                istouch=1;

               //backgroundAlpha(0.5f);

            }else {
                lp= seach_view.getLayoutParams();
                lp.height=((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics()));
                seach_view.setLayoutParams(lp);
                item.setIcon(R.drawable.down);
                istouch=0;
               // backgroundAlpha(1f);

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
                adapter.notifyDataSetChanged();
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


