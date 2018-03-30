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
import com.example.zhujia.dxracer_factory.Activity.MySalaryByMonth;
import com.example.zhujia.dxracer_factory.Activity.Myequipment;
import com.example.zhujia.dxracer_factory.Activity.MysalaryDetail;
import com.example.zhujia.dxracer_factory.Activity.PhotoView;
import com.example.zhujia.dxracer_factory.Data.AllData;
import com.example.zhujia.dxracer_factory.Data.Dict;
import com.example.zhujia.dxracer_factory.Data.MData;
import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.BaseRecyclerAdapter;
import com.example.zhujia.dxracer_factory.Tools.FixedGridLayout;
import com.example.zhujia.dxracer_factory.Tools.Net.Constant;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtility;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtils;
import com.example.zhujia.dxracer_factory.Tools.Net.IHttpCallBack;

import net.sf.json.JSON;

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

public class MySalaryByMonthAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder> implements View.OnClickListener  {

    private List<MData> data;
    public MySalaryByMonth context;
    private int type=0;
    private SharedPreferences sharedPreferences;
    private String business_id,departmentId,departmentPersonName,departmentPersonSession;
    Map<String,String> params;
    private Spinner change_receiver;
    ProgressDialog progressDialog;
    private int istouch=0;
    private int istouch1=0;
    private int istouch2=0;
    private int istouch3=0;

    List<Dict> dicts = new ArrayList<Dict>();
    private MySalaryByMonthAdapter.OnitemClickListener onitemClickListener=null;
    private String forkliftPersonId;
    private  View views;
    private LinearLayout lin3;
    AlertDialog dialog;
    private PopupWindow pop;//pop弹窗
    @SuppressLint("WrongConstant")
    public MySalaryByMonthAdapter(MySalaryByMonth context1, List<MData>data){
        this.context=context1;
        this.data=data;
        sharedPreferences =context1.getSharedPreferences("Session", Context.MODE_APPEND);
        business_id=sharedPreferences.getString("business_id","");
        departmentPersonSession=sharedPreferences.getString("departmentPersonSession","");
        departmentId=sharedPreferences.getString("departmentId","");
        departmentPersonName=sharedPreferences.getString("personCode","")+" "+sharedPreferences.getString("realName","");
        Log.e("TAG", "StockinorderAdapter: "+ departmentPersonSession);
    }
    @Override
    public void onClick(View view) {
        if(onitemClickListener!=null){
            onitemClickListener.onItemClick(view,(int)view.getTag());
        }
    }

    public void setOnitemClickListener(MySalaryByMonthAdapter.OnitemClickListener onitemClickListener) {
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

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mysalarybymonth_data, parent, false);
            MySalaryByMonthAdapter.LinearViewHolder linearViewHolder = new MySalaryByMonthAdapter.LinearViewHolder(baseView);
            baseView.setOnClickListener(this);
            return linearViewHolder;

        }
        else {

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedulingsheet_data, parent, false);
            MySalaryByMonthAdapter.GridViewHolder gridViewHolder = new MySalaryByMonthAdapter.GridViewHolder(baseView);
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
            final MySalaryByMonthAdapter.LinearViewHolder linearViewHolder= (MySalaryByMonthAdapter.LinearViewHolder) holder;

            linearViewHolder.salaryDate.setText(data.get(position).getSalaryDate());
            linearViewHolder.realName.setText(data.get(position).getRealName());
            linearViewHolder.positionName.setText(data.get(position).getPositionName());
            linearViewHolder.departmentName.setText(data.get(position).getDepartmentName());
            linearViewHolder.planTotalFee.setText(data.get(position).getPlanTotalFee());
            linearViewHolder.actTotalFee.setText(data.get(position).getActTotalFee());
            linearViewHolder.tax.setText(data.get(position).getTax());
            linearViewHolder.baseSalarySocialSecurity.setText(data.get(position).getBaseSalarySocialSecurity());
            linearViewHolder.accumulationFundPersosn.setText(data.get(position).getAccumulationFundPersosn());
            linearViewHolder.accumulationFundCompany.setText(data.get(position).getAccumulationFundCompany());
            linearViewHolder.socialSecurityPerson.setText(data.get(position).getSocialSecurityPerson());
            linearViewHolder.socialSecurityCompany.setText(data.get(position).getSocialSecurityCompany());
            linearViewHolder.pension_person.setText(data.get(position).getPension_person());
            linearViewHolder.pension_company.setText(data.get(position).getPension_company());
            linearViewHolder.base_care_company.setText(data.get(position).getBase_care_company());
            linearViewHolder.base_care_person.setText(data.get(position).getBase_care_person());
            linearViewHolder.unemployment_company.setText(data.get(position).getUnemployment_company());
            linearViewHolder.unemployment_person.setText(data.get(position).getUnemployment_person());
            linearViewHolder.birth_company.setText(data.get(position).getBirth_company());
            linearViewHolder.injury_company.setText(data.get(position).getInjury_company());
            linearViewHolder.treatment_company.setText(data.get(position).getTreatment_company());

            linearViewHolder.planTotalFee1.setText(data.get(position).getPlanTotalFee());
            linearViewHolder.accumulationFundPersosns.setText("-"+data.get(position).getAccumulationFundPersosn());
            linearViewHolder.socialSecurityPersons.setText("-"+data.get(position).getSocialSecurityPerson());
            linearViewHolder.taxs.setText("-"+data.get(position).getTax());



           /* linearViewHolder.salary_total1.setText(data.get(position).getSalary_total1());
            linearViewHolder.salary_total2.setText(data.get(position).getSalary_total2());
            linearViewHolder.salary_total3.setText(data.get(position).getSalary_total3());
            linearViewHolder.salary_total4.setText(data.get(position).getSalary_total4());
            linearViewHolder.salary_total5.setText(data.get(position).getSalary_total5());*/
            //linearViewHolder.salary_total6.setText(data.get(position).getSalary_total6());
            Log.e("TAG", "onBindViewHolder: "+ data.get(position).getSalary_total6());
            net.sf.json.JSONArray detailList= net.sf.json.JSONArray.fromObject(data.get(position).getSalary_total6());
            for(int j=0;j<detailList.size();j++){
              View  view1=View.inflate(context.getApplicationContext(),R.layout.layout_views,null);
                TextView title=(TextView)view1.findViewById(R.id.title);
                TextView text=(TextView)view1.findViewById(R.id.text);
                net.sf.json.JSONObject object1=detailList.getJSONObject(j);
                title.setText(object1.getString("salary_type"));
                if(!object1.getString("salary_total").isEmpty()){
                    if(object1.getString("salary_type").equals("考勤扣款")){
                        text.setText(object1.getString("salary_total"));
                        text.setTextColor(context.getResources().getColor(R.color.text_color));
                    }else {
                        text.setText(object1.getString("salary_total"));
                        text.setTextColor(context.getResources().getColor(R.color.blue));
                    }

                }else {
                    text.setText("-");
                }

                linearViewHolder.grid.addView(view1);
            }


            linearViewHolder.sb_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(istouch==0){
                        linearViewHolder.lin1.setVisibility(View.VISIBLE);
                        istouch=1;
                    }else {
                        linearViewHolder.lin1.setVisibility(View.GONE);
                        istouch=0;

                    }
                }
            });

            linearViewHolder.sb_btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(istouch1==0){
                        linearViewHolder.lin2.setVisibility(View.VISIBLE);
                        istouch1=1;
                    }else {
                        linearViewHolder.lin2.setVisibility(View.GONE);
                        istouch1=0;

                    }
                }
            });

            linearViewHolder.yf_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(istouch2==0){
                        linearViewHolder.grid.setVisibility(View.VISIBLE);
                        istouch2=1;
                    }else {
                        linearViewHolder.grid.setVisibility(View.GONE);
                        istouch2=0;

                    }
                }
            });

            linearViewHolder.sf_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(istouch3==0){
                        linearViewHolder.lin4.setVisibility(View.VISIBLE);
                        istouch3=1;
                    }else {
                        linearViewHolder.lin4.setVisibility(View.GONE);
                        istouch3=0;

                    }
                }
            });
        }
    }


    public static class LinearViewHolder extends BaseRecyclerViewHolder {

        private TextView salary_total1,salary_total2,salary_total3,salary_total4,salary_total5,salary_total6,
        salaryDate,realName,positionName,departmentName,planTotalFee,actTotalFee,tax,baseSalarySocialSecurity,accumulationFundPersosn,accumulationFundCompany,
                socialSecurityPerson,socialSecurityCompany,pension_person,base_care_person,unemployment_person,pension_company,base_care_company,
                unemployment_company,birth_company,injury_company,treatment_company
                ,planTotalFee1,accumulationFundPersosns,socialSecurityPersons,taxs;

        private LinearLayout grid;
        private TextView sb_btn,sb_btn1,yf_btn,sf_btn;
        private LinearLayout lin1,lin2,lin3,lin4,confirmTimelin;
        public LinearViewHolder(View itemView) {
            super(itemView);
            salaryDate=(TextView)itemView.findViewById(R.id.salaryDate);
            realName=(TextView)itemView.findViewById(R.id.realName);
            positionName=(TextView)itemView.findViewById(R.id.positionName);
            departmentName=(TextView)itemView.findViewById(R.id.departmentName);
            planTotalFee=(TextView)itemView.findViewById(R.id.planTotalFee);
            actTotalFee=(TextView)itemView.findViewById(R.id.actTotalFee);
            tax=(TextView)itemView.findViewById(R.id.tax);
            baseSalarySocialSecurity=(TextView)itemView.findViewById(R.id.baseSalarySocialSecurity);
            accumulationFundPersosn=(TextView)itemView.findViewById(R.id.accumulationFundPersosn);
            accumulationFundCompany=(TextView)itemView.findViewById(R.id.accumulationFundCompany);
            socialSecurityPerson=(TextView)itemView.findViewById(R.id.socialSecurityPerson);
            socialSecurityCompany=(TextView)itemView.findViewById(R.id.socialSecurityCompany);


            pension_person=(TextView)itemView.findViewById(R.id.pension_person);
            base_care_person=(TextView)itemView.findViewById(R.id.base_care_person);
            unemployment_person=(TextView)itemView.findViewById(R.id.unemployment_person);
            pension_company=(TextView)itemView.findViewById(R.id.pension_company);
            base_care_company=(TextView)itemView.findViewById(R.id.base_care_company);
            unemployment_company=(TextView)itemView.findViewById(R.id.unemployment_company);
            birth_company=(TextView)itemView.findViewById(R.id.birth_company);
            injury_company=(TextView)itemView.findViewById(R.id.injury_company);
            treatment_company=(TextView)itemView.findViewById(R.id.treatment_company);

            planTotalFee1=(TextView)itemView.findViewById(R.id.planTotalFee1);
            accumulationFundPersosns=(TextView)itemView.findViewById(R.id.accumulationFundPersosns);
            socialSecurityPersons=(TextView)itemView.findViewById(R.id.socialSecurityPersons);
            taxs=(TextView)itemView.findViewById(R.id.taxs);

            salary_total1=(TextView)itemView.findViewById(R.id.salary_total1);
            salary_total2=(TextView)itemView.findViewById(R.id.salary_total2);
            salary_total3=(TextView)itemView.findViewById(R.id.salary_total3);
            salary_total4=(TextView)itemView.findViewById(R.id.salary_total4);
            salary_total5=(TextView)itemView.findViewById(R.id.salary_total5);
            salary_total6=(TextView)itemView.findViewById(R.id.salary_total6);


            lin1=(LinearLayout)itemView.findViewById(R.id.lin1);
            sb_btn=(TextView)itemView.findViewById(R.id.sb_btn);

            sb_btn1=(TextView)itemView.findViewById(R.id.sb_btn1);
            lin2=(LinearLayout) itemView.findViewById(R.id.lin2);

            yf_btn=(TextView)itemView.findViewById(R.id.yf_btn);
            lin3=(LinearLayout)itemView.findViewById(R.id.lin3);

            sf_btn=(TextView)itemView.findViewById(R.id.sf_btn);
            lin4=(LinearLayout)itemView.findViewById(R.id.lin4);

            grid=(LinearLayout)itemView.findViewById(R.id.grid);

        }
    }

    public static class GridViewHolder extends BaseRecyclerViewHolder {


        public GridViewHolder(View itemView) {
            super(itemView);

        }
    }

}