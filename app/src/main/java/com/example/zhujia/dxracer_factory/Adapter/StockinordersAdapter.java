package com.example.zhujia.dxracer_factory.Adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhujia.dxracer_factory.Activity.BatchwarehouseDetail;
import com.example.zhujia.dxracer_factory.Activity.DetailsActivity;
import com.example.zhujia.dxracer_factory.Activity.Stockinorder;
import com.example.zhujia.dxracer_factory.Activity.Stockinorderitem;
import com.example.zhujia.dxracer_factory.Activity.Stockinorders;
import com.example.zhujia.dxracer_factory.Data.BatchwareData;
import com.example.zhujia.dxracer_factory.Data.Dict;
import com.example.zhujia.dxracer_factory.Data.ScheduData;
import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.BaseRecyclerAdapter;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtility;
import com.example.zhujia.dxracer_factory.Tools.Net.IHttpCallBack;
import com.example.zhujia.dxracer_factory.Tools.universalimageloader.core.DisplayImageOptions;
import com.example.zhujia.dxracer_factory.Tools.universalimageloader.core.ImageLoader;
import com.example.zhujia.dxracer_factory.Tools.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by DXSW5 on 2017/9/6.
 */

public class StockinordersAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder> implements View.OnClickListener  {

    private List<BatchwareData> data;
    public Stockinorders context;
    private int type=0;
    private SharedPreferences sharedPreferences;
    private String business_id,departmentId,departmentPersonName,departmentPersonSession;
    Map<String,String> params;
    private Spinner set_forklift;
    ProgressDialog progressDialog;
    List<Dict> dicts = new ArrayList<Dict>();
    private StockinordersAdapter.OnitemClickListener onitemClickListener=null;
    private String forkliftPersonId;
    private  View views;
    private LinearLayout lin3;
    AlertDialog dialog;
    private PopupWindow pop;//pop弹窗
    @SuppressLint("WrongConstant")
    public StockinordersAdapter(Stockinorders context1, List<BatchwareData>data){
        this.context=context1;
        this.data=data;
        sharedPreferences =context1.getSharedPreferences("Session", Context.MODE_APPEND);
        business_id=sharedPreferences.getString("business_id","");
        departmentPersonSession=sharedPreferences.getString("departmentPersonSession","");
        departmentId=sharedPreferences.getString("departmentId","");
        departmentPersonName=sharedPreferences.getString("personCode","")+" "+sharedPreferences.getString("realName","");
        Log.e("TAG", "StockinorderAdapter: "+ departmentPersonSession);
    }
    @Override
    public void onClick(View view) {
        if(onitemClickListener!=null){
            onitemClickListener.onItemClick(view,(int)view.getTag());
        }
    }

    public void setOnitemClickListener(StockinordersAdapter.OnitemClickListener onitemClickListener) {
        this.onitemClickListener = onitemClickListener;
    }

    public static interface OnitemClickListener{
        void onItemClick(View view, int position);
    }


    //点击切换布局的时候通过这个方法设置type
    public void setType(int type) {
        this.type = type;
    }

    @Override
    //用来获取当前项Item是哪种类型的布局
    public int getItemViewType(int position) {
        return type;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View baseView;
        if (viewType == 0) {

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stockinorders_data, parent, false);
            StockinordersAdapter.LinearViewHolder linearViewHolder = new StockinordersAdapter.LinearViewHolder(baseView);
            baseView.setOnClickListener(this);
            return linearViewHolder;

        }
        else {

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedulingsheet_data, parent, false);
            StockinordersAdapter.GridViewHolder gridViewHolder = new StockinordersAdapter.GridViewHolder(baseView);
            baseView.setOnClickListener(this);
            return gridViewHolder;
        }

    }



    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position, List<Map<String, Object>> data) {

    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position) {
        if (type==0){
            final StockinordersAdapter.LinearViewHolder linearViewHolder= (StockinordersAdapter.LinearViewHolder) holder;
            linearViewHolder.stockInOrderNo.setText(data.get(position).getStockInOrderNo());
            linearViewHolder.orderNo.setText(data.get(position).getOrderNo());
            linearViewHolder.purchaseOrderNo.setText(data.get(position).getPurchaseOrderNo());
            linearViewHolder.plateNumber.setText(data.get(position).getPlateNumber());
            linearViewHolder.sendTime.setText(data.get(position).getSendTime());
            linearViewHolder.planProductionDate.setText(data.get(position).getPlanProductionDate());
            if(data.get(position).getStockInStatus().equals("created")){
                linearViewHolder.stockInStatus.setText("待收货");
            }
            if(data.get(position).getStockInStatus().equals("working")){
                linearViewHolder.stockInStatus.setText("待入库");
            }
            if(data.get(position).getStockInStatus().equals("finished")){
                linearViewHolder.stockInStatus.setText("已入库");
            }
            if(data.get(position).getStockInStatus().equals("canceled")){
                linearViewHolder.stockInStatus.setText("已取消");
            }
            if(data.get(position).getStockInStatus().equals("returned")){
                linearViewHolder.stockInStatus.setText("已退货");
            }

            if(data.get(position).getManual().equals("Y")){
                linearViewHolder.manual.setImageDrawable(context.getResources().getDrawable(R.mipmap.shi));
            }else {
                linearViewHolder.manual.setImageDrawable(context.getResources().getDrawable(R.mipmap.fou));
            }
            linearViewHolder.supplierNo.setText(data.get(position).getSupplierNo());
            linearViewHolder.receivePerson.setText(data.get(position).getReceivePerson());
            linearViewHolder.forkliftPerson.setText(data.get(position).getForkliftPerson());
            linearViewHolder.stockInTime.setText(data.get(position).getStockInTime());

            linearViewHolder.lin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,BatchwarehouseDetail.class);
                    intent.putExtra("stockInOrderId",data.get(position).getId());
                    context.startActivity(intent);
                }
            });


           /*
            if(row.stockInStatus != "finished") {
                $("#cancel_finished_stockinorder_btn").attr("disabled", true);
            }
            if(row.stockInStatus == "finished" || row.stockInStatus == "canceled" || row.stockInStatus == "returned") {
                $("#set_forklift_person_btn").attr("disabled", true);
                $("#set_location_btn").attr("disabled", true);
                $("#change_receiver_btn").attr("disabled", true);
            }*/
            if(data.get(position).getForkliftPersonId().equals("")|| data.get(position).getForkliftPersonId().equals("null")) {
                linearViewHolder.ruku.setEnabled(false);
                linearViewHolder.ruku.setBackgroundColor(context.getResources().getColor(R.color.base));
            }else {
                linearViewHolder.ruku.setEnabled(true);
                linearViewHolder.ruku.setBackgroundColor(context.getResources().getColor(R.color.btn_info));
            }
            if(data.get(position).getStockInStatus().equals("finished") || data.get(position).getStockInStatus().equals("canceled")|| data.get(position).getStockInStatus().equals("returned")) {
                linearViewHolder.setcha.setEnabled(false);
                linearViewHolder.setruku.setEnabled(false);
                linearViewHolder.setcha.setBackgroundColor(context.getResources().getColor(R.color.base));
                linearViewHolder.setruku.setBackgroundColor(context.getResources().getColor(R.color.base));

            }else {
                linearViewHolder.setcha.setEnabled(true);
                linearViewHolder.setruku.setEnabled(true);
                linearViewHolder.setcha.setBackgroundColor(context.getResources().getColor(R.color.btn_info));
                linearViewHolder.setruku.setBackgroundColor(context.getResources().getColor(R.color.btn_info));
            }

            //设置叉车工
            linearViewHolder.setcha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadchose(business_id,data.get(position).getId());
                    View view=(LinearLayout)context.getLayoutInflater().inflate(R.layout.dialogs,null);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    TextView orderno=(TextView)view.findViewById(R.id.orderNo);
                    TextView stockInOrderNo=(TextView)view.findViewById(R.id.stockInOrderNo);
                    TextView purchaseOrderNo=(TextView)view.findViewById(R.id.purchaseOrderNo);
                    TextView supplierNo=(TextView)view.findViewById(R.id.supplierNo);
                    set_forklift=(Spinner)view.findViewById(R.id.set_forklift);
                    set_forklift.setOnItemSelectedListener(listener);
                    orderno.setText(data.get(position).getOrderNo());
                    stockInOrderNo.setText(data.get(position).getStockInOrderNo());
                    purchaseOrderNo.setText(data.get(position).getPurchaseOrderNo());
                    supplierNo.setText(data.get(position).getSupplierNo());
                    builder.setTitle("选择叉车工");
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

                                    progressDialog = new ProgressDialog(context,
                                            R.style.AppTheme_Dark_Dialog);
                                    progressDialog.setIndeterminate(true);
                                    progressDialog.setMessage("正在保存...");
                                    progressDialog.show();
                                    params=new HashMap<>();
                                    params.put("businessId",business_id);
                                    params.put("id",data.get(position).getId());
                                    params.put("forkliftPersonId",forkliftPersonId);
                                    params.put("departmentPersonName",departmentPersonName);
                                    HttpUtility.doPostAsyn("/servlet/stock/stockinorder/set_forklift", params, new IHttpCallBack() {
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
                                                        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
                                                        context.onRefresh();
                                                        progressDialog.dismiss();
                                                        dialog.dismiss();
                                                        Looper.loop();

                                                    }else {
                                                        Looper.prepare();
                                                        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
                                                        progressDialog.dismiss();
                                                        Looper.loop();
                                                    }
                                                }catch (JSONException e){
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    },mHandler,context);

                            }
                        });
                    }
                }
            });




            //设置存放位置
            linearViewHolder.setruku.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View view=(LinearLayout)inflater.inflate(R.layout.setloaction,null);
                    builder.setView(view);
                    lin3=(LinearLayout)view.findViewById(R.id.gruop);
                    loaddata(data.get(position).getId());
                    TextView orderno=(TextView)view.findViewById(R.id.orderNo);
                    TextView stockInOrderNo=(TextView)view.findViewById(R.id.stockInOrderNo);
                    TextView purchaseOrderNo=(TextView)view.findViewById(R.id.purchaseOrderNo);
                    TextView supplierNo=(TextView)view.findViewById(R.id.supplierNo);
                    orderno.setText(data.get(position).getOrderNo());
                    stockInOrderNo.setText(data.get(position).getStockInOrderNo());
                    purchaseOrderNo.setText(data.get(position).getPurchaseOrderNo());
                    supplierNo.setText(data.get(position).getSupplierNo());
                    builder.setTitle("设置存放位置");

                    builder.setNegativeButton("关闭", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // TODO Auto-generated method stub
                            //	提示信息
                        }
                    });
                    builder.setPositiveButton("保存",null);
                    dialog=builder.create();
                    //	Diglog的显示
                    if(dialog.getButton(AlertDialog.BUTTON_POSITIVE)!=null){
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                progressDialog = new ProgressDialog(context,
                                        R.style.AppTheme_Dark_Dialog);
                                progressDialog.setIndeterminate(true);
                                progressDialog.setMessage("正在保存...");
                                progressDialog.show();
                                params=new HashMap<>();
                                params.put("businessId",business_id);
                                params.put("id",data.get(position).getId());
                                params.put("forkliftPersonId",forkliftPersonId);
                                params.put("departmentPersonName",departmentPersonName);
                                HttpUtility.doPostAsyn("/servlet/stock/stockinorder/set_forklift", params, new IHttpCallBack() {
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
                                                    Toast.makeText(context,message,Toast.LENGTH_LONG).show();
                                                    context.onRefresh();
                                                    progressDialog.dismiss();
                                                    dialog.dismiss();
                                                    Looper.loop();

                                                }else {
                                                    Looper.prepare();
                                                    Toast.makeText(context,message,Toast.LENGTH_LONG).show();
                                                    progressDialog.dismiss();
                                                    Looper.loop();
                                                }
                                            }catch (JSONException e){
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                },mHandler,context);

                            }
                        });
                    }

                    dialog.show();
                    dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

                }
            });


            //入库
            linearViewHolder.ruku.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(business_id,data.get(position).getId());
                }
            });
        }
    }

    private void showDialog(final String business_id, final String id) {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setTitle("提示！");
        builder.setMessage("确认入库单已入库吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                params=new HashMap<>();
                params.put("businessId",business_id);
                params.put("id",id);
                HttpUtility.doPostAsyn("/servlet/stock/stockinorder/stock_in", params, new IHttpCallBack() {
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
                                    Toast.makeText(context,message,Toast.LENGTH_LONG).show();
                                    context.onRefresh();
                                    Looper.loop();

                                }else {
                                    Looper.prepare();
                                    Toast.makeText(context,message,Toast.LENGTH_LONG).show();
                                    Looper.loop();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    }
                },mHandler,context);
            }
        });
        builder.show();
    }


    private void loaddata(String stockInOrderId){

        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("stockInOrderId",stockInOrderId);
        HttpUtility.doPostAsyn("/servlet/stock/stockinorderitem/item/list", params, new IHttpCallBack() {
            @Override
            public void onRequestComplete(String result, Handler handler, String errcode) {
                if ((null == result) || (result.equals(""))) {
                    // 网络连接异常
                    mHandler.sendEmptyMessage(9);

                }else {


                    Message msg=Message.obtain(
                            handler,1,result
                    );
                    mHandler.sendMessage(msg);

                }
            }
        },mHandler,context);
    }

    Spinner.OnItemSelectedListener listener=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            forkliftPersonId= String.valueOf(((Dict)set_forklift.getSelectedItem()).getId());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    //加载叉车工
    private void loadchose(String businessId,String id){
        params=new HashMap<>();
        params.put("businessId",businessId);
        params.put("id",id);
        params.put("departmentId",departmentId);
        HttpUtility.doPostAsyn("/servlet/stock/stockinorder/set_forklift/inittext", params, new IHttpCallBack() {
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
                                handler,0,result
                        );
                        mHandler.sendMessage(msg);

                    }

                }
            }
        },mHandler,context);


    }
    @SuppressLint("HandlerLeak")
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try{
                switch (msg.what) {

                    case 0:
                        //返回item类型数据
                        JSONObject reslutJSONObject=new JSONObject(msg.obj.toString());
                        JSONObject rows=reslutJSONObject.getJSONObject("rows");
                        JSONArray receiverList=rows.getJSONArray("receiverList");
                        dicts.clear();
                        for(int i=0;i<receiverList.length();i++){
                            JSONObject object=receiverList.getJSONObject(i);
                            String configName=object.getString("personCode")+" "+object.getString("realName");
                            int id=object.getInt("id");
                            dicts.add(new Dict(id,configName));
                            ArrayAdapter<Dict> arrAdapterpay3 = new ArrayAdapter<Dict>(context, R.layout.simple_spinner_item,dicts);
                            //设置样式
                            arrAdapterpay3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            set_forklift.setAdapter(arrAdapterpay3);
                        }
                        break;
                    case 1:
                        JSONArray reslutJSONObject1=new JSONArray(msg.obj.toString());
                        for(int j=0;j<reslutJSONObject1.length();j++){
                            JSONObject object=reslutJSONObject1.getJSONObject(j);
                            View view1= LayoutInflater.from(context).inflate(R.layout.layout,null);
                            TextView partNo=(TextView) view1.findViewById(R.id.partNo);
                            TextView quantity=(TextView)view1.findViewById(R.id.quantity);
                            final EditText location=(EditText)view1.findViewById(R.id.location);
                            partNo.setText(object.getString("partNo"));
                            quantity.setText(object.getString("quantity"));
                            if(!object.isNull("location")){
                                location.setText(object.getString("location"));
                            }
                            lin3.addView(view1);

                        }
                        break;
                    default:
                        Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
                        break;
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    };
    public static class LinearViewHolder extends BaseRecyclerViewHolder {

        private TextView stockInOrderNo,orderNo,purchaseOrderNo,plateNumber,sendTime,planProductionDate,stockInStatus,supplierNo,receivePerson,forkliftPerson,stockInTime;
        private Button setcha,setruku,ruku;
        private LinearLayout lin;
        private ImageView manual;
        public LinearViewHolder(View itemView) {
            super(itemView);
            stockInOrderNo=(TextView)itemView.findViewById(R.id.stockInOrderNo);
            orderNo=(TextView)itemView.findViewById(R.id.orderNo);
            purchaseOrderNo=(TextView)itemView.findViewById(R.id.purchaseOrderNo);
            plateNumber=(TextView)itemView.findViewById(R.id.plateNumber);
            purchaseOrderNo=(TextView)itemView.findViewById(R.id.purchaseOrderNo);
            sendTime=(TextView)itemView.findViewById(R.id.sendTime);
            planProductionDate=(TextView)itemView.findViewById(R.id.planProductionDate);
            stockInStatus=(TextView)itemView.findViewById(R.id.stockInStatus);
            setcha=(Button)itemView.findViewById(R.id.setcha);
            setruku=(Button)itemView.findViewById(R.id.setruku);
            ruku=(Button)itemView.findViewById(R.id.ruku);
            lin=(LinearLayout)itemView.findViewById(R.id.lin_btn);
            manual=(ImageView)itemView.findViewById(R.id.manual);
            supplierNo=(TextView)itemView.findViewById(R.id.supplierNo);
            receivePerson=(TextView)itemView.findViewById(R.id.receivePerson);
            forkliftPerson=(TextView)itemView.findViewById(R.id.forkliftPerson);
            stockInTime=(TextView)itemView.findViewById(R.id.stockInTime);
        }
    }

    public static class GridViewHolder extends BaseRecyclerViewHolder {


        public GridViewHolder(View itemView) {
            super(itemView);

        }
    }

}