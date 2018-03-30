package com.example.zhujia.dxracer_factory.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zhujia.dxracer_factory.Activity.DetailsActivity;
import com.example.zhujia.dxracer_factory.Activity.PurchaseDetails;
import com.example.zhujia.dxracer_factory.Data.MData;
import com.example.zhujia.dxracer_factory.Data.ScheduData;
import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.BaseRecyclerAdapter;
import com.example.zhujia.dxracer_factory.Tools.universalimageloader.core.DisplayImageOptions;
import com.example.zhujia.dxracer_factory.Tools.universalimageloader.core.ImageLoader;
import com.example.zhujia.dxracer_factory.Tools.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by DXSW5 on 2017/9/6.
 */

public class purchaseordersAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder> implements View.OnClickListener  {

    private List<MData> data;
    public Context context;
    private int type=0;
    private purchaseordersAdapter.OnitemClickListener onitemClickListener=null;
    Map<String,String> params;

    public purchaseordersAdapter(Context context1, List<MData>data){
        this.context=context1;
        this.data=data;
    }
    @Override
    public void onClick(View view) {
        if(onitemClickListener!=null){
            onitemClickListener.onItemClick(view,(int)view.getTag());
        }
    }

    public void setOnitemClickListener(purchaseordersAdapter.OnitemClickListener onitemClickListener) {
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

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchaseorders_data, parent, false);
            purchaseordersAdapter.LinearViewHolder linearViewHolder = new purchaseordersAdapter.LinearViewHolder(baseView);
            baseView.setOnClickListener(this);
            return linearViewHolder;

        }
        else {

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedulingsheet_data, parent, false);
            purchaseordersAdapter.GridViewHolder gridViewHolder = new purchaseordersAdapter.GridViewHolder(baseView);
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
            final purchaseordersAdapter.LinearViewHolder linearViewHolder= (purchaseordersAdapter.LinearViewHolder) holder;
            linearViewHolder.purchaseOrderNo.setText(data.get(position).getPurchaseOrderNo());
            linearViewHolder.orderNo.setText(data.get(position).getOrderNo());
            linearViewHolder.supplierNo.setText(data.get(position).getSupplierNo());
            if(data.get(position).getPurchaseOrderType().equals("PIA")){
                linearViewHolder.purchaseOrderType.setText("批量订单采购单");
            }

            if(data.get(position).getPurchaseOrderType().equals("PIC")){
                linearViewHolder.purchaseOrderType.setText("现货商品采购单");
            }
            if(data.get(position).getPurchaseOrderType().equals("PID")){
                linearViewHolder.purchaseOrderType.setText("售后部件采购单");
            }
            if(data.get(position).getManual().equals("Y")){
                linearViewHolder.manual.setImageDrawable(context.getResources().getDrawable(R.mipmap.shi));
            }else {
                linearViewHolder.manual.setImageDrawable(context.getResources().getDrawable(R.mipmap.fou));
            }
            linearViewHolder.planProductionDate.setText(data.get(position).getPlanProductionDate());
            linearViewHolder.plateNumber.setText(data.get(position).getPlateNumber());
            linearViewHolder.sendTime.setText(data.get(position).getSendTime());
            linearViewHolder.receivePerson.setText(data.get(position).getReceivePerson());
            linearViewHolder.forkliftPerson.setText(data.get(position).getForkliftPerson());
            if(data.get(position).getStatus().equals("created")){
                linearViewHolder.status.setText("待确认");
                linearViewHolder.status.setTextColor(context.getResources().getColor(R.color.del_color));
            }
            if(data.get(position).getStatus().equals("confirmed")){
                linearViewHolder.status.setText("已确认");
                linearViewHolder.status.setTextColor(context.getResources().getColor(R.color.btn_info));
            }
            if(data.get(position).getStatus().equals("returned")){
                linearViewHolder.status.setText("已退货");
                linearViewHolder.status.setTextColor(context.getResources().getColor(R.color.cancelled));
            }
            if(data.get(position).getStatus().equals("finished")){
                linearViewHolder.status.setText("已入库");
                linearViewHolder.status.setTextColor(context.getResources().getColor(R.color.yrk));
            }
            if(data.get(position).getStatus().equals("canceled")){
                linearViewHolder.status.setText("已取消");
                linearViewHolder.status.setTextColor(context.getResources().getColor(R.color.cancelled));
            }

            linearViewHolder.lin_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //详情
                    Intent intent=new Intent(context,PurchaseDetails.class);
                    String purchaseOrderId= String.valueOf(data.get(position).getId());
                    intent.putExtra("purchaseOrderId",purchaseOrderId);
                    context.startActivity(intent);
                }
            });


        }
    }


    public static class LinearViewHolder extends BaseRecyclerViewHolder {

        private TextView purchaseOrderNo,orderNo,supplierNo,purchaseOrderType,planProductionDate,plateNumber,sendTime,receivePerson,forkliftPerson,status;
        private LinearLayout lin_btn;
        private ImageView manual;
        public LinearViewHolder(View itemView) {
            super(itemView);
            purchaseOrderNo=(TextView)itemView.findViewById(R.id.purchaseOrderNo);
            orderNo=(TextView)itemView.findViewById(R.id.orderNo);
            supplierNo=(TextView)itemView.findViewById(R.id.supplierNo);
            purchaseOrderType=(TextView)itemView.findViewById(R.id.purchaseOrderType);
            manual=(ImageView) itemView.findViewById(R.id.manual);
            planProductionDate=(TextView)itemView.findViewById(R.id.planProductionDate);
            plateNumber=(TextView)itemView.findViewById(R.id.plateNumber);
            sendTime=(TextView)itemView.findViewById(R.id.sendTime);
            receivePerson=(TextView)itemView.findViewById(R.id.receivePerson);
            forkliftPerson=(TextView)itemView.findViewById(R.id.forkliftPerson);
            status=(TextView)itemView.findViewById(R.id.status);
            lin_btn=(LinearLayout)itemView.findViewById(R.id.lin_btn);
        }
    }

    public static class GridViewHolder extends BaseRecyclerViewHolder {


        public GridViewHolder(View itemView) {
            super(itemView);

        }
    }

}