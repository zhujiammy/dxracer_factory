package com.example.zhujia.dxracer_factory.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zhujia.dxracer_factory.Activity.DetailsActivity;
import com.example.zhujia.dxracer_factory.Activity.Myindex;
import com.example.zhujia.dxracer_factory.Activity.MyindexDetail;
import com.example.zhujia.dxracer_factory.Activity.PhotoView;
import com.example.zhujia.dxracer_factory.Data.MData;
import com.example.zhujia.dxracer_factory.Data.ScheduData;
import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.BaseRecyclerAdapter;
import com.example.zhujia.dxracer_factory.Tools.ListDataSave;
import com.example.zhujia.dxracer_factory.Tools.Net.Constant;
import com.example.zhujia.dxracer_factory.Tools.insertComma;
import com.example.zhujia.dxracer_factory.Tools.universalimageloader.core.DisplayImageOptions;
import com.example.zhujia.dxracer_factory.Tools.universalimageloader.core.ImageLoader;
import com.example.zhujia.dxracer_factory.Tools.universalimageloader.core.ImageLoaderConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by DXSW5 on 2017/9/6.
 */

public class MyindexAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder> implements View.OnClickListener  {

    private List<MData> data;
    public Myindex context;
    private int type=0;
    ListDataSave dataSave,dataSave1;
    private SharedPreferences sharedPreferences,sharedPreferences1;
    private int Positionid,Departmentid;
    private MyindexAdapter.OnitemClickListener onitemClickListener=null;
    public MyindexAdapter(Myindex context1, List<MData>data){
        this.context=context1;
        this.data=data;
    }
    @Override
    public void onClick(View view) {
        if(onitemClickListener!=null){
            onitemClickListener.onItemClick(view,(int)view.getTag());
        }
    }

    public void setOnitemClickListener(MyindexAdapter.OnitemClickListener onitemClickListener) {
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

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.myindex_data, parent, false);
            MyindexAdapter.LinearViewHolder linearViewHolder = new MyindexAdapter.LinearViewHolder(baseView);
            baseView.setOnClickListener(this);
            return linearViewHolder;

        }
        else {

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedulingsheet_data, parent, false);
            MyindexAdapter.GridViewHolder gridViewHolder = new MyindexAdapter.GridViewHolder(baseView);
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
            final MyindexAdapter.LinearViewHolder linearViewHolder= (MyindexAdapter.LinearViewHolder) holder;
            if(!data.get(position).getFile().equals("null")){
                Glide.with(context).load(Constant.APPURLIMG+data.get(position).getFile()).into(linearViewHolder.file);
            }else {
                linearViewHolder.file.setImageDrawable(context.getResources().getDrawable(R.mipmap.img_def));
            }
            linearViewHolder.realName.setText(data.get(position).getRealName());
            linearViewHolder.personCode.setText(data.get(position).getPersonCode());
            if(data.get(position).getSex().equals("M")){
                linearViewHolder.sex.setText("男");
            }else {
                linearViewHolder.sex.setText("女");
            }
            linearViewHolder.phone.setText(data.get(position).getPhone());
            linearViewHolder.bqq.setText(data.get(position).getBqq());
            linearViewHolder.enterpriseMailbox.setText(data.get(position).getEnterpriseMailbox());
            Positionid= Integer.parseInt(data.get(position).getPositionId());
            Departmentid= Integer.parseInt(data.get(position).getDepartmentId());
            dataSave = new ListDataSave(context,"departmentPositionList");
            dataSave1 = new ListDataSave(context,"departmentPositionList1");
            for(int i=0;i<dataSave.getDataList("dictList").size();i++){
                if(Positionid==dataSave.getDataList("dictList").get(i).getId()){
                    linearViewHolder.positionId.setText(dataSave.getDataList("dictList").get(i).getText());
                }
            }


            for(int i=0;i<dataSave1.getDataList("dictList1").size();i++){
                if(Departmentid==dataSave1.getDataList("dictList1").get(i).getId()){
                    linearViewHolder.departmentId.setText(dataSave1.getDataList("dictList1").get(i).getText());
                }
            }


           if(data.get(position).getStatus().equals("A")) {
                linearViewHolder.status.setText("待入职");
                linearViewHolder.btn.setVisibility(View.VISIBLE);
            }else if(data.get(position).getStatus().equals("Y")){
                linearViewHolder.status.setText("在职");
                linearViewHolder.btn.setVisibility(View.GONE);
            }else if(data.get(position).getStatus().equals("N")) {
                linearViewHolder.status.setText("离职");
                linearViewHolder.btn.setVisibility(View.GONE);
            }



            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(System.currentTimeMillis());
            String nowdate = format.format(date);
            if(insertComma.isdate(data.get(position).getFormalDate(),nowdate)){
                linearViewHolder.formalDate.setText("未转正");
            }else {

                linearViewHolder.formalDate.setText("已转正");
            }


            linearViewHolder.provinceName.setText(data.get(position).getProvinceName()+data.get(position).getCityName());
            if(data.get(position).getPayStatus().equals("Y")){
                linearViewHolder.payStatus.setText("是");
            }else {
                linearViewHolder.payStatus.setText("否");
            }
            linearViewHolder.file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, PhotoView.class);
                    intent.putExtra("photoview",data.get(position).getFile());
                    context.startActivity(intent);
                }
            });
            linearViewHolder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            //详情
            linearViewHolder.lin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,MyindexDetail.class);
                    String personId= String.valueOf(data.get(position).getId());
                    intent.putExtra("personId",personId);
                    context.startActivity(intent);
                }
            });

        }
    }


    public static class LinearViewHolder extends BaseRecyclerViewHolder {

        private TextView realName,departmentId,positionId,personCode,sex,phone,bqq,enterpriseMailbox,status,provinceName,formalDate,payStatus;
        private LinearLayout lin;
        private Button btn;
        private ImageView file;

        public LinearViewHolder(View itemView) {
            super(itemView);

            realName=(TextView)itemView.findViewById(R.id.realName);
            departmentId=(TextView)itemView.findViewById(R.id.departmentId);
            positionId=(TextView)itemView.findViewById(R.id.positionId);
            personCode=(TextView)itemView.findViewById(R.id.personCode);
            sex=(TextView)itemView.findViewById(R.id.sex);
            phone=(TextView)itemView.findViewById(R.id.phone);
            bqq=(TextView)itemView.findViewById(R.id.bqq);
            enterpriseMailbox=(TextView)itemView.findViewById(R.id.enterpriseMailbox);
            status=(TextView)itemView.findViewById(R.id.status);
            provinceName=(TextView)itemView.findViewById(R.id.provinceName);
            formalDate=(TextView)itemView.findViewById(R.id.formalDate);
            payStatus=(TextView)itemView.findViewById(R.id.payStatus);
            file=(ImageView) itemView.findViewById(R.id.file);
            lin=(LinearLayout)itemView.findViewById(R.id.lin_btn);
            btn=(Button)itemView.findViewById(R.id.btn);

        }
    }

    public static class GridViewHolder extends BaseRecyclerViewHolder {


        public GridViewHolder(View itemView) {
            super(itemView);

        }
    }

}