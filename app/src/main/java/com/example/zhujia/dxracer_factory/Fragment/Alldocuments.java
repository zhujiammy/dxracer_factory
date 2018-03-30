package com.example.zhujia.dxracer_factory.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhujia.dxracer_factory.Adapter.AlldocumentsAdapter;
import com.example.zhujia.dxracer_factory.Adapter.SchedulingsheetAdapter;
import com.example.zhujia.dxracer_factory.Data.AllData;
import com.example.zhujia.dxracer_factory.Data.Dict;
import com.example.zhujia.dxracer_factory.Data.ScheduData;
import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtility;
import com.example.zhujia.dxracer_factory.Tools.Net.IHttpCallBack;
import com.example.zhujia.dxracer_factory.Tools.OnLoadMoreListener;
import com.example.zhujia.dxracer_factory.Tools.OnRefreshListener;
import com.example.zhujia.dxracer_factory.Tools.SuperRefreshRecyclerView;

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
 * Created by zhujia on 2017/11/21.
 *
 * 所有单据
 */

public class Alldocuments extends Fragment implements View.OnClickListener,OnRefreshListener,OnLoadMoreListener {
    private LinearLayout seach_view;
    private ViewGroup.LayoutParams lp;
    private SharedPreferences sharedPreferences;
    private String business_id,supplierId;
    Map<String,String> params;
    private int pageindex=1;
    private int istouch=0;
    boolean hasMoreData;
    private Handler mHandler;
    private View view;
    List<AllData> dicts = new ArrayList<AllData>();
    private List<ScheduData> mListData=new ArrayList<>();
    JSONObject reslutJSONObject;
    JSONArray contentjsonarry;
    JSONObject contentjsonobject;
    private AlldocumentsAdapter adapter;
    private  int total;
    private Intent intent;
    private SuperRefreshRecyclerView recyclerView;
    private String username;
    private String DealerName;
    private java.util.Calendar cal;
    private TextView productionOrderNo,orderNo,productionDate,fcno;
    private Spinner status;
    private int year,month,day;
    private Button button;

    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.alldocuments,container,false);
        setHasOptionsMenu(true);
        intent=getActivity().getIntent();
        sharedPreferences =getActivity().getSharedPreferences("Session", Context.MODE_APPEND);
        business_id=sharedPreferences.getString("business_id","");
        username=sharedPreferences.getString("username","");
        supplierId=sharedPreferences.getString("supplierId","");
        initUI();
        loaddata();//加载列表数据
        getDate();//获取当前日期
        adapter=new AlldocumentsAdapter(getContext(),getData());
        return view;
    }

    private void initUI(){

        //初始化
        seach_view=(LinearLayout)view.findViewById(R.id.seach_view);
        recyclerView= (SuperRefreshRecyclerView)view.findViewById(R.id.recyclerview);
        recyclerView.init(this,this);
        recyclerView.setRefreshEnabled(true);
        recyclerView.setLoadingMoreEnable(true);
        button=(Button)view.findViewById(R.id.ok_btn);
        button.setVisibility(View.GONE);
        recyclerView.setHasFixedSize(true);
        productionOrderNo=(TextView)view.findViewById(R.id.productionOrderNo);
        orderNo=(TextView)view.findViewById(R.id.orderNo);
        productionDate=(TextView)view.findViewById(R.id.productionDate);
        productionDate.setOnClickListener(this);
        fcno=(TextView)view.findViewById(R.id.fcno);
        status=(Spinner)view.findViewById(R.id.status);
        status.setOnItemSelectedListener(listener);
        dicts.add(new AllData("1","请选择"));
        dicts.add(new AllData("create","待生产"));
        dicts.add(new AllData("producting","生产中"));
        dicts.add(new AllData("producted","已完成"));
        dicts.add(new AllData("finished","已入库"));
        ArrayAdapter<AllData> arrAdapterpay3 = new ArrayAdapter<AllData>(getActivity(), R.layout.simple_spinner_item,dicts);
        //设置样式
        arrAdapterpay3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status.setAdapter(arrAdapterpay3);
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
        params.put("sorttype","desc");
        params.put("sort","productionDate");
        params.put("productionOrderNo",productionOrderNo.getText().toString());
        params.put("orderNo",orderNo.getText().toString());
        params.put("productionDate",productionDate.getText().toString());
        params.put("fcno",fcno.getText().toString());

        if(DealerName==null||DealerName.equals("1")){
            params.put("status","");
        }else {
            params.put("status",DealerName);
        }
        HttpUtility.doPostAsyn("/servlet/production/productionorder/all/list", params, new IHttpCallBack() {
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
        params.put("sorttype","desc");
        params.put("sort","productionDate");
        params.put("productionOrderNo",productionOrderNo.getText().toString());
        params.put("orderNo",orderNo.getText().toString());
        params.put("productionDate",productionDate.getText().toString());
        params.put("fcno",fcno.getText().toString());
        if(DealerName==null||DealerName.equals("请选择")){
            params.put("status","");
        }else {
            params.put("status",DealerName);
        }
        HttpUtility.doPostAsyn("/servlet/production/productionorder/all/list", params, new IHttpCallBack() {
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
                        contentjsonarry=contentjsonobject.getJSONArray("list");
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
        contentjsonobject=data.getJSONObject("rows");

        if(!contentjsonobject.isNull("list")){
            contentjsonarry=contentjsonobject.getJSONArray("list");
            ScheduData rechargData=null;
            for(int i=0;i<contentjsonarry.length();i++){
                rechargData=new ScheduData();
                JSONObject object=contentjsonarry.getJSONObject(i);
                 rechargData.setId(object.getString("id"));
                 rechargData.setBusiness_id(object.getString("businessId"));
                 rechargData.setSearchMaterialPerson(object.getString("searchMaterialPerson"));
                 if(!object.isNull("searchMaterialScore")){
                     rechargData.setSearchMaterialScore(object.getString("searchMaterialScore"));
                 }
                 rechargData.setSearchMaterialStatus(object.getString("searchMaterialStatus"));
                 rechargData.setProductionOrderNo(object.getString("productionOrderNo"));
                 rechargData.setOrderId(object.getString("orderId"));
                 rechargData.setOrderNo(object.getString("orderNo"));
                 rechargData.setProductionDate(object.getString("productionDate"));
                 rechargData.setFcno(object.getString("fcno"));
                 rechargData.setQuantity(object.getString("quantity"));
                 rechargData.setStatus(object.getString("status"));
                 if(!object.isNull("field1")){
                     rechargData.setField1(object.getString("field1"));
                 }
                 if (!object.isNull("field4")){
                     rechargData.setField4(object.getString("field4"));
                 }

                mListData.add(rechargData);
            }
        }


    }

    @SuppressLint("HandlerLeak")
    private List<ScheduData>getData(){
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main,menu);
    }


    //经销商名称

    Spinner.OnItemSelectedListener listener=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            DealerName=((AllData)status.getSelectedItem()).getStr();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

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
                pageindex=1;
                productionOrderNo.setText("");
                orderNo.setText("");
                productionDate.setText("");
                fcno.setText("");
                loaddata();
            }
        },1000);
    }

    @Override
    public void onLoadMore() {
        initItemMoreData();
    }

    @Override
    public void onClick(View v) {

       if(v==productionDate){
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
                        productionDate.setText(startime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            };
            DatePickerDialog dialog=new DatePickerDialog(getActivity(),DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,listener,year,month,day);
            dialog.show();
        }
    }
}
