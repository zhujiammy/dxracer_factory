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
import com.example.zhujia.dxracer_factory.Activity.DetailsActivity;
import com.example.zhujia.dxracer_factory.Activity.Formaterial;
import com.example.zhujia.dxracer_factory.Activity.Materialorderitem;
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

public class AllmaterialAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder> implements View.OnClickListener  {

    private List<BatchwareData> data;
    public Allmaterial context;
    private int type=0;
    private SharedPreferences sharedPreferences;
    private String business_id,departmentId,departmentPersonName,departmentPersonSession;
    Map<String,String> params;
    private Spinner set_forklift;
    ProgressDialog progressDialog;
    List<Dict> dicts = new ArrayList<Dict>();
    private AllmaterialAdapter.OnitemClickListener onitemClickListener=null;
    private String forkliftPersonId;
    private  View views;
    private LinearLayout lin3;
    AlertDialog dialog;
    private PopupWindow pop;//pop弹窗
    @SuppressLint("WrongConstant")
    public AllmaterialAdapter(Allmaterial context1, List<BatchwareData>data){
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

    public void setOnitemClickListener(AllmaterialAdapter.OnitemClickListener onitemClickListener) {
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

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.allmaterial_data, parent, false);
            AllmaterialAdapter.LinearViewHolder linearViewHolder = new AllmaterialAdapter.LinearViewHolder(baseView);
            baseView.setOnClickListener(this);
            return linearViewHolder;

        }
        else {

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedulingsheet_data, parent, false);
            AllmaterialAdapter.GridViewHolder gridViewHolder = new AllmaterialAdapter.GridViewHolder(baseView);
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
            final AllmaterialAdapter.LinearViewHolder linearViewHolder= (AllmaterialAdapter.LinearViewHolder) holder;
            linearViewHolder.materialPerson.setText(data.get(position).getMaterialPerson());
            linearViewHolder.orderNo.setText(data.get(position).getOrderNo());
            linearViewHolder.materialNo.setText(data.get(position).getMaterialNo());
            linearViewHolder.productionOrderNo.setText(data.get(position).getProductionOrderNo());
            if(!data.get(position).getMaterialTime().equals("null")){
                linearViewHolder.materialTime.setText(insertComma.stampToDate(data.get(position).getMaterialTime()));
            }else {
                linearViewHolder.materialTime.setText("-");
            }

            if(!data.get(position).getOutputTime().equals("null")){
                linearViewHolder.outputTime.setText(insertComma.stampToDate(data.get(position).getOutputTime()));
            }else {
                linearViewHolder.outputTime.setText("-");
            }

            if(!data.get(position).getScore().equals("null")){
                linearViewHolder.score.setText(data.get(position).getScore());
            }else {
                linearViewHolder.score.setText("-");
            }

            if(data.get(position).getStatus().equals("create")){
                linearViewHolder.status.setText("待领料");
            }
            if(data.get(position).getStatus().equals("picking")){
                linearViewHolder.status.setText("领料中");
            }
            if(data.get(position).getStatus().equals("picked")){
                linearViewHolder.status.setText("已领料");
            } if(data.get(position).getStatus().equals("delivery")){
                linearViewHolder.status.setText("已出库");
            }

            linearViewHolder.lin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,Materialorderitem.class);
                    intent.putExtra("materialOrderId",data.get(position).getId());
                    context.startActivity(intent);
                }
            });






        }
    }



    public static class LinearViewHolder extends BaseRecyclerViewHolder {

        private TextView materialPerson, materialNo,productionOrderNo,orderNo,status,materialTime,score,outputTime;
        private LinearLayout lin;

        public LinearViewHolder(View itemView) {
            super(itemView);
            materialPerson=(TextView)itemView.findViewById(R.id.materialPerson);
            orderNo=(TextView)itemView.findViewById(R.id.orderNo);
            materialNo=(TextView)itemView.findViewById(R.id.materialNo);
            productionOrderNo=(TextView)itemView.findViewById(R.id.productionOrderNo);
            status=(TextView)itemView.findViewById(R.id.status);
            materialTime=(TextView)itemView.findViewById(R.id.materialTime);
            score=(TextView)itemView.findViewById(R.id.score);
            outputTime=(TextView)itemView.findViewById(R.id.outputTime);
            lin=(LinearLayout)itemView.findViewById(R.id.lin_btn);

        }
    }

    public static class GridViewHolder extends BaseRecyclerViewHolder {


        public GridViewHolder(View itemView) {
            super(itemView);

        }
    }

}