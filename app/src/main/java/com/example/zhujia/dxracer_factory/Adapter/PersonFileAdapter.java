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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zhujia.dxracer_factory.Activity.DetailsActivity;
import com.example.zhujia.dxracer_factory.Activity.Myindex;
import com.example.zhujia.dxracer_factory.Activity.MyindexDetail;
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

public class PersonFileAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder> implements View.OnClickListener  {

    private List<MData> data;
    public PersonFile context;
    private int type=0;
    ListDataSave dataSave,dataSave1;
    private SharedPreferences sharedPreferences,sharedPreferences1;
    private int Positionid,Departmentid;
    private PersonFileAdapter.OnitemClickListener onitemClickListener=null;
    public PersonFileAdapter(PersonFile context1, List<MData>data){
        this.context=context1;
        this.data=data;
    }
    @Override
    public void onClick(View view) {
        if(onitemClickListener!=null){
            onitemClickListener.onItemClick(view,(int)view.getTag());
        }
    }

    public void setOnitemClickListener(PersonFileAdapter.OnitemClickListener onitemClickListener) {
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

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.personfile_data, parent, false);
            PersonFileAdapter.LinearViewHolder linearViewHolder = new PersonFileAdapter.LinearViewHolder(baseView);
            baseView.setOnClickListener(this);
            return linearViewHolder;

        }
        else {

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedulingsheet_data, parent, false);
            PersonFileAdapter.GridViewHolder gridViewHolder = new PersonFileAdapter.GridViewHolder(baseView);
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
            final PersonFileAdapter.LinearViewHolder linearViewHolder= (PersonFileAdapter.LinearViewHolder) holder;
            linearViewHolder.enterDate.setText(data.get(position).getEnterDate());
            linearViewHolder.formalDate.setText(data.get(position).getFormalDate());
            linearViewHolder.idCard.setText(data.get(position).getIdCard());
            linearViewHolder.birth.setText(data.get(position).getBirthYear()+data.get(position).getBirthMonth()+data.get(position).getBirthDay());
            linearViewHolder.graduationSchool.setText(data.get(position).getGraduationSchool());
            linearViewHolder.graduationDate.setText(data.get(position).getGraduationDate());
            linearViewHolder.major.setText(data.get(position).getMajor());
            linearViewHolder.englishLevel.setText(data.get(position).getEnglishLevel());
            linearViewHolder.historyCompany.setText(data.get(position).getHistoryCompany());
            linearViewHolder.historyPosition.setText(data.get(position).getHistoryPosition());
            linearViewHolder.marriage.setText(data.get(position).getMarriage());
            linearViewHolder.socialSecurityAccount.setText(data.get(position).getSocialSecurityAccount());
            linearViewHolder.accumulationFundAccount.setText(data.get(position).getAccumulationFundAccount());
            linearViewHolder.bankAccount.setText(data.get(position).getBankAccount());
            linearViewHolder.field1.setText(data.get(position).getField1());

            if(data.get(position).getEducation().equals("1")){
                linearViewHolder.education.setText("初中");
            }
            if(data.get(position).getEducation().equals("2")){
                linearViewHolder.education.setText("高中");
            }
            if(data.get(position).getEducation().equals("3")){
                linearViewHolder.education.setText("本科");
            }
            if(data.get(position).getEducation().equals("4")){
                linearViewHolder.education.setText("硕士");
            }
            if(data.get(position).getEducation().equals("5")){
                linearViewHolder.education.setText("博士");
            }
            if(data.get(position).getEducation().equals("6")){
                linearViewHolder.education.setText("博士后");
            }
            if(data.get(position).getEducation().equals("7")){
                linearViewHolder.education.setText("大专");
            }


        }
    }


    public static class LinearViewHolder extends BaseRecyclerViewHolder {

        private TextView enterDate,formalDate,idCard,birth,graduationSchool,graduationDate,major,education,englishLevel,historyCompany,historyPosition,marriage,socialSecurityAccount,accumulationFundAccount,field1,bankAccount;
        private LinearLayout lin;
        private ImageView file;

        public LinearViewHolder(View itemView) {
            super(itemView);
            enterDate=(TextView)itemView.findViewById(R.id.enterDate);
            formalDate=(TextView)itemView.findViewById(R.id.formalDate);
            idCard=(TextView)itemView.findViewById(R.id.idCard);
            birth=(TextView)itemView.findViewById(R.id.birth);
            graduationSchool=(TextView)itemView.findViewById(R.id.graduationSchool);
            graduationDate=(TextView)itemView.findViewById(R.id.graduationDate);
            major=(TextView)itemView.findViewById(R.id.major);
            education=(TextView)itemView.findViewById(R.id.education);
            englishLevel=(TextView)itemView.findViewById(R.id.englishLevel);
            historyCompany=(TextView)itemView.findViewById(R.id.historyCompany);
            historyPosition=(TextView)itemView.findViewById(R.id.historyPosition);
            marriage=(TextView)itemView.findViewById(R.id.marriage);
            socialSecurityAccount=(TextView)itemView.findViewById(R.id.socialSecurityAccount);
            accumulationFundAccount=(TextView)itemView.findViewById(R.id.accumulationFundAccount);
            field1=(TextView)itemView.findViewById(R.id.field1);
            bankAccount=(TextView)itemView.findViewById(R.id.bankAccount);
            file=(ImageView) itemView.findViewById(R.id.file);
            lin=(LinearLayout)itemView.findViewById(R.id.lin_btn);

        }
    }

    public static class GridViewHolder extends BaseRecyclerViewHolder {


        public GridViewHolder(View itemView) {
            super(itemView);

        }
    }

}