package com.example.zhujia.dxracer_factory.Adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhujia.dxracer_factory.Activity.DetailsActivity;
import com.example.zhujia.dxracer_factory.Activity.Particulars;
import com.example.zhujia.dxracer_factory.Data.AllData;
import com.example.zhujia.dxracer_factory.Data.Dict;
import com.example.zhujia.dxracer_factory.Data.ScheduData;
import com.example.zhujia.dxracer_factory.Fragment.myorder;
import com.example.zhujia.dxracer_factory.LoginActivitys;
import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.BaseRecyclerAdapter;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtility;
import com.example.zhujia.dxracer_factory.Tools.Net.IHttpCallBack;
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

public class myorderAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder> implements View.OnClickListener  {

    private List<ScheduData> data;
    public myorder context;
    private int type=0;
    private SharedPreferences sharedPreferences;
    private myorderAdapter.OnitemClickListener onitemClickListener=null;
    JSONObject reslutJSONObject;
    Map<String,String> params;
    private Spinner myorder_s;
    private String userId;
    private String configId;
    List<Dict> dicts = new ArrayList<Dict>();
    ProgressDialog progressDialog;


    @SuppressLint("WrongConstant")
    public myorderAdapter(myorder context1, List<ScheduData>data){
        this.context=context1;
        this.data=data;
        sharedPreferences =context1.getActivity().getSharedPreferences("Session", Context.MODE_APPEND);
        userId=sharedPreferences.getString("systemUser_id","");
    }
    @Override
    public void onClick(View view) {
        if(onitemClickListener!=null){
            onitemClickListener.onItemClick(view,(int)view.getTag());
        }
    }

    public void setOnitemClickListener(myorderAdapter.OnitemClickListener onitemClickListener) {
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

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.myorder_data, parent, false);
            myorderAdapter.LinearViewHolder linearViewHolder = new myorderAdapter.LinearViewHolder(baseView);
            baseView.setOnClickListener(this);
            return linearViewHolder;

        }
        else {

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedulingsheet_data, parent, false);
            myorderAdapter.GridViewHolder gridViewHolder = new myorderAdapter.GridViewHolder(baseView);
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
            final myorderAdapter.LinearViewHolder linearViewHolder= (myorderAdapter.LinearViewHolder) holder;

            linearViewHolder.productionDate.setText(data.get(position).getProductionDate());
            linearViewHolder.productionOrderNo.setText(data.get(position).getProductionOrderNo());
            linearViewHolder.orderNo.setText(data.get(position).getOrderNo());
            linearViewHolder.quantity.setText(data.get(position).getQuantity());
            linearViewHolder.fcno.setText(data.get(position).getFcno());
            Log.e("TAG", "onBindViewHolder: "+data.get(position).getId());

            if(data.get(position).getField1()==null||data.get(position).getField1().equals("N")){
                linearViewHolder.field1.setText("正在生产中");
                linearViewHolder.field1.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }else {
                linearViewHolder.field1.setText("已暂停生产");
                linearViewHolder.field1.setTextColor(context.getResources().getColor(R.color.del_color));
            }
            if(!data.get(position).getSearchMaterialPerson().equals("null")){
                linearViewHolder.searchMaterialPerson.setText(data.get(position).getSearchMaterialPerson());
            }else {
                linearViewHolder.searchMaterialPerson.setText("-");
            }

            linearViewHolder.searchMaterialScore.setText(data.get(position).getSearchMaterialScore());
            if(data.get(position).getStatus().equals("producting")){
                linearViewHolder.status.setText("生产中");
            }
            if(data.get(position).getStatus().equals("producted")){
                linearViewHolder.status.setText("已完成");
            }
            if(data.get(position).getStatus().equals("finished")){
                linearViewHolder.status.setText("已入库");
            }
            if(data.get(position).getStatus().equals("create")){
                linearViewHolder.status.setText("待生产");
            }

            if(data.get(position).getSearchMaterialStatus().equals("create")){
                linearViewHolder.searchMaterialStatus.setText("待领料");
            }
            if(data.get(position).getSearchMaterialStatus().equals("picking")){
                linearViewHolder.searchMaterialStatus.setText("领料中");
            }

            if(data.get(position).getSearchMaterialStatus().equals("picked")){
                linearViewHolder.searchMaterialStatus.setText("已领料");
            }
            if(data.get(position).getSearchMaterialStatus().equals("delivery")){
                linearViewHolder.searchMaterialStatus.setText("已出库");
            }

            if(data.get(position).getStatus().equals("producting")){
                linearViewHolder.choice_btn.setEnabled(true);
                linearViewHolder.choice_btn.setBackgroundColor(context.getResources().getColor(R.color.btn_info));
            }else {
                linearViewHolder.choice_btn.setEnabled(false);
                linearViewHolder.choice_btn.setBackgroundColor(context.getResources().getColor(R.color.btn_inf_f));
            }

            if(data.get(position).getStatus().equals("producting")&&data.get(position).getSearchMaterialStatus().equals("delivery")){
                linearViewHolder.score_btn.setEnabled(true);
                linearViewHolder.score_btn.setBackgroundColor(context.getResources().getColor(R.color.btn_info));
            }else {
                linearViewHolder.score_btn.setEnabled(false);
                linearViewHolder.score_btn.setBackgroundColor(context.getResources().getColor(R.color.btn_inf_f));
            }

            if(data.get(position).getStatus().equals("producting")&&data.get(position).getSearchMaterialStatus().equals("delivery")&&data.get(position).getSearchMaterialScore()!=null){
                linearViewHolder.confirm_btn.setEnabled(true);
                linearViewHolder.confirm_btn.setBackgroundColor(context.getResources().getColor(R.color.btn_info));
            }else {
                linearViewHolder.confirm_btn.setEnabled(false);
                linearViewHolder.confirm_btn.setBackgroundColor(context.getResources().getColor(R.color.btn_inf_f));
            }

            linearViewHolder.lin_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //详情
                    Intent intent=new Intent(context.getActivity(),Particulars.class);
                    intent.putExtra("productionOrderId",data.get(position).getId());
                    context.startActivity(intent);
                }
            });


            linearViewHolder.choice_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //选择小组

                    loadchose(data.get(position).getBusiness_id(),data.get(position).getId());
                    View view=(LinearLayout)context.getLayoutInflater().inflate(R.layout.mydialog,null);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context.getActivity());
                    myorder_s=(Spinner)view.findViewById(R.id.myorder_s);
                    myorder_s.setOnItemSelectedListener(listener);
                    builder.setTitle("选择小组");
                    builder.setView(view);
                    builder.setNegativeButton("关闭", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // TODO Auto-generated method stub
                            //	提示信息
                        }
                    });
                    builder.setPositiveButton("保存",null);
                    final AlertDialog dialog=builder.create();
                    //	Diglog的显示
                    dialog.show();
                    if(dialog.getButton(AlertDialog.BUTTON_POSITIVE)!=null){
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(configId.equals("1")){
                                    Toast.makeText(context.getActivity(),"请选择工艺配置",Toast.LENGTH_LONG).show();
                                    return;
                                }else {
                                    progressDialog = new ProgressDialog(context.getActivity(),
                                            R.style.AppTheme_Dark_Dialog);
                                    progressDialog.setIndeterminate(true);
                                    progressDialog.setMessage("正在保存...");
                                    progressDialog.show();
                                    params=new HashMap<>();
                                    params.put("businessId",data.get(position).getBusiness_id());
                                    params.put("order_id",data.get(position).getId());
                                    params.put("configId",configId);
                                    HttpUtility.doPostAsyn("/servlet/production/productionorder/myorder/config", params, new IHttpCallBack() {
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
                                                    String result_code=resulutJsonobj.getString("result_code");
                                                    if(result_code.equals("success")){
                                                        Looper.prepare();
                                                        Toast.makeText(context.getActivity(),"配置成功",Toast.LENGTH_LONG).show();
                                                        context.onRefresh();
                                                        progressDialog.dismiss();
                                                        dialog.dismiss();
                                                        Looper.loop();

                                                    }else {
                                                        Looper.prepare();
                                                        Toast.makeText(context.getActivity(),"配置失败",Toast.LENGTH_LONG).show();
                                                        progressDialog.dismiss();
                                                        Looper.loop();
                                                    }
                                                }catch (JSONException e){
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    },mHandler,context.getActivity());
                                }
                            }
                        });
                    }

                }
            });



            linearViewHolder.score_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //领料评分
                    showDialog(data.get(position).getBusiness_id(),data.get(position).getId());
                }
            });


            linearViewHolder.confirm_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //确认完成
                    params=new HashMap<>();
                    params.put("businessId",data.get(position).getBusiness_id());
                    params.put("id",data.get(position).getId());
                    params.put("userId",userId);
                    HttpUtility.doPostAsyn("/servlet/production/productionorder/myorder/producted", params, new IHttpCallBack() {
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
                                        Toast.makeText(context.getActivity(),message,Toast.LENGTH_LONG).show();
                                        context.onRefresh();
                                        Looper.loop();

                                    }else {
                                        Looper.prepare();
                                        Toast.makeText(context.getActivity(),message,Toast.LENGTH_LONG).show();
                                        Looper.loop();
                                    }
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    },mHandler,context.getActivity());
                }
            });



        }
    }

    //小组下拉
    Spinner.OnItemSelectedListener listener=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            configId= String.valueOf(((Dict)myorder_s.getSelectedItem()).getId());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    //加载小组名称
    private void loadchose(String businessId,String id){
        params=new HashMap<>();
        params.put("businessId",businessId);
        params.put("id",id);
        HttpUtility.doPostAsyn("/servlet/production/productionorder/myorder/configtext", params, new IHttpCallBack() {
            @Override
            public void onRequestComplete(String result, Handler handler, String errcode) {
                if ((null == result) || (result.equals(""))) {
                    // 网络连接异常
                    mHandler.sendEmptyMessage(9);

                }else {

                        if ((null == result) || (result.equals(""))) {
                            // 网络连接异常
                            mHandler.sendEmptyMessage(9);

                        }else {


                            Message msg=Message.obtain(
                                    handler,0,result
                            );
                            mHandler.sendMessage(msg);

                        }

                }
            }
        },mHandler,context.getActivity());


    }

    @SuppressLint("HandlerLeak")
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try{
                switch (msg.what) {

                    case 0:
                        //返回item类型数据
                        reslutJSONObject=new JSONObject(msg.obj.toString());
                        JSONObject rows=reslutJSONObject.getJSONObject("rows");
                        JSONArray dealerList=rows.getJSONArray("productionOrderPersonList");
                        dicts.clear();
                        dicts.add(new Dict(1,"请选择"));
                        for(int i=0;i<dealerList.length();i++){
                            JSONObject object=dealerList.getJSONObject(i);
                            String configName=object.getString("configName");
                            int id=object.getInt("id");
                            dicts.add(new Dict(id,configName));
                            ArrayAdapter<Dict> arrAdapterpay3 = new ArrayAdapter<Dict>(context.getActivity(), R.layout.simple_spinner_item,dicts);
                            //设置样式
                            arrAdapterpay3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            myorder_s.setAdapter(arrAdapterpay3);
                        }
                        break;
                    default:
                        Toast.makeText(context.getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                        break;
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    };
    private void showDialog(final String business_id, final String id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context.getActivity());

        builder.setTitle("领料评分");
        builder.setMessage("请根据领料实际情况，进行打分");
        //	第一个按钮
        builder.setPositiveButton("多料", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                //	提示信息
                PF(business_id,id,"多料");
            }
        });
        //	中间的按钮
        builder.setNeutralButton("少料", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                //	提示信息
                PF(business_id,id,"少料");

            }
        });
        //	第三个按钮
        builder.setNegativeButton("正常", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                //	提示信息
                PF(business_id,id,"正常");
            }
        });

        //	Diglog的显示
        builder.create().show();
    }



    private void PF(String business_id,String id,String score){
        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("id",id);
        params.put("userId",userId);
        params.put("score",score);
        HttpUtility.doPostAsyn("/servlet/production/productionorder/myorder/score", params, new IHttpCallBack() {
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
                        String result_code=resulutJsonobj.getString("result_code");
                        if(result_code.equals("success")){

                            Looper.prepare();
                            Toast.makeText(context.getActivity(),"评分成功",Toast.LENGTH_LONG).show();
                            context.onRefresh();
                            Looper.loop();

                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        },mHandler,context.getActivity());
    }

    public static class LinearViewHolder extends BaseRecyclerViewHolder {

        private TextView productionDate,fcno,quantity,productionOrderNo,orderNo,status,field4,searchMaterialPerson,searchMaterialStatus,searchMaterialScore,field1;
        private LinearLayout lin_btn;
        private Button choice_btn,score_btn,confirm_btn;
        public LinearViewHolder(View itemView) {
            super(itemView);
            productionDate=(TextView)itemView.findViewById(R.id.productionDate);
            fcno=(TextView)itemView.findViewById(R.id.fcno);
            quantity=(TextView)itemView.findViewById(R.id.quantity);
            productionOrderNo=(TextView)itemView.findViewById(R.id.productionOrderNo);
            orderNo=(TextView)itemView.findViewById(R.id.orderNo);
            status=(TextView)itemView.findViewById(R.id.status);
            field4=(TextView)itemView.findViewById(R.id.field4);
            searchMaterialPerson=(TextView)itemView.findViewById(R.id.searchMaterialPerson);
            searchMaterialStatus=(TextView)itemView.findViewById(R.id.searchMaterialStatus);
            searchMaterialScore=(TextView)itemView.findViewById(R.id.searchMaterialScore);
            field1=(TextView)itemView.findViewById(R.id.field1);
            lin_btn=(LinearLayout)itemView.findViewById(R.id.lin_btn);
            choice_btn=(Button)itemView.findViewById(R.id.choice_btn);
            score_btn=(Button)itemView.findViewById(R.id.score_btn);
            confirm_btn=(Button)itemView.findViewById(R.id.confirm_btn);
        }
    }

    public static class GridViewHolder extends BaseRecyclerViewHolder {


        public GridViewHolder(View itemView) {
            super(itemView);

        }
    }

}