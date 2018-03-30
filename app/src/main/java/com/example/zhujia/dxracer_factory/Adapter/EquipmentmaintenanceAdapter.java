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
import com.example.zhujia.dxracer_factory.Activity.Editform;
import com.example.zhujia.dxracer_factory.Activity.Equipmentmaintenance;
import com.example.zhujia.dxracer_factory.Activity.Myequipment;
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

public class EquipmentmaintenanceAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder> implements View.OnClickListener  {

    private List<MData> data;
    public Equipmentmaintenance context;
    private int type=0;
    private SharedPreferences sharedPreferences;
    private String business_id,departmentId,departmentPersonName,departmentPersonSession;
    Map<String,String> params;
    private Spinner change_receiver;
    ProgressDialog progressDialog;
    List<Dict> dicts = new ArrayList<Dict>();
    private EquipmentmaintenanceAdapter.OnitemClickListener onitemClickListener=null;
    private String forkliftPersonId;
    private  View views;
    private LinearLayout lin3;
    AlertDialog dialog;
    private PopupWindow pop;//pop弹窗
    @SuppressLint("WrongConstant")
    public EquipmentmaintenanceAdapter(Equipmentmaintenance context1, List<MData>data){
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

    public void setOnitemClickListener(EquipmentmaintenanceAdapter.OnitemClickListener onitemClickListener) {
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

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.equipmentmaintenance_data, parent, false);
            EquipmentmaintenanceAdapter.LinearViewHolder linearViewHolder = new EquipmentmaintenanceAdapter.LinearViewHolder(baseView);
            baseView.setOnClickListener(this);
            return linearViewHolder;

        }
        else {

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedulingsheet_data, parent, false);
            EquipmentmaintenanceAdapter.GridViewHolder gridViewHolder = new EquipmentmaintenanceAdapter.GridViewHolder(baseView);
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
            final EquipmentmaintenanceAdapter.LinearViewHolder linearViewHolder= (EquipmentmaintenanceAdapter.LinearViewHolder) holder;

            linearViewHolder.field2.setText(data.get(position).getField2());
            linearViewHolder.equipmentNo.setText(data.get(position).getEquipmentNo());
            linearViewHolder.equipmentName.setText(data.get(position).getEquipmentName());
            linearViewHolder.startTime.setText(data.get(position).getStartTime());
            linearViewHolder.content.setText(data.get(position).getContent());
            linearViewHolder.supplierName.setText(data.get(position).getSupplierName());
            linearViewHolder.realName.setText(data.get(position).getRealName());
            if(data.get(position).getEndTime().equals("null")){
                linearViewHolder.endTime.setText("-");
            }else {

                linearViewHolder.endTime.setText(data.get(position).getEndTime());
            }

            if(data.get(position).getCheckName().equals("null")){
                linearViewHolder.checkName.setText("-");
            }else {
                linearViewHolder.checkName.setText(data.get(position).getCheckName());
            }

            if(!data.get(position).getTotal().equals("null")){
                linearViewHolder.total.setText(data.get(position).getTotal());
            }else {
                linearViewHolder.total.setText("-");
            }

            if(data.get(position).getStatus().equals("A")){
                linearViewHolder.status.setText("新建");
            }
            if(data.get(position).getStatus().equals("B")){
                linearViewHolder.status.setText("已审核");
            }
            if(data.get(position).getStatus().equals("C")){
                linearViewHolder.status.setText("未通过");
            }
            if(data.get(position).getStatus().equals("D")){
                linearViewHolder.status.setText("维修成功");
            }
            if(data.get(position).getStatus().equals("E")){
                linearViewHolder.status.setText("维修失败");
            }
            if(data.get(position).getStatus().equals("F")){
                linearViewHolder.status.setText("已生成付款单");
            }

            //删除
            linearViewHolder.del_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id= String.valueOf(data.get(position).getId());
                    showNormalDialog(position,id);
                }
            });

            //编辑
            linearViewHolder.edit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context.getApplicationContext(), Editform.class);
                    String id= String.valueOf(data.get(position).getId());
                    intent.putExtra("id",id);
                    intent.putExtra("field2",data.get(position).getField2());
                    intent.putExtra("equipmentId",data.get(position).getEquipmentId());
                    intent.putExtra("startTime",data.get(position).getStartTime());
                    intent.putExtra("realName",data.get(position).getRealName());
                    intent.putExtra("supplierName",data.get(position).getSupplierName());
                    intent.putExtra("content",data.get(position).getContent());
                    context.startActivity(intent);
                }
            });


        }
    }


    private void showNormalDialog(final int position, final String id){

        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(context);
        normalDialog.setMessage("确认删除吗?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        params=new HashMap<>();
                        params.put("businessId",business_id);
                        params.put("id",id);

                        new HttpUtils().post(Constant.APPURLS+"/servlet/equipment/equipmentmaintenance/delete",params,new HttpUtils.HttpCallback() {

                            @Override
                            public void onSuccess(String data) {
                                // TODO Auto-generated method stub
                                com.example.zhujia.dxracer_factory.Tools.Log.printJson("tag",data,"header");

                                Message msg=Message.obtain(
                                        mHandler,0,data
                                );
                                mHandler.sendMessage(msg);
                            }


                        });
                        data.remove(position);
                        notifyDataSetChanged();
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what){
                    case 0:
                        JSONObject resulutJsonobj;
                        resulutJsonobj=new JSONObject(msg.obj.toString());
                        if(resulutJsonobj.getString("code").equals("success")){
                            Toast.makeText(context.getApplicationContext(),"删除成功",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context.getApplicationContext(),"删除失败",Toast.LENGTH_SHORT).show();
                        }
                        break;


                }
            }catch (JSONException e){
                e.printStackTrace();
            }

        }
    };

    public static class LinearViewHolder extends BaseRecyclerViewHolder {

        private TextView field2,equipmentNo,equipmentName,startTime,content,supplierName,realName,endTime,checkName,total,status;
        private LinearLayout lin,confirmTimelin;
        private Button edit_btn,del_btn;
        public LinearViewHolder(View itemView) {
            super(itemView);
            field2=(TextView)itemView.findViewById(R.id.field2);
            equipmentNo=(TextView)itemView.findViewById(R.id.equipmentNo);
            equipmentName=(TextView)itemView.findViewById(R.id.equipmentName);
            startTime=(TextView)itemView.findViewById(R.id.startTime);
            supplierName=(TextView)itemView.findViewById(R.id.supplierName);
            realName=(TextView)itemView.findViewById(R.id.realName);
            content=(TextView)itemView.findViewById(R.id.content);
            lin=(LinearLayout)itemView.findViewById(R.id.lin_btn);
            endTime=(TextView)itemView.findViewById(R.id.endTime);
            checkName=(TextView)itemView.findViewById(R.id.checkName);
            total=(TextView)itemView.findViewById(R.id.total);
            status=(TextView)itemView.findViewById(R.id.status);
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