package com.example.zhujia.dxracer_factory.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zhujia.dxracer_factory.Activity.DetailsActivity;
import com.example.zhujia.dxracer_factory.Activity.Myindex;
import com.example.zhujia.dxracer_factory.Activity.MyindexDetail;
import com.example.zhujia.dxracer_factory.Activity.PDFView;
import com.example.zhujia.dxracer_factory.Activity.PersonCert;
import com.example.zhujia.dxracer_factory.Activity.PersonFile;
import com.example.zhujia.dxracer_factory.Activity.PhotoView;
import com.example.zhujia.dxracer_factory.Data.MData;
import com.example.zhujia.dxracer_factory.Data.ScheduData;
import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.BaseRecyclerAdapter;
import com.example.zhujia.dxracer_factory.Tools.ListDataSave;
import com.example.zhujia.dxracer_factory.Tools.Net.Constant;
import com.example.zhujia.dxracer_factory.Tools.universalimageloader.core.DisplayImageOptions;
import com.example.zhujia.dxracer_factory.Tools.universalimageloader.core.ImageLoader;
import com.example.zhujia.dxracer_factory.Tools.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by DXSW5 on 2017/9/6.
 */

public class PersonCertAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder> implements View.OnClickListener  {

    private List<MData> data;
    public PersonCert context;
    private int type=0;
    ListDataSave dataSave,dataSave1;
    private SharedPreferences sharedPreferences,sharedPreferences1;
    private int Positionid,Departmentid;
    private PersonCertAdapter.OnitemClickListener onitemClickListener=null;
    public PersonCertAdapter(PersonCert context1, List<MData>data){
        this.context=context1;
        this.data=data;
    }
    @Override
    public void onClick(View view) {
        if(onitemClickListener!=null){
            onitemClickListener.onItemClick(view,(int)view.getTag());
        }
    }

    public void setOnitemClickListener(PersonCertAdapter.OnitemClickListener onitemClickListener) {
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

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.personcert_data, parent, false);
            PersonCertAdapter.LinearViewHolder linearViewHolder = new PersonCertAdapter.LinearViewHolder(baseView);
            baseView.setOnClickListener(this);
            return linearViewHolder;

        }
        else {

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedulingsheet_data, parent, false);
            PersonCertAdapter.GridViewHolder gridViewHolder = new PersonCertAdapter.GridViewHolder(baseView);
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
            final PersonCertAdapter.LinearViewHolder linearViewHolder= (PersonCertAdapter.LinearViewHolder) holder;
            linearViewHolder.fieldType.setText(data.get(position).getFieltype());
            linearViewHolder.fieldUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, PDFView.class);
                    intent.putExtra("url",data.get(position).getFieldUrl());
                    Log.e("TAG", "onClick: "+data.get(position).getFieldUrl());
                    context.startActivity(intent);
                }
            });


        }
    }


    public static class LinearViewHolder extends BaseRecyclerViewHolder {

        private TextView fieldType,fieldUrl;
        private LinearLayout lin;

        public LinearViewHolder(View itemView) {
            super(itemView);
            fieldType=(TextView)itemView.findViewById(R.id.fieldType);
            fieldUrl=(TextView)itemView.findViewById(R.id.fieldUrl);
            lin=(LinearLayout)itemView.findViewById(R.id.lin_btn);

        }
    }

    public static class GridViewHolder extends BaseRecyclerViewHolder {


        public GridViewHolder(View itemView) {
            super(itemView);

        }
    }

}