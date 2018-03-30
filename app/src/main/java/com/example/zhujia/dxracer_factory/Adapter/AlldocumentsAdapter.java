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
import com.example.zhujia.dxracer_factory.Activity.Particulars;
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

public class AlldocumentsAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder> implements View.OnClickListener  {

    private List<ScheduData> data;
    public Context context;
    private int type=0;
    private AlldocumentsAdapter.OnitemClickListener onitemClickListener=null;
    private Handler mHandler;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    Map<String,String> params;

    public AlldocumentsAdapter(Context context1, List<ScheduData>data){
        this.context=context1;
        this.data=data;
    }
    @Override
    public void onClick(View view) {
        if(onitemClickListener!=null){
            onitemClickListener.onItemClick(view,(int)view.getTag());
        }
    }

    public void setOnitemClickListener(AlldocumentsAdapter.OnitemClickListener onitemClickListener) {
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

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.alldocuments_data, parent, false);
            AlldocumentsAdapter.LinearViewHolder linearViewHolder = new AlldocumentsAdapter.LinearViewHolder(baseView);
            baseView.setOnClickListener(this);
            return linearViewHolder;

        }
        else {

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedulingsheet_data, parent, false);
            AlldocumentsAdapter.GridViewHolder gridViewHolder = new AlldocumentsAdapter.GridViewHolder(baseView);
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
            final AlldocumentsAdapter.LinearViewHolder linearViewHolder= (AlldocumentsAdapter.LinearViewHolder) holder;

            linearViewHolder.productionDate.setText(data.get(position).getProductionDate());
            linearViewHolder.productionOrderNo.setText(data.get(position).getProductionOrderNo());
            linearViewHolder.orderNo.setText(data.get(position).getOrderNo());
            linearViewHolder.quantity.setText(data.get(position).getQuantity());
            linearViewHolder.fcno.setText(data.get(position).getFcno());
            linearViewHolder.field4.setText(data.get(position).getField4());
            linearViewHolder.field1.setText(data.get(position).getField1());
            linearViewHolder.searchMaterialPerson.setText(data.get(position).getSearchMaterialPerson());
            linearViewHolder.searchMaterialScore.setText(data.get(position).getSearchMaterialScore());
            if(data.get(position).getStatus().equals("producting")){
                linearViewHolder.status.setText("生产中");
            }
            if(data.get(position).getStatus().equals("producted")){
                linearViewHolder.status.setText("已完成");
            }
            if(data.get(position).getStatus().equals("finished")){
                linearViewHolder.status.setText("已入库");
            }
            if(data.get(position).getStatus().equals("create")){
                linearViewHolder.status.setText("待生产");
            }

            if(data.get(position).getSearchMaterialStatus().equals("create")){
                linearViewHolder.searchMaterialStatus.setText("待领料");
            }
            if(data.get(position).getSearchMaterialStatus().equals("picking")){
                linearViewHolder.searchMaterialStatus.setText("领料中");
            }

            if(data.get(position).getSearchMaterialStatus().equals("picked")){
                linearViewHolder.searchMaterialStatus.setText("已领料");
            }
            if(data.get(position).getSearchMaterialStatus().equals("delivery")){
                linearViewHolder.searchMaterialStatus.setText("已出库");
            }



            linearViewHolder.lin_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //详情
                    Intent intent=new Intent(context,Particulars.class);
                    intent.putExtra("productionOrderId",data.get(position).getId());
                    context.startActivity(intent);
                }
            });


        }
    }


    public static class LinearViewHolder extends BaseRecyclerViewHolder {

        private TextView productionDate,fcno,quantity,productionOrderNo,orderNo,status,field4,searchMaterialPerson,searchMaterialStatus,searchMaterialScore,field1;
        private LinearLayout lin_btn;
        public LinearViewHolder(View itemView) {
            super(itemView);
            productionDate=(TextView)itemView.findViewById(R.id.productionDate);
            fcno=(TextView)itemView.findViewById(R.id.fcno);
            quantity=(TextView)itemView.findViewById(R.id.quantity);
            productionOrderNo=(TextView)itemView.findViewById(R.id.productionOrderNo);
            orderNo=(TextView)itemView.findViewById(R.id.orderNo);
            status=(TextView)itemView.findViewById(R.id.status);
            field4=(TextView)itemView.findViewById(R.id.field4);
            searchMaterialPerson=(TextView)itemView.findViewById(R.id.searchMaterialPerson);
            searchMaterialStatus=(TextView)itemView.findViewById(R.id.searchMaterialStatus);
            searchMaterialScore=(TextView)itemView.findViewById(R.id.searchMaterialScore);
            field1=(TextView)itemView.findViewById(R.id.field1);
            lin_btn=(LinearLayout)itemView.findViewById(R.id.lin_btn);
        }
    }

    public static class GridViewHolder extends BaseRecyclerViewHolder {


        public GridViewHolder(View itemView) {
            super(itemView);

        }
    }

}