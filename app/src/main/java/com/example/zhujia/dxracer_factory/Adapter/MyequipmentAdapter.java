package com.example.zhujia.dxracer_factory.Adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zhujia.dxracer_factory.Activity.Applicationmaintenance;
import com.example.zhujia.dxracer_factory.Activity.Applymaintenance;
import com.example.zhujia.dxracer_factory.Activity.Editform;
import com.example.zhujia.dxracer_factory.Activity.Myequipment;
import com.example.zhujia.dxracer_factory.Data.Dict;
import com.example.zhujia.dxracer_factory.Data.MData;
import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.BaseRecyclerAdapter;
import com.example.zhujia.dxracer_factory.Tools.Net.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by DXSW5 on 2017/9/6.
 */

public class MyequipmentAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder> implements View.OnClickListener  {

    private List<MData> data;
    public Myequipment context;
    private int type=0;
    private SharedPreferences sharedPreferences;
    private String business_id,departmentId,departmentPersonName,departmentPersonSession;
    Map<String,String> params;
    private Spinner change_receiver;
    ProgressDialog progressDialog;
    List<Dict> dicts = new ArrayList<Dict>();
    private MyequipmentAdapter.OnitemClickListener onitemClickListener=null;
    private String forkliftPersonId;
    private  View views;
    private LinearLayout lin3;
    AlertDialog dialog;
    private PopupWindow pop;//pop弹窗
    @SuppressLint("WrongConstant")
    public MyequipmentAdapter(Myequipment context1, List<MData>data){
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

    public void setOnitemClickListener(MyequipmentAdapter.OnitemClickListener onitemClickListener) {
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

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.myequipment_data, parent, false);
            MyequipmentAdapter.LinearViewHolder linearViewHolder = new MyequipmentAdapter.LinearViewHolder(baseView);
            baseView.setOnClickListener(this);
            return linearViewHolder;

        }
        else {

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedulingsheet_data, parent, false);
            MyequipmentAdapter.GridViewHolder gridViewHolder = new MyequipmentAdapter.GridViewHolder(baseView);
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
            final MyequipmentAdapter.LinearViewHolder linearViewHolder= (MyequipmentAdapter.LinearViewHolder) holder;
            if(data.get(position).getField3()!=null){
                Glide.with(context).load(Constant.APPURLIMG+data.get(position).getPicture()).into(linearViewHolder.field3);
            }else {
                linearViewHolder.field3.setImageDrawable(context.getResources().getDrawable(R.mipmap.img_def));
            }
            if(data.get(position).getField4()!=null){
                Glide.with(context).load(Constant.APPURLIMG+data.get(position).getPicture()).into(linearViewHolder.field4);
            }else {
                linearViewHolder.field4.setImageDrawable(context.getResources().getDrawable(R.mipmap.img_def));
            }

            linearViewHolder.equipmentNo.setText(data.get(position).getEquipmentNo());
            linearViewHolder.equipmentName.setText(data.get(position).getEquipmentName());
            linearViewHolder.equipmentColor.setText(data.get(position).getEquipmentColor());
            linearViewHolder.equipmentCategory.setText(data.get(position).getCategoryName());
            if (!data.get(position).getField1().equals("null")) {
                linearViewHolder.field1.setText(data.get(position).getField1());
            }else {
                linearViewHolder.field1.setText("-");
            }

            linearViewHolder.realName.setText(data.get(position).getRealName());
            linearViewHolder.field2.setText(data.get(position).getField2());
            linearViewHolder.supplierName.setText(data.get(position).getSupplierName());
            linearViewHolder.equipmentUseTime.setText(data.get(position).getEquipmentUseTime());
            if(data.get(position).getEquipmentStatus().equals("A")){
                linearViewHolder.equipmentStatus.setText("正常");
                linearViewHolder.equipmentStatus.setTextColor(context.getResources().getColor(R.color.blue));
            }else if(data.get(position).getEquipmentStatus().equals("B")) {
                linearViewHolder.equipmentStatus.setText("停用");
                linearViewHolder.equipmentStatus.setTextColor(context.getResources().getColor(R.color.text_color));
            }else if(data.get(position).getEquipmentStatus().equals("C")) {
                linearViewHolder.equipmentStatus.setText("维修中");
                linearViewHolder.equipmentStatus.setTextColor(context.getResources().getColor(R.color.text_color));
            }


            //维修申请
            linearViewHolder.edit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context.getApplicationContext(), Applicationmaintenance.class);
                    String id= String.valueOf(data.get(position).getId());
                    intent.putExtra("equipmentId",id);
                    context.startActivity(intent);
                }
            });


            //保养申请
            linearViewHolder.del_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context.getApplicationContext(), Applymaintenance.class);
                    String id= String.valueOf(data.get(position).getId());
                    intent.putExtra("equipmentId",id);
                    context.startActivity(intent);
                }
            });


        }
    }




    public static class LinearViewHolder extends BaseRecyclerViewHolder {

        private TextView equipmentNo,field2,equipmentName,equipmentColor,equipmentCategory,field1,realName,supplierName,equipmentUseTime,equipmentStatus;
        private LinearLayout lin,confirmTimelin;
        private ImageView field3,field4;
        private Button edit_btn,del_btn;
        public LinearViewHolder(View itemView) {
            super(itemView);
            equipmentNo=(TextView)itemView.findViewById(R.id.equipmentNo);
            equipmentName=(TextView)itemView.findViewById(R.id.equipmentName);
            equipmentColor=(TextView)itemView.findViewById(R.id.equipmentColor);
            equipmentCategory=(TextView)itemView.findViewById(R.id.equipmentCategory);
            field1=(TextView)itemView.findViewById(R.id.field1);
            realName=(TextView)itemView.findViewById(R.id.realName);
            field2=(TextView)itemView.findViewById(R.id.field2);
            supplierName=(TextView)itemView.findViewById(R.id.supplierName);
            lin=(LinearLayout)itemView.findViewById(R.id.lin_btn);
            equipmentUseTime=(TextView)itemView.findViewById(R.id.equipmentUseTime);
            equipmentStatus=(TextView)itemView.findViewById(R.id.equipmentStatus);
            field3=(ImageView) itemView.findViewById(R.id.field3);
            field4=(ImageView) itemView.findViewById(R.id.field4);
            edit_btn=(Button)itemView.findViewById(R.id.edit_btn);
            del_btn=(Button)itemView.findViewById(R.id.del_btn);
        }
    }

    public static class GridViewHolder extends BaseRecyclerViewHolder {


        public GridViewHolder(View itemView) {
            super(itemView);

        }
    }

}