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
import com.example.zhujia.dxracer_factory.Activity.BatchorderDetail;
import com.example.zhujia.dxracer_factory.Activity.BatchwarehouseDetail;
import com.example.zhujia.dxracer_factory.Activity.Billentry;
import com.example.zhujia.dxracer_factory.Activity.DetailsActivity;
import com.example.zhujia.dxracer_factory.Activity.Formaterial;
import com.example.zhujia.dxracer_factory.Activity.Materialorderitem;
import com.example.zhujia.dxracer_factory.Activity.Pendingshipment;
import com.example.zhujia.dxracer_factory.Activity.Personset;
import com.example.zhujia.dxracer_factory.Activity.Stockinorder;
import com.example.zhujia.dxracer_factory.Activity.Stockinorderitem;
import com.example.zhujia.dxracer_factory.Activity.Stockinorders;
import com.example.zhujia.dxracer_factory.Data.AllData;
import com.example.zhujia.dxracer_factory.Data.BatchwareData;
import com.example.zhujia.dxracer_factory.Data.Dict;
import com.example.zhujia.dxracer_factory.Data.ScheduData;
import com.example.zhujia.dxracer_factory.Fragment.Productionorderperson;
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

public class ProductionorderpersonAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder> implements View.OnClickListener  {

    private List<BatchwareData> data;
    public Productionorderperson context;
    private int type=0;
    private SharedPreferences sharedPreferences;
    private String business_id,departmentId,departmentPersonName,departmentPersonSession,personCode;
    Map<String,String> params;
    private ProductionorderpersonAdapter.OnitemClickListener onitemClickListener=null;
    private  View views;
    private LinearLayout lin3;
    private Handler mHandler;
    AlertDialog dialog;
    private EditText configName;
    @SuppressLint("WrongConstant")
    public ProductionorderpersonAdapter(Productionorderperson context1, List<BatchwareData>data){
        this.context=context1;
        this.data=data;
        sharedPreferences =context1.getActivity().getSharedPreferences("Session", Context.MODE_APPEND);
        business_id=sharedPreferences.getString("business_id","");
        departmentPersonSession=sharedPreferences.getString("departmentPersonSession","");
        departmentId=sharedPreferences.getString("departmentId","");
        personCode=sharedPreferences.getString("personCode","");
        departmentPersonName=sharedPreferences.getString("personCode","")+" "+sharedPreferences.getString("realName","");
        Log.e("TAG", "StockinorderAdapter: "+ departmentPersonSession);
    }
    @Override
    public void onClick(View view) {
        if(onitemClickListener!=null){
            onitemClickListener.onItemClick(view,(int)view.getTag());
        }
    }

    public void setOnitemClickListener(ProductionorderpersonAdapter.OnitemClickListener onitemClickListener) {
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

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.productionorderperson_data, parent, false);
            ProductionorderpersonAdapter.LinearViewHolder linearViewHolder = new ProductionorderpersonAdapter.LinearViewHolder(baseView);
            baseView.setOnClickListener(this);
            return linearViewHolder;

        }
        else {

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedulingsheet_data, parent, false);
            ProductionorderpersonAdapter.GridViewHolder gridViewHolder = new ProductionorderpersonAdapter.GridViewHolder(baseView);
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
            final ProductionorderpersonAdapter.LinearViewHolder linearViewHolder= (ProductionorderpersonAdapter.LinearViewHolder) holder;

            linearViewHolder.configName.setText(data.get(position).getConfigName());
            linearViewHolder.stencilNo.setText(data.get(position).getStencilNo());
            linearViewHolder.stencilDesc.setText(data.get(position).getStencilDesc());
            linearViewHolder.name.setText(data.get(position).getName());
            linearViewHolder.del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(data.get(position).getBusinessId(),data.get(position).getId(),position);
                }
            });

            linearViewHolder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View view=(LinearLayout)context.getActivity().getLayoutInflater().inflate(R.layout.layout7,null);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context.getActivity());
                    configName=(EditText) view.findViewById(R.id.configName);
                    configName.setText(data.get(position).getConfigName());
                    builder.setTitle("修改");
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
                                params=new HashMap<>();
                                params.put("businessId",business_id);
                                params.put("personCode",personCode);
                                params.put("configName",configName.getText().toString());
                                params.put("id",data.get(position).getId());
                                HttpUtility.doPostAsyn("/servlet/productionconfig/productionorderperson/update", params, new IHttpCallBack() {
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
                                                    dialog.dismiss();
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
            });


            //人员配置

            linearViewHolder.personset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context.getContext(),Personset.class);
                    intent.putExtra("orderPersonId",data.get(position).getId());
                    intent.putExtra("craftId",data.get(position).getCrafId());
                    context.getActivity().startActivity(intent);
                }
            });


        }
    }



    private void showDialog(final String business_id, final String id, final int position) {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context.getActivity());
        builder.setTitle("提示！");
        builder.setMessage("确认删除吗？");
        builder.setNegativeButton("关闭", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                //	提示信息
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                params=new HashMap<>();
                params.put("businessId",business_id);
                params.put("id",id);
                HttpUtility.doPostAsyn("/servlet/productionconfig/productionorderperson/delete", params, new IHttpCallBack() {
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
                                    Toast.makeText(context.getActivity(),"删除成功",Toast.LENGTH_LONG).show();
                                    Looper.loop();


                                }else {
                                    Looper.prepare();
                                    Toast.makeText(context.getActivity(),"删除失败",Toast.LENGTH_LONG).show();
                                    Looper.loop();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    }
                },mHandler,context.getActivity());
                data.remove(position);
                notifyDataSetChanged();

            }
        });
        builder.show();
    }

    public static class LinearViewHolder extends BaseRecyclerViewHolder {

        private TextView configName,stencilNo,stencilDesc,name;
        private LinearLayout lin;
        private Button del,edit,personset;

        public LinearViewHolder(View itemView) {
            super(itemView);
            configName=(TextView)itemView.findViewById(R.id.configName);
            stencilNo=(TextView)itemView.findViewById(R.id.stencilNo);
            stencilDesc=(TextView)itemView.findViewById(R.id.stencilDesc);
            name=(TextView)itemView.findViewById(R.id.name);
            lin=(LinearLayout)itemView.findViewById(R.id.lin_btn);
            del=(Button) itemView.findViewById(R.id.del);
            edit=(Button)itemView.findViewById(R.id.edit);
            personset=(Button)itemView.findViewById(R.id.personset);

        }
    }

    public static class GridViewHolder extends BaseRecyclerViewHolder {


        public GridViewHolder(View itemView) {
            super(itemView);

        }
    }

}