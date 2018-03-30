package com.example.zhujia.dxracer_factory.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhujia.dxracer_factory.Adapter.BillentryAdapter;
import com.example.zhujia.dxracer_factory.Adapter.EquipmentsupplierAdapter;
import com.example.zhujia.dxracer_factory.Adapter.OrderitemAdapter;
import com.example.zhujia.dxracer_factory.Adapter.ProductionplanlogAdapter;
import com.example.zhujia.dxracer_factory.Adapter.PurchaseplanitemAdapter;
import com.example.zhujia.dxracer_factory.Adapter.SchedulingsheetAdapter;
import com.example.zhujia.dxracer_factory.Adapter.StockinorderAdapter;
import com.example.zhujia.dxracer_factory.Adapter.StockinordersAdapter;
import com.example.zhujia.dxracer_factory.Data.BatchwareData;
import com.example.zhujia.dxracer_factory.Data.Dict;
import com.example.zhujia.dxracer_factory.Data.MData;
import com.example.zhujia.dxracer_factory.Data.ScheduData;
import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.MyradionGroup;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtility;
import com.example.zhujia.dxracer_factory.Tools.Net.IHttpCallBack;
import com.example.zhujia.dxracer_factory.Tools.OnLoadMoreListener;
import com.example.zhujia.dxracer_factory.Tools.OnRefreshListener;
import com.example.zhujia.dxracer_factory.Tools.SuperRefreshRecyclerView;
import com.example.zhujia.dxracer_factory.Tools.insertComma;
import com.hmy.popwindow.PopWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhujia on 2017/11/22.
 *设备维修商
 *
 */

public class Equipmentsupplier extends AppCompatActivity implements View.OnClickListener,OnRefreshListener,OnLoadMoreListener {


    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private String business_id,departmentId;
    Map<String,String> params;
    private int pageindex=1;
    boolean hasMoreData;
    List<Dict> dicts = new ArrayList<Dict>();
    private Handler mHandler;
    private List<MData> mListData=new ArrayList<>();
    JSONObject reslutJSONObject;
    JSONArray contentjsonarry;
    JSONObject contentjsonobject;
    private EquipmentsupplierAdapter adapter;
    private  int total;
    private Intent intent;
    private String SupplierNo;
    private SuperRefreshRecyclerView recyclerView;
    private TextView text;
    private String type,status;
    private MyradionGroup radioGroup;
    private EditText supplierCode,supplierName,supplierConPerson,supplierConPersonTel;
    private TextView dateTimeRange2;
    private Spinner supplierNo;
    private PopWindow popWindow;
    private   View customView;
    private Button cancel,okbtn;
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
        type=intent.getStringExtra("type");
        initUI();
        loaddata();//加载列表数据
        adapter=new EquipmentsupplierAdapter(Equipmentsupplier.this,getData());

    }


    private void initUI(){

        //初始化
        recyclerView= (SuperRefreshRecyclerView)findViewById(R.id.recyclerview);
        recyclerView.init(this,this);
        recyclerView.setRefreshEnabled(true);
        recyclerView.setLoadingMoreEnable(true);
        recyclerView.setHasFixedSize(true);
        text=(TextView) findViewById(R.id.text1);
        text.setText("设备维修商");
        customView = View.inflate(Equipmentsupplier.this,R.layout.equipment_seach, null);
        popWindow = new PopWindow.Builder(Equipmentsupplier.this)
                .setStyle(PopWindow.PopWindowStyle.PopDown)
                .setView(customView)
                .create();
        cancel=(Button)customView.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        okbtn=(Button)customView.findViewById(R.id.okbtn);
        okbtn.setOnClickListener(this);
        supplierCode=(EditText) customView.findViewById(R.id.supplierCode);
        supplierName=(EditText) customView.findViewById(R.id.supplierName);
        supplierConPerson=(EditText) customView.findViewById(R.id.supplierConPerson);
        supplierConPersonTel=(EditText) customView.findViewById(R.id.supplierConPersonTel);
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
                status="Y";

            }else if(rb.getText().toString().equals("停用")){
                status="N";
            }

        }



    }


    private void loaddata(){

        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("sorttype","asc");
        params.put("sort","undefined");
        params.put("supplierCode",supplierCode.getText().toString());
        params.put("supplierName",supplierName.getText().toString());
        params.put("supplierConPerson",supplierConPerson.getText().toString());
        params.put("supplierConPersonTel",supplierConPersonTel.getText().toString());
        if(status==null){
            params.put("status","");
        }else {
            params.put("status",status);
        }

        params.put("page", String.valueOf(pageindex));

        HttpUtility.doPostAsyn("/servlet/equipment/equipmentsupplier/list", params, new IHttpCallBack() {
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
        },mHandler,Equipmentsupplier.this);
    }


    //加载更多
    private void initItemMoreData() {
        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("page", String.valueOf(pageindex+1));
        params.put("sorttype","asc");
        params.put("sort","undefined");
        params.put("supplierCode",supplierCode.getText().toString());
        params.put("supplierName",supplierName.getText().toString());
        params.put("supplierConPerson",supplierConPerson.getText().toString());
        params.put("supplierConPersonTel",supplierConPersonTel.getText().toString());
        if(status==null){
            params.put("status","");
        }else {
            params.put("status",status);
        }
        HttpUtility.doPostAsyn("/servlet/equipment/equipmentsupplier/list", params, new IHttpCallBack() {
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
        },null,Equipmentsupplier.this);
    }

    private void fillDataToList(JSONObject data) throws JSONException {

        if(!data.isNull("rows")){
            contentjsonarry=data.getJSONArray("rows");
            MData rechargData=null;
            for(int i=0;i<contentjsonarry.length();i++){
                rechargData=new MData();
                JSONObject object=contentjsonarry.getJSONObject(i);
                rechargData.setId(object.getInt("id"));
                rechargData.setSupplierCode(object.getString("supplierCode"));
                rechargData.setSupplierName(object.getString("supplierName"));
                rechargData.setSupplierAddress(object.getString("supplierAddress"));
                rechargData.setSupplierTel(object.getString("supplierTel"));
                rechargData.setSupplierConPerson(object.getString("supplierConPerson"));
                rechargData.setSupplierConPersonTel(object.getString("supplierConPersonTel"));
                rechargData.setStatus(object.getString("status"));
                rechargData.setField1(object.getString("field1"));
                rechargData.setField2(object.getString("field2"));
                rechargData.setField3(object.getString("field3"));
                rechargData.setField4(object.getString("field4"));
                mListData.add(rechargData);
            }
        }


    }

    Spinner.OnItemSelectedListener listener=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            SupplierNo=((Dict)supplierNo.getSelectedItem()).getText();
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
                        case 3:
                            adapter.notifyDataSetChanged();
                            recyclerView.setLoadingMore(false);
                            Toast.makeText(getApplicationContext(),"到底啦！",Toast.LENGTH_SHORT).show();
                            break;
                        case 4:
                            adapter.notifyDataSetChanged();
                            break;

                        default:
                            Toast.makeText(Equipmentsupplier.this, "网络异常", Toast.LENGTH_SHORT).show();
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

        if(v==dateTimeRange2){
            Intent intent=new Intent(getApplicationContext(), Main_ac.class);
            startActivity(intent);
        }

        if(v==okbtn){
            loaddata();
            popWindow.dismiss();
        }
        if(v==cancel){
            popWindow.dismiss();
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
        // as you specify a parent activity in AndroidManifest.xml.
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
