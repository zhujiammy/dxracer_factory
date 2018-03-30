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
import com.example.zhujia.dxracer_factory.Tools.insertComma;
import com.example.zhujia.dxracer_factory.Tools.universalimageloader.core.DisplayImageOptions;
import com.example.zhujia.dxracer_factory.Tools.universalimageloader.core.ImageLoader;
import com.example.zhujia.dxracer_factory.Tools.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by DXSW5 on 2017/9/6.
 */

public class ParticularsAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder> implements View.OnClickListener  {

    private List<ScheduData> data;
    public Context context;
    private int type=0;
    private ParticularsAdapter.OnitemClickListener onitemClickListener=null;
    public ParticularsAdapter(Context context1, List<ScheduData>data){
        this.context=context1;
        this.data=data;
    }
    @Override
    public void onClick(View view) {
        if(onitemClickListener!=null){
            onitemClickListener.onItemClick(view,(int)view.getTag());
        }
    }

    public void setOnitemClickListener(ParticularsAdapter.OnitemClickListener onitemClickListener) {
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

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.particulars_data, parent, false);
            ParticularsAdapter.LinearViewHolder linearViewHolder = new ParticularsAdapter.LinearViewHolder(baseView);
            baseView.setOnClickListener(this);
            return linearViewHolder;

        }
        else {

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedulingsheet_data, parent, false);
            ParticularsAdapter.GridViewHolder gridViewHolder = new ParticularsAdapter.GridViewHolder(baseView);
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
            final ParticularsAdapter.LinearViewHolder linearViewHolder= (ParticularsAdapter.LinearViewHolder) holder;
            linearViewHolder.craftName.setText(data.get(position).getCraftName());
            linearViewHolder.quantity.setText(data.get(position).getQuantity());
            linearViewHolder.overtimePay.setText(data.get(position).getOvertimePay());
            linearViewHolder.realName.setText(data.get(position).getPersonCode()+data.get(position).getRealName());
            linearViewHolder.unitPrice.setText(data.get(position).getUnitPrice());
            double price= Double.parseDouble(data.get(position).getUnitPrice());
            double total=price*Double.parseDouble(data.get(position).getQuantity());
            String sum= String.valueOf(total);
            linearViewHolder.totalprice.setText(insertComma.insertComma(sum,2));

        }
    }


    public static class LinearViewHolder extends BaseRecyclerViewHolder {

        private TextView craftName,quantity,overtimePay,totalprice,realName,unitPrice;
        public LinearViewHolder(View itemView) {
            super(itemView);
            craftName=(TextView)itemView.findViewById(R.id.craftName);
            quantity=(TextView)itemView.findViewById(R.id.quantity);
            overtimePay=(TextView)itemView.findViewById(R.id.overtimePay);
            totalprice=(TextView)itemView.findViewById(R.id.totalprice);
            realName=(TextView)itemView.findViewById(R.id.realName);
            unitPrice=(TextView)itemView.findViewById(R.id.unitPrice);
        }
    }

    public static class GridViewHolder extends BaseRecyclerViewHolder {


        public GridViewHolder(View itemView) {
            super(itemView);

        }
    }

}