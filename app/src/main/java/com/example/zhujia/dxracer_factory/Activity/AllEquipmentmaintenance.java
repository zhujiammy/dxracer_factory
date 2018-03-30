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
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhujia.dxracer_factory.Adapter.AllEquipmentmaintenanceAdapter;
import com.example.zhujia.dxracer_factory.Adapter.EquipmentmaintenanceAdapter;
import com.example.zhujia.dxracer_factory.Adapter.MyequipmentAdapter;
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
 *所有维修申请
 *
 */

public class AllEquipmentmaintenance extends AppCompatActivity implements View.OnClickListener,OnRefreshListener,OnLoadMoreListener {


    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private String business_id,departmentId,departmentPersonId;
    Map<String,String> params;
    private int pageindex=1;
    boolean hasMoreData;
    List<AllData> dicts1 = new ArrayList<AllData>();
    List<AllData> dicts2 = new ArrayList<AllData>();
    List<AllData> dicts3 = new ArrayList<AllData>();
    private Handler mHandler;
    private List<MData> mListData=new ArrayList<>();
    JSONObject reslutJSONObject;
    JSONArray contentjsonarry;
    JSONObject contentjsonobject;
    private AllEquipmentmaintenanceAdapter adapter;
    private  int total;
    private Intent intent;
    private String SupplierNo;
    private SuperRefreshRecyclerView recyclerView;
    private TextView text,content,equipmentUseTime;
    private String type,status,maintenancePeopleOutId,equipmentId,originatorId;
    private MyradionGroup radioGroup;
    private EditText field2;
    private TextView startTime,endTime;
    private Spinner equipment,maintenancePeopleOut,originator;
    private PopWindow popWindow;
    private   View customView;
    private Button cancel,okbtn;
    private int istouch=0;
    private java.util.Calendar cal;
    private int year,month,day;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_layout);
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

        type=intent.getStringExtra("type");
        initUI();
        laodmyequipment();
        loaddata();//加载列表数据
        getDate();//获取当前日期
        adapter=new AllEquipmentmaintenanceAdapter(AllEquipmentmaintenance.this,getData());
    }
    @SuppressLint("WrongConstant")
    private void getDate(){
        cal= java.util.Calendar.getInstance();
        year=cal.get(java.util.Calendar.YEAR);
        month=cal.get(java.util.Calendar.MONTH);
        day=cal.get(java.util.Calendar.DAY_OF_MONTH);

    }

    private void initUI(){
        //初始化
        recyclerView= (SuperRefreshRecyclerView)findViewById(R.id.recyclerview);
        recyclerView.init(this,this);
        recyclerView.setRefreshEnabled(true);
        recyclerView.setLoadingMoreEnable(true);
        recyclerView.setHasFixedSize(true);
        text=(TextView) findViewById(R.id.text1);
        text.setText("所有维修申请");
        content=(TextView)findViewById(R.id.content);
        customView = View.inflate(AllEquipmentmaintenance.this,R.layout.allequipmentmaintenance_seach, null);
        popWindow = new PopWindow.Builder(AllEquipmentmaintenance.this)
                .setStyle(PopWindow.PopWindowStyle.PopDown)
                .setView(customView)
                .create();
        cancel=(Button)customView.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        okbtn=(Button)customView.findViewById(R.id.okbtn);
        okbtn.setOnClickListener(this);

        field2=(EditText)customView.findViewById(R.id.field2);
        maintenancePeopleOut=(Spinner) customView.findViewById(R.id.maintenancePeopleOut);
        equipment=(Spinner) customView.findViewById(R.id.equipment);
        originator=(Spinner)customView.findViewById(R.id.originator);
        startTime=(TextView)customView.findViewById(R.id.startTime);
        startTime.setOnClickListener(this);
        endTime=(TextView)customView.findViewById(R.id.endTime);
        endTime.setOnClickListener(this);
        equipment.setOnItemSelectedListener(listener1);
        maintenancePeopleOut.setOnItemSelectedListener(listener);
        originator.setOnItemSelectedListener(listener2);
        radioGroup=(MyradionGroup)customView.findViewById(R.id.group);
        radioGroup.setOnCheckedChangeListener(new MyradionGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(MyradionGroup group, int checkedId) {
                selectRadioBtn();
            }
        });
    }




    private void selectRadioBtn(){
        RadioButton rb = (RadioButton)customView.findViewById(radioGroup.getCheckedRadioButtonId());

        if(rb!=null){
            if(rb.getText().toString().equals("新建")){
                status="A";
            }else if(rb.getText().toString().equals("已审核")){
                status="B";
            }
            else if(rb.getText().toString().equals("未通过")){
                status="C";
            }
            else if(rb.getText().toString().equals("维修成功")){
                status="D";
            }
            else if(rb.getText().toString().equals("维修失败")){
                status="E";
            }
            else if(rb.getText().toString().equals("已生成付款单")){
                status="F";
            }

        }



    }


    private void  laodmyequipment(){
        params=new HashMap<>();
        params.put("businessId",business_id);
        new HttpUtils().post(Constant.APPURLS+"/servlet/equipment/equipmentmaintenance",params,new HttpUtils.HttpCallback() {

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
        params.put("sorttype","asc");
        params.put("sort","undefined");
        params.put("field2",field2.getText().toString());
        if(equipmentId==null||equipmentId.equals("0")){
            params.put("equipmentId","");
        }else {
            params.put("equipmentId",equipmentId);
        }
        if(maintenancePeopleOutId==null||maintenancePeopleOutId.equals("0")){
            params.put("maintenancePeopleOut","");
        }else {
            params.put("maintenancePeopleOut",maintenancePeopleOutId);
        }
        params.put("startTime",startTime.getText().toString());

        if(originatorId==null||originatorId.equals("0")){
            params.put("originator","");
        }else {
            params.put("originator",originatorId);
        }
        params.put("endTime",endTime.getText().toString());
        if(status==null){
            params.put("status","");
        }else {
            params.put("status",status);
        }

        params.put("page", String.valueOf(pageindex));

        Log.e("TAG", "loaddata: "+params );
        new HttpUtils().post(Constant.APPURLS+"/servlet/equipment/equipmentmaintenance/list",params,new HttpUtils.HttpCallback() {
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
                        mHandler,0,data
                );
                mHandler.sendMessage(msg);
            }

            @Override
            public void onError(String msg) {
                // TODO Auto-generated method stub
                super.onError(msg);
                Toast.makeText(AllEquipmentmaintenance.this, "error", Toast.LENGTH_LONG).show();
            }
        });
    }




    //加载更多
    private void initItemMoreData() {
        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("page", String.valueOf(pageindex+1));
        params.put("sorttype","asc");
        params.put("sort","undefined");
        params.put("field2",field2.getText().toString());
        if(originatorId==null||originatorId.equals("0")){
            params.put("originator","");
        }else {
            params.put("originator",originatorId);
        }
        params.put("startTime",startTime.getText().toString());
        params.put("endTime",endTime.getText().toString());
        if(equipmentId==null||equipmentId.equals("0")){
            params.put("equipmentId","");
        }else {
            params.put("equipmentId",equipmentId);
        }
        if(maintenancePeopleOutId==null||maintenancePeopleOutId.equals("0")){
            params.put("maintenancePeopleOut","");
        }else {
            params.put("maintenancePeopleOut",maintenancePeopleOutId);
        }
        if(status==null){
            params.put("status","");
        }else {
            params.put("status",status);
        }
        new HttpUtils().post(Constant.APPURLS+"/servlet/equipment/equipmentmaintenance/list",params,new HttpUtils.HttpCallback() {
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
                        if(!resulutJsonobj.isNull("rows")){
                            contentjsonarry=resulutJsonobj.getJSONArray("rows");
                            if(contentjsonarry.length()<0){
                                hasMoreData=false;
                            }
                            pageindex=pageindex+1;
                            hasMoreData=true;
                            fillDataToList(resulutJsonobj);
                            if(!hasMoreData){
                                mHandler.sendEmptyMessage(4);
                            }else {
                                mHandler.sendEmptyMessage(3);
                            }
                        }else {
                            recyclerView.setLoadingMore(false);
                        }


                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }

        });
    }

    private void fillDataToList(JSONObject data) throws JSONException {

        if(!data.isNull("rows")){
            contentjsonarry=data.getJSONArray("rows");
            MData rechargData=null;
            for(int i=0;i<contentjsonarry.length();i++){
                rechargData=new MData();
                JSONObject object=contentjsonarry.getJSONObject(i);
                rechargData.setId(object.getInt("id"));
                rechargData.setField2(object.getString("field2"));
                JSONObject equipment=object.getJSONObject("equipment");
                rechargData.setEquipmentNo(equipment.getString("equipmentNo"));
                rechargData.setEquipmentId(equipment.getString("id"));
                rechargData.setEquipmentName(equipment.getString("equipmentName"));
                rechargData.setStartTime(object.getString("startTime"));
                JSONObject departmentPerson=object.getJSONObject("departmentPerson");
                rechargData.setRealName(departmentPerson.getString("realName"));
                JSONObject equipmentSupplier=object.getJSONObject("equipmentSupplier");
                rechargData.setSupplierName(equipmentSupplier.getString("supplierName"));
                rechargData.setContent(object.getString("content"));
                rechargData.setEndTime(object.getString("endTime"));
                rechargData.setCheckName(object.getString("checkName"));
                rechargData.setTotal(object.getString("total"));
                rechargData.setStatus(object.getString("status"));
                rechargData.setField3(equipment.getString("field3"));
                rechargData.setPaymentStatus(object.getString("paymentStatus"));
                rechargData.setField4(object.getString("field4"));
                rechargData.setField5(object.getString("field5"));
                rechargData.setField1(object.getString("field1"));
                mListData.add(rechargData);
            }
        }


    }

    //设备维修商
    Spinner.OnItemSelectedListener listener=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            equipmentId=((AllData)maintenancePeopleOut.getSelectedItem()).getStr();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    //设备编号
    Spinner.OnItemSelectedListener listener1=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            maintenancePeopleOutId=((AllData)equipment.getSelectedItem()).getStr();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    //设备编号
    Spinner.OnItemSelectedListener listener2=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            originatorId=((AllData)originator.getSelectedItem()).getStr();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

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
                        case 2:
                            reslutJSONObject=new JSONObject(msg.obj.toString());
                            JSONArray equipmentCategoryList=reslutJSONObject.getJSONArray("equipmentList");

                            dicts1.add(new AllData("0","全部设备编号"));
                            for(int i=0;i<equipmentCategoryList.length();i++){
                                JSONObject object=equipmentCategoryList.getJSONObject(i);
                                dicts1.add(new AllData(object.getString("id"),object.getString("equipmentNo")+"--"+object.getString("equipmentName")));
                                ArrayAdapter<AllData> arrAdapterpay3 = new ArrayAdapter<AllData>(getApplicationContext(), R.layout.simple_spinner_item,dicts1);
                                //设置样式
                                arrAdapterpay3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                maintenancePeopleOut.setAdapter(arrAdapterpay3);
                            }

                            dicts2.add(new AllData("0","全部设备维修商"));
                            JSONArray equipmentSupplierList=reslutJSONObject.getJSONArray("equipmentSupplierList");
                            for(int i=0;i<equipmentSupplierList.length();i++){
                                JSONObject object=equipmentSupplierList.getJSONObject(i);
                                dicts2.add(new AllData(object.getString("id"),object.getString("supplierName")));
                                ArrayAdapter<AllData> arrAdapterpay3 = new ArrayAdapter<AllData>(getApplicationContext(), R.layout.simple_spinner_item,dicts2);
                                //设置样式
                                arrAdapterpay3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                equipment.setAdapter(arrAdapterpay3);
                            }

                            dicts3.add(new AllData("0","请选择"));
                            JSONArray departmentPersonList=reslutJSONObject.getJSONArray("departmentPersonList");
                            for(int i=0;i<departmentPersonList.length();i++){
                                JSONObject object2=departmentPersonList.getJSONObject(i);
                                dicts3.add(new AllData(object2.getString("id"),object2.getString("realName")));
                                ArrayAdapter<AllData> arrAdapterpay3 = new ArrayAdapter<AllData>(getApplicationContext(), R.layout.simple_spinner_item,dicts3);
                                //设置样式
                                arrAdapterpay3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                originator.setAdapter(arrAdapterpay3);
                            }
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
                            Toast.makeText(AllEquipmentmaintenance.this, "网络异常", Toast.LENGTH_SHORT).show();
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


        if(v==okbtn){
            loaddata();
            popWindow.dismiss();
        }
        if(v==cancel){
            popWindow.dismiss();
        }


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
            DatePickerDialog dialog=new DatePickerDialog(AllEquipmentmaintenance.this,DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,listener,year,month,day);
            dialog.show();
        }

        if(v==endTime){
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
                        endTime.setText(startime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            };
            DatePickerDialog dialog=new DatePickerDialog(AllEquipmentmaintenance.this,DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,listener,year,month,day);
            dialog.show();
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
        if (id == R.id.seach_btn) {
            popWindow.show(customView);
        }
        if(id==R.id.add){
            intent=new Intent(getApplicationContext(),AddSupplier.class);
            startActivity(intent);

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
}
