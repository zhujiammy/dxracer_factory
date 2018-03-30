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
import com.example.zhujia.dxracer_factory.Data.ScheduData;
import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.BaseRecyclerAdapter;
import com.example.zhujia.dxracer_factory.Tools.universalimageloader.core.DisplayImageOptions;
import com.example.zhujia.dxracer_factory.Tools.universalimageloader.core.ImageLoader;
import com.example.zhujia.dxracer_factory.Tools.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by DXSW5 on 2017/9/6.
 */

public class PurchaseorderAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder> implements View.OnClickListener  {

    private List<ScheduData> data;
    public Context context;
    private int type=0;
    private PurchaseorderAdapter.OnitemClickListener onitemClickListener=null;
    public PurchaseorderAdapter(Context context1, List<ScheduData>data){
        this.context=context1;
        this.data=data;
    }
    @Override
    public void onClick(View view) {
        if(onitemClickListener!=null){
            onitemClickListener.onItemClick(view,(int)view.getTag());
        }
    }

    public void setOnitemClickListener(PurchaseorderAdapter.OnitemClickListener onitemClickListener) {
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

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchaseorder_data, parent, false);
            PurchaseorderAdapter.LinearViewHolder linearViewHolder = new PurchaseorderAdapter.LinearViewHolder(baseView);
            baseView.setOnClickListener(this);
            return linearViewHolder;

        }
        else {

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedulingsheet_data, parent, false);
            PurchaseorderAdapter.GridViewHolder gridViewHolder = new PurchaseorderAdapter.GridViewHolder(baseView);
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
            final PurchaseorderAdapter.LinearViewHolder linearViewHolder= (PurchaseorderAdapter.LinearViewHolder) holder;
            linearViewHolder.purchaseOrderNo.setText(data.get(position).getPurchaseOrderNo());
            if(!data.get(position).getOriginalPurchaseOrderNo().equals("null")){
                linearViewHolder.originalPurchaseOrderNo.setText(data.get(position).getOriginalPurchaseOrderNo());
            }else {
                linearViewHolder.originalPurchaseOrderNo.setText("-");
            }
            if(data.get(position).getPurchasePaymentOrder()!=null){
                try {
                    JSONObject object=new JSONObject(data.get(position).getPurchasePaymentOrder());
                    if(!object.isNull("purchaseOrderInvoice")){
                        linearViewHolder.purchasePaymentOrder.setText(object.getString("purchaseOrderInvoice"));
                    }else {
                        linearViewHolder.purchasePaymentOrder.setText("-");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            linearViewHolder.supplierNo.setText(data.get(position).getSupplierNo());
            linearViewHolder.planProductionDate.setText(data.get(position).getPlanProductionDate());
            if(!data.get(position).getSendTime().equals("null")){
                linearViewHolder.sendTime.setText(data.get(position).getSendTime());
            }else {
                linearViewHolder.sendTime.setText("-");
            }

            if(!data.get(position).getPlateNumber().equals("null")){
                linearViewHolder.plateNumber.setText(data.get(position).getPlateNumber());
            }else {
                linearViewHolder.plateNumber.setText("-");
            }
            if(data.get(position).getStatus().equals("created")){
                linearViewHolder.status.setText("待确认");
            }else if(data.get(position).getStatus().equals("confirmed")){
                linearViewHolder.status.setText("已确认");
            }
            else if(data.get(position).getStatus().equals("returned")){
                linearViewHolder.status.setText("已退货");
            }
            else if(data.get(position).getStatus().equals("finished")){
                linearViewHolder.status.setText("已入库");
            }
            else if(data.get(position).getStatus().equals("canceled")){
                linearViewHolder.status.setText("已取消");
            }





        }
    }


    public static class LinearViewHolder extends BaseRecyclerViewHolder {

        private TextView purchaseOrderNo,originalPurchaseOrderNo,purchasePaymentOrder,supplierNo,planProductionDate,sendTime,plateNumber,status;
        private LinearLayout lin3;
        public LinearViewHolder(View itemView) {
            super(itemView);
            purchaseOrderNo=(TextView)itemView.findViewById(R.id.purchaseOrderNo);
            originalPurchaseOrderNo=(TextView)itemView.findViewById(R.id.originalPurchaseOrderNo);
            purchasePaymentOrder=(TextView)itemView.findViewById(R.id.purchasePaymentOrder);
            supplierNo=(TextView)itemView.findViewById(R.id.supplierNo);
            planProductionDate=(TextView)itemView.findViewById(R.id.planProductionDate);
            sendTime=(TextView)itemView.findViewById(R.id.sendTime);
            plateNumber=(TextView)itemView.findViewById(R.id.plateNumber);
            status=(TextView)itemView.findViewById(R.id.status);
            lin3=(LinearLayout) itemView.findViewById(R.id.lin3);
        }
    }

    public static class GridViewHolder extends BaseRecyclerViewHolder {


        public GridViewHolder(View itemView) {
            super(itemView);

        }
    }

}