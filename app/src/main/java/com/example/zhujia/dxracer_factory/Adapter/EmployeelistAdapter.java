package com.example.zhujia.dxracer_factory.Adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhujia.dxracer_factory.Activity.Allmaterial;
import com.example.zhujia.dxracer_factory.Activity.BatchwarehouseDetail;
import com.example.zhujia.dxracer_factory.Activity.Billentry;
import com.example.zhujia.dxracer_factory.Activity.DetailsActivity;
import com.example.zhujia.dxracer_factory.Activity.Employeelist;
import com.example.zhujia.dxracer_factory.Activity.Formaterial;
import com.example.zhujia.dxracer_factory.Activity.Materialorderitem;
import com.example.zhujia.dxracer_factory.Activity.Order;
import com.example.zhujia.dxracer_factory.Activity.OrderDetail;
import com.example.zhujia.dxracer_factory.Activity.Pendingshipment;
import com.example.zhujia.dxracer_factory.Activity.PhotoView;
import com.example.zhujia.dxracer_factory.Activity.Stockinorder;
import com.example.zhujia.dxracer_factory.Activity.Stockinorderitem;
import com.example.zhujia.dxracer_factory.Activity.Stockinorders;
import com.example.zhujia.dxracer_factory.Data.BatchwareData;
import com.example.zhujia.dxracer_factory.Data.Dict;
import com.example.zhujia.dxracer_factory.Data.MData;
import com.example.zhujia.dxracer_factory.Data.ScheduData;
import com.example.zhujia.dxracer_factory.Data.SpotData;
import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.BaseRecyclerAdapter;
import com.example.zhujia.dxracer_factory.Tools.ListDataSave;
import com.example.zhujia.dxracer_factory.Tools.Net.Constant;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtility;
import com.example.zhujia.dxracer_factory.Tools.Net.IHttpCallBack;
import com.example.zhujia.dxracer_factory.Tools.insertComma;
import com.example.zhujia.dxracer_factory.Tools.universalimageloader.core.DisplayImageOptions;
import com.example.zhujia.dxracer_factory.Tools.universalimageloader.core.ImageLoader;
import com.example.zhujia.dxracer_factory.Tools.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by DXSW5 on 2017/9/6.
 */

public class EmployeelistAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder> implements View.OnClickListener  {

    private List<MData> data;
    public Employeelist context;
    ListDataSave dataSave,dataSave1;
    private int type=0;
    private SharedPreferences sharedPreferences,sharedPreferences1;
    private int Positionid,Departmentid;
    private String business_id,departmentId,departmentPersonName,departmentPersonSession;
    Map<String,String> params;
    String positionName;
    int id;
    private List<Dict>list=new ArrayList<>();
    private EmployeelistAdapter.OnitemClickListener onitemClickListener=null;
    @SuppressLint("WrongConstant")
    public EmployeelistAdapter(Employeelist context1, List<MData>data){
        this.context=context1;
        this.data=data;
        sharedPreferences =context1.getSharedPreferences("Session", Context.MODE_APPEND);
        sharedPreferences1 =context1.getSharedPreferences("Session", Context.MODE_APPEND);
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

    public void setOnitemClickListener(EmployeelistAdapter.OnitemClickListener onitemClickListener) {
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

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.employeelist_data, parent, false);
            EmployeelistAdapter.LinearViewHolder linearViewHolder = new EmployeelistAdapter.LinearViewHolder(baseView);
            baseView.setOnClickListener(this);
            return linearViewHolder;

        }
        else {

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedulingsheet_data, parent, false);
            EmployeelistAdapter.GridViewHolder gridViewHolder = new EmployeelistAdapter.GridViewHolder(baseView);
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
            final EmployeelistAdapter.LinearViewHolder linearViewHolder= (EmployeelistAdapter.LinearViewHolder) holder;
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


            linearViewHolder.file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, PhotoView.class);
                    intent.putExtra("photoview",data.get(position).getFile());
                    context.startActivity(intent);
                }
            });

        }
    }


    public static class LinearViewHolder extends BaseRecyclerViewHolder {

        private TextView realName, departmentId,positionId,personCode,sex,phone,bqq,enterpriseMailbox;
        private LinearLayout lin;
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