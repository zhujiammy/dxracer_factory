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

import com.example.zhujia.dxracer_factory.Activity.Allmaterial;
import com.example.zhujia.dxracer_factory.Activity.BatchwarehouseDetail;
import com.example.zhujia.dxracer_factory.Activity.Billentry;
import com.example.zhujia.dxracer_factory.Activity.Consumables;
import com.example.zhujia.dxracer_factory.Activity.DetailsActivity;
import com.example.zhujia.dxracer_factory.Activity.Formaterial;
import com.example.zhujia.dxracer_factory.Activity.Materialorderitem;
import com.example.zhujia.dxracer_factory.Activity.Outboundorder;
import com.example.zhujia.dxracer_factory.Activity.Pendingshipment;
import com.example.zhujia.dxracer_factory.Activity.Stockinorder;
import com.example.zhujia.dxracer_factory.Activity.Stockinorderitem;
import com.example.zhujia.dxracer_factory.Activity.Stockinorders;
import com.example.zhujia.dxracer_factory.Data.BatchwareData;
import com.example.zhujia.dxracer_factory.Data.Dict;
import com.example.zhujia.dxracer_factory.Data.ScheduData;
import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.BaseRecyclerAdapter;
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

public class OutboundorderAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder> implements View.OnClickListener  {

    private List<BatchwareData> data;
    public Outboundorder context;
    private int type=0;
    private Handler mHandler;
    private SharedPreferences sharedPreferences;
    private String business_id,departmentId,departmentPersonName,departmentPersonSession;
    Map<String,String> params;
    private OutboundorderAdapter.OnitemClickListener onitemClickListener=null;
    @SuppressLint("WrongConstant")
    public OutboundorderAdapter(Outboundorder context1, List<BatchwareData>data){
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

    public void setOnitemClickListener(OutboundorderAdapter.OnitemClickListener onitemClickListener) {
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

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.outboundorder_data, parent, false);
            OutboundorderAdapter.LinearViewHolder linearViewHolder = new OutboundorderAdapter.LinearViewHolder(baseView);
            baseView.setOnClickListener(this);
            return linearViewHolder;

        }
        else {

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedulingsheet_data, parent, false);
            OutboundorderAdapter.GridViewHolder gridViewHolder = new OutboundorderAdapter.GridViewHolder(baseView);
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
            final OutboundorderAdapter.LinearViewHolder linearViewHolder= (OutboundorderAdapter.LinearViewHolder) holder;
            linearViewHolder.consumablesNo.setText(data.get(position).getConsumablesNo());
            linearViewHolder.createPerson.setText(data.get(position).getCreatePersonName());
            if(!data.get(position).getCreateTime().equals("null")){
                linearViewHolder.createTime.setText(insertComma.stampToDate(data.get(position).getCreateTime()));
            }else {
                linearViewHolder.createTime.setText("-");
            }
            if(!data.get(position).getPickPersonName().equals("null")){
                linearViewHolder.pickPerson.setText(data.get(position).getPickPersonName());
            }else {
                linearViewHolder.pickPerson.setText("");
            }

            if(!data.get(position).getPickTime().equals("null")){
                linearViewHolder.pickTime.setText(insertComma.stampToDate(data.get(position).getPickTime()));
            }else {
                linearViewHolder.pickTime.setText("-");
            }

            if(data.get(position).getStatus().equals("created")){
                linearViewHolder.status.setText("待出库");
                linearViewHolder.status.setTextColor(context.getResources().getColor(R.color.text_color));
            }
            if(data.get(position).getStatus().equals("cancelled")){
                linearViewHolder.status.setText("已作废");
                linearViewHolder.status.setTextColor(context.getResources().getColor(R.color.cancelled));
            }
             if(data.get(position).getStatus().equals("finished")){
                linearViewHolder.status.setText("已出库");
                 linearViewHolder.status.setTextColor(context.getResources().getColor(R.color.blue));
            }

            linearViewHolder.lin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,Consumables.class);
                    intent.putExtra("id",data.get(position).getId());
                    context.startActivity(intent);
                }
            });
            if(linearViewHolder.status.getText().equals("待出库")){
                linearViewHolder.audit_consumables_btn.setEnabled(true);
                linearViewHolder.delete_consumables_btn.setEnabled(true);
                linearViewHolder.audit_consumables_btn.setBackgroundColor(context.getResources().getColor(R.color.btn_info));
                linearViewHolder.delete_consumables_btn.setBackgroundColor(context.getResources().getColor(R.color.btn_info));
            }else{
                linearViewHolder.audit_consumables_btn.setEnabled(false);
                linearViewHolder.delete_consumables_btn.setEnabled(false);
                linearViewHolder.audit_consumables_btn.setBackgroundColor(context.getResources().getColor(R.color.base));
                linearViewHolder.delete_consumables_btn.setBackgroundColor(context.getResources().getColor(R.color.base));
            }

             //作废
            linearViewHolder.delete_consumables_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(business_id,data.get(position).getId(),"确认作废？","/servlet/material/consumables/delete");
                }
            });

            //出库
            linearViewHolder.audit_consumables_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(business_id,data.get(position).getId(),"确认出库?","/servlet/material/consumables/audit");
                }
            });




        }
    }

    private void showDialog(final String business_id, final String id, String msg, final String url) {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setTitle("提示！");
        builder.setMessage(msg);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                params=new HashMap<>();
                params.put("businessId",business_id);
                params.put("id",id);
                params.put("departmentPersonName",departmentPersonName);
                HttpUtility.doPostAsyn(url, params, new IHttpCallBack() {
                    @Override
                    public void onRequestComplete(String result, Handler handler, String errcode) {
                        if ((null == result) || (result.equals(""))) {
                            // 网络连接异常
                            mHandler.sendEmptyMessage(9);

                        }else {
                            JSONObject resulutJsonobj;
                            try
                            {
                                resulutJsonobj=new JSONObject(result);
                                String result_code=resulutJsonobj.getString("code");
                                String message=resulutJsonobj.getString("message");
                                if(result_code.equals("success")){
                                    Looper.prepare();
                                    Toast.makeText(context,message,Toast.LENGTH_LONG).show();
                                    context.onRefresh();
                                    Looper.loop();

                                }else {
                                    Looper.prepare();
                                    Toast.makeText(context,message,Toast.LENGTH_LONG).show();
                                    Looper.loop();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    }
                },mHandler,context);
            }
        });
        builder.show();
    }

    public static class LinearViewHolder extends BaseRecyclerViewHolder {

        private TextView consumablesNo, createPerson,pickPerson,createTime,status,pickTime;
        private LinearLayout lin;
        private Button delete_consumables_btn,audit_consumables_btn;
        public LinearViewHolder(View itemView) {
            super(itemView);
            consumablesNo=(TextView)itemView.findViewById(R.id.consumablesNo);
            createPerson=(TextView)itemView.findViewById(R.id.createPerson);
            pickPerson=(TextView)itemView.findViewById(R.id.pickPerson);
            createTime=(TextView)itemView.findViewById(R.id.createTime);
            status=(TextView)itemView.findViewById(R.id.status);
            pickTime=(TextView)itemView.findViewById(R.id.pickTime);
            lin=(LinearLayout)itemView.findViewById(R.id.lin_btn);
            delete_consumables_btn=(Button)itemView.findViewById(R.id.delete_consumables_btn);
            audit_consumables_btn=(Button)itemView.findViewById(R.id.audit_consumables_btn);

        }
    }

    public static class GridViewHolder extends BaseRecyclerViewHolder {


        public GridViewHolder(View itemView) {
            super(itemView);

        }
    }

}