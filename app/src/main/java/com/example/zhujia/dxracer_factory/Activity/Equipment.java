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

import com.example.zhujia.dxracer_factory.Adapter.EquipmentAdapter;
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
 *所有设备
 *
 */

public class Equipment extends AppCompatActivity implements View.OnClickListener,OnRefreshListener,OnLoadMoreListener {


    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private String business_id,departmentId,departmentPersonId;
    Map<String,String> params;
    private int pageindex=1;
    boolean hasMoreData;
    List<AllData> dicts1 = new ArrayList<AllData>();
    List<AllData> dicts2 = new ArrayList<AllData>();
    private Handler mHandler;
    private List<MData> mListData=new ArrayList<>();
    JSONObject reslutJSONObject;
    JSONArray contentjsonarry;
    JSONObject contentjsonobject;
    private EquipmentAdapter adapter;
    private  int total;
    private Intent intent;
    private String SupplierNo;
    private SuperRefreshRecyclerView recyclerView;
    private TextView text,content,equipmentUseTime;
    private String type,status,equipmentCategoryId,equipmentSupplierId;
    private MyradionGroup radioGroup;
    private EditText equipmentNo,equipmentName,field2;
    private TextView dateTimeRange2;
    private Spinner equipmentSupplier,equipmentCategory;
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
        adapter=new EquipmentAdapter(Equipment.this,getData());

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
        text.setText("所有设备");
        content=(TextView)findViewById(R.id.content);
        customView = View.inflate(Equipment.this,R.layout.equipment_seachs, null);
        popWindow = new PopWindow.Builder(Equipment.this)
                .setStyle(PopWindow.PopWindowStyle.PopDown)
                .setView(customView)
                .create();
        cancel=(Button)customView.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        okbtn=(Button)customView.findViewById(R.id.okbtn);
        okbtn.setOnClickListener(this);
        equipmentNo=(EditText) customView.findViewById(R.id.equipmentNo);
        field2=(EditText)customView.findViewById(R.id.field2);
        equipmentName=(EditText) customView.findViewById(R.id.equipmentName);
        equipmentCategory=(Spinner) customView.findViewById(R.id.equipmentCategory);
        equipmentSupplier=(Spinner) customView.findViewById(R.id.equipmentSupplier);
        equipmentUseTime=(TextView)customView.findViewById(R.id.equipmentUseTime);
        equipmentUseTime.setOnClickListener(this);
        equipmentSupplier.setOnItemSelectedListener(listener1);
        equipmentCategory.setOnItemSelectedListener(listener);
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
            if(rb.getText().toString().equals("正常")){
                status="A";

            }else if(rb.getText().toString().equals("停用")){
                status="B";
            }

        }



    }


    private void  laodmyequipment(){
        params=new HashMap<>();
        params.put("businessId",business_id);
        new HttpUtils().post(Constant.APPURLS+"/servlet/equipment/equipment",params,new HttpUtils.HttpCallback() {

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
        params.put("equipmentNo",equipmentNo.getText().toString());
        params.put("equipmentName",equipmentName.getText().toString());
        if(equipmentCategoryId==null||equipmentCategoryId.equals("0")){
            params.put("equipmentCategoryId","");
        }else {
            params.put("equipmentCategoryId",equipmentCategoryId);
        }
        if(equipmentSupplierId==null||equipmentSupplierId.equals("0")){
            params.put("equipmentSupplierId","");
        }else {
            params.put("equipmentSupplierId",equipmentSupplierId);
        }
        params.put("field2",field2.getText().toString());
        params.put("equipmentUseTime",equipmentUseTime.getText().toString());
        params.put("equipmentManager","");
        if(status==null){
            params.put("equipmentStatus","");
        }else {
            params.put("equipmentStatus",status);
        }

        params.put("page", String.valueOf(pageindex));
        new HttpUtils().post(Constant.APPURLS+"/servlet/equipment/equipment/list",params,new HttpUtils.HttpCallback() {

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
        params.put("sorttype","asc");
        params.put("sort","undefined");
        params.put("equipmentNo",equipmentNo.getText().toString());
        params.put("equipmentName",equipmentName.getText().toString());
        if(equipmentCategoryId==null||equipmentCategoryId.equals("0")){
            params.put("equipmentCategoryId","");
        }else {
            params.put("equipmentCategoryId",equipmentCategoryId);
        }
        if(equipmentSupplierId==null||equipmentSupplierId.equals("0")){
            params.put("equipmentSupplierId","");
        }else {
            params.put("equipmentSupplierId",equipmentSupplierId);
        }
        params.put("field2",field2.getText().toString());
        params.put("equipmentUseTime",equipmentUseTime.getText().toString());
        params.put("equipmentManager","");
        if(status==null){
            params.put("equipmentStatus","");
        }else {
            params.put("equipmentStatus",status);
        }
        new HttpUtils().post(Constant.APPURLS+"/servlet/equipment/equipment/list",params,new HttpUtils.HttpCallback() {

            @Override
            public void onSuccess(String data) {
                // TODO Auto-generated method stub
                com.example.zhujia.dxracer_factory.Tools.Log.printJson("tag",data,"header");
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
                rechargData.setField3(object.getString("field3"));
                rechargData.setField4(object.getString("field4"));
                rechargData.setEquipmentNo(object.getString("equipmentNo"));
                rechargData.setEquipmentName(object.getString("equipmentName"));
                rechargData.setEquipmentColor(object.getString("equipmentColor"));
                if(!object.isNull("equipmentCategory")){
                    JSONObject equipmentCategory=object.getJSONObject("equipmentCategory");
                    rechargData.setCategoryName(equipmentCategory.getString("name"));
                }
                rechargData.setField1(object.getString("field1"));
                rechargData.setField2(object.getString("field2"));
                if(!object.isNull("departmentPerson1")){
                    JSONObject departmentPerson1=object.getJSONObject("departmentPerson1");
                    rechargData.setRealName(departmentPerson1.getString("realName"));
                }
                if(!object.isNull("equipmentSupplier")){
                    JSONObject equipmentSupplier=object.getJSONObject("equipmentSupplier");
                    rechargData.setSupplierName(equipmentSupplier.getString("supplierName"));
                }else {
                    rechargData.setSupplierName("-");
                }
                rechargData.setEquipmentUseTime(object.getString("equipmentUseTime"));
                rechargData.setEquipmentStatus(object.getString("equipmentStatus"));
                mListData.add(rechargData);
            }
        }


    }

    //设备类别
    Spinner.OnItemSelectedListener listener=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            equipmentCategoryId=((AllData)equipmentCategory.getSelectedItem()).getStr();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    //设备维修商
    Spinner.OnItemSelectedListener listener1=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            equipmentSupplierId=((AllData)equipmentSupplier.getSelectedItem()).getStr();
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
                            JSONArray equipmentCategoryList=reslutJSONObject.getJSONArray("equipmentCategoryList");
                            dicts1.add(new AllData("0","全部设备类别"));
                            for(int i=0;i<equipmentCategoryList.length();i++){
                                JSONObject object=equipmentCategoryList.getJSONObject(i);
                                dicts1.add(new AllData(object.getString("id"),object.getString("name")));
                                ArrayAdapter<AllData> arrAdapterpay3 = new ArrayAdapter<AllData>(getApplicationContext(), R.layout.simple_spinner_item,dicts1);
                                //设置样式
                                arrAdapterpay3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                equipmentCategory.setAdapter(arrAdapterpay3);
                            }

                            dicts2.add(new AllData("0","全部设备维修商"));
                            JSONArray equipmentSupplierList=reslutJSONObject.getJSONArray("equipmentSupplierList");
                            for(int i=0;i<equipmentSupplierList.length();i++){
                                JSONObject object=equipmentSupplierList.getJSONObject(i);
                                dicts2.add(new AllData(object.getString("id"),object.getString("supplierName")));
                                ArrayAdapter<AllData> arrAdapterpay3 = new ArrayAdapter<AllData>(getApplicationContext(), R.layout.simple_spinner_item,dicts2);
                                //设置样式
                                arrAdapterpay3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                equipmentSupplier.setAdapter(arrAdapterpay3);
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
                            Toast.makeText(Equipment.this, "网络异常", Toast.LENGTH_SHORT).show();
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


        if(v==equipmentUseTime){
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
                        equipmentUseTime.setText(startime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            };
            DatePickerDialog dialog=new DatePickerDialog(Equipment.this,DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,listener,year,month,day);
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
        getMenuInflater().inflate(R.menu.seach_menu, menu);
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
            intent=new Intent(getApplicationContext(),AddEquipment.class);
            intent.putExtra("type","1");
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
