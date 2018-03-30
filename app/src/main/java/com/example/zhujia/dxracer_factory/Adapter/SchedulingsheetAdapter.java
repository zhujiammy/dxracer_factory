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

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by DXSW5 on 2017/9/6.
 */

public class SchedulingsheetAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder> implements View.OnClickListener  {

    private List<ScheduData> data;
    public Context context;
    private int type=0;
    private SchedulingsheetAdapter.OnitemClickListener onitemClickListener=null;
    private Handler mHandler;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    Map<String,String> params;

    public SchedulingsheetAdapter(Context context1, List<ScheduData>data){
        this.context=context1;
        this.data=data;
    }
    @Override
    public void onClick(View view) {
        if(onitemClickListener!=null){
            onitemClickListener.onItemClick(view,(int)view.getTag());
        }
    }

    public void setOnitemClickListener(SchedulingsheetAdapter.OnitemClickListener onitemClickListener) {
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

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedulingsheet_data, parent, false);
            SchedulingsheetAdapter.LinearViewHolder linearViewHolder = new SchedulingsheetAdapter.LinearViewHolder(baseView);
            baseView.setOnClickListener(this);
            return linearViewHolder;

        }
        else {

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedulingsheet_data, parent, false);
            SchedulingsheetAdapter.GridViewHolder gridViewHolder = new SchedulingsheetAdapter.GridViewHolder(baseView);
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
            final SchedulingsheetAdapter.LinearViewHolder linearViewHolder= (SchedulingsheetAdapter.LinearViewHolder) holder;
            linearViewHolder.planDate.setText(data.get(position).getPlanDate());
            linearViewHolder.orderNo.setText(data.get(position).getOrderNo());
            linearViewHolder.totalQuantity.setText(data.get(position).getTotalQuantity());
            linearViewHolder.dealerName.setText(data.get(position).getDealerName());

            if(!data.get(position).getPurchasePlan().equals("null")){
                if(data.get(position).getOrderstatus().equals("created")){
                    linearViewHolder.text1.setBackgroundColor(context.getResources().getColor(R.color.color1));
                }else {
                    linearViewHolder.text1.setBackgroundColor(context.getResources().getColor(R.color.color2));
                    linearViewHolder.text2.setBackgroundColor(context.getResources().getColor(R.color.color2));
                }

            }

            linearViewHolder.lin_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //详情
                    Intent intent=new Intent(context,DetailsActivity.class);
                    intent.putExtra("orderId",data.get(position).getOrderId());
                    intent.putExtra("orderNo",data.get(position).getOrderNo());
                    context.startActivity(intent);
                }
            });


        }
    }


    public static class LinearViewHolder extends BaseRecyclerViewHolder {

        private TextView planDate,orderNo,totalQuantity,dealerName,text1,text2,text3;
        private LinearLayout lin_btn;
        public LinearViewHolder(View itemView) {
            super(itemView);
            planDate=(TextView)itemView.findViewById(R.id.planDate);
            orderNo=(TextView)itemView.findViewById(R.id.orderNo);
            totalQuantity=(TextView)itemView.findViewById(R.id.totalQuantity);
            dealerName=(TextView)itemView.findViewById(R.id.dealerName);
            text1=(TextView)itemView.findViewById(R.id.text1);
            text2=(TextView)itemView.findViewById(R.id.text2);
            text3=(TextView)itemView.findViewById(R.id.text3);
            lin_btn=(LinearLayout)itemView.findViewById(R.id.lin_btn);
        }
    }

    public static class GridViewHolder extends BaseRecyclerViewHolder {


        public GridViewHolder(View itemView) {
            super(itemView);

        }
    }

}