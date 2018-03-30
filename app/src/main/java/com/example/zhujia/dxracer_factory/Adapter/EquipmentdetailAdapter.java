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

import com.example.zhujia.dxracer_factory.Activity.AddEqcategory;
import com.example.zhujia.dxracer_factory.Activity.AddEqdetail;
import com.example.zhujia.dxracer_factory.Activity.AddSupplier;
import com.example.zhujia.dxracer_factory.Activity.BatchwarehouseDetail;
import com.example.zhujia.dxracer_factory.Activity.Billentry;
import com.example.zhujia.dxracer_factory.Activity.DetailsActivity;
import com.example.zhujia.dxracer_factory.Activity.EquipmentCategory;
import com.example.zhujia.dxracer_factory.Activity.Equipmentdetail;
import com.example.zhujia.dxracer_factory.Activity.Equipmentsupplier;
import com.example.zhujia.dxracer_factory.Activity.Stockinorder;
import com.example.zhujia.dxracer_factory.Activity.Stockinorderitem;
import com.example.zhujia.dxracer_factory.Activity.Stockinorders;
import com.example.zhujia.dxracer_factory.Data.BatchwareData;
import com.example.zhujia.dxracer_factory.Data.Dict;
import com.example.zhujia.dxracer_factory.Data.MData;
import com.example.zhujia.dxracer_factory.Data.ScheduData;
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

public class EquipmentdetailAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder> implements View.OnClickListener  {

    private List<MData> data;
    public Equipmentdetail context;
    private int type=0;
    private SharedPreferences sharedPreferences;
    private String business_id,departmentId,departmentPersonName,departmentPersonSession;
    Map<String,String> params;
    private Spinner change_receiver;
    ProgressDialog progressDialog;
    List<Dict> dicts = new ArrayList<Dict>();
    private EquipmentdetailAdapter.OnitemClickListener onitemClickListener=null;
    private String forkliftPersonId;
    private  View views;
    private LinearLayout lin3;
    AlertDialog dialog;
    private PopupWindow pop;//pop弹窗
    @SuppressLint("WrongConstant")
    public EquipmentdetailAdapter(Equipmentdetail context1, List<MData>data){
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

    public void setOnitemClickListener(EquipmentdetailAdapter.OnitemClickListener onitemClickListener) {
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

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.equipmentdetail_data, parent, false);
            EquipmentdetailAdapter.LinearViewHolder linearViewHolder = new EquipmentdetailAdapter.LinearViewHolder(baseView);
            baseView.setOnClickListener(this);
            return linearViewHolder;

        }
        else {

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedulingsheet_data, parent, false);
            EquipmentdetailAdapter.GridViewHolder gridViewHolder = new EquipmentdetailAdapter.GridViewHolder(baseView);
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
            final EquipmentdetailAdapter.LinearViewHolder linearViewHolder= (EquipmentdetailAdapter.LinearViewHolder) holder;
            linearViewHolder.name.setText(data.get(position).getName());
            linearViewHolder.categoryName.setText(data.get(position).getCategoryName());
            //删除
            linearViewHolder.del_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id= String.valueOf(data.get(position).getId());
                    showNormalDialog(position,id);
                }
            });

            //修改
            linearViewHolder.edit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, AddEqdetail.class);
                    intent.putExtra("name",data.get(position).getName());
                    intent.putExtra("categoryName",data.get(position).getCategoryName());
                    String id= String.valueOf(data.get(position).getId());
                    intent.putExtra("id",id);
                    intent.putExtra("type","1");
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
                        HttpUtility.doPostAsyn("/servlet/equipment/equipmentdetail/delete", params, new IHttpCallBack() {
                            @Override
                            public void onRequestComplete(String result, Handler handler, String errcode) {
                                if ((null == result) || (result.equals(""))) {
                                    // 网络连接异常


                                }else {
                                    JSONObject resulutJsonobj;
                                    try
                                    {
                                        resulutJsonobj=new JSONObject(result);
                                        String result_code=resulutJsonobj.getString("code");
                                        if(result_code.equals("success")){

                                            Looper.prepare();
                                            Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
                                            Looper.loop();
                                        }
                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }
                                }
                            }
                        },null,context);
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



    public static class LinearViewHolder extends BaseRecyclerViewHolder {

        private TextView name,categoryName;
        private LinearLayout lin,confirmTimelin;
        private Button edit_btn,del_btn;
        public LinearViewHolder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.name);
            categoryName=(TextView)itemView.findViewById(R.id.categoryName);
            lin=(LinearLayout)itemView.findViewById(R.id.lin_btn);
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