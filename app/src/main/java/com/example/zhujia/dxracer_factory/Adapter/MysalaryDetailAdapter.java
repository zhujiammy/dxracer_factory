package com.example.zhujia.dxracer_factory.Adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhujia.dxracer_factory.Activity.AddEquipment;
import com.example.zhujia.dxracer_factory.Activity.AllEquipmentMaintain;
import com.example.zhujia.dxracer_factory.Activity.ApplyPayment;
import com.example.zhujia.dxracer_factory.Activity.ApplyPayments;
import com.example.zhujia.dxracer_factory.Activity.Editform;
import com.example.zhujia.dxracer_factory.Activity.EditformB;
import com.example.zhujia.dxracer_factory.Activity.EquipmentMaintain;
import com.example.zhujia.dxracer_factory.Activity.EquipmentMaintaincheck;
import com.example.zhujia.dxracer_factory.Activity.Equipmentmaintenance;
import com.example.zhujia.dxracer_factory.Activity.Equipmentmaintenancecheck;
import com.example.zhujia.dxracer_factory.Activity.MaintenanConfirm;
import com.example.zhujia.dxracer_factory.Activity.MaintenanCosts;
import com.example.zhujia.dxracer_factory.Activity.MaintenanceConfirm;
import com.example.zhujia.dxracer_factory.Activity.MaintenanceCosts;
import com.example.zhujia.dxracer_factory.Activity.Myequipment;
import com.example.zhujia.dxracer_factory.Activity.MysalaryDetail;
import com.example.zhujia.dxracer_factory.Activity.PhotoView;
import com.example.zhujia.dxracer_factory.Data.AllData;
import com.example.zhujia.dxracer_factory.Data.Dict;
import com.example.zhujia.dxracer_factory.Data.MData;
import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.BaseRecyclerAdapter;
import com.example.zhujia.dxracer_factory.Tools.Net.Constant;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtility;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtils;
import com.example.zhujia.dxracer_factory.Tools.Net.IHttpCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DXSW5 on 2017/9/6.
 */

public class MysalaryDetailAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder> implements View.OnClickListener  {

    private List<MData> data;
    public MysalaryDetail context;
    private int type=0;
    private SharedPreferences sharedPreferences;
    private String business_id,departmentId,departmentPersonName,departmentPersonSession,roleId;
    Map<String,String> params;
    private Spinner change_receiver;
    ProgressDialog progressDialog;
    List<Dict> dicts = new ArrayList<Dict>();
    private MysalaryDetailAdapter.OnitemClickListener onitemClickListener=null;
    private String forkliftPersonId;
    private  View views;
    private LinearLayout lin3;
    AlertDialog dialog;
    private PopupWindow pop;//pop弹窗
    @SuppressLint("WrongConstant")
    public MysalaryDetailAdapter(MysalaryDetail context1, List<MData>data){
        this.context=context1;
        this.data=data;
        sharedPreferences =context1.getSharedPreferences("Session", Context.MODE_APPEND);
        business_id=sharedPreferences.getString("business_id","");
        departmentPersonSession=sharedPreferences.getString("departmentPersonSession","");
        departmentId=sharedPreferences.getString("departmentId","");
        departmentPersonName=sharedPreferences.getString("personCode","")+" "+sharedPreferences.getString("realName","");
        roleId=sharedPreferences.getString("roleId","");
        Log.e("TAG", "StockinorderAdapter: "+ departmentPersonSession);
    }
    @Override
    public void onClick(View view) {
        if(onitemClickListener!=null){
            onitemClickListener.onItemClick(view,(int)view.getTag());
        }
    }

    public void setOnitemClickListener(MysalaryDetailAdapter.OnitemClickListener onitemClickListener) {
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
        View baseView=null;
        if (viewType == 0) {

            if(roleId.equals("104")){
                baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mysalarydetail_data, parent, false);
            }else if(roleId.equals("42")) {
                baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mysalarydetail_datas, parent, false);
            }

            MysalaryDetailAdapter.LinearViewHolder linearViewHolder = new MysalaryDetailAdapter.LinearViewHolder(baseView);
            baseView.setOnClickListener(this);
            return linearViewHolder;

        }
        else {

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedulingsheet_data, parent, false);
            MysalaryDetailAdapter.GridViewHolder gridViewHolder = new MysalaryDetailAdapter.GridViewHolder(baseView);
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
            final MysalaryDetailAdapter.LinearViewHolder linearViewHolder= (MysalaryDetailAdapter.LinearViewHolder) holder;
            linearViewHolder.salaryMonth.setText(data.get(position).getSalaryMonth());
            linearViewHolder.field1.setText(data.get(position).getField1());
            linearViewHolder.actTotalResult.setText(data.get(position).getActTotalResult());
            if(roleId.equals("104")){
                linearViewHolder.salary_total1.setText(data.get(position).getSalary_total1());
                linearViewHolder.salary_total2.setText(data.get(position).getSalary_total2());
                linearViewHolder.salary_total3.setText(data.get(position).getSalary_total3());
                linearViewHolder.salary_total4.setText(data.get(position).getSalary_total4());
                linearViewHolder.salary_total5.setText(data.get(position).getSalary_total5());
                linearViewHolder.salary_total6.setText(data.get(position).getSalary_total6());
            }else if(roleId.equals("42")){
                linearViewHolder.salaryDayBase.setText(data.get(position).getSalaryDayBase());
                linearViewHolder.salaryDayDuty.setText(data.get(position).getSalaryDayDuty());
                linearViewHolder.workDay.setText(data.get(position).getWorkDay());
                linearViewHolder.baseSalaryMonth.setText(data.get(position).getBaseSalaryMonth());
                linearViewHolder.postSalaryMonth.setText(data.get(position).getPostSalaryMonth());
                linearViewHolder.salary_total1.setText(data.get(position).getSalary_total1());
                linearViewHolder.salary_total2.setText(data.get(position).getSalary_total2());
                linearViewHolder.salary_total3.setText(data.get(position).getSalary_total3());
                linearViewHolder.salary_total4.setText(data.get(position).getSalary_total4());
            }

        }
    }




    public static class LinearViewHolder extends BaseRecyclerViewHolder {

        private TextView salaryMonth,field1,actTotalResult,salary_total1,salary_total2,salary_total3,salary_total4,salary_total5,salary_total6;
        private LinearLayout lin,confirmTimelin;
        private TextView salaryDayBase,salaryDayDuty,workDay,baseSalaryMonth,postSalaryMonth;
        public LinearViewHolder(View itemView) {
            super(itemView);
            salaryMonth=(TextView)itemView.findViewById(R.id.salaryMonth);
            field1=(TextView)itemView.findViewById(R.id.field1);
            actTotalResult=(TextView)itemView.findViewById(R.id.actTotalResult);

            salary_total1=(TextView)itemView.findViewById(R.id.salary_total1);
            salary_total2=(TextView)itemView.findViewById(R.id.salary_total2);
            salary_total3=(TextView)itemView.findViewById(R.id.salary_total3);
            salary_total4=(TextView)itemView.findViewById(R.id.salary_total4);
            salary_total5=(TextView)itemView.findViewById(R.id.salary_total5);
            salary_total6=(TextView)itemView.findViewById(R.id.salary_total6);

            salaryDayBase=(TextView)itemView.findViewById(R.id.salaryDayBase);
            salaryDayDuty=(TextView)itemView.findViewById(R.id.salaryDayDuty);
            workDay=(TextView)itemView.findViewById(R.id.workDay);
            baseSalaryMonth=(TextView)itemView.findViewById(R.id.baseSalaryMonth);
            postSalaryMonth=(TextView)itemView.findViewById(R.id.postSalaryMonth);

        }
    }

    public static class GridViewHolder extends BaseRecyclerViewHolder {


        public GridViewHolder(View itemView) {
            super(itemView);

        }
    }


}