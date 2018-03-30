package com.example.zhujia.dxracer_factory.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zhujia.dxracer_factory.Activity.BatchwarehouseDetail;
import com.example.zhujia.dxracer_factory.Activity.DetailsActivity;
import com.example.zhujia.dxracer_factory.Activity.Stockinorder;
import com.example.zhujia.dxracer_factory.Activity.Stockinorderd;
import com.example.zhujia.dxracer_factory.Activity.stockinorderDetail;
import com.example.zhujia.dxracer_factory.Data.BatchwareData;
import com.example.zhujia.dxracer_factory.Data.ScheduData;
import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.BaseRecyclerAdapter;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtility;
import com.example.zhujia.dxracer_factory.Tools.Net.IHttpCallBack;
import com.example.zhujia.dxracer_factory.Tools.universalimageloader.core.DisplayImageOptions;
import com.example.zhujia.dxracer_factory.Tools.universalimageloader.core.ImageLoader;
import com.example.zhujia.dxracer_factory.Tools.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by DXSW5 on 2017/9/6.
 */

public class StockinorderAdapters extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder> implements View.OnClickListener  {

    private List<BatchwareData> data;
    public Stockinorderd context;
    private int type=0;
    private SharedPreferences sharedPreferences;
    private String business_id,userId,departmentPersonName,departmentPersonSession;
    Map<String,String> params;
    private Handler mHandler;
    private StockinorderAdapters.OnitemClickListener onitemClickListener=null;
    @SuppressLint("WrongConstant")
    public StockinorderAdapters(Stockinorderd context1, List<BatchwareData>data){
        this.context=context1;
        this.data=data;
        sharedPreferences =context1.getSharedPreferences("Session", Context.MODE_APPEND);
        business_id=sharedPreferences.getString("business _id","");
        userId=sharedPreferences.getString("systemUser_id","");
        departmentPersonName=sharedPreferences.getString("personCode","")+" "+sharedPreferences.getString("realName","");
        departmentPersonSession=sharedPreferences.getString("departmentPersonSession","");
        Log.e("TAG", "StockinorderAdapter: "+ departmentPersonSession);
    }
    @Override
    public void onClick(View view) {
        if(onitemClickListener!=null){
            onitemClickListener.onItemClick(view,(int)view.getTag());
        }
    }

    public void setOnitemClickListener(StockinorderAdapters.OnitemClickListener onitemClickListener) {
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

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stockinorder_data, parent, false);
            StockinorderAdapters.LinearViewHolder linearViewHolder = new StockinorderAdapters.LinearViewHolder(baseView);
            baseView.setOnClickListener(this);
            return linearViewHolder;

        }
        else {

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedulingsheet_data, parent, false);
            StockinorderAdapters.GridViewHolder gridViewHolder = new StockinorderAdapters.GridViewHolder(baseView);
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
            final StockinorderAdapters.LinearViewHolder linearViewHolder= (StockinorderAdapters.LinearViewHolder) holder;
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

            linearViewHolder.lin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,stockinorderDetail.class);
                    intent.putExtra("stockInOrderId",data.get(position).getId());
                    context.startActivity(intent);
                }
            });

            linearViewHolder.ok_btn.setVisibility(View.GONE);
            linearViewHolder.ok_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //接单
                    params=new HashMap<>();
                    params.put("businessId",business_id);
                    params.put("id",data.get(position).getId());
                    params.put("departmentPersonName",departmentPersonName);
                    params.put("departmentPersonSession",departmentPersonSession);
                    HttpUtility.doPostAsyn("/servlet/stock/stockinorder/receive_order", params, new IHttpCallBack() {
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
                                    String msg=resulutJsonobj.getString("message");
                                    if(result_code.equals("success")){
                                        Looper.prepare();
                                        context.onRefresh();
                                        showDialog(msg);
                                        Looper.loop();

                                    }else {
                                        Looper.prepare();
                                        context.onRefresh();
                                        showDialog(msg);
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


    private void showDialog(String msg) {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setTitle("提示！");
        builder.setMessage(msg);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
    public static class LinearViewHolder extends BaseRecyclerViewHolder {

        private TextView stockInOrderNo,orderNo,purchaseOrderNo,plateNumber,sendTime,planProductionDate,stockInStatus;
        private Button ok_btn;
        private LinearLayout lin;
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
            ok_btn=(Button)itemView.findViewById(R.id.ok_btn);
            lin=(LinearLayout)itemView.findViewById(R.id.lin_btn);
        }
    }

    public static class GridViewHolder extends BaseRecyclerViewHolder {


        public GridViewHolder(View itemView) {
            super(itemView);

        }
    }

}