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
import com.example.zhujia.dxracer_factory.Activity.Personcontact;
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

public class PersoncontactAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder> implements View.OnClickListener  {

    private List<MData> data;
    public Personcontact context;
    private int type=0;
    ListDataSave dataSave,dataSave1;
    private SharedPreferences sharedPreferences,sharedPreferences1;
    private int Positionid,Departmentid;
    private PersoncontactAdapter.OnitemClickListener onitemClickListener=null;
    public PersoncontactAdapter(Personcontact context1, List<MData>data){
        this.context=context1;
        this.data=data;
    }
    @Override
    public void onClick(View view) {
        if(onitemClickListener!=null){
            onitemClickListener.onItemClick(view,(int)view.getTag());
        }
    }

    public void setOnitemClickListener(PersoncontactAdapter.OnitemClickListener onitemClickListener) {
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

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.personcontact_data, parent, false);
            PersoncontactAdapter.LinearViewHolder linearViewHolder = new PersoncontactAdapter.LinearViewHolder(baseView);
            baseView.setOnClickListener(this);
            return linearViewHolder;

        }
        else {

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedulingsheet_data, parent, false);
            PersoncontactAdapter.GridViewHolder gridViewHolder = new PersoncontactAdapter.GridViewHolder(baseView);
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
            final PersoncontactAdapter.LinearViewHolder linearViewHolder= (PersoncontactAdapter.LinearViewHolder) holder;
            linearViewHolder.contactType.setText(data.get(position).getContactType());
            linearViewHolder.startDate.setText(data.get(position).getStartDate());
            linearViewHolder.endDate.setText(data.get(position).getEndDate());
            linearViewHolder.code.setText(data.get(position).getCode());
            linearViewHolder.file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(context, PDFView.class);
                    intent.putExtra("url",data.get(position).getFile());
                    context.startActivity(intent);
                }
            });


        }
    }


    public static class LinearViewHolder extends BaseRecyclerViewHolder {

        private TextView contactType,startDate,endDate,code,file;
        private LinearLayout lin;

        public LinearViewHolder(View itemView) {
            super(itemView);
            contactType=(TextView)itemView.findViewById(R.id.contactType);
            startDate=(TextView)itemView.findViewById(R.id.startDate);
            endDate=(TextView)itemView.findViewById(R.id.endDate);
            code=(TextView)itemView.findViewById(R.id.code);
            file=(TextView)itemView.findViewById(R.id.file);
            lin=(LinearLayout)itemView.findViewById(R.id.lin_btn);

        }
    }

    public static class GridViewHolder extends BaseRecyclerViewHolder {


        public GridViewHolder(View itemView) {
            super(itemView);

        }
    }

}