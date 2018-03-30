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
 *假日设置
 */

public class Productionholiday extends Fragment implements View.OnClickListener,OnRefreshListener,OnLoadMoreListener {


    private SharedPreferences sharedPreferences;
    private String business_id,departmentPersonSession,departmentPersonName;
    Map<String,String> params;
    private int pageindex=1;
    private int istouch=0;
    boolean hasMoreData;
    private Handler mHandler;
    private LinearLayout seach_view;
    private ViewGroup.LayoutParams lp;
    private List<BatchwareData> mListData=new ArrayList<>();
    private EditText holidayWeek;
    private TextView holidayDay,holidayDay1;
    JSONObject reslutJSONObject;
    JSONArray contentjsonarry;
    JSONObject contentjsonobject;
    private ProductionholidayAdapter adapter;
    private  int total;
    private Intent intent;
    private View view;
    private SuperRefreshRecyclerView recyclerView;
    private java.util.Calendar cal;
    private int year,month,day;
    private FloatingActionButton Add_btn; //新增按钮

    @SuppressLint("WrongConstant")

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.productionholiday,container,false);

        intent=getActivity().getIntent();
        setHasOptionsMenu(true);
        sharedPreferences =getActivity().getSharedPreferences("Session", Context.MODE_APPEND);
        business_id=sharedPreferences.getString("business_id","");
        departmentPersonSession=sharedPreferences.getString("departmentPersonId","");
        departmentPersonName=sharedPreferences.getString("personCode","")+" "+sharedPreferences.getString("realName","");
        initUI();
        loaddata();//加载列表数据
        getDate();//获取当前日期
        adapter=new ProductionholidayAdapter(getActivity(),getData());
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
        holidayDay=(TextView) view.findViewById(R.id.holidayDay);
        holidayWeek=(EditText)view.findViewById(R.id.holidayWeek);
        holidayDay.setOnClickListener(this);
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
        params.put("sorttype","desc");
        params.put("sort","holidayDay");
        params.put("departmentPersonSessionId",departmentPersonSession);
        params.put("holidayDay",holidayDay.getText().toString());
        params.put("holidayWeek",holidayWeek.getText().toString());

        HttpUtility.doPostAsyn("/servlet/production/productionholiday/list", params, new IHttpCallBack() {
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
        params.put("sort","holidayDay");
        params.put("departmentPersonSessionId",departmentPersonSession);
        params.put("holidayDay",holidayDay.getText().toString());
        params.put("holidayWeek",holidayWeek.getText().toString());
        HttpUtility.doPostAsyn("/servlet/production/productionholiday/list", params, new IHttpCallBack() {
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
                        reslutJSONObject=resulutJsonobj.getJSONObject("rows");
                        contentjsonarry=reslutJSONObject.getJSONArray("resultList");
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

        reslutJSONObject=data.getJSONObject("rows");
        contentjsonarry=reslutJSONObject.getJSONArray("resultList");
        BatchwareData rechargData=null;
        for(int i=0;i<contentjsonarry.length();i++){
            rechargData=new BatchwareData();
            JSONObject object=contentjsonarry.getJSONObject(i);
            rechargData.setId(object.getString("id"));
            rechargData.setBusinessId(object.getString("businessId"));
            rechargData.setHolidayDay(object.getString("holidayDay"));
            rechargData.setHolidayWeek(object.getString("holidayWeek"));
            mListData.add(rechargData);
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

    @Override
    public void onClick(View v) {

        if(v==holidayDay){
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
                        holidayDay.setText(startime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            };
            DatePickerDialog dialog=new DatePickerDialog(getActivity(),DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,listener,year,month,day);
            dialog.show();
        }

        if(v==Add_btn){
            //新增

            View view=(LinearLayout)getActivity().getLayoutInflater().inflate(R.layout.layout5,null);
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            holidayDay1=(TextView)view.findViewById(R.id.holidayDay);
            holidayDay1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            String date=(i+"-"+(++i1)+"-"+i2);
                            DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                            Date date1 = null;
                            try {
                                date1 = format1.parse(date);
                                String startime = format1.format(date1);
                                holidayDay1.setText(startime);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    DatePickerDialog dialog=new DatePickerDialog(getActivity(),DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,listener,year,month,day);
                    dialog.show();
                }
            });
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
                        params.put("holidayDay",holidayDay1.getText().toString());
                        HttpUtility.doPostAsyn("/servlet/production/productionholiday/add", params, new IHttpCallBack() {
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
                                        String result_code=resulutJsonobj.getString("result_code");
                                        String message=resulutJsonobj.getString("result_msg");
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
                holidayDay.setText("");
                holidayWeek.setText("");
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
