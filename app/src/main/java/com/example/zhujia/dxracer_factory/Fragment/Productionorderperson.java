package com.example.zhujia.dxracer_factory.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhujia.dxracer_factory.Adapter.AllmaterialAdapter;
import com.example.zhujia.dxracer_factory.Adapter.BatchorderAdapter;
import com.example.zhujia.dxracer_factory.Adapter.BillentryAdapter;
import com.example.zhujia.dxracer_factory.Adapter.FormaterialAdapter;
import com.example.zhujia.dxracer_factory.Adapter.OrderitemAdapter;
import com.example.zhujia.dxracer_factory.Adapter.PendingshipmentAdapter;
import com.example.zhujia.dxracer_factory.Adapter.ProductionholidayAdapter;
import com.example.zhujia.dxracer_factory.Adapter.ProductionorderpersonAdapter;
import com.example.zhujia.dxracer_factory.Adapter.ProductionplanlogAdapter;
import com.example.zhujia.dxracer_factory.Adapter.PurchaseplanitemAdapter;
import com.example.zhujia.dxracer_factory.Adapter.SchedulingsheetAdapter;
import com.example.zhujia.dxracer_factory.Adapter.StockinorderAdapter;
import com.example.zhujia.dxracer_factory.Adapter.StockinordersAdapter;
import com.example.zhujia.dxracer_factory.Data.AllData;
import com.example.zhujia.dxracer_factory.Data.BatchwareData;
import com.example.zhujia.dxracer_factory.Data.Dict;
import com.example.zhujia.dxracer_factory.Data.ScheduData;
import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtility;
import com.example.zhujia.dxracer_factory.Tools.Net.IHttpCallBack;
import com.example.zhujia.dxracer_factory.Tools.OnLoadMoreListener;
import com.example.zhujia.dxracer_factory.Tools.OnRefreshListener;
import com.example.zhujia.dxracer_factory.Tools.SuperRefreshRecyclerView;
import com.example.zhujia.dxracer_factory.Tools.insertComma;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
 *
 *小组管理
 */

public class Productionorderperson extends Fragment implements View.OnClickListener,OnRefreshListener,OnLoadMoreListener {


    private SharedPreferences sharedPreferences;
    private String business_id,departmentPersonSession,departmentPersonName,departmentId,personCode;
    Map<String,String> params;
    private int pageindex=1;
    private int istouch=0;
    boolean hasMoreData;
    private Handler mHandler;
    List<Dict> dicts = new ArrayList<Dict>();
    private LinearLayout seach_view;
    private ViewGroup.LayoutParams lp;
    private List<BatchwareData> mListData=new ArrayList<>();
    private EditText crafId,configName,createUser;
    JSONObject reslutJSONObject;
    JSONArray contentjsonarry;
    JSONObject contentjsonobject;
    private ProductionorderpersonAdapter adapter;
    private  int total;
    private Intent intent;
    private View view;
    private SuperRefreshRecyclerView recyclerView;
    private java.util.Calendar cal;
    private int year,month,day;
    private FloatingActionButton Add_btn; //新增按钮

    private EditText configNames;
    private Spinner crafIds;
    private String cra;

    @SuppressLint("WrongConstant")

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.productionorderperson,container,false);

        intent=getActivity().getIntent();
        setHasOptionsMenu(true);
        sharedPreferences =getActivity().getSharedPreferences("Session", Context.MODE_APPEND);
        business_id=sharedPreferences.getString("business_id","");
        departmentPersonSession=sharedPreferences.getString("departmentPersonId","");
        personCode=sharedPreferences.getString("personCode","");
        departmentPersonName=sharedPreferences.getString("personCode","")+" "+sharedPreferences.getString("realName","");
        departmentId=sharedPreferences.getString("departmentId","");
        initUI();
        loaddata();//加载列表数据
        getDate();//获取当前日期
        adapter=new ProductionorderpersonAdapter(this,getData());
        return view;
    }

    private void initUI(){

        //初始化
        recyclerView= (SuperRefreshRecyclerView)view.findViewById(R.id.recyclerview);
        recyclerView.init(this,this);
        recyclerView.setRefreshEnabled(true);
        recyclerView.setLoadingMoreEnable(true);
        recyclerView.setHasFixedSize(true);
        seach_view=(LinearLayout)view.findViewById(R.id.seach_view);
        configName=(EditText) view.findViewById(R.id.configName);
        crafId=(EditText)view.findViewById(R.id.crafId);
        createUser=(EditText)view.findViewById(R.id.createUser);
        Add_btn=(FloatingActionButton)view.findViewById(R.id.Add_btn);
        Add_btn.setOnClickListener(this);

    }



    @SuppressLint("WrongConstant")
    private void getDate(){
        cal= java.util.Calendar.getInstance();
        year=cal.get(java.util.Calendar.YEAR);
        month=cal.get(java.util.Calendar.MONTH);
        day=cal.get(java.util.Calendar.DAY_OF_MONTH);

    }

    private void loaddata(){

        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("page", String.valueOf(pageindex));
        params.put("sorttype","asc");
        params.put("sort","undefined");
        params.put("departmentPersonSessionId",departmentPersonSession);
        params.put("crafId",crafId.getText().toString());
        params.put("configName",configName.getText().toString());
        params.put("createUser",createUser.getText().toString());

        HttpUtility.doPostAsyn("/servlet/productionconfig/productionorderperson/list", params, new IHttpCallBack() {
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
        },mHandler,getActivity());
    }


    //加载更多
    private void initItemMoreData() {
        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("page", String.valueOf(pageindex+1));
        params.put("sorttype","asc");
        params.put("sort","undefined");
        params.put("departmentPersonSessionId",departmentPersonSession);
        params.put("crafId",crafId.getText().toString());
        params.put("configName",configName.getText().toString());
        params.put("createUser",createUser.getText().toString());
        HttpUtility.doPostAsyn("/servlet/productionconfig/productionorderperson/list", params, new IHttpCallBack() {
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




                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        },null,getActivity());
    }

    private void fillDataToList(JSONObject data) throws JSONException {

        if(!data.isNull("rows")){
            contentjsonarry=data.getJSONArray("rows");
            BatchwareData rechargData=null;
            for(int i=0;i<contentjsonarry.length();i++){
                rechargData=new BatchwareData();
                JSONObject object=contentjsonarry.getJSONObject(i);
                rechargData.setId(object.getString("id"));
                rechargData.setBusinessId(object.getString("businessId"));
                rechargData.setConfigName(object.getString("configName"));
                JSONObject modelCraftBomStencil=object.getJSONObject("modelCraftBomStencil");
                rechargData.setStencilNo(modelCraftBomStencil.getString("stencilNo"));
                rechargData.setStencilDesc(modelCraftBomStencil.getString("stencilDesc"));
                JSONObject modelCraftType=modelCraftBomStencil.getJSONObject("modelCraftType");
                rechargData.setCrafId(object.getString("crafId"));
                rechargData.setName(modelCraftType.getString("name"));
                mListData.add(rechargData);
            }
        }




    }


    @SuppressLint("HandlerLeak")
    private List<BatchwareData>getData(){
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
                            //返回item类型数据
                            JSONObject reslutJSONObject=new JSONObject(msg.obj.toString());
                            JSONObject rows=reslutJSONObject.getJSONObject("rows");
                            JSONArray receiverList=rows.getJSONArray("modelCraftBomStencilList");
                            dicts.clear();
                            for(int i=0;i<receiverList.length();i++){
                                JSONObject object=receiverList.getJSONObject(i);
                                JSONObject modelCraftType=object.getJSONObject("modelCraftType");
                                String configName=object.getString("stencilNo")+" "+modelCraftType.getString("name");
                                int id=object.getInt("id");
                                dicts.add(new Dict(id,configName));
                                ArrayAdapter<Dict> arrAdapterpay3 = new ArrayAdapter<Dict>(getActivity(), R.layout.simple_spinner_item,dicts);
                                //设置样式
                                arrAdapterpay3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                crafIds.setAdapter(arrAdapterpay3);
                            }
                            break;
                        case 3:
                            adapter.notifyDataSetChanged();
                            recyclerView.setLoadingMore(false);
                            break;
                        case 4:
                            adapter.notifyDataSetChanged();
                            break;
                        default:
                            Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };
        return mListData;
    }

    Spinner.OnItemSelectedListener listener=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            cra= String.valueOf(((Dict)crafIds.getSelectedItem()).getId());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    public void onClick(View v) {

        if(v==Add_btn){
            //新增
            loadchose(business_id);
            View view=(LinearLayout)getActivity().getLayoutInflater().inflate(R.layout.layout6,null);
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            configNames=(EditText) view.findViewById(R.id.configName);
            crafIds=(Spinner)view.findViewById(R.id.crafId);
            crafIds.setOnItemSelectedListener(listener);

            builder.setTitle("新增");
            builder.setView(view);
            builder.setNegativeButton("关闭", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    //	提示信息
                }
            });
            builder.setPositiveButton("保存",null);
            final AlertDialog dialog=builder.create();
            //	Diglog的显示
            dialog.show();
            if(dialog.getButton(AlertDialog.BUTTON_POSITIVE)!=null){
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        params=new HashMap<>();
                        params.put("businessId",business_id);
                        params.put("personCode",personCode);
                        params.put("configName",configNames.getText().toString());
                        params.put("crafId",cra);
                        HttpUtility.doPostAsyn("/servlet/productionconfig/productionorderperson/add", params, new IHttpCallBack() {
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
                                        String result_code=resulutJsonobj.getString("code");
                                        String message=resulutJsonobj.getString("message");
                                        if(result_code.equals("success")){
                                            Looper.prepare();
                                            Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
                                            onRefresh();
                                            dialog.dismiss();
                                            Looper.loop();

                                        }else {
                                            Looper.prepare();
                                            Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
                                            Looper.loop();
                                        }
                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }
                                }
                            }
                        },mHandler,getActivity());

                    }
                });
            }

        }

    }

    private void loadchose(String businessId){
        params=new HashMap<>();
        params.put("businessId",businessId);
        params.put("departmentId",departmentId);
        HttpUtility.doPostAsyn("/servlet/productionconfig/productionorderperson/add/inittext", params, new IHttpCallBack() {
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
        },mHandler,getActivity());


    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main,menu);
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
                lp.height=((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ViewGroup.LayoutParams.WRAP_CONTENT, getResources().getDisplayMetrics()));
                seach_view.setLayoutParams(lp);
                item.setIcon(R.drawable.up);
                istouch=1;

            }else {
                lp= seach_view.getLayoutParams();
                lp.height=((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics()));
                seach_view.setLayoutParams(lp);
                item.setIcon(R.drawable.down);
                istouch=0;

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
                crafId.setText("");
                configName.setText("");
                createUser.setText("");
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
