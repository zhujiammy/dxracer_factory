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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhujia.dxracer_factory.Activity.Allmaterial;
import com.example.zhujia.dxracer_factory.Activity.BatchwarehouseDetail;
import com.example.zhujia.dxracer_factory.Activity.Billentry;
import com.example.zhujia.dxracer_factory.Activity.DetailsActivity;
import com.example.zhujia.dxracer_factory.Activity.Formaterial;
import com.example.zhujia.dxracer_factory.Activity.Materialorderitem;
import com.example.zhujia.dxracer_factory.Activity.Order;
import com.example.zhujia.dxracer_factory.Activity.OrderDetail;
import com.example.zhujia.dxracer_factory.Activity.Pendingshipment;
import com.example.zhujia.dxracer_factory.Activity.Purchaseplan;
import com.example.zhujia.dxracer_factory.Activity.PurchaseplanDetail;
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

public class PurchaseplanAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder> implements View.OnClickListener  {

    private List<MData> data;
    public Purchaseplan context;
    private int type=0;

    private SharedPreferences sharedPreferences;
    private String business_id,departmentId,departmentPersonName,departmentPersonSession;
    Map<String,String> params;
    private PurchaseplanAdapter.OnitemClickListener onitemClickListener=null;
    @SuppressLint("WrongConstant")
    public PurchaseplanAdapter(Purchaseplan context1, List<MData>data){
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

    public void setOnitemClickListener(PurchaseplanAdapter.OnitemClickListener onitemClickListener) {
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

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchaseplan_data, parent, false);
            PurchaseplanAdapter.LinearViewHolder linearViewHolder = new PurchaseplanAdapter.LinearViewHolder(baseView);
            baseView.setOnClickListener(this);
            return linearViewHolder;

        }
        else {

            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedulingsheet_data, parent, false);
            PurchaseplanAdapter.GridViewHolder gridViewHolder = new PurchaseplanAdapter.GridViewHolder(baseView);
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
            final PurchaseplanAdapter.LinearViewHolder linearViewHolder= (PurchaseplanAdapter.LinearViewHolder) holder;


            if(data.get(position).getPlanDeliveryDate()!=null){
                linearViewHolder.planDeliveryDate.setText(insertComma.stampToDate(data.get(position).getPlanDeliveryDate()));
            }else {
                linearViewHolder.planDeliveryDate.setText("-");
            }
            linearViewHolder.orderNo.setText(data.get(position).getOrderNo());
            linearViewHolder.purchasePlanNo.setText(data.get(position).getPurchasePlanNo());
            if(data.get(position).getOrderStatus().equals("created")){
                linearViewHolder.orderStatus.setText("已创建");
            }else if(data.get(position).getOrderStatus().equals("createPurchaseOrder")){
                linearViewHolder.orderStatus.setText("未完成");
            } else if(data.get(position).getOrderStatus().equals("supplierCheckPurchaseOrder")){
                linearViewHolder.orderStatus.setText("未完成");
            }else if(data.get(position).getOrderStatus().equals("supplierConfirmed")){
                linearViewHolder.orderStatus.setText("未完成");
            }
            else if(data.get(position).getOrderStatus().equals("createPurchaseWarehouseReceipt")){
                linearViewHolder.orderStatus.setText("未完成");
            }
            else if(data.get(position).getOrderStatus().equals("finished")){
                linearViewHolder.orderStatus.setText("已完成");
            }

            Log.e("TAG", "onBindViewHolder: "+data.get(position).getPurchaseOrderList() );
            if(!data.get(position).getPurchaseOrderList().equals("[]")){
                int totalCount = 0;
                int finishedCount = 0;
                int canceledCount = 0;
                int recreateCount = 0;
                int res = 0;
                try {
                    JSONArray purchaseOrderList=new JSONArray(data.get(position).getPurchaseOrderList());
                    for(int i=0;i<purchaseOrderList.length();i++){
                        JSONObject object=purchaseOrderList.getJSONObject(i);
                        if(object.getString("status").equals("finished")){
                            finishedCount++;
                        }
                        if(object.getString("status").equals("'canceled'")||object.getString("status").equals("'returned'")){
                            canceledCount++;
                            continue;
                        }
                        if(object.getString("needRecreate").equals("Y")){
                            recreateCount++;
                            continue;
                        }
                        totalCount++;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

               if (finishedCount > 0) {
                    res = 100 * finishedCount/totalCount;
                    linearViewHolder.progressBar.setProgress(res);
                    linearViewHolder.jindu.setText(res+"%"+" "+finishedCount + "/" + totalCount);
                }

            }else {
                linearViewHolder.jindu.setText("-"+0 + "/" + 0);
                linearViewHolder.progressBar.setProgress(0);
            }








            linearViewHolder.lin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,PurchaseplanDetail.class);
                    String purchasePlanId= String.valueOf(data.get(position).getId());
                    intent.putExtra("purchasePlanId",purchasePlanId);

                    context.startActivity(intent);
                }
            });






        }
    }



    public static class LinearViewHolder extends BaseRecyclerViewHolder {

        private TextView purchasePlanNo, orderNo,planDeliveryDate,orderStatus,jindu;
        private LinearLayout lin;
        private ProgressBar progressBar;
        public LinearViewHolder(View itemView) {
            super(itemView);
            purchasePlanNo=(TextView)itemView.findViewById(R.id.purchasePlanNo);
            orderNo=(TextView)itemView.findViewById(R.id.orderNo);
            planDeliveryDate=(TextView)itemView.findViewById(R.id.planDeliveryDate);
            orderStatus=(TextView)itemView.findViewById(R.id.orderStatus);
            jindu=(TextView)itemView.findViewById(R.id.jindu);
            lin=(LinearLayout)itemView.findViewById(R.id.lin_btn);
            progressBar=(ProgressBar)itemView.findViewById(R.id.progressBar);
        }
    }

    public static class GridViewHolder extends BaseRecyclerViewHolder {


        public GridViewHolder(View itemView) {
            super(itemView);

        }
    }

}