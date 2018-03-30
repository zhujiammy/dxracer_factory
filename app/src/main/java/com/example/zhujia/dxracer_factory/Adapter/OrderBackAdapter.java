package com.example.zhujia.dxracer_factory.Adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhujia.dxracer_factory.Activity.AddEquipment;
import com.example.zhujia.dxracer_factory.Activity.Equipment;
import com.example.zhujia.dxracer_factory.Activity.Myequipment;
import com.example.zhujia.dxracer_factory.Activity.OrderBack;
import com.example.zhujia.dxracer_factory.Activity.WebviewActivity;
import com.example.zhujia.dxracer_factory.Data.Dict;
import com.example.zhujia.dxracer_factory.Data.MData;
import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.BaseRecyclerAdapter;
import com.example.zhujia.dxracer_factory.Tools.Net.Constant;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtility;
import com.example.zhujia.dxracer_factory.Tools.Net.IHttpCallBack;
import com.example.zhujia.dxracer_factory.Tools.insertComma;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DXSW5 on 2017/9/6.
 */

public class OrderBackAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder> implements View.OnClickListener  {

    private List<MData> data;
    public OrderBack context;
    private int type=0;
    private SharedPreferences sharedPreferences;
    private String business_id,departmentId,departmentPersonName,departmentPersonSession;
    Map<String,String> params;
    private Spinner change_receiver;
    ProgressDialog progressDialog;
    List<Dict> dicts = new ArrayList<Dict>();
    private OrderBackAdapter.OnitemClickListener onitemClickListener=null;
    private String forkliftPersonId;
    private  View views;
    private LinearLayout lin3;
    AlertDialog dialog;
    private PopupWindow pop;//pop弹窗
    @SuppressLint("WrongConstant")
    public OrderBackAdapter(OrderBack context1, List<MData>data){
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

    public void setOnitemClickListener(OrderBackAdapter.OnitemClickListener onitemClickListener) {
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

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderback_data, parent, false);
            OrderBackAdapter.LinearViewHolder linearViewHolder = new OrderBackAdapter.LinearViewHolder(baseView);
            baseView.setOnClickListener(this);
            return linearViewHolder;

        }
        else {

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedulingsheet_data, parent, false);
            OrderBackAdapter.GridViewHolder gridViewHolder = new OrderBackAdapter.GridViewHolder(baseView);
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
            final OrderBackAdapter.LinearViewHolder linearViewHolder= (OrderBackAdapter.LinearViewHolder) holder;
            linearViewHolder.orderNo.setText(data.get(position).getOrderNo());
            linearViewHolder.dealerName.setText(data.get(position).getDealerName());
            linearViewHolder.createTime.setText(insertComma.stampToDates(data.get(position).getCreateTime()));
            if(data.get(position).getOrderStatus().equals("confirm")){
                linearViewHolder.orderStatus.setText("待确认订单");
            }
            if(data.get(position).getOrderStatus().equals("confirmed")){
                linearViewHolder.orderStatus.setText("已确认订单");
            }
            if(data.get(position).getOrderStatus().equals("production")){
                linearViewHolder.orderStatus.setText("生产中订单");
            }
            if(data.get(position).getOrderStatus().equals("delivery")){
                linearViewHolder.orderStatus.setText("已发货订单");
            }
            if(data.get(position).getOrderStatus().equals("cancel")){
                linearViewHolder.orderStatus.setText("已取消订单");
            }
            if(data.get(position).getOrderStatus().equals("undelivery")){
                linearViewHolder.orderStatus.setText("待发货订单");
            }

            if(!data.get(position).getField11().equals("null")){
                linearViewHolder.field11.setText(data.get(position).getField11());
            }else {
                linearViewHolder.field11.setText("-");
            }
            if(!data.get(position).getField7().equals("null")){
                linearViewHolder.field7.setText(data.get(position).getField7());
            }else {
                linearViewHolder.field7.setText("-");
            }


        }
    }




    public static class LinearViewHolder extends BaseRecyclerViewHolder {

        private TextView orderNo,dealerName,createTime,orderStatus,field11,field7;
        private LinearLayout lin,confirmTimelin;
        private Button edit_btn,del_btn;
        public LinearViewHolder(View itemView) {
            super(itemView);
            orderNo=(TextView)itemView.findViewById(R.id.orderNo);
            dealerName=(TextView)itemView.findViewById(R.id.dealerName);
            createTime=(TextView)itemView.findViewById(R.id.createTime);
            orderStatus=(TextView)itemView.findViewById(R.id.orderStatus);
            field11=(TextView)itemView.findViewById(R.id.field11);
            field7=(TextView)itemView.findViewById(R.id.field7);
        }
    }

    public static class GridViewHolder extends BaseRecyclerViewHolder {


        public GridViewHolder(View itemView) {
            super(itemView);

        }
    }

}