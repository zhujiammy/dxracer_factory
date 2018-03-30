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

public class PurchaseplanitemAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder> implements View.OnClickListener  {

    private List<ScheduData> data;
    public Context context;
    private int type=0;
    private PurchaseplanitemAdapter.OnitemClickListener onitemClickListener=null;
    public PurchaseplanitemAdapter(Context context1, List<ScheduData>data){
        this.context=context1;
        this.data=data;
    }
    @Override
    public void onClick(View view) {
        if(onitemClickListener!=null){
            onitemClickListener.onItemClick(view,(int)view.getTag());
        }
    }

    public void setOnitemClickListener(PurchaseplanitemAdapter.OnitemClickListener onitemClickListener) {
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

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchaseplanitem_data, parent, false);
            PurchaseplanitemAdapter.LinearViewHolder linearViewHolder = new PurchaseplanitemAdapter.LinearViewHolder(baseView);
            baseView.setOnClickListener(this);
            return linearViewHolder;

        }
        else {

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedulingsheet_data, parent, false);
            PurchaseplanitemAdapter.GridViewHolder gridViewHolder = new PurchaseplanitemAdapter.GridViewHolder(baseView);
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
            final PurchaseplanitemAdapter.LinearViewHolder linearViewHolder= (PurchaseplanitemAdapter.LinearViewHolder) holder;
            linearViewHolder.partNo.setText(data.get(position).getPartNo());
            linearViewHolder.quantity.setText(data.get(position).getQuantity());
            linearViewHolder.name.setText(data.get(position).getName());
            if(data.get(position).getLocation()==null||data.get(position).getLocation().equals("1")){
                linearViewHolder.lin3.setVisibility(View.GONE);

            }else {
                linearViewHolder.location.setText(data.get(position).getLocation());
            }
        }
    }


    public static class LinearViewHolder extends BaseRecyclerViewHolder {

        private TextView quantity,partNo,name,location;
        private LinearLayout lin3;
        public LinearViewHolder(View itemView) {
            super(itemView);
            partNo=(TextView)itemView.findViewById(R.id.partNo);
            quantity=(TextView)itemView.findViewById(R.id.quantity);
            name=(TextView)itemView.findViewById(R.id.name);
            location=(TextView)itemView.findViewById(R.id.location);
            lin3=(LinearLayout) itemView.findViewById(R.id.lin3);
        }
    }

    public static class GridViewHolder extends BaseRecyclerViewHolder {


        public GridViewHolder(View itemView) {
            super(itemView);

        }
    }

}