package com.example.zhujia.dxracer_factory.Fragment;

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
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhujia on 2017/11/22.
 *批量订单
 *
 */

public class Batchorder extends Fragment implements View.OnClickListener,OnRefreshListener,OnLoadMoreListener {


    private SharedPreferences sharedPreferences;
    private String business_id,departmentPersonSession;
    Map<String,String> params;
    private int pageindex=1;
    private int istouch=0;
    boolean hasMoreData;
    private Handler mHandler;
    private LinearLayout seach_view;
    private ViewGroup.LayoutParams lp;
    private List<BatchwareData> mListData=new ArrayList<>();
    private EditText dealerName,orderNo,field7;
    JSONObject reslutJSONObject;
    JSONArray contentjsonarry;
    JSONObject contentjsonobject;
    private BatchorderAdapter adapter;
    private  int total;
    private Spinner status;
    private String STA;
    private Intent intent;
    List<AllData> dicts = new ArrayList<AllData>();
    private View view;
    private SuperRefreshRecyclerView recyclerView;
    @SuppressLint("WrongConstant")

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.batchorder,container,false);

        intent=getActivity().getIntent();
        setHasOptionsMenu(true);
        sharedPreferences =getActivity().getSharedPreferences("Session", Context.MODE_APPEND);
        business_id=sharedPreferences.getString("business_id","");
        departmentPersonSession=sharedPreferences.getString("departmentPersonId","");
        initUI();
        loaddata();//加载列表数据
        adapter=new BatchorderAdapter(getActivity(),getData());
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
        dealerName=(EditText)view.findViewById(R.id.dealerName);
        orderNo=(EditText)view.findViewById(R.id.orderNo);
        field7=(EditText)view.findViewById(R.id.field7);
        status=(Spinner)view.findViewById(R.id.status);
        status.setOnItemSelectedListener(listener);
        dicts.add(new AllData("1","请选择"));
        dicts.add(new AllData("confirmed","已确认订单"));
        dicts.add(new AllData("production","生产中订单"));
        dicts.add(new AllData("undelivery","待发货订单"));
        dicts.add(new AllData("delivery","已发货订单"));
        dicts.add(new AllData("cancel","已取消订单"));
        ArrayAdapter<AllData> arrAdapterpay3 = new ArrayAdapter<AllData>(getActivity(), R.layout.simple_spinner_item,dicts);
        //设置样式
        arrAdapterpay3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status.setAdapter(arrAdapterpay3);

    }




    //单据状态
    Spinner.OnItemSelectedListener listener=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            STA=((AllData)status.getSelectedItem()).getStr();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private void loaddata(){

        params=new HashMap<>();


        params.put("businessId",business_id);
        params.put("page", String.valueOf(pageindex));
        params.put("sorttype","desc");
        params.put("sort","createTime");

        if(STA==null||STA.equals("1")){
            params.put("orderStatus","");
        }else {
            params.put("orderStatus",STA);
        }
        params.put("departmentPersonSessionId",departmentPersonSession);
        params.put("dealerName",dealerName.getText().toString());
        params.put("orderNo",orderNo.getText().toString());
        params.put("field7",field7.getText().toString());

        HttpUtility.doPostAsyn("/servlet/order/batch/list", params, new IHttpCallBack() {
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
        params.put("sort","createTime");

        if(STA==null||STA.equals("1")){
            params.put("orderStatus","");
        }else {
            params.put("orderStatus",STA);
        }
        params.put("departmentPersonSessionId",departmentPersonSession);
        params.put("dealerName",dealerName.getText().toString());
        params.put("orderNo",orderNo.getText().toString());
        params.put("field7",field7.getText().toString());
        HttpUtility.doPostAsyn("/servlet/order/batch/list", params, new IHttpCallBack() {
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
            rechargData.setOrderNo(object.getString("orderNo"));
            rechargData.setDealerName(object.getString("dealerName"));
            rechargData.setStatus(object.getString("orderStatus"));
            rechargData.setContainer(object.getString("container"));
            rechargData.setPlanDeliveryDate(object.getString("planDeliveryDate"));
            rechargData.setActualDeliveryDate(object.getString("actualDeliveryDate"));
            rechargData.setField7(object.getString("field7"));
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
    public void onClick(View v) {


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
                orderNo.setText("");
                dealerName.setText("");
                field7.setText("");
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
