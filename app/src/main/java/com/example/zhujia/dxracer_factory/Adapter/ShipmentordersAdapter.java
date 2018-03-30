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

public class ShipmentordersAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder> implements View.OnClickListener  {

    private List<MData> data;
    public Context context;
    private int type=0;
    private ShipmentordersAdapter.OnitemClickListener onitemClickListener=null;
    Map<String,String> params;

    public ShipmentordersAdapter(Context context1, List<MData>data){
        this.context=context1;
        this.data=data;
    }
    @Override
    public void onClick(View view) {
        if(onitemClickListener!=null){
            onitemClickListener.onItemClick(view,(int)view.getTag());
        }
    }

    public void setOnitemClickListener(ShipmentordersAdapter.OnitemClickListener onitemClickListener) {
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

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shipmentorders_data, parent, false);
            ShipmentordersAdapter.LinearViewHolder linearViewHolder = new ShipmentordersAdapter.LinearViewHolder(baseView);
            baseView.setOnClickListener(this);
            return linearViewHolder;

        }
        else {

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedulingsheet_data, parent, false);
            ShipmentordersAdapter.GridViewHolder gridViewHolder = new ShipmentordersAdapter.GridViewHolder(baseView);
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

            final ShipmentordersAdapter.LinearViewHolder linearViewHolder= (ShipmentordersAdapter.LinearViewHolder) holder;

            if(data.get(position).getType().equals("Create")||data.get(position).getType().equals("pending")){
                linearViewHolder.lin4.setVisibility(View.GONE);
                linearViewHolder.lin5.setVisibility(View.GONE);
                linearViewHolder.lin8.setVisibility(View.GONE);
            }
            linearViewHolder.shipmentNo.setText(data.get(position).getShipmentNo());
            linearViewHolder.dealerName.setText(data.get(position).getDealerName());
            if(!data.get(position).getPlanShipmentDate().equals("null")){
                linearViewHolder.planShipmentDate.setText(data.get(position).getPlanShipmentDate());
            }else {
                linearViewHolder.planShipmentDate.setText("-");
            }

            if(!data.get(position).getActualDate().equals("null")){
                linearViewHolder.actualDate.setText(data.get(position).getActualDate());
            }else {
                linearViewHolder.actualDate.setText("-");
            }


            if(data.get(position).getStatus().equals("create")){
                linearViewHolder.status.setText("已申请");
                linearViewHolder.status.setTextColor(context.getResources().getColor(R.color.create));
            }
            if(data.get(position).getStatus().equals("pending")){
                linearViewHolder.status.setText("待出货");
                linearViewHolder.status.setTextColor(context.getResources().getColor(R.color.pending));
            }
            if(data.get(position).getStatus().equals("finish")){
                linearViewHolder.status.setText("已完成");
                linearViewHolder.status.setTextColor(context.getResources().getColor(R.color.finish));
            }
            linearViewHolder.logistics.setText(data.get(position).getLogistics());


            if(!data.get(position).getCardNo().equals("null")){
                linearViewHolder.cardNo.setText(data.get(position).getCardNo());
            }else {
                linearViewHolder.cardNo.setText("-");
            }
            if(!data.get(position).getServiceCompanyName().equals("null")){
                linearViewHolder.serviceCompanyName.setText(data.get(position).getServiceCompanyName());
            }else {
                linearViewHolder.serviceCompanyName.setText("-");
            }


        }
    }


    public static class LinearViewHolder extends BaseRecyclerViewHolder {

        private TextView shipmentNo,dealerName,planShipmentDate,actualDate,status,logistics,serviceCompanyName,cardNo;
        private LinearLayout lin_btn,lin1,lin2,lin3,lin4,lin5,lin6,lin7,lin8;
        private ImageView manual;
        public LinearViewHolder(View itemView) {
            super(itemView);
            shipmentNo=(TextView)itemView.findViewById(R.id.shipmentNo);
            dealerName=(TextView)itemView.findViewById(R.id.dealerName);
            planShipmentDate=(TextView)itemView.findViewById(R.id.planShipmentDate);
            actualDate=(TextView)itemView.findViewById(R.id.actualDate);
            manual=(ImageView) itemView.findViewById(R.id.manual);
            logistics=(TextView)itemView.findViewById(R.id.logistics);
            serviceCompanyName=(TextView)itemView.findViewById(R.id.serviceCompanyName);
            cardNo=(TextView)itemView.findViewById(R.id.cardNo);
            status=(TextView)itemView.findViewById(R.id.status);
            status=(TextView)itemView.findViewById(R.id.status);
            lin_btn=(LinearLayout)itemView.findViewById(R.id.lin_btn);
            lin1=(LinearLayout)itemView.findViewById(R.id.lin1);
            lin2=(LinearLayout)itemView.findViewById(R.id.lin2);
            lin3=(LinearLayout)itemView.findViewById(R.id.lin3);
            lin4=(LinearLayout)itemView.findViewById(R.id.lin4);
            lin5=(LinearLayout)itemView.findViewById(R.id.lin5);
            lin6=(LinearLayout)itemView.findViewById(R.id.lin6);
            lin7=(LinearLayout)itemView.findViewById(R.id.lin7);
            lin8=(LinearLayout)itemView.findViewById(R.id.lin8);
        }
    }

    public static class GridViewHolder extends BaseRecyclerViewHolder {


        public GridViewHolder(View itemView) {
            super(itemView);

        }
    }

}