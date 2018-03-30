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

import com.bumptech.glide.Glide;
import com.example.zhujia.dxracer_factory.Activity.Allmaterial;
import com.example.zhujia.dxracer_factory.Activity.BatchwarehouseDetail;
import com.example.zhujia.dxracer_factory.Activity.Billentry;
import com.example.zhujia.dxracer_factory.Activity.Consumables;
import com.example.zhujia.dxracer_factory.Activity.DetailsActivity;
import com.example.zhujia.dxracer_factory.Activity.Formaterial;
import com.example.zhujia.dxracer_factory.Activity.Inventory;
import com.example.zhujia.dxracer_factory.Activity.Materialorderitem;
import com.example.zhujia.dxracer_factory.Activity.Officegoodsstaff;
import com.example.zhujia.dxracer_factory.Activity.Outboundorder;
import com.example.zhujia.dxracer_factory.Activity.Pendingshipment;
import com.example.zhujia.dxracer_factory.Activity.PhotoView;
import com.example.zhujia.dxracer_factory.Activity.Stockinorder;
import com.example.zhujia.dxracer_factory.Activity.Stockinorderitem;
import com.example.zhujia.dxracer_factory.Activity.Stockinorders;
import com.example.zhujia.dxracer_factory.Data.BatchwareData;
import com.example.zhujia.dxracer_factory.Data.Dict;
import com.example.zhujia.dxracer_factory.Data.MData;
import com.example.zhujia.dxracer_factory.Data.ScheduData;
import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.BaseRecyclerAdapter;
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

public class OfficegoodAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder> implements View.OnClickListener  {

    private List<MData> data;
    public Officegoodsstaff context;
    private int type=0;
    private SharedPreferences sharedPreferences;
    private String business_id,departmentId,departmentPersonName,departmentPersonSession;
    Map<String,String> params;
    private OfficegoodAdapter.OnitemClickListener onitemClickListener=null;
    @SuppressLint("WrongConstant")
    public OfficegoodAdapter(Officegoodsstaff context1, List<MData>data){
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

    public void setOnitemClickListener(OfficegoodAdapter.OnitemClickListener onitemClickListener) {
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

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.officegoods_data, parent, false);
            OfficegoodAdapter.LinearViewHolder linearViewHolder = new OfficegoodAdapter.LinearViewHolder(baseView);
            baseView.setOnClickListener(this);
            return linearViewHolder;

        }
        else {

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedulingsheet_data, parent, false);
            OfficegoodAdapter.GridViewHolder gridViewHolder = new OfficegoodAdapter.GridViewHolder(baseView);
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
            final OfficegoodAdapter.LinearViewHolder linearViewHolder= (OfficegoodAdapter.LinearViewHolder) holder;
            if(data.get(position).getPicture()!=null){
                Glide.with(context).load(Constant.APPURLIMG+data.get(position).getPicture()).into(linearViewHolder.picture);
            }else {
                linearViewHolder.picture.setImageDrawable(context.getResources().getDrawable(R.mipmap.img_def));
            }
            linearViewHolder.code.setText(data.get(position).getCode());
            linearViewHolder.name.setText(data.get(position).getName());
            linearViewHolder.brand.setText(data.get(position).getBrand());
            linearViewHolder.model.setText(data.get(position).getModel());
            if(data.get(position).getStatus().equals("normal")){
                linearViewHolder.state.setText("正常");
            }else if(data.get(position).getStatus().equals("repairing")){
                linearViewHolder.state.setText("维修中");
            }
            if(data.get(position).getStatus().equals("returning")){
                linearViewHolder.state.setText("归还中");
            }
            if(data.get(position).getStatus().equals("returned")){
                linearViewHolder.state.setText("已归还");
            }
            if(data.get(position).getStatus().equals("repair_failed")){
                linearViewHolder.state.setText("维修失败");
            }

            linearViewHolder.num.setText(data.get(position).getNum());
            if(data.get(position).getDurableType().equals("NY")){
                linearViewHolder.durableType.setText("耐用品");
            }else  if(data.get(position).getDurableType().equals("YH")){
                linearViewHolder.durableType.setText("易耗品");
            }
            linearViewHolder.price.setText(data.get(position).getPrice());
            linearViewHolder.takerName.setText(data.get(position).getTakerName());
            linearViewHolder.takeTime.setText(insertComma.stampToDate(data.get(position).getTakeTime()));

            linearViewHolder.picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, PhotoView.class);
                    intent.putExtra("photoview",data.get(position).getPicture());
                    context.startActivity(intent);
                }
            });



        }
    }



    public static class LinearViewHolder extends BaseRecyclerViewHolder {

        private TextView code,name,brand,model,standard,state,num,durableType,price,takerName,takeTime;
        private LinearLayout lin;
        private ImageView picture;

        public LinearViewHolder(View itemView) {
            super(itemView);
            code=(TextView)itemView.findViewById(R.id.code);
            name=(TextView)itemView.findViewById(R.id.name);
            brand=(TextView)itemView.findViewById(R.id.brand);
            model=(TextView)itemView.findViewById(R.id.model);
            standard=(TextView)itemView.findViewById(R.id.standard);
            state=(TextView)itemView.findViewById(R.id.state);
            num=(TextView)itemView.findViewById(R.id.num);
            picture=(ImageView)itemView.findViewById(R.id.picture);
            lin=(LinearLayout)itemView.findViewById(R.id.lin_btn);
            durableType=(TextView)itemView.findViewById(R.id.durableType);
            price=(TextView)itemView.findViewById(R.id.price);
            takerName=(TextView)itemView.findViewById(R.id.takerName);
            takeTime=(TextView)itemView.findViewById(R.id.takeTime);

        }
    }

    public static class GridViewHolder extends BaseRecyclerViewHolder {


        public GridViewHolder(View itemView) {
            super(itemView);

        }
    }

}